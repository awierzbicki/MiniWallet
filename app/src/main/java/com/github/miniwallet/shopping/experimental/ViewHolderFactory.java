package com.github.miniwallet.shopping.experimental;

import android.view.View;

import com.github.miniwallet.R;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public class ViewHolderFactory {

    public enum ViewHolderType {
        BEST_SELLING_ROW(R.layout.best_selling_row), PURCHASE_ROW(R.layout.purchase_row);
        private final int layoutId;

        private ViewHolderType(int layoutId) {
            this.layoutId = layoutId;
        }

        public int getLayoutId() {
            return layoutId;
        }
    }


    public ViewHolder createViewHolder(View view, ViewHolderType type) {
        switch (type) {
            case BEST_SELLING_ROW:
                return new BestSellingViewHolder(view);
            case PURCHASE_ROW:
                return new PurchaseViewHolder(view);
        }
        return null;
    }
}
