package me.tatarka.bindingcollectionadapter2;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList;
import me.tatarka.bindingcollectionadapter2.recyclerview.R;

/**
 * @see {@link BindingCollectionAdapters}
 */
public class BindingRecyclerViewAdapters {
    @BindingAdapter(value = {"layoutManager"}, requireAll = false)
    public static <T> void setLayoutManager(RecyclerView recyclerView, @Nullable RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    // RecyclerView
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder", "diffConfig"}, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView,
                                      ItemBinding<? super T> itemBinding,
                                      List<T> items,
                                      BindingRecyclerViewAdapter<T> adapter,
                                      BindingRecyclerViewAdapter.ItemIds<? super T> itemIds,
                                      BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory,
                                      AsyncDifferConfig<T> diffConfig) {
        if (itemBinding != null) {
            BindingRecyclerViewAdapter oldAdapter = (BindingRecyclerViewAdapter) recyclerView.getAdapter();
            if (adapter == null) {
                if (oldAdapter == null) {
                    adapter = new BindingRecyclerViewAdapter<>();
                } else {
                    adapter = oldAdapter;
                }
            }
            adapter.setItemBinding(itemBinding);

            if (diffConfig != null && items != null) {
                AsyncDiffObservableList<T> list = (AsyncDiffObservableList<T>) recyclerView.getTag(R.id.bindingcollectiondapter_list_id);
                if (list == null) {
                    list = new AsyncDiffObservableList<>(diffConfig);
                    recyclerView.setTag(R.id.bindingcollectiondapter_list_id, list);
                    adapter.setItems(list);
                }
                list.update(items);
            } else {
                adapter.setItems(items);
            }

            adapter.setItemIds(itemIds);
            adapter.setViewHolderFactory(viewHolderFactory);

            if (oldAdapter != adapter) {
                recyclerView.setAdapter(adapter);
            }
        } else {
            recyclerView.setAdapter(null);
        }
    }

    // RecyclerView
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder", "diffConfig", "headerBinding", "footerBinding", "headerObject", "footerObject", "loadMoreListener"}, requireAll = false)
    public static <T> void setBindAdapter(RecyclerView recyclerView,
                                          ItemBinding<? super T> itemBinding,
                                          List<T> items,
                                          BindingRecyclerViewHeaderFooterAdapter<T> adapter,
                                          BindingRecyclerViewHeaderFooterAdapter.ItemIds<? super T> itemIds,
                                          BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory,
                                          AsyncDifferConfig<T> diffConfig,
                                          @Nullable
                                          ItemViewBinding headerBinding,
                                          @Nullable
                                          ItemViewBinding footerBinding,
                                          @Nullable
                                          Object headerObject,
                                          @Nullable
                                          Object footerObject,
                                          @Nullable
                                          OnLoadMoreListener loadMoreListener
    ) {
        if (itemBinding != null) {
            BindingRecyclerViewHeaderFooterAdapter oldAdapter = (BindingRecyclerViewHeaderFooterAdapter) recyclerView.getAdapter();
            if (adapter == null) {
                if (oldAdapter == null) {
                    adapter = new BindingRecyclerViewHeaderFooterAdapter<>();
                } else {
                    adapter = oldAdapter;
                }
            }
            adapter.setItemBinding(itemBinding);
            adapter.setHeaderBinding(headerBinding);
            adapter.setFooterBinding(footerBinding);
            adapter.setHeaderObject(headerObject);
            adapter.setFooterObject(footerObject);
            adapter.setOnLoadMoreListener(loadMoreListener);

            if (diffConfig != null && items != null) {
                AsyncDiffObservableList<T> list = (AsyncDiffObservableList<T>) recyclerView.getTag(R.id.bindingcollectiondapter_list_id);
                if (list == null) {
                    list = new AsyncDiffObservableList<>(diffConfig);
                    recyclerView.setTag(R.id.bindingcollectiondapter_list_id, list);
                    adapter.setItems(list);
                }
                list.update(items);
            } else {
                adapter.setItems(items);
            }

            adapter.setItemIds(itemIds);
            adapter.setViewHolderFactory(viewHolderFactory);

            if (oldAdapter != adapter) {
                recyclerView.setAdapter(adapter);
            }
        } else {
            recyclerView.setAdapter(null);
        }
    }

    @BindingConversion
    public static <T> AsyncDifferConfig<T> toAsyncDifferConfig(DiffUtil.ItemCallback<T> callback) {
        return new AsyncDifferConfig.Builder<>(callback).build();
    }
}
