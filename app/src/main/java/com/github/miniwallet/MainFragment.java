package com.github.miniwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.miniwallet.adapters.EntityListAdapter;
import com.github.miniwallet.db.daos.CategoryDAO;
import com.github.miniwallet.db.daos.ProductDAO;
import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.CategoryDAOImpl;
import com.github.miniwallet.db.daos.impl.ProductDAOImpl;
import com.github.miniwallet.db.daos.impl.PurchaseDAOImpl;
import com.github.miniwallet.location.Locator;
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
import butterknife.OnItemSelected;

public class MainFragment extends Fragment {

    public static final String NEW_CATEGORY = "New category...";
    public static final int ADD = 0;

    private EntityListAdapter<Product> adapter;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private PurchaseDAO purchaseDAO;
    private Category defaultCategory;
    private List<Product> productList;
    private ArrayList<String> categories;
    private ArrayAdapter<String> adapterState;
    private Locator locator;

    @InjectView(R.id.editText)
    EditText editText;
    @InjectView(R.id.editText2)
    EditText editText2;
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.listView)
    ListView listView;
    @InjectView(R.id.spinner)
    Spinner spinner;

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
                Purchase purchase = new Purchase(product.getLastPrice(), product, locator.getLocation(), new Date());
                purchaseItem(purchase);
            }
        });
        adapterState = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categories);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterState);
        spinner.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int arg2, long arg3) {
                Toast.makeText(getActivity(), "!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        adapter.notifyDataSetChanged();
        System.out.println("onCreateView");
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
//
//        locator = new Locator(getActivity());
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        purchaseDAO = new PurchaseDAOImpl();
        defaultCategory = new Category("default");
        categoryDAO.insertCategory(defaultCategory);
        productList = productDAO.getMostBoughtProducts(5);
        adapter = new EntityListAdapter<>(getActivity(), productList,
                ViewHolder.Type.BEST_SELLING_ROW);
        adapterState = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        categories = categoryDAO.getAllCategoriesNames();
        categories.add(NEW_CATEGORY);
    }

    @OnClick(R.id.button)
    public void onClick(Button button) {
        Toast.makeText(getActivity(), editText.getText().toString(), Toast.LENGTH_SHORT).show();
        String name = editText.getText().toString();
        String priceText = editText2.getText().toString();
        double price;
        if (priceText.isEmpty())
            price = 0;
        else
            price = Double.parseDouble(priceText);

        Purchase purchase = new Purchase(price, new Product(new Category((String)spinner.getSelectedItem()), price, name, 0), locator.getLocation(), new Date());
        purchaseItem(purchase);
    }

    @OnItemSelected(R.id.spinner)
    public void onItemSelected(int position) {
        if (spinner.getSelectedItem().equals(NEW_CATEGORY)) {
            Toast.makeText(getActivity(), "nowa...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
            startActivityForResult(intent, ADD);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD && resultCode == AddCategoryActivity.OK) {
            if (data != null) {
                String result = data.getStringExtra(AddCategoryActivity.CATEGORY);
                Category category = new Category(result);
                categoryDAO.insertCategory(category);
                categories.remove(categories.size() - 1);
                categories.add(result);
                categories.add(NEW_CATEGORY);
                adapterState.notifyDataSetChanged();
                Toast.makeText(getActivity(), "DODANO: " + result, Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == ADD && resultCode == AddCategoryActivity.CANCEL) {
            spinner.setSelection(0);
        }
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
        updateList();
    }

    public void updateList() {
        productList = (ArrayList) productDAO.getMostBoughtProducts(5);
        setActualTotal();
        adapter.setNewValuesAndNotify(productList);
    }

    public void setActualTotal() {
        String info = "Today : " + String.format("%.2f", getDailyExpenses());
        textView.setText((CharSequence) info);
    }

    public void setLocator(Locator locator) {
        this.locator = locator;
    }
}
