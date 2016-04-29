### 集成addHeaderView和addFooterView功能，增加上拉加载功能的RecyclerView

### 使用方式：
```java
compile 'com.jph:xxxrecyclerview:1.1.0'
```

```java
public class MyAdapter extends BaseRecyclerAdapter<MyAdapter.ViewHolder, String>...
```

```java
 recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLoadMoreView(View.inflate(this, R.layout.layout_load_more, null));
        mMyAdapter = new MyAdapter(generateData(0, 20));
        recyclerView.setAdapter(mMyAdapter);
        recyclerView.setOnLoadMoreListener(new XXXRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mMyAdapter.addList(generateData(mMyAdapter.getRealItemCount(), 20));
                recyclerView.stopLoadMore();
            }
        });
```
