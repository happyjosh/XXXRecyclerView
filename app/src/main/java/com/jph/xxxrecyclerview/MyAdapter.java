package com.jph.xxxrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jph on 2016/4/20.
 */
public class MyAdapter extends BaseRecyclerAdapter<MyAdapter.ViewHolder, String> {

    public MyAdapter(List<String> listData) {
        super(listData);
    }

    @Override
    public ViewHolder onRealCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onRealBindViewHolder(ViewHolder viewHolder, int position) {
        super.onRealBindViewHolder(viewHolder, position);
        String str = getItem(position);
        viewHolder.mTxt.setText(str+" position:"+position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt)
        TextView mTxt;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txt, parent, false));
//            super(View.inflate(parent.getContext(), R.layout.item_txt, null));
            ButterKnife.bind(this, itemView);
        }
    }
}
