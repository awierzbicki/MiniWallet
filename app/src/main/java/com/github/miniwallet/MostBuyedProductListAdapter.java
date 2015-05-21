package com.github.miniwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.miniwallet.shopping.Product;

import java.util.ArrayList;

/**
 * Created by Dominik on 2015-05-21.
 */
public class MostBuyedProductListAdapter extends ArrayAdapter<Product>{
    private final Context context;
    private ArrayList<Product> values;

    public MostBuyedProductListAdapter (Context context, ArrayList<Product> values) {
        super(context, R.layout.most_buyed_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.most_buyed_row, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.productName);
        TextView category = (TextView) rowView.findViewById(R.id.category);
        TextView price = (TextView) rowView.findViewById(R.id.price);

        name.setText(values.get(position).getName());
        category.setText(values.get(position).getCategory().getName());
        price.setText(Double.toString(values.get(position).getLastPrice()));
        return rowView;

    }
}
