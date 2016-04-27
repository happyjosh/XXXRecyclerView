package com.xxxrecylcerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jph on 2016/4/21.
 */
public class XXXRecyclerView extends RecyclerView implements HatShoe {
    private static final String TAG = "XXXRecyclerView";

    private boolean mLoadable = true;
    private boolean mIsLoading = false;
    private int mVisibleItemCount = 0;
    private int mTotalItemCount = 0;

    private int mLastTouchY;
    private boolean mTouchThis = false;//是否触摸

    private OnLoadMoreListener mOnLoadMoreListener;
    private MScrollListener mScrollListener;

    private View mLoadMoreView;//加载更多view

    public XXXRecyclerView(Context context) {
        super(context);
    }

    public XXXRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public XXXRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof XXXAdapter) {
            setLoadMoreView((XXXAdapter) adapter);
            if (mScrollListener != null) {
                removeOnScrollListener(mScrollListener);
            }
            mScrollListener = new MScrollListener();
            addOnScrollListener(new MScrollListener());
        }
    }

    private void setLoadMoreView(XXXAdapter adapter) {
        mLoadMoreView = createLoadMoreView();
        adapter.removeAllFooterView();
        adapter.addFooterView(mLoadMoreView);
        setLoadMoreShow(false);
    }

    private View createLoadMoreView() {
        return View.inflate(getContext(), R.layout.layout_loadmore, null);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        //TODO 暂时只支持LinearLayoutManager
        if (getLayoutManager() == null || !(getLayoutManager() instanceof LinearLayoutManager)) {
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

                LinearLayoutManager ll = (LinearLayoutManager) getLayoutManager();
                mVisibleItemCount = ll.getChildCount();
                mTotalItemCount = ll.getItemCount();
                int firstPosition = ll.findFirstVisibleItemPosition();
                if (!mIsLoading && dy < 0) {
                    if ((mTotalItemCount - mVisibleItemCount) <=
                            firstPosition) {
                        startLoadMore();
                        mIsLoading = true;
                    } else if (!loadMoreIsShow() && (mTotalItemCount - mVisibleItemCount) <=
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
        if (getAdapter() != null && getAdapter() instanceof HatShoe) {
            ((HatShoe) getAdapter()).addHeaderView(v);
        }
    }

    @Override
    public void addFooterView(View v) {
        if (getAdapter() != null && getAdapter() instanceof HatShoe) {
            ((HatShoe) getAdapter()).addFooterView(v);
        }
    }

    @Override
    public void removeHeaderView(View v) {
        if (getAdapter() != null && getAdapter() instanceof HatShoe) {
            ((HatShoe) getAdapter()).removeHeaderView(v);
        }
    }

    @Override
    public void removeFooterView(View v) {
        if (getAdapter() != null && getAdapter() instanceof HatShoe) {
            ((HatShoe) getAdapter()).removeFooterView(v);
        }
    }

    @Override
    public void removeAllHeaderView() {
        if (getAdapter() != null && getAdapter() instanceof HatShoe) {
            ((HatShoe) getAdapter()).removeAllHeaderView();
        }
    }

    @Override
    public void removeAllFooterView() {
        if (getAdapter() != null && getAdapter() instanceof HatShoe) {
            ((HatShoe) getAdapter()).removeAllFooterView();
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

    public void setLoadMoreShow(boolean show) {
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

    /**
     * 是否显示loadmore
     *
     * @return
     */
    private boolean loadMoreIsShow() {
        return mLoadMoreView.getVisibility() == VISIBLE;
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
            //TODO 暂时只支持LinearLayoutManager
            if (!(getLayoutManager() instanceof LinearLayoutManager)) {
                return;
            }
            if (dy <= 0) {
                return;
            }
            LinearLayoutManager ll = (LinearLayoutManager) getLayoutManager();
            mVisibleItemCount = ll.getChildCount();
            mTotalItemCount = ll.getItemCount();
            int firstPosition = ll.findFirstVisibleItemPosition();

            if (!loadMoreIsShow() && !mIsLoading && (mTotalItemCount - mVisibleItemCount) <=
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
