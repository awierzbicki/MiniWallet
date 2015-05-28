package com.github.miniwallet.shopping.experimental;

import android.view.View;
import android.widget.TextView;

import com.github.miniwallet.R;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Agnieszka on 2015-05-27.
 */
public class HistoryViewHolder extends ViewHolder {
    @InjectView(R.id.category)
    TextView category;
    @InjectView(R.id.date)
    TextView date;

    public HistoryViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    @Override
    public void setComponentsParameters(Object data) {
        Purchase purchase = (Purchase) data;
        date.setText(purchase.getDate().toString());
        price.setText(Double.toString(purchase.getPrice()));
        Product p = purchase.getProduct();
        category.setText(p.getCategory().getName());
        name.setText(p.getName());
    }
}
