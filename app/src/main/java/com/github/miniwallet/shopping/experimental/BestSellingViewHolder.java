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
public class BestSellingViewHolder extends ViewHolder {

    @InjectView(R.id.category)
    TextView category;

    public BestSellingViewHolder(View view) {
        ButterKnife.inject(this, view);
    }


    @Override
    public void setComponentsParameters(Object data) {
        Product p = (Product) data;
        name.setText(p.getName());
        price.setText(Double.toString(p.getLastPrice()));
        category.setText(p.getCategory().getName());
    }

}
