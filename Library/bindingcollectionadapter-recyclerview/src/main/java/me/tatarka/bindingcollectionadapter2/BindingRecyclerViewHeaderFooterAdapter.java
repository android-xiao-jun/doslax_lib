package me.tatarka.bindingcollectionadapter2;

import static android.view.View.NO_ID;

import android.annotation.SuppressLint;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * A {@link RecyclerView.Adapter} that binds items to layouts using the given {@link ItemBinding}.
 * If you give it an {@link ObservableList} it will also updated itself based on changes to that
 * list.
 */
public class BindingRecyclerViewHeaderFooterAdapter<T> extends BindingRecyclerViewAdapter<T> {
    @Nullable
    private Object headerObject;
    @Nullable
    private Object footerObject;
    @Nullable
    private ItemViewBinding headerBinding;
    @Nullable
    private ItemViewBinding footerBinding;
    @Nullable
    private OnLoadMoreListener mOnLoadMoreListener;

    public void setHeaderObject(@Nullable Object headerObject) {
        this.headerObject = headerObject;
    }

    public void setFooterObject(@Nullable Object footerObject) {
        this.footerObject = footerObject;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHeaderBinding(@Nullable ItemViewBinding binding) {
        if ((binding == null && headerBinding == null) || binding == headerBinding) {
            return;
        }
        if (headerBinding != null && binding == null) {
            headerBinding = binding;
            notifyItemRemoved(0);
        } else if (headerBinding != null) {
            headerBinding = binding;
            notifyItemChanged(0);
        } else if (binding != null) {
            headerBinding = binding;
            notifyItemInserted(0);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFooterBinding(@Nullable ItemViewBinding binding) {
        if ((binding == null && footerBinding == null) || binding == footerBinding) {
            return;
        }
        footerBinding = binding;
        notifyDataSetChanged();
    }

    public int getHeadersCount() {
        return headerBinding != null ? 1 : 0;
    }

    public void setOnLoadMoreListener(@Nullable OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public int getFootersCount() {
        return footerBinding != null ? 1 : 0;
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    private int getRealItemCount() {
        return items == null ? 0 : items.size();
    }

    public void onHeaderBindBinding(@NonNull ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, Object item) {
        tryGetLifecycleOwner();
        if (headerBinding != null && headerBinding.bind(binding, item)) {
            binding.executePendingBindings();
            if (lifecycleOwner != null) {
                binding.setLifecycleOwner(lifecycleOwner);
            }
        }
    }

    public void onFooterBindBinding(@NonNull ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, Object item) {
        tryGetLifecycleOwner();
        if (footerBinding != null && footerBinding.bind(binding, item)) {
            binding.executePendingBindings();
            if (lifecycleOwner != null) {
                binding.setLifecycleOwner(lifecycleOwner);
            }
        }
        if (getRealItemCount() > 0 && mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMoreRequested();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setItems(@Nullable List<T> items) {
        if (this.items == items) {
            return;
        }
        // If a recyclerview is listening, set up listeners. Otherwise wait until one is attached.
        // No need to make a sound if nobody is listening right?
        if (recyclerView != null) {
            if (this.items instanceof ObservableList) {
                ((ObservableList<T>) this.items).removeOnListChangedCallback(callback);
                callback = null;
            }
            if (items instanceof ObservableList) {
                callback = new WeakReferenceOnListChangedCallbackChild<>(this, (ObservableList<T>) items);
                ((ObservableList<T>) items).addOnListChangedCallback(callback);
            }
        }
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        if (this.recyclerView == null && items instanceof ObservableList) {
            callback = new WeakReferenceOnListChangedCallbackChild<>(this, (ObservableList<T>) items);
            ((ObservableList<T>) items).addOnListChangedCallback(callback);
        }
        this.recyclerView = recyclerView;
    }

    @Override
    public T getAdapterItem(int position) {
        if (headerBinding != null && isHeaderViewPos(position)) {
            return null;
        } else if (footerBinding != null && isFooterViewPos(position)) {
            return null;
        } else {
            return super.getAdapterItem(position - getHeadersCount());
        }
    }

    @Override
    @CallSuper
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        if (isForDataBinding(payloads)) {
            binding.executePendingBindings();
        } else if (headerBinding != null && isHeaderViewPos(position)) {
            onHeaderBindBinding(binding, headerBinding.variableId(), headerBinding.layoutRes(), position, headerObject);
        } else if (footerBinding != null && isFooterViewPos(position)) {
            onFooterBindBinding(binding, footerBinding.variableId(), footerBinding.layoutRes(), position, footerObject);
        } else {
            super.onBindViewHolder(holder, position - getHeadersCount(), payloads);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (headerBinding != null && isHeaderViewPos(position)) {
            return headerBinding.layoutRes();
        } else if (footerBinding != null && isFooterViewPos(position)) {
            return footerBinding.layoutRes();
        }
        return super.getItemViewType(position - getHeadersCount());
    }


    @Override
    public int getItemCount() {
        return getHeadersCount() + getRealItemCount() + getFootersCount();
    }

    @Override
    public long getItemId(int position) {
        if (headerBinding != null && isHeaderViewPos(position)) {
            return headerBinding.layoutRes();
        } else if (footerBinding != null && isFooterViewPos(position)) {
            return footerBinding.layoutRes();
        }
        return super.getItemId(position - getHeadersCount());
    }

    protected static class WeakReferenceOnListChangedCallbackChild<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {

        final WeakReference<BindingRecyclerViewHeaderFooterAdapter<T>> adapterRef;

        WeakReferenceOnListChangedCallbackChild(BindingRecyclerViewHeaderFooterAdapter<T> adapter, ObservableList<T> items) {
            this.adapterRef = AdapterReferenceCollector.createRef(adapter, items, this);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onChanged(ObservableList sender) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, final int positionStart, final int itemCount) {
            BindingRecyclerViewHeaderFooterAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyItemRangeChanged(positionStart + adapter.getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, final int positionStart, final int itemCount) {
            BindingRecyclerViewHeaderFooterAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyItemRangeInserted(positionStart + adapter.getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, final int fromPosition, final int toPosition, final int itemCount) {
            BindingRecyclerViewHeaderFooterAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            for (int i = 0; i < itemCount; i++) {
                adapter.notifyItemMoved(fromPosition + adapter.getHeadersCount() + i, toPosition + i);
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, final int positionStart, final int itemCount) {
            BindingRecyclerViewHeaderFooterAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyItemRangeRemoved(positionStart + adapter.getHeadersCount(), itemCount);
        }
    }

}
