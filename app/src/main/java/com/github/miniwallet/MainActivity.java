package com.github.miniwallet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity {

    ProductDAO productDAO;
    CategoryDAO categoryDAO;
    PurchaseDAO purchaseDAO;
    Category defaultCategory;
    MostBuyedProductListAdapter adapter;
    List<Product> productList;
    List<Product> pl;

    @InjectView(R.id.button)
    Button exampleButton;

    @InjectView(R.id.editText)
    EditText editText;

    @InjectView(R.id.editText2)
    EditText editText2;

    @InjectView(R.id.textView)
    TextView textView;

    @InjectView(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        purchaseDAO = new PurchaseDAOImpl();
        defaultCategory = new Category("default");
        productList = productDAO.getAllProducts();
        adapter = new MostBuyedProductListAdapter(this, productList);
        String info = "";
        String dailyExpences = String.format("%.2f", getDailyExpenses());

        info += "Today : " + dailyExpences;
        textView.setText(info);
        
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productList = productDAO.getMostBoughtProducts(5);
                Product product = productList.get(position);
                Purchase purchase = new Purchase(product.getLastPrice(), product, new LatLng(1.1, 2.2), new Date());
                purchaseItem(purchase);
            }
        });
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
        purchaseItem(purchase);
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

    public double getWeeklyExpenses() {
        Date now = new Date();
        Date weekAgo = new Date();
        weekAgo.setTime(now.getTime() - 1000*60*60*24*7);

        return purchaseDAO.getExpensesBetween(weekAgo, now);
    }

    public double getDailyExpenses() {
        Date now = new Date();
        Calendar c = new GregorianCalendar();

        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date startOfDay = c.getTime();

        return purchaseDAO.getExpensesBetween(startOfDay, now);
    }
    
    public void purchaseItem(Purchase purchase) {
        purchaseDAO.insertPurchase(purchase);
        productList = productDAO.getMostBoughtProducts(5);
        String info = "";
        String dailyExpenses = String.format("%.2f", getDailyExpenses());
        info += "Today : " + dailyExpenses;
        textView.setText(info);

        adapter.clear();
        adapter.addAll(productList);
    }
}
