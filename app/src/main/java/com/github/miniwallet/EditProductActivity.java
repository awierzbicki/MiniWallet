package com.github.miniwallet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.miniwallet.db.daos.CategoryDAO;
import com.github.miniwallet.db.daos.ProductDAO;
import com.github.miniwallet.db.daos.impl.CategoryDAOImpl;
import com.github.miniwallet.db.daos.impl.ProductDAOImpl;
import com.github.miniwallet.graphs.LineChartItem;
import com.github.miniwallet.shopping.Category;
import com.github.miniwallet.shopping.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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
    @InjectView(R.id.productChart)
    LineChart productChart;

    ProductDAO productDAO;
    Product editingProduct;
    ArrayList<String> categoriesNames;
    private LineChartItem monthExpensesChart;
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

        YAxis yax = productChart.getAxisLeft();
        yax.setYOffset(20f);
        yax.setTextSize(15f);
        yax.setTextColor(Color.WHITE);
        yax.setGridColor(Color.WHITE);

        XAxis xax =  productChart.getXAxis();
        xax.setEnabled(false);
        xax.setXOffset(20f);

        productChart.setDescription("");

        Legend legend = productChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(18f);

        productChart.setDrawGridBackground(false);
        productChart.getAxisRight().setEnabled(false);
        productChart.setDoubleTapToZoomEnabled(false);
        productChart.setScaleEnabled(false);

        productChart.setData(generateDataLine());


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

    private LineData generateDataLine() {

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<>();
        int i = 0;
        for (Map.Entry<Date, Double> entry :
                productDAO.getPriceHistoryForProduct(editingProduct).entrySet()) {
            System.out.println(entry.getKey().toString() + entry.getValue());
            e1.add(new Entry(new Float(entry.getValue()), i));
            labels.add(entry.getKey().toString());
            i++;
        }

        LineDataSet d1 = new LineDataSet(e1, "Price change");
        d1.setLineWidth(2.5f);
        d1.setDrawCircles(true);
        d1.setDrawCircleHole(false);
        d1.setDrawValues(false);
        d1.setColor(Color.parseColor("#FFC107"));
        d1.setCircleColor(Color.parseColor("#FFB300"));

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);

        return new LineData(labels,sets);
    }

}
