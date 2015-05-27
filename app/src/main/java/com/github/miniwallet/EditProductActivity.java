package com.github.miniwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.miniwallet.db.daos.CategoryDAO;
import com.github.miniwallet.db.daos.ProductDAO;
import com.github.miniwallet.db.daos.impl.CategoryDAOImpl;
import com.github.miniwallet.db.daos.impl.ProductDAOImpl;
import com.github.miniwallet.shopping.Category;
import com.github.miniwallet.shopping.Product;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public class EditProductActivity extends Activity {
    @InjectView(R.id.newCategory)
    TextView productName;
    @InjectView(R.id.editProductPrice)
    EditText productPrice;
    @InjectView(R.id.chooseCategory)
    Spinner categorySpinner;

    ProductDAO productDAO;
    Product editingProduct;
    ArrayList<String> categoriesNames;
    private CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_purchase);

        ButterKnife.inject(this);
        productDAO = new ProductDAOImpl();
        System.out.println(getIntent().getStringExtra("ProductName") + ", ");
        editingProduct = productDAO.getProductByName(getIntent().getStringExtra("ProductName"));

        productName.setText(editingProduct.getName());
        productPrice.setText(Double.toString(editingProduct.getLastPrice()));
        categoryDAO = new CategoryDAOImpl();
        categoriesNames = categoryDAO.getAllCategoriesNames();
        setEditingProductCategoryFirst();
        ArrayAdapter<String> adapterState = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, categoriesNames);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapterState);
    }

    private void setEditingProductCategoryFirst() {
        //int index = categoriesNames.indexOf(editingProduct.getCategory().getName());
        System.out.println("Size= " + categoriesNames.size() + " , ");
        categoriesNames.remove(editingProduct.getCategory().getName());
        categoriesNames.add(0, editingProduct.getCategory().getName());
    }


    public void onClickOK(View view) {
        Intent returnIntent = new Intent();
        productDAO.modifyProductPrice(editingProduct, Double.parseDouble(productPrice.getText().toString()));
        Category newCategory = categoryDAO.getCategoryByName(categoriesNames.get(categorySpinner.getSelectedItemPosition()));
        productDAO.modifyProductCategory(editingProduct, newCategory);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void onClickCancel(View view) {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
