package com.xxxrecylcerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * 处理header和footer的Adapter
 * Created by jph on 2016/4/20.
 */
public abstract class XXXAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //以两种不同的itemType来作为header和footer
    private static int ITEM_TYPE_HEADER = -101;
    private static int ITEM_TYPE_FOOTER = -102;

    private ViewGroup mLayoutHeader;//header容器
    private ViewGroup mLayoutFooter;//footer容器

    private boolean mIsVerticallyContainer = false;//是否是纵向的容器

    void setLayoutHeader(ViewGroup layoutHeader) {
        mLayoutHeader = layoutHeader;
    }

    void setLayoutFooter(ViewGroup layoutFooter) {
        mLayoutFooter = layoutFooter;
    }

    @Override
    public final int getItemCount() {
        int itemCount = getRealItemCount();

        //将header和footer添加到item总数
        if (isAllowHasHeader()) {
            itemCount++;
        }
        if (isAllowHasFooter()) {
            itemCount++;
        }
        return itemCount;
    }

    @Override
    public final int getItemViewType(int position) {
        if (isAllowHasHeader() && position == 0) {
            return ITEM_TYPE_HEADER;
        } else if (isAllowHasFooter() && position == getItemCount() - 1) {
            return ITEM_TYPE_FOOTER;
        }
        if (isAllowHasHeader()) {
            position--;
        }
        return getRealItemViewType(position);
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            return new RecyclerView.ViewHolder(mLayoutHeader) {
            };
        } else if (viewType == ITEM_TYPE_FOOTER) {
            return new RecyclerView.ViewHolder(mLayoutFooter) {
            };
        }
        return onRealCreateViewHolder(parent, viewType);
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ((isAllowHasHeader() && position == 0) ||
                (isAllowHasFooter() && position == getItemCount() - 1)) {
            //header和footer不执行实际复写的onRealBindViewHolder
            return;
        }
        if (isAllowHasHeader()) {
            position--;
        }
        onRealBindViewHolder((VH) holder, position);
    }

    public abstract int getRealItemCount();

    public int getRealItemViewType(int position) {
        return 0;
    }

    public abstract VH onRealCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onRealBindViewHolder(VH holder, int position);


    ViewGroup getHeaderContainer() {
        return mLayoutHeader;
    }


    public void setVerticallyContainer(boolean verticallyContainer) {
        mIsVerticallyContainer = verticallyContainer;
    }

    /**
     * 是否允许有header
     *
     * @return
     */
    private boolean isAllowHasHeader() {
        return mIsVerticallyContainer;
    }

    /**
     * 是否允许有footer
     *
     * @return
     */
    private boolean isAllowHasFooter() {
        return mIsVerticallyContainer;
    }
}
