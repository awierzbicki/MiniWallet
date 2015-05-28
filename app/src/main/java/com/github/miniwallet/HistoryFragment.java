package com.github.miniwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.miniwallet.adapters.EntityListAdapter;
import com.github.miniwallet.db.daos.ProductDAO;
import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.ProductDAOImpl;
import com.github.miniwallet.db.daos.impl.PurchaseDAOImpl;
import com.github.miniwallet.shopping.Purchase;
import com.github.miniwallet.shopping.experimental.ViewHolder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public class HistoryFragment extends Fragment {

    private enum SortingType {
        PRICE("Lowest price", "price"), PRICE_DESC("Highest price", " price DESC"), DATE("Latest data", "date DESC"), DATE_DESC("Earliest data", "date");

        private final String name;
        private final String command;

        private SortingType(String name, String command) {
            this.name = name;
            this.command = command;
        }

        private String getCommand() {
            return command;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private ProductDAO productDAO;
    private PurchaseDAO purchaseDAO;
    private EntityListAdapter<Purchase> adapter;
    private List<Purchase> purchaseList;

    private Date startDate;
    private Date endDate;
    private SortingType sortingType = SortingType.DATE;


    @InjectView(R.id.purchaseList)
    ListView listView;
    @InjectView(R.id.spinnerSortBy)
    Spinner sortBySpinner;
    @InjectView(R.id.buttonToday)
    Button buttonToday;
    @InjectView(R.id.buttonWeek)
    Button buttonWeek;
    @InjectView(R.id.buttonMonth)
    Button buttonMonth;
    @InjectView(R.id.buttonYear)
    Button buttonYear;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productDAO = new ProductDAOImpl();
        purchaseDAO = new PurchaseDAOImpl();
        adapter = new EntityListAdapter(getActivity(), purchaseList, ViewHolder.Type.HISTORY_ROW);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.inject(this, rootView);
        listView.setAdapter(adapter);
        ArrayAdapter<SortingType> adapterState = new ArrayAdapter<SortingType>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, SortingType.values());
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapterState);
        loadPurchaseFromTimeAgo(1, Calendar.DAY_OF_MONTH);

        buttonToday.setPressed(true);
        return rootView;
    }

    @OnItemClick(R.id.purchaseList)
    public void onPurchaseClick(int position) {
        Purchase purchase = purchaseList.get(position);
        Intent intent = new Intent(getActivity(), PurchaseMapActivity.class);
        intent.putExtra("PurchaseLat", purchase.getLocation().latitude);
        intent.putExtra("PurchaseLng", purchase.getLocation().longitude);
        startActivity(intent);
    }

    @OnClick(R.id.buttonToday)
    public void onTodayClick() {
        loadPurchaseFromTimeAgo(1, Calendar.DAY_OF_MONTH);
        setButtonsUnpressed();
        buttonToday.setPressed(true);
    }

    @OnClick(R.id.buttonWeek)
    public void onWeekClick() {
        loadPurchaseFromTimeAgo(7, Calendar.DAY_OF_MONTH);
        setButtonsUnpressed();
        buttonWeek.setPressed(true);
    }

    @OnClick(R.id.buttonMonth)
    public void onMonthClick() {
        loadPurchaseFromTimeAgo(1, Calendar.MONTH);
        setButtonsUnpressed();
        buttonMonth.setPressed(true);
    }

    @OnClick(R.id.buttonYear)
    public void onYearClick() {
        loadPurchaseFromTimeAgo(1, Calendar.YEAR);
        setButtonsUnpressed();
        buttonYear.setPressed(true);
    }


    private void loadPurchaseFromTimeAgo(int value, int calendarField) {
        Calendar cal = Calendar.getInstance();
        Calendar c = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        endDate = c.getTime();
        c.roll(calendarField, (-1) * value);
        startDate = c.getTime();
        validate();
    }

    @OnItemSelected(R.id.spinnerSortBy)
    public void onItemSelected(int position) {
        sortingType = SortingType.values()[position];
        validate();
    }

    private void validate() {
        purchaseList = purchaseDAO.getSortedPurchasesBetween(startDate, endDate, sortingType.getCommand());
        adapter.setNewValuesAndNotify(purchaseList);
    }

    private void setButtonsUnpressed() {
        buttonToday.setPressed(false);
        buttonMonth.setPressed(false);
        buttonWeek.setPressed(false);
        buttonYear.setPressed(false);
    }
}
