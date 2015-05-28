package com.github.miniwallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.github.miniwallet.db.daos.CategoryDAO;
import com.github.miniwallet.db.daos.ProductDAO;
import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.CategoryDAOImpl;
import com.github.miniwallet.db.daos.impl.ProductDAOImpl;
import com.github.miniwallet.db.daos.impl.PurchaseDAOImpl;
import com.github.miniwallet.shopping.Category;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

import static android.view.View.OnClickListener;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public class PurchaseFragment extends Fragment {
    public static final int EDIT = 0;
    private static final int CATEGORY_ALL_POSITION = 0;

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private PurchaseDAO purchaseDAO;
    private Category defaultCategory;
    private EntityListAdapter<Product> adapter;
    private List<Product> productList;
    private ArrayList<String> categories;
    private int inputNameLastLength = 0;
    private double minPrice = 0;
    private double maxPrice;

    private AlertDialog alertDialog;

    @InjectView(R.id.search_edit_text)
    EditText searchText;
    @InjectView(R.id.purchaseList)
    ListView listView;
    @InjectView(R.id.minPrice)
    TextView editMinPrice;
    @InjectView(R.id.maxPrice)
    TextView editMaxPrice;
    @InjectView(R.id.spinnerCategory)
    Spinner categorySpinner;
    EditText min;
    EditText max;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        purchaseDAO = new PurchaseDAOImpl();
        productList = productDAO.getAllProducts();
        categories = categoryDAO.getAllCategoriesNames();
        adapter = new ProductListFilterableAdapter(getActivity(), productList);
        if (productDAO.getHighestPrice() != null)
            maxPrice = productDAO.getHighestPrice();
        else
            maxPrice = 0.0d;
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
        categories.add(CATEGORY_ALL_POSITION, "All");
        ArrayAdapter<String> adapterState = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapterState);
        listView.setTextFilterEnabled(true);
        createMinMaxPriceDialog();
        OnMinMaxClickedListener minMaxClickedListener = new OnMinMaxClickedListener();
        editMaxPrice.setOnClickListener(minMaxClickedListener);
        editMinPrice.setOnClickListener(minMaxClickedListener);
        //searchText.addTextChangedListener(new ProductNameTextWatcher());
        //categorySpinner.setOnItemSelectedListener(new CategorySelectedListener());
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
        productList = getMatchingProducts(categorySpinner.getSelectedItemPosition());
        adapter.notifyDataSetChanged();
        adapter.setNewValuesAndNotify(productList);

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
        if (s.length() <= inputNameLastLength) {
            productList = getMatchingProducts(categorySpinner.getSelectedItemPosition());
            adapter.setNewValuesAndNotify(productList);
        } else {
            adapter.getFilter().filter(s.toString());
        }
    }

    @OnItemSelected(R.id.spinnerCategory)
    public void onItemSelected(int position) {
        productList = getMatchingProducts(position);
        adapter.setNewValuesAndNotify(productList);
    }

    private void createMinMaxPriceDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.edit_prices_range,
                (ViewGroup) getActivity().findViewById(R.id.linearLayout));
        min = (EditText) layout.findViewById(R.id.editMin);
        max = (EditText) layout.findViewById(R.id.editMax);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    minPrice = Double.parseDouble(min.getText().toString().replace(',', '.'));
                    if (minPrice < 0) minPrice = 0;
                } catch (NumberFormatException e) {
                    minPrice = 0;
                }
                try {
                    maxPrice = Double.parseDouble(max.getText().toString().replace(',', '.'));
                    if (maxPrice < minPrice) maxPrice = minPrice + 100;
                } catch (NumberFormatException e) {
                    maxPrice = minPrice + 100;
                }
                editMaxPrice.setText(String.format("%.2f", maxPrice));
                editMinPrice.setText(String.format("%.2f", minPrice));
                productList = getMatchingProducts(categorySpinner.getSelectedItemPosition());
                adapter.setNewValuesAndNotify(productList);
                alertDialog.dismiss();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                alertDialog.dismiss();
            }
        });
    }

    private class OnMinMaxClickedListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            min.setText(String.format("%.2f", minPrice));
            max.setText(String.format("%.2f", maxPrice));
            alertDialog.show();
        }
    }
//
//    private class ProductNameTextWatcher implements TextWatcher {
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if( s.length() <= inputNameLastLength){
//                adapter.setNewValuesAndNotify(productDAO.getAllProducts());
//            } else {
//                adapter.getFilter().filter(s.toString());
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {}
//    }
//
//    private class CategorySelectedListener implements OnItemSelectedListener{
//
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            if( position == CATEGORY_ALL_POSITION){
//                productList = productDAO.getAllProducts();
//            } else {
//                productList = productDAO.getProductsByCategory(categoryDAO.getCategoryByName(categories.get(position)));
//            }
//            //adapter.setNewValuesAndNotify(productList);
//            adapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {}
//    }


//    @OnFocusChange(R.id.editMinPrice)
//    void onMinPriceChanged(boolean focused) {
//        if (!focused) {
//            try {
//                minPrice = Double.parseDouble(editMinPrice.getText().toString());
//                if (minPrice < 0) minPrice = 0;
//                editMinPrice.setText(String.format("%.2f", minPrice));
//                editMinPrice.on
//            } catch (NumberFormatException e) {
//                minPrice = 0;
//                editMinPrice.setText(String.format("%.2f", minPrice));
//            }
//        }
//        productList = getMatchingProducts(categorySpinner.getSelectedItemPosition());
//        adapter.setNewValuesAndNotify(productList);
//    }
//
//    @OnFocusChange(R.id.editMaxPrice)
//    void onMaxPriceChanged(boolean focused) {
//        if (!focused) {
//            try {
//                maxPrice = Double.parseDouble(editMaxPrice.getText().toString());
//                if (maxPrice < minPrice) maxPrice = minPrice + 100;
//                editMaxPrice.setText(String.format("%.2f", maxPrice));
//            } catch (NumberFormatException e) {
//                maxPrice = minPrice + 100;
//                editMaxPrice.setText(String.format("%.2f", maxPrice));
//            }
//        }
//        productList = getMatchingProducts(categorySpinner.getSelectedItemPosition());
//        adapter.setNewValuesAndNotify(productList);
//    }


    private List<Product> getMatchingProducts(int categoryPosition) {
        if (categoryPosition == CATEGORY_ALL_POSITION) {
            return productDAO.getProductsInPriceRange(minPrice, maxPrice);
        } else {
            return productDAO.getProductsByCategoryInPriceRange(categoryDAO.getCategoryByName(categories.get(categoryPosition)), minPrice, maxPrice);
        }
    }
}
