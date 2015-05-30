package com.github.miniwallet.shopping.experimental;

import android.view.View;
import android.widget.TextView;

import com.github.miniwallet.R;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Agnieszka on 2015-05-27.
 */
public class HistoryViewHolder extends ViewHolder {
    static final SimpleDateFormat dataFormat = new SimpleDateFormat("EEE d MMMMMMM yyyy HH:mm");

    @InjectView(R.id.date)
    TextView date;

    public HistoryViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    @Override
    public void setComponentsParameters(Object data) {
        Purchase purchase = (Purchase) data;
        date.setText(dataFormat.format(purchase.getDate()));
        price.setText(String.format("%.2f", purchase.getPrice()));
        Product p = purchase.getProduct();
        category.setText(p.getCategory().getName());
        name.setText(p.getName());
    }
}
