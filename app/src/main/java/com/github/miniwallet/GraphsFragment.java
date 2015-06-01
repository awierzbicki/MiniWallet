package com.github.miniwallet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.miniwallet.adapters.EntityListAdapter;
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
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

    static final Comparator<Product> COUNT_ORDER =
            new Comparator<Product>() {
                @Override
                public int compare(Product lhs, Product rhs) {
                    return Integer.compare(lhs.getTotalPurchases(), rhs.getTotalPurchases());
                }
            };

    @InjectView(R.id.graphsListView)
    ListView listView;

    @InjectView(R.id.loadingPanel)
    ProgressBar loadingPanel;

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
        setColorOfProgressBar(loadingPanel, 0xFFC107);
        loadingPanel.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0x0, 0xFFC107));
        return rootView;
    }

    public void setUp() {

        list = new ArrayList<ChartItem>();

        monthExpensesChart = new LineChartItem(generateDataLine(), getActivity());
        productCountChart = new BarChartItem(generateDataBar(), getActivity());
        categoryPercentageChart = new PieChartItem(generateCategoriesDataPie(), getActivity());

        list.add(monthExpensesChart);
        list.add(productCountChart);
        list.add(categoryPercentageChart);

        if(getActivity() != null) {
            ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
            listView.setAdapter(cda);
        }

    }

    public void deleteGraphs() {
        if(list != null){
            list.clear();
        }

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
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        ArrayList<Date> monthDates = getMonthsDates();
        int sum = 0;
        float curr;
        for (int i = 0; i < 12; i++) {
            curr = (float)(purchaseDAO.getExpensesBetween(monthDates.get(i), monthDates.get(i+1)));
            sum += curr;
            e1.add(new Entry(curr, i));
        }
        for (int i = 0; i < 12; i++) {
            e2.add(new Entry(sum/6,i));
        }

        LineDataSet d1 = new LineDataSet(e1, "Year expenses");
        LineDataSet d2 = new LineDataSet(e2, "Average");

        d1.setLineWidth(2.5f);
        d1.setDrawCircles(false);
        d1.setColor(Color.parseColor("#8BC34A"));
        d1.setDrawValues(true);
        d1.setValueTextSize(10f);

        d2.setLineWidth(1.5f);
        d2.setColor(Color.parseColor("#F4511E"));
        d2.setDrawValues(false);
        d2.setDrawCircles(false);

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(getMonths(), sets);
        return cd;
    }

    private BarData generateDataBar() {

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Product> mostBought = (ArrayList)productDAO.getMostBoughtProducts(10);
        Collections.sort(mostBought, COUNT_ORDER);
        Collections.reverse(mostBought);

        ArrayList<Integer> counts = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        for (Product p : mostBought) {
            counts.add(p.getTotalPurchases());
            names.add(p.getName());
        }

        for (int i = 0; i < counts.size(); i++) {
            entries.add(new BarEntry(counts.get(i), i));
        }

        BarDataSet d = new BarDataSet(entries, "Most popular products");
        d.setBarSpacePercent(40f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        
        d.setColors(colors);

        d.setHighLightAlpha(255);
        d.setValueTextSize(10f);
        return new BarData(names, d);
    }

    private PieData generateCategoriesDataPie() {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Purchase> purchases = (ArrayList)purchaseDAO.getAllPurchases();
        ArrayList<String> categories = (ArrayList)categoryDAO.getAllCategoriesNames();
        ArrayList<String> categoriesToRemove = new ArrayList<>();
        int i = 0;
        float other = 0;
        double fivePercent = purchaseDAO.getExpensesFrom(new Date(0)) / 20.0;
        for (String category : categories) {
            float sum = 0;
            for (Purchase pur : purchases) {
                if (pur.getProduct().getCategory().getName().equals(category))
                    sum += pur.getPrice();
            }
            if(sum > fivePercent) {
                entries.add(new Entry(sum, i++));
            }
            else {
                categoriesToRemove.add(category);
                other += sum;
            }
        }

        categories.removeAll(categoriesToRemove);
        categories.add("Others");
        entries.add(new Entry(other, i));

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
    public static void setColorOfProgressBar(ProgressBar mProgressBar, int mColor){
        mProgressBar.getIndeterminateDrawable().setColorFilter(mColor, PorterDuff.Mode.MULTIPLY);
    }

}
