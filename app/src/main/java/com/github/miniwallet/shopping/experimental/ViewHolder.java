package com.github.miniwallet.shopping.experimental;

import android.view.View;
import android.widget.TextView;

import com.github.miniwallet.R;
import com.github.miniwallet.shopping.Product;

import butterknife.InjectView;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public abstract class ViewHolder {
    @InjectView(R.id.productName)
    TextView name;
    @InjectView(R.id.price)
    TextView price;

    public ViewHolder(View view) {
        //ButterKnife.inject(this, view);
    }

    public void setComponentsParameters(Object data) {
        Product p = (Product) data;
        name.setText(p.getName());
        price.setText(Double.toString(p.getLastPrice()));
    }
}
