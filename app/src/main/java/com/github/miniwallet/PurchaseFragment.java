package com.github.miniwallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.miniwallet.db.daos.CategoryDAO;
import com.github.miniwallet.db.daos.ProductDAO;
import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.ProductDAOImpl;
import com.github.miniwallet.shopping.Category;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.experimental.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public class PurchaseFragment extends Fragment {
    ProductDAO productDAO;
    CategoryDAO categoryDAO;
    PurchaseDAO purchaseDAO;
    Category defaultCategory;
    EntityListAdapter<Product> adapter;
    List<Product> productList;

    @InjectView(R.id.purchaseList)
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TO DO productDAO.getAllProducts();
        productDAO = new ProductDAOImpl();
        productList = productDAO.getMostBoughtProducts(10);
        adapter = new EntityListAdapter<>(getActivity(), productList, ViewHolder.Type.BEST_SELLING_ROW);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_purchase, container, false);
        ButterKnife.inject(this, rootView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = adapter.getItem(position);
                Toast.makeText(getActivity(), product.toString(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        //adapter.notifyDataSetChanged();
        return rootView;
    }
}
