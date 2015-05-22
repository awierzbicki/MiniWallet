package com.github.miniwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.miniwallet.db.daos.CategoryDAO;
import com.github.miniwallet.db.daos.ProductDAO;
import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.CategoryDAOImpl;
import com.github.miniwallet.db.daos.impl.ProductDAOImpl;
import com.github.miniwallet.db.daos.impl.PurchaseDAOImpl;
import com.github.miniwallet.shopping.Category;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;
import com.github.miniwallet.shopping.experimental.ViewHolder;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public class PurchaseFragment extends Fragment {
    public static final int EDIT = 0;
    ProductDAO productDAO;
    CategoryDAO categoryDAO;
    PurchaseDAO purchaseDAO;
    Category defaultCategory;
    EntityListAdapter<Product> adapter;
    List<Product> productList;
    ArrayList<String> categories;

    @InjectView(R.id.purchaseList)
    ListView listView;
    @InjectView(R.id.editMinPrice)
    EditText minPrice;
    @InjectView(R.id.editMaxPrice)
    EditText maxPrice;
    @InjectView(R.id.spinnerCategory)
    Spinner categorySpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TO DO productDAO.getAllProducts();
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        purchaseDAO = new PurchaseDAOImpl();
        productList = productDAO.getAllProducts();
        categories = categoryDAO.getAllCategoriesNames();
        adapter = new EntityListAdapter<>(getActivity(), productList, ViewHolder.Type.BEST_SELLING_ROW);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_purchase, container, false);
        ButterKnife.inject(this, rootView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new EditItemListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                buyProduct(productList.get(position));
            }
        });
        ArrayAdapter<String> adapterState = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapterState);

        return rootView;
    }

    private void buyProduct(Product product) {
        Purchase purchase = new Purchase(product.getLastPrice(), product, new LatLng(1.1, 2.2), new Date());
        purchaseDAO.insertPurchase(purchase);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode + " " + resultCode);
        productList = productDAO.getAllProducts();
        adapter.setNewValues(productList);
        adapter.notifyDataSetChanged();
    }

    private class EditItemListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Product product = adapter.getItem(position);
            Intent intent = new Intent(getActivity(), EditProductActivity.class);
            intent.putExtra("ProductName", product.getName());
            startActivityForResult(intent, EDIT);
            return true;
        }
    }

}
