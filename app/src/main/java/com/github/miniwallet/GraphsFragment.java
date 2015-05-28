package com.github.miniwallet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.miniwallet.db.daos.CategoryDAO;
import com.github.miniwallet.db.daos.ProductDAO;
import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.CategoryDAOImpl;
import com.github.miniwallet.db.daos.impl.ProductDAOImpl;
import com.github.miniwallet.db.daos.impl.PurchaseDAOImpl;
import com.github.miniwallet.graphs.BarChartItem;
import com.github.miniwallet.graphs.ChartItem;
import com.github.miniwallet.graphs.LineChartItem;
import com.github.miniwallet.graphs.PieChartItem;
import com.github.miniwallet.shopping.Category;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GraphsFragment extends Fragment {
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private PurchaseDAO purchaseDAO;

    private EntityListAdapter<Product> adapter;
    private List<Product> productList;
    private ArrayList<String> categories;
    private ArrayList<ChartItem> list;

    private LineChartItem monthExpensesChart;
    private BarChartItem productCountChart;
    private PieChartItem categoryPercentageChart;
    @InjectView(R.id.graphsListView)
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        purchaseDAO = new PurchaseDAOImpl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_graphs, container, false);
        ButterKnife.inject(this, rootView);
        listView.setAdapter(adapter);

        ArrayAdapter<String> adapterState = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        list = new ArrayList<ChartItem>();
        monthExpensesChart = new LineChartItem(generateDataLine(), getActivity());
        productCountChart = new BarChartItem(generateDataBar(1), getActivity());
        categoryPercentageChart = new PieChartItem(generateCategoriesDataPie(), getActivity());
        list.add(monthExpensesChart);
        list.add(productCountChart);
        list.add(categoryPercentageChart);

        //fabricatePurchases(10, 10);

        ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
        listView.setAdapter(cda);

        return rootView;
    }

    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }

    private LineData generateDataLine() {

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        ArrayList<Date> monthDates = getMonthsDates();
        for (int i = 0; i < 12; i++) {
            e1.add(new Entry((float)(purchaseDAO.getExpensesBetween(monthDates.get(i), monthDates.get(i+1))), i));
        }

        LineDataSet d1 = new LineDataSet(e1, "Expenses");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(true);
        d1.setDrawCubic(true);
        d1.setDrawFilled(true);

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);

        LineData cd = new LineData(getMonths(), sets);
        return cd;
    }

    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry((int) (Math.random() * 70) + 30, i));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
        d.setBarSpacePercent(20f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(getMonths(), d);
        return cd;
    }

    private PieData generateCategoriesDataPie() {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Purchase> purchases = (ArrayList)purchaseDAO.getAllPurchases();
        ArrayList<String> categories = (ArrayList)categoryDAO.getAllCategoriesNames();
        int i = 0;
        for (String category : categories) {
            float sum = 0;
            for (Purchase pur : purchases) {
                if (pur.getProduct().getCategory().getName().equals(category))
                    sum += pur.getPrice();
            }
            entries.add(new Entry(sum, i++));
        }
        PieDataSet data = new PieDataSet(entries, "");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        data.setColors(colors);

        PieData categoriesDataPie = new PieData(categories, data);
        categoriesDataPie.setDrawValues(true);
        return categoriesDataPie;
    }

    private ArrayList<String> getMonths() {

        ArrayList<String> m = new ArrayList<String>();
        m.add("Jan");
        m.add("Feb");
        m.add("Mar");
        m.add("Apr");
        m.add("May");
        m.add("Jun");
        m.add("Jul");
        m.add("Aug");
        m.add("Sep");
        m.add("Oct");
        m.add("Nov");
        m.add("Dec");

        return m;
    }

    private ArrayList<Date> getMonthsDates() {
        ArrayList<Date> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for(int i = 0; i < 12; i++) {
            cal.set(Calendar.MONTH, i);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            dates.add(cal.getTime());
        }
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.YEAR, 1);
        dates.add(cal.getTime());
        return dates;
    }

    private void fabricatePurchases(int maxProductsPerMonth, int maxPricePerProduct) {
        Purchase purchase;
        Product product;
        Category categories[] = {new Category("Pieczywo"), new Category("Napoje"), new Category("default"), new Category("Słodycze"), new Category("Prasa"), new Category("Fast Food")};
        Random r = new Random();
        Date today = new Date();
        for(Date date : getMonthsDates()) {
            if(today.getTime() > date.getTime()) {
                for (int i = 0; i < r.nextInt(maxProductsPerMonth)+1; i++) {
                    int cat = r.nextInt(categories.length);
                    double price = (int)(maxPricePerProduct * r.nextDouble()) + 1;
                    product = new Product(categories[cat], 0, UUID.randomUUID().toString().substring(0,5), 0);
                    purchase = new Purchase(price , product , new LatLng(10.0, 11.0), date);
                    purchaseDAO.insertPurchase(purchase);
                    System.out.println("PURCHASE " + purchase.toString());
                }
            }
        }

    }

}
