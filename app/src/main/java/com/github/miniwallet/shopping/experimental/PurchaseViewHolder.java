package com.github.miniwallet.shopping.experimental;

import android.view.View;
import android.widget.TextView;

import com.github.miniwallet.R;
import com.github.miniwallet.shopping.Product;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public class PurchaseViewHolder extends ViewHolder {

    @InjectView(R.id.category)
    TextView category;

    public PurchaseViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    @Override
    public void setComponentsParameters(Object data) {
        super.setComponentsParameters(data);
        Product p = (Product) data;
        category.setText(p.getCategory().getName());
    }
}
