package com.github.miniwallet;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.astuetz.PagerSlidingTabStrip;
import com.astuetz.PagerSlidingTabStrip.IconTabProvider;
import com.github.mikephil.charting.utils.Utils;
import com.github.miniwallet.actions.purchase.PurchaseFragment;
import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.PurchaseDAOImpl;
import com.github.miniwallet.location.Locator;
import com.github.miniwallet.shopping.Purchase;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends FragmentActivity {
    private static final int PAGES_NUMBER = 5;
    private static final String TAG = "MAIN_ACTIVITY";
    private PagerAdapter pagerAdapter;
    private static final int GRAPH_PAGE = 0;
    private static final int MAIN_PAGE = 1;
    private static final int PURCHASE_PAGE = 2;
    private static final int HISTORY_PAGE = 3;
    private static final int MAP_PAGE = 4;
    private int actualPage = MAIN_PAGE;
    private MainFragment mainFragment;

    private PurchaseFragment purchaseFragment;
    private GraphsFragment graphsFragment;
    private HistoryFragment historyFragment;
    private PurchaseMapFragment purchaseMapFragment;

    @InjectView(R.id.pager)
    ViewPager pager;

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabsStrip;

    private Locator locator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window newWindow = this.getWindow();

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_screen_slide);
        Utils.init(this);
        ButterKnife.inject(this);

        locator = new Locator(this);
        locator.connect();
        Log.d(TAG, "upon creation location is " + locator.getLocation().latitude + locator.getLocation().longitude);

        insertBasePurchases();

        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(MAIN_PAGE);
        pager.setOnPageChangeListener(pageChangeListener);

        tabsStrip.setViewPager(pager);
        tabsStrip.setTabPaddingLeftRight(5);
        tabsStrip.setOnPageChangeListener(pageChangeListener);

        tabsStrip.setBackgroundColor(Color.parseColor("#689F38"));
        tabsStrip.setIndicatorColor(Color.parseColor("#DCEDC8"));
        newWindow.setStatusBarColor(Color.parseColor("#558B2F"));
        pager.setBackgroundColor(Color.parseColor("#F1F8E9"));



    }


    @Override
    protected void onStop() {
        super.onStop();

        locator.disconnect();
    }

    private void insertBasePurchases() {
        PurchaseDAO purchaseDAO = new PurchaseDAOImpl();
        if (purchaseDAO.getAllPurchases().isEmpty()) {
            for (Purchase p : PurchasesGenerator.randomPurchases(50)) {
                purchaseDAO.insertPurchase(p);
            }
        }
    }


    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            Log.d("Main activity", "POSITION " + position);
            switch (position) {

                case GRAPH_PAGE:
                    if (graphsFragment != null) {
                        graphsFragment.loadingPanel.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                graphsFragment.loadingPanel.setVisibility(View.GONE);
                                graphsFragment.setUp();
                            }
                        }, 2000);

                    }
                case MAIN_PAGE:
                    if (purchaseFragment != null) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mainFragment.updateList();
                                mainFragment.setActualTotal();
                                graphsFragment.deleteGraphs();
                            }
                        }, 200);


                    }
                case PURCHASE_PAGE:
                    if (purchaseFragment != null) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                purchaseFragment.validate();
                            }
                        }, 200);

                    }
                case HISTORY_PAGE:
                    if (historyFragment != null) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                historyFragment.onTodayClick();
                            }
                        }, 200);
                    }
            }

        }


        @Override
        public void onPageScrollStateChanged(int state) {
        }

    };


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter implements IconTabProvider{

        private int ICONS[] = {
                R.mipmap.ic_assessment_white_24dp,
                R.mipmap.ic_account_balance_white_24dp,
                R.mipmap.ic_add_shopping_cart_white_24dp,
                R.mipmap.ic_list_white_24dp,
                R.mipmap.ic_explore_white_24dp,
        };

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getPageIconResId(int i) {
            return ICONS[i];
        }

        @Override
        public Fragment getItem(int position) {
            System.out.println("Page: " + position);
            switch (position) {
                case MAIN_PAGE:
                    mainFragment = new MainFragment();
                    mainFragment.setLocator(locator);
                    return mainFragment;
                case PURCHASE_PAGE:
                    purchaseFragment = new PurchaseFragment();
                    purchaseFragment.setLocator(locator);
                    return purchaseFragment;
                case GRAPH_PAGE:
                    graphsFragment = new GraphsFragment();
                    return graphsFragment;
                case HISTORY_PAGE:
                    historyFragment = new HistoryFragment();
                    return historyFragment;
                case MAP_PAGE:
                    purchaseMapFragment = new PurchaseMapFragment();
                    return purchaseMapFragment;
            }
            return new PurchaseFragment();
        }
        @Override
        public int getCount() {
            return PAGES_NUMBER;
        }
    }
}
