package com.github.miniwallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.miniwallet.shopping.experimental.ViewHolder;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public class MainFragment extends Fragment {

    ProductDAO productDAO;
    CategoryDAO categoryDAO;
    PurchaseDAO purchaseDAO;
    Category defaultCategory;
    EntityListAdapter<Product> adapter;
    List<Product> productList;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);
        setActualTotal();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productList = productDAO.getMostBoughtProducts(5);
                Product product = productList.get(position);
                Purchase purchase = new Purchase(product.getLastPrice(), product, new LatLng(1.1, 2.2), new Date());
                purchaseItem(purchase);
            }
        });
        adapter.notifyDataSetChanged();
        System.out.println("onCreateView");
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        purchaseDAO = new PurchaseDAOImpl();
        defaultCategory = new Category("default");
        productList = productDAO.getMostBoughtProducts(5);
        adapter = new EntityListAdapter<>(getActivity(), productList,
                ViewHolder.Type.BEST_SELLING_ROW);
    }

    @OnClick(R.id.button)
    public void onExampleButtonClick(Button button) {
        Toast.makeText(getActivity(), editText.getText().toString(), Toast.LENGTH_SHORT).show();
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


    public double getWeeklyExpenses() {
        ArrayList<Purchase> weeklyPurchases;
        Date now = new Date();
        Date weekAgo = new Date();
        double expencesSum = 0;
        weekAgo.setTime(now.getTime() - 1000 * 60 * 60 * 24 * 7);
        return purchaseDAO.getExpensesBetween(weekAgo, now);
    }

    public double getDailyExpenses() {
        ArrayList<Purchase> dailyPurchases;
        Date now = new Date();
        Calendar c = new GregorianCalendar();
        double expencesSum = 0;

        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date startOfDay = c.getTime();

        return purchaseDAO.getExpensesBetween(startOfDay, now);
    }

    public void purchaseItem(Purchase purchase) {
        purchaseDAO.insertPurchase(purchase);
        productList = (ArrayList) productDAO.getMostBoughtProducts(5);
        setActualTotal();
        adapter.setNewValuesAndNotify(productList);
    }

    public void setActualTotal() {
        String info = "Today : " + String.format("%.2f", getDailyExpenses());
        textView.setText((CharSequence) info);
    }

}
