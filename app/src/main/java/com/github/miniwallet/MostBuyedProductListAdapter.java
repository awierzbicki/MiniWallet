package com.github.miniwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.miniwallet.shopping.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dominik on 2015-05-21.
 */
public class MostBuyedProductListAdapter extends ArrayAdapter<Product>{
    private final Context context;
    private List<Product> values;
    private LayoutInflater inflater;

    public MostBuyedProductListAdapter (Context context, List<Product> values) {
        super(context, R.layout.most_buyed_row, values);
        this.context = context;
        this.values = values;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ProductHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.most_buyed_row, parent, false);
            holder = new ProductHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ProductHolder) convertView.getTag();
        }

        holder.name.setText(values.get(position).getName());
        holder.category.setText(values.get(position).getCategory().getName());
        holder.price.setText(Double.toString(values.get(position).getLastPrice()));
        return convertView;
    }


    static class ProductHolder {
        @InjectView(R.id.productName)
        TextView name;
        @InjectView(R.id.category)
        TextView category;
        @InjectView(R.id.price)
        TextView price;

        ProductHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
