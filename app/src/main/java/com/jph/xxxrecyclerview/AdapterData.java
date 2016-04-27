package com.jph.xxxrecyclerview;

import java.util.List;

/**
 * adapter数据操作
 *
 * @author jph
 *
 */
public interface AdapterData<T> {

	List<T> getList();

	void addList(List<T> listData);

	void setList(List<T> listData);

	void clearList();

	T getItem(int position);

	void addItem(T item);

	void deleteItem(T item);

	void deleteItem(int position);
}
