package com.github.miniwallet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.miniwallet.adapters.EntityListAdapter;
import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.PurchaseDAOImpl;
import com.github.miniwallet.shopping.Purchase;
import com.github.miniwallet.shopping.experimental.ViewHolder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public class HistoryFragment extends Fragment implements PagingListView.PagingListener {

    private enum SortingType {
        PRICE("Lowest price", "price"), PRICE_DESC("Highest price", "price DESC"), DATE("Latest date", "date DESC"), DATE_DESC("Earliest date", "date");

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

    private static final int ITEM_PER_PAGE = 50;

    private PurchaseDAO purchaseDAO;
    private EntityListAdapter<Purchase> adapter;
    private List<Purchase> purchaseList;
    private Date startDate;
    private long lastItemIndex = 0;
    private long maxIndex;

    private Date endDate;
    private SortingType sortingType = SortingType.DATE;
    @InjectView(R.id.purchaseList)
    PagingListView<Purchase> pagingListView;


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
        purchaseDAO = new PurchaseDAOImpl();
        adapter = new EntityListAdapter(getActivity(), purchaseList, ViewHolder.Type.HISTORY_ROW);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.inject(this, rootView);
        pagingListView.setAdapter(adapter);
        pagingListView.setListener(this);
        ArrayAdapter<SortingType> adapterState = new ArrayAdapter<SortingType>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, SortingType.values());
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapterState);
        onTodayClick();
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
        setButtonsAlpha((float) 0.3);
        buttonToday.setAlpha(1);
    }

    @OnClick(R.id.buttonWeek)
    public void onWeekClick() {

        Log.i("button Week", "lastIndex=" + lastItemIndex + "maxIndex=" + maxIndex);
        loadPurchaseFromTimeAgo(7, Calendar.DAY_OF_MONTH);
        setButtonsAlpha((float) 0.3);
        buttonWeek.setAlpha(1);
    }

    @OnClick(R.id.buttonMonth)
    public void onMonthClick() {
        loadPurchaseFromTimeAgo(1, Calendar.MONTH);
        setButtonsAlpha((float) 0.3);
        buttonMonth.setAlpha(1);
    }

    @OnClick(R.id.buttonYear)
    public void onYearClick() {
        loadPurchaseFromTimeAgo(1, Calendar.YEAR);
        setButtonsAlpha((float) 0.3);
        buttonYear.setAlpha(1);
    }

    private void loadPurchaseFromTimeAgo(int value, int calendarField) {
        Calendar cal = Calendar.getInstance();
        endDate = cal.getTime();
        cal.roll(calendarField, (-1) * value);
        startDate = cal.getTime();
        validate();
    }


    @OnItemSelected(R.id.spinnerSortBy)
    public void onItemSelected(int position) {
        sortingType = SortingType.values()[position];
        Log.i("HistoryFragment", sortingType.getCommand());
        validate();
    }

    private void validate() {
        lastItemIndex = 0;
        Log.i("HistoryFragment", "startDate=" + startDate + ", endDate=" + endDate);
        purchaseList = purchaseDAO.getSortedPurchasesBetween(startDate, endDate, sortingType.getCommand(), ITEM_PER_PAGE, 0);
        maxIndex = purchaseDAO.getPurchasesTotalNumber(startDate, endDate);
        adapter.setNewValuesAndNotify(purchaseList);
    }

    private void setButtonsAlpha(float alpha) {
        buttonToday.setAlpha(alpha);
        buttonMonth.setAlpha(alpha);
        buttonWeek.setAlpha(alpha);
        buttonYear.setAlpha(alpha);
    }

    private class PurchaseLoading extends AsyncTask<String, Void, List<Purchase>> {

        @Override
        protected List<Purchase> doInBackground(String... params) {
            Log.i("Purchase_Loading", params[0]);
            return purchaseDAO.getSortedPurchasesBetween(startDate, endDate, sortingType.getCommand(), ITEM_PER_PAGE, lastItemIndex);
        }

        @Override
        protected void onPostExecute(List<Purchase> purchases) {
            if (isCancelled()) {
                purchases = null;
            }
            super.onPostExecute(purchases);
            pagingListView.addNewData(purchases);
        }
    }

    @Override
    public void loadData() {
        lastItemIndex = adapter.getCount();
        Log.i("Loading history", "lastIndex=" + lastItemIndex + "maxIndex=" + maxIndex);
        if (lastItemIndex >= maxIndex) {
            return;
        }
        //PurchaseLoading task = new PurchaseLoading();
        //task.execute(String.valueOf(lastItemIndex));
        Log.i("HistoryFragment", sortingType.getCommand());
        pagingListView.addNewData(purchaseDAO.getSortedPurchasesBetween(startDate, endDate, sortingType.getCommand(), ITEM_PER_PAGE, lastItemIndex));
    }
}
