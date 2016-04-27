package com.xxxrecylcerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 处理header和footer的Adapter
 * Created by jph on 2016/4/20.
 */
public abstract class XXXAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HatShoe {

    //以两种不同的itemType来作为header和footer
    private static int ITEM_TYPE_HEADER = -101;
    private static int ITEM_TYPE_FOOTER = -102;

    private ViewGroup mLayoutHeader;//header容器
    private ViewGroup mLayoutFooter;//footer容器

    @Override
    public final int getItemCount() {
        int itemCount = getRealItemCount();

        //将header和footer添加到item总数
        itemCount++;
        itemCount++;
        return itemCount;
    }

    @Override
    public final int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else if (position == getItemCount() - 1) {
            return ITEM_TYPE_FOOTER;
        }
        return getRealItemViewType(position);
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            if (mLayoutHeader == null) {
                mLayoutHeader = createContainer(parent.getContext());
            }
            return new RecyclerView.ViewHolder(mLayoutHeader) {
            };
        } else if (viewType == ITEM_TYPE_FOOTER) {
            if (mLayoutFooter == null) {
                mLayoutFooter = createContainer(parent.getContext());
            }
            return new RecyclerView.ViewHolder(mLayoutFooter) {
            };
        }
        return onRealCreateViewHolder(parent, viewType);
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0 || position == getItemCount() - 1) {
            //header和footer不执行实际复写的onRealBindViewHolder
            return;
        }
        position--;
        onRealBindViewHolder((VH) holder, position);
    }

    public abstract int getRealItemCount();

    public int getRealItemViewType(int position) {
        return 0;
    }

    public abstract VH onRealCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onRealBindViewHolder(VH holder, int position);

    private ViewGroup createContainer(Context context) {
        LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    @Override
    public void addHeaderView(View v) {
        if (mLayoutHeader == null) {
            mLayoutHeader = createContainer(v.getContext());
        }
        mLayoutHeader.addView(v);
    }

    @Override
    public void addFooterView(View v) {
        if (mLayoutFooter == null) {
            mLayoutFooter = createContainer(v.getContext());
        }
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
}
