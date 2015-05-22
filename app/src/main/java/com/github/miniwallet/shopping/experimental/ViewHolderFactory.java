package com.github.miniwallet.shopping.experimental;

import android.view.View;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public final class ViewHolderFactory {

    private ViewHolderFactory() {
    }

    public static ViewHolder createViewHolder(View view, ViewHolder.Type type) {
        switch (type) {
            case BEST_SELLING_ROW:
                return new BestSellingViewHolder(view);
            case PURCHASE_ROW:
                return new PurchaseViewHolder(view);
        }
        return null;
    }
}
