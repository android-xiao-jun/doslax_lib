package me.tatarka.bindingcollectionadapter2;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

/**
 * Provides the necessary information to bind an item in a collection to a view. This includes the
 * variable id and the layout as well as any extra bindings you may want to provide.
 */
public final class ItemViewBinding {

    /**
     * Use this constant as the variable id to not bind the item in the collection to the layout if
     * no data is need, like a static footer or loading indicator.
     */
    public static final int VAR_NONE = 0;
    private static final int VAR_INVALID = -1;
    private static final int LAYOUT_NONE = 0;

    /**
     * Constructs an instance with the given variable id and layout.
     */
    @NonNull
    public static ItemViewBinding of(int variableId, @LayoutRes int layoutRes) {
        return new ItemViewBinding().set(variableId, layoutRes);
    }

    private int variableId;
    @LayoutRes
    private int layoutRes;

    private ItemViewBinding() {

    }

    /**
     * Set the variable id and layout. This is normally called in {@link
     */
    @NonNull
    public final ItemViewBinding set(int variableId, @LayoutRes int layoutRes) {
        this.variableId = variableId;
        this.layoutRes = layoutRes;
        return this;
    }

    /**
     * Returns the current variable id of this binding.
     */
    public final int variableId() {
        return variableId;
    }

    /**
     * Returns the current layout fo this binding.
     */
    @LayoutRes
    public final int layoutRes() {
        return layoutRes;
    }

    /**
     * Binds the item and extra bindings to the given binding. Returns true if anything was bound
     * and false otherwise. This is called internally by the binding collection adapters.
     *
     * @throws IllegalStateException if the variable id isn't present in the layout.
     */
    public boolean bind(@NonNull ViewDataBinding binding, Object item) {
        if (variableId == VAR_NONE) {
            return false;
        }
        boolean result = binding.setVariable(variableId, item);
        if (!result) {
            Utils.throwMissingVariable(binding, variableId, layoutRes);
        }
        return true;
    }
}
