package com.github.miniwallet.shopping.experimental;

import android.widget.TextView;

import com.github.miniwallet.R;

import butterknife.InjectView;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public abstract class ViewHolder {
    @InjectView(R.id.newCategory)
    TextView name;
    @InjectView(R.id.price)
    TextView price;

    public enum Type {
        BEST_SELLING_ROW(R.layout.best_selling_row), PURCHASE_ROW(R.layout.purchase_row);
        private final int layoutId;

        private Type(int layoutId) {
            this.layoutId = layoutId;
        }

        public int getLayoutId() {
            return layoutId;
        }
    }

    public abstract void setComponentsParameters(Object data);
}
