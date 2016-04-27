package com.jph.xxxrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import com.xxxrecylcerview.XXXAdapter;

import java.util.List;

public abstract class BaseRecyclerAdapter<VH extends RecyclerView.ViewHolder, T>
        extends XXXAdapter<VH> implements AdapterData<T> {

    private OnItemClickListener mOnItemClickListener;
    private List<T> mListData;

    public BaseRecyclerAdapter(List<T> listData) {
        super();
        this.mListData = listData;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getRealItemViewType(int position) {
        return 0;
    }

    @Override
    public VH onRealCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onRealBindViewHolder(VH viewHolder, final int position) {
        viewHolder.itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(v, position);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getRealItemCount() {
        if (mListData == null)
            return 0;
        return mListData.size();
    }

    @Override
    public List<T> getList() {
        return mListData;
    }

    @Override
    public void addList(List<T> listData) {
        this.mListData.addAll(listData);
        notifyDataSetChanged();
    }

    @Override
    public void setList(List<T> listData) {
        if (listData != null) {
            this.mListData.clear();
            this.mListData.addAll(listData);
            notifyDataSetChanged();
        }
    }

    @Override
    public void clearList() {

        this.mListData.clear();
        notifyDataSetChanged();
    }

    @Override
    public T getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public void addItem(T item) {

        this.mListData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void deleteItem(T item) {

        this.mListData.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public void deleteItem(int position) {

        this.mListData.remove(position);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }

}
