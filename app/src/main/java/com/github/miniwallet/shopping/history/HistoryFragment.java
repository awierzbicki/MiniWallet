package com.github.miniwallet.shopping.history;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.miniwallet.EntityListAdapter;
import com.github.miniwallet.R;
import com.github.miniwallet.db.daos.ProductDAO;
import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.ProductDAOImpl;
import com.github.miniwallet.db.daos.impl.PurchaseDAOImpl;
import com.github.miniwallet.shopping.Purchase;
import com.github.miniwallet.shopping.experimental.ViewHolder;
import com.github.miniwallet.shopping.purchase.EditProductActivity;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;

/**
 * Created by Agnieszka on 2015-05-22.
 */
public class HistoryFragment extends Fragment {

    private enum SortingTypes {
        PRICE_DESC, PRICE, DATE, DATE_DESC
    }

    private ProductDAO productDAO;
    private PurchaseDAO purchaseDAO;
    private EntityListAdapter<Purchase> adapter;
    private List<Purchase> purchaseList;

    private Date startDate;

    private AlertDialog alertDialog;


    @InjectView(R.id.purchaseList)
    ListView listView;
    @InjectView(R.id.spinnerSortBy)
    Spinner sortBySpinner;


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
        listView.setOnItemLongClickListener(new EditItemListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TO DO
            }
        });
        ArrayAdapter<SortingTypes> adapterState = new ArrayAdapter<SortingTypes>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, SortingTypes.values());
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapterState);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode + " " + resultCode);
        //TO DO
    }

    private class EditItemListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Purchase purchase = adapter.getItem(position);
            Intent intent = new Intent(getActivity(), EditProductActivity.class);
            //startActivityForResult(intent, EDIT);
            return true;
        }
    }

    @OnItemLongClick(R.id.purchaseList)
    public boolean onPurchaseLongClick(int positon) {
        return false;
    }

    @OnClick(R.id.buttonToday)
    public void onTodayClick() {
        loadPurchaseFromTimeAgoToNow(1);
    }

    @OnClick(R.id.buttonWeek)
    public void onWeekClick() {
        loadPurchaseFromTimeAgoToNow(7);
    }

    @OnClick(R.id.buttonMonth)
    public void onMonthClick() {
        loadPurchaseFromTimeAgoToNow(31);
    }

    @OnClick(R.id.buttonYear)
    public void onYearClick() {
        loadPurchaseFromTimeAgoToNow(365);
    }


    private void loadPurchaseFromTimeAgoToNow(int days) {
        Date now = new Date();
        Date timeAgo = new Date();

        timeAgo.setTime(now.getTime() - 1000 * 60 * 60 * 24 * days);
        System.out.println(now.getTime() - 1000 * 60 * 60 * 24 * days + "    " + timeAgo.toString() + " days=" + days);
        purchaseList = purchaseDAO.getPurchasesBetween(timeAgo, now);
        adapter.setNewValuesAndNotify(purchaseList);
    }

    @OnItemSelected(R.id.spinnerSortBy)
    public void onItemSelected(int position) {
//        purchaseList = getMatchingProducts(position);
//        adapter.setNewValuesAndNotify(purchaseList);
    }


//    private List<Purchase> getMatchingProducts(int categoryPosition) {
//        return purchaseDAO.getPurchasesBetween()
//    }
}
