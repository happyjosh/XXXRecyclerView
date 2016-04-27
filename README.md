### 集成addHeaderView和addFooterView功能，增加上拉加载功能的RecyclerView

### 使用方式：
```java
public class MyAdapter extends BaseRecyclerAdapter<MyAdapter.ViewHolder, String>...
```

```java
recyclerView.setOnLoadMoreListener(new XXXRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mMyAdapter.addList(generateData(mMyAdapter.getRealItemCount(), 20));
                recyclerView.stopLoadMore();
            }
        });
```
