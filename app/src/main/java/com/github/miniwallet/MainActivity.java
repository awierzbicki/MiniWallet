package com.github.miniwallet;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.astuetz.PagerSlidingTabStrip;
import com.github.mikephil.charting.utils.Utils;
import com.github.miniwallet.actions.purchase.PurchaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends FragmentActivity {
    private static final int PAGES_NUMBER = 5;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_screen_slide);
        Utils.init(this);
        ButterKnife.inject(this);

        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(MAIN_PAGE);
        pager.setOnPageChangeListener(getListener());

        tabsStrip.setViewPager(pager);
        tabsStrip.setTabPaddingLeftRight(25);
    }



    private ViewPager.OnPageChangeListener getListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*
                float appColor[]= {50.0f * (position + positionOffset), 0.5f, 0.9f};
                pager.setBackgroundColor(Color.HSVToColor(appColor));
                */
            }

            @Override
            public void onPageSelected(int position) {
                /*
                float appColor[]= {50.0f * (position), 0.5f, 0.9f};
                mPager.setBackgroundColor(Color.HSVToColor(appColor));
                */
                switch (position) {
                    case GRAPH_PAGE:
                    case MAIN_PAGE:
                        mainFragment.updateList();
                    case PURCHASE_PAGE:
                        if (purchaseFragment != null) {
                            purchaseFragment.validate();
                        }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        };
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            System.out.println("Page: " + position);
            switch (position) {
                case MAIN_PAGE:
                    mainFragment = new MainFragment();
                    return mainFragment;
                case PURCHASE_PAGE:
                    purchaseFragment = new PurchaseFragment();
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
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case MAIN_PAGE:
                    return "Start page";
                case PURCHASE_PAGE:
                    return "Products";
                case GRAPH_PAGE:
                    return "Graphs";
                case HISTORY_PAGE:
                    return "History";
                case MAP_PAGE:
                    return "Locations";
            }
            return "";
        }

        @Override
        public int getCount() {
            return PAGES_NUMBER;
        }
    }
}
