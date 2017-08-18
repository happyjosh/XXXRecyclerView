package com.jph.xxxrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.xxxrecylcerview.XXXRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试界面
 * Created by jph on 2016/4/20.
 */
public class MainActivity extends AppCompatActivity {

    private MyAdapter mMyAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final XXXRecyclerView recyclerView = (XXXRecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLoadMoreView(View.inflate(this, R.layout.layout_load_more, null));
        mMyAdapter = new MyAdapter(generateData(0, 20));
        recyclerView.setAdapter(mMyAdapter);
        recyclerView.addHeaderView(View.inflate(this, R.layout.header, null));
        recyclerView.setOnLoadMoreListener(new XXXRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mMyAdapter.addList(generateData(mMyAdapter.getRealItemCount(), 20));
                recyclerView.stopLoadMore();
            }
        });


        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMyAdapter.setList(generateData(0, 20));
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

//        final PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) findViewById(R.id.layout_refresh);
//        ptrFrameLayout.setHeaderView(View.inflate(this, R.layout.layout_load_more, null));
//        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mMyAdapter.setList(generateData(0, 20));
//                        ptrFrameLayout.refreshComplete();
//                    }
//                }, 2000);
//            }
//        });
    }

    private List<String> generateData(int beginIndex, int count) {
        List<String> list = new ArrayList<>();
        for (int i = beginIndex; i < beginIndex + count; i++) {
            list.add("XXX : " + i);
        }
        return list;
    }
}
