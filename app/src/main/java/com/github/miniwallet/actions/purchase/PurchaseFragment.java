package com.github.miniwallet.actions.purchase;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.miniwallet.EditProductActivity;
import com.github.miniwallet.R;
import com.github.miniwallet.adapters.FilterableEntityListAdapter;
import com.github.miniwallet.db.daos.CategoryDAO;
import com.github.miniwallet.db.daos.ProductDAO;
import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.CategoryDAOImpl;
import com.github.miniwallet.db.daos.impl.ProductDAOImpl;
import com.github.miniwallet.db.daos.impl.PurchaseDAOImpl;
import com.github.miniwallet.filters.ComplexFilter;
import com.github.miniwallet.filters.ProductCategoryFilter;
import com.github.miniwallet.filters.ProductMaxPriceFilter;
import com.github.miniwallet.filters.ProductMinPriceFilter;
import com.github.miniwallet.filters.ProductNameFilter;
import com.github.miniwallet.location.Locator;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;
import com.github.miniwallet.shopping.experimental.ViewHolder;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public class PurchaseFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static final int EDIT = 0;
    private static final int CATEGORY_ALL_POSITION = 0;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private PurchaseDAO purchaseDAO;

    private FilterableEntityListAdapter<Product> adapter;
    private ArrayList<String> categories;
    private ArrayAdapter<String> categoriesAdapter;

    @InjectView(R.id.search_edit_text)
    EditText searchText;
    @InjectView(R.id.purchaseList)
    ListView listView;
    @InjectView(R.id.editMinPrice)
    EditText editMinPrice;
    @InjectView(R.id.editMaxPrice)
    EditText editMaxPrice;
    @InjectView(R.id.spinnerCategory)
    Spinner categorySpinner;

    private ProductNameFilter productNameFilter;
    private ProductCategoryFilter productCategoryFilter;
    private ProductMinPriceFilter productMinPriceFilter;
    private ProductMaxPriceFilter productMaxPriceFilter;
    private Locator locator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        purchaseDAO = new PurchaseDAOImpl();
//        locator = new Locator(getActivity());

        categories = categoryDAO.getAllCategoriesNames();
        categories.add(CATEGORY_ALL_POSITION, "All");

        ComplexFilter<Product> filter = setUpFilter();
        adapter = new FilterableEntityListAdapter<>(getActivity(), productDAO.getAllProducts(), ViewHolder.Type.PURCHASE_ROW, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_purchase, container, false);
        ButterKnife.inject(this, rootView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new EditItemListener());
        listView.setOnItemClickListener(this);

        categoriesAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoriesAdapter);
        listView.setTextFilterEnabled(true);

        addPriceTextListeners();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        productCategoryFilter.setCategories(categoryDAO.getCategoryByName(categories.get(categorySpinner.getSelectedItemPosition())));
        adapter.setNewValuesAndNotify( productDAO.getAllProducts());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product product = adapter.getItem(position);
        Purchase purchase = new Purchase(product.getLastPrice(), product, locator.getLocation(), new Date());
        purchaseDAO.insertPurchase(purchase);
    }

    public void setLocator(Locator locator) {
        this.locator = locator;
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
    @OnTextChanged(R.id.search_edit_text)
    public void onTextChanged(CharSequence s) {
        productNameFilter.setPattern(searchText.getText().toString());
        adapter.filterAndNotify();
    }

    @OnItemSelected(R.id.spinnerCategory)
    public void onItemSelected(int position) {
        productCategoryFilter.setCategories(categoryDAO.getCategoryByName(categories.get(position)));
        adapter.filterAndNotify();
    }

    private ComplexFilter<Product> setUpFilter() {
        productNameFilter = new ProductNameFilter();
        productCategoryFilter = new ProductCategoryFilter();

        productMinPriceFilter = new ProductMinPriceFilter();
        productMaxPriceFilter = new ProductMaxPriceFilter();

        return new ComplexFilter<>(productDAO.getAllProducts(), productNameFilter,
                productMaxPriceFilter, productMinPriceFilter, productCategoryFilter);
    }

    private void addPriceTextListeners() {
        editMinPrice.addTextChangedListener(new PriceEditTextWatcher(editMinPrice, adapter, productMinPriceFilter));
        editMaxPrice.addTextChangedListener(new PriceEditTextWatcher(editMaxPrice, adapter, productMaxPriceFilter));
    }

    public void validate() {
        categories = categoryDAO.getAllCategoriesNames();
        categories.add(CATEGORY_ALL_POSITION, "All");
        categoriesAdapter.notifyDataSetChanged();
        adapter.setNewValuesAndNotify(productDAO.getAllProducts());
    }
}
