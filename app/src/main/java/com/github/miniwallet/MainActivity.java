package com.github.miniwallet;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity {

    ProductDAO productDAO;
    CategoryDAO categoryDAO;
    PurchaseDAO purchaseDAO;
    Category defaultCategory;

    @InjectView(R.id.button)
    Button exampleButton;

    @InjectView(R.id.editText)
    EditText editText;

    @InjectView(R.id.editText2)
    EditText editText2;

    @InjectView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        purchaseDAO = new PurchaseDAOImpl();
        defaultCategory = new Category("default");
        ButterKnife.inject(this);
    }

    @OnClick(R.id.button)
    public void onExampleButtonClick(Button button) {
        Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
        String name = editText.getText().toString();
        String priceText = editText2.getText().toString();
        double price;
        if (priceText.isEmpty())
           price = 0;
        else
            price = Double.parseDouble(priceText);
        Purchase purchase = new Purchase(price, new Product(defaultCategory, price, name, 0), new LatLng(1.1, 2.2), new Date());
        purchaseDAO.insertPurchase(purchase);
        Product newProduct = productDAO.getMostBoughtProducts(1).get(0);
        textView.setText((CharSequence) (newProduct.getName() + newProduct.getTotalPurchases()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
