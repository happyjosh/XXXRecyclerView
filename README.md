# XXXRecyclerView
A recyclerview can load more

public class MyAdapter extends BaseRecyclerAdapter...

... recyclerView.setOnLoadMoreListener(new XXXRecyclerView.OnLoadMoreListener() { 
        @Override public void onLoadMore() { 
          mMyAdapter.addList(generateData(mMyAdapter.getRealItemCount(), 20));
          recyclerView.stopLoadMore();
          } 
        });
