package com.github.miniwallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.miniwallet.R;
import com.github.miniwallet.shopping.experimental.ViewHolder;
import com.github.miniwallet.shopping.experimental.ViewHolderFactory;

import java.util.List;


public class EntityListAdapter<T> extends ArrayAdapter<T> {
    private final Context context;
    protected List<T> values;
    private ViewHolder.Type viewHolderType;

    public EntityListAdapter(Context context, List<T> values, ViewHolder.Type type) {
        super(context, R.layout.best_selling_row, values);
        this.context = context;
        this.values = values;
        this.viewHolderType = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(viewHolderType.getLayoutId(), parent, false);
            holder = ViewHolderFactory.createViewHolder(convertView, viewHolderType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setComponentsParameters(values.get(position));
        return convertView;
    }

    public void addAll(List<T> list) {
        synchronized (values) {
            values.addAll(list);
        }
    }


    @Override
    public int getCount() {
        return values != null ? values.size() : 0;
    }

    public void setNewValuesAndNotify(List<T> newValues) {
        values = newValues;
        notifyDataSetChanged();
    }
}
