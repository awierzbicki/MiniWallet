package com.github.miniwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.miniwallet.shopping.Product;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ProductListAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private ArrayList<Product> values;
    //private ViewHolderFactory viewHolderFactory = new ViewHolderFactory();
    //private ViewHolderFactory.ViewHolderType viewHolderType;

    public ProductListAdapter(Context context, ArrayList<Product> values) {
        super(context, R.layout.best_selling_row, values);
        this.context = context;
        this.values = values;
        //this.viewHolderFactory = viewHolderFactory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View rowView = convertView;

        if (convertView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.best_selling_row, parent, false);
            //holder = viewHolderFactory.createViewHolder(rowView, viewHolderType);
            holder = new ViewHolder(rowView);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        holder.name.setText(values.get(position).getName());
        holder.category.setText(values.get(position).getCategory().getName());
        holder.price.setText(Double.toString(values.get(position).getLastPrice()));
        return rowView;
    }

    public void setProducts(ArrayList<Product> newProducts) {
        values = newProducts;
    }

    static class ViewHolder {
        @InjectView(R.id.productName)
        TextView name;
        @InjectView(R.id.price)
        TextView price;
        @InjectView(R.id.category)
        TextView category;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
