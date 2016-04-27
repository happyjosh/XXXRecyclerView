package com.xxxrecylcerview;

import android.view.View;

/**
 * 添加、删除header/footer
 * Created by jph on 2016/4/27.
 */
public interface HatShoe {

    void addHeaderView(View v);

    void addFooterView(View v);

    void removeHeaderView(View v);

    void removeFooterView(View v);

    void removeAllHeaderView();

    void removeAllFooterView();
}
