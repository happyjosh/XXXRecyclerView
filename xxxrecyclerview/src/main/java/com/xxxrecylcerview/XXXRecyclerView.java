package com.xxxrecylcerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 支持loadmore，addHeaderViw，addFooterView的RecyclerView
 * Created by jph on 2016/4/21.
 */
public class XXXRecyclerView extends RecyclerView implements HatShoe {


    private static final String TAG = "XXXRecyclerView";

    private boolean mLoadable = true;
    private boolean mIsLoading = false;
    private @LayoutManagerType
    int layoutManagerType;//LayoutManager类型

    private int mLastTouchY;
    private boolean mTouchThis = false;//是否触摸

    private OnLoadMoreListener mOnLoadMoreListener;
    private MScrollListener mScrollListener;

    private View mLoadMoreView;//加载更多view

    private ViewGroup mLayoutHeader;//header容器
    private ViewGroup mLayoutFooter;//footer容器

    public XXXRecyclerView(Context context) {
        super(context);
        init();
    }

    public XXXRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XXXRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mLayoutHeader = createContainer();
        mLayoutFooter = createContainer();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (layout != null) {
            if (layout instanceof GridLayoutManager) {
                layoutManagerType =LayoutManagerType.GRID;
            } else if (layout instanceof LinearLayoutManager) {
                layoutManagerType = LayoutManagerType.LINEAR;
            } else if (layout instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LayoutManagerType.STAGGERED_GRID;
            } else {
                throw new RuntimeException("Unsupported LayoutManager used." +
                        " Valid ones are LinearLayoutManager, " +
                        "GridLayoutManager and StaggeredGridLayoutManager");
            }
        }
    }

    @Override
    public void setAdapter(@NonNull Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof XXXAdapter) {
            boolean isVertically = isVertically();
            XXXAdapter xxxAdapter = (XXXAdapter) adapter;
            xxxAdapter.setLayoutHeader(mLayoutHeader);
            xxxAdapter.setLayoutFooter(mLayoutFooter);
            xxxAdapter.setVerticallyContainer(isVertically);
            if (!isVertically) {
                return;
            }

            if (mLoadMoreView == null) {
                mLoadMoreView = createDefaultLoadMoreView();
            } else if (mLoadMoreView.getParent() != null) {
                ((ViewGroup) mLoadMoreView.getParent()).removeView(mLoadMoreView);
            }
            setLoadMoreView(xxxAdapter);
            if (mScrollListener != null) {
                removeOnScrollListener(mScrollListener);
            }
            mScrollListener = new MScrollListener();
            addOnScrollListener(new MScrollListener());
        }
    }

    public void setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
    }

    private void setLoadMoreView(XXXAdapter adapter) {
        removeAllFooterView();
        addFooterView(mLoadMoreView);
        setLoadMoreShow(false);
        setFooterSpanSizeLookup(getLayoutManager(), adapter);
    }

    private View createDefaultLoadMoreView() {
        return View.inflate(getContext(), R.layout.layout_loadmore, null);
    }

    /**
     * 设置footer跨行
     *
     * @param layout
     * @param adapter
     */
    private void setFooterSpanSizeLookup(LayoutManager layout, final Adapter adapter) {
        if (layout == null || !(layout instanceof GridLayoutManager)) {
            return;
        }
        final GridLayoutManager gridLayoutManager = (GridLayoutManager) layout;
        final GridLayoutManager.SpanSizeLookup oldSpanSizeLookup = gridLayoutManager.getSpanSizeLookup();

        //grid时设置loadmore跨行
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0 || position == adapter.getItemCount() - 1) {
                    return gridLayoutManager.getSpanCount();
                }
                return oldSpanSizeLookup != null ? oldSpanSizeLookup.getSpanSize(position) : 1;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (getLayoutManager() == null) {
            return super.onInterceptTouchEvent(e);
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchThis = true;
                mLastTouchY = (int) (e.getY() + 0.5f);
                break;
            case MotionEvent.ACTION_MOVE:
                final int y = (int) (e.getY() + 0.5f);
                final int dy = y - mLastTouchY;

                LayoutManager layoutManager = getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstPosition = getFirstPosition();
                if (!mIsLoading && dy < 0) {
                    if ((totalItemCount - visibleItemCount) <=
                            firstPosition) {
                        startLoadMore();
                        mIsLoading = true;
                    } else if (!loadMoreIsShow() && (totalItemCount - visibleItemCount) <=
                            firstPosition + 1) {
                        startLoadMore();
                        mIsLoading = true;
                    }
                }
                break;
        }

        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_CANCEL || e.getAction() == MotionEvent.ACTION_UP) {
            //手指离开
            mTouchThis = false;
        }
        return super.onTouchEvent(e);
    }


    @Override
    public void addHeaderView(View v) {
        mLayoutHeader.addView(v);
    }

    @Override
    public void addFooterView(View v) {
        mLayoutFooter.addView(v);
    }

    @Override
    public void removeHeaderView(View v) {
        if (mLayoutHeader != null) {
            mLayoutHeader.removeView(v);
        }
    }

    @Override
    public void removeFooterView(View v) {
        if (mLayoutFooter != null) {
            mLayoutFooter.removeView(v);
        }
    }

    @Override
    public void removeAllHeaderView() {
        if (mLayoutHeader != null) {
            mLayoutHeader.removeAllViews();
        }
    }

    @Override
    public void removeAllFooterView() {
        if (mLayoutFooter != null) {
            mLayoutFooter.removeAllViews();
        }
    }

    public void startLoadMore() {
        if (!mLoadable || mIsLoading) {
            return;
        }
        mIsLoading = true;
        if (mLoadMoreView != null) {
            setLoadMoreShow(true);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mOnLoadMoreListener != null) {
                    mOnLoadMoreListener.onLoadMore();
                }
            }
        }, 600);
    }

    public void stopLoadMore() {
        mIsLoading = false;
        setLoadMoreShow(false);
    }

    private void setLoadMoreShow(boolean show) {
        if (mLoadMoreView != null) {
            mLoadMoreView.setVisibility(show ? VISIBLE : GONE);
        }
    }

    public boolean isLoadable() {
        return mLoadable;
    }

    public void setLoadable(boolean loadable) {
        mLoadable = loadable;
    }

    private int findMin(int[] lastPositions) {
        int min = Integer.MAX_VALUE;
        for (int value : lastPositions) {
            if (value != RecyclerView.NO_POSITION && value < min)
                min = value;
        }
        return min;
    }

    private int getFirstPosition() {
        LayoutManager layoutManager = getLayoutManager();
        int firstPosition = 0;

        switch (layoutManagerType) {
            case LayoutManagerType.LINEAR:
                firstPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                break;
            case LayoutManagerType.GRID:
                firstPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                break;
            case LayoutManagerType.STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] firstPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                staggeredGridLayoutManager.findFirstVisibleItemPositions(firstPositions);
                firstPosition = findMin(firstPositions);
                break;
        }
        return firstPosition;
    }

    /**
     * 是否显示loadmore
     *
     * @return
     */
    private boolean loadMoreIsShow() {
        return mLoadMoreView.getVisibility() == VISIBLE;
    }

    /**
     * 是否是纵向的RecyclerView
     *
     * @return
     */
    private boolean isVertically() {
        if (layoutManagerType == 0) {
            throw new NullPointerException("Null layoutManager");
        }
        switch (layoutManagerType) {
            case LayoutManagerType.GRID:
                return ((GridLayoutManager) getLayoutManager()).getOrientation() ==
                        GridLayoutManager.VERTICAL;
            case LayoutManagerType.LINEAR:
                return ((LinearLayoutManager) getLayoutManager()).getOrientation() ==
                        LinearLayoutManager.VERTICAL;
            case LayoutManagerType.STAGGERED_GRID:
                return ((StaggeredGridLayoutManager) getLayoutManager()).getOrientation() ==
                        StaggeredGridLayoutManager.VERTICAL;
        }
        return false;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        //解决当header不显示时，各种下拉刷新控件无效的问题
        if (getAdapter() == null) {
            return super.canScrollVertically(direction);
        }

        if (!XXXAdapter.class.isInstance(getAdapter())) {
            return super.canScrollVertically(direction);
        }

        XXXAdapter xxxAdapter = (XXXAdapter) getAdapter();
        if (xxxAdapter.getHeaderContainer() == null ||
                xxxAdapter.getHeaderContainer().getChildCount() != 0) {
            return super.canScrollVertically(direction);
        }

        if (direction < 0) {
            if (getAdapter().getItemCount() <= 1) {
                return false;
            }
            View fistItemV = getLayoutManager().findViewByPosition(1);

            if (fistItemV == null) {
                //排除header外的第一个item被回收了
                return true;
            }

            return fistItemV.getTop() < 0;
        }
        return super.canScrollVertically(direction);
    }

    private ViewGroup createContainer() {
        LinearLayout container = new LinearLayout(getContext());
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return mOnLoadMoreListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    private class MScrollListener extends OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy <= 0) {
                return;
            }

            LayoutManager layoutManager = getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstPosition = getFirstPosition();

            if (!loadMoreIsShow() && !mIsLoading && (totalItemCount - visibleItemCount) <=
                    (mTouchThis ? firstPosition : firstPosition + 1)) {
                //手指离开，自动滑动firstPosition+1,预先加载
                startLoadMore();
                mIsLoading = true;
            }
        }
    }

    /**
     * 加载监听
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
