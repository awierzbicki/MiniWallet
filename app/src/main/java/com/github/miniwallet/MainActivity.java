package com.github.miniwallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    private static final int PAGES_NUMBER = 2;
    //@InjectView(R.id.pager)
    ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private static final int MAIN_PAGE = 0;
    private static final int PURCHASE_PAGE = 1;
    private int actualPage = MAIN_PAGE;
    private MainFragment mainFragment;
    private PurchaseFragment purchaseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case MAIN_PAGE:
                        mainFragment.setActualTotal();
                        Toast.makeText(getApplicationContext(), "main", Toast.LENGTH_SHORT).show();
                        mainFragment.updateList();
                    case PURCHASE_PAGE:
                        Toast.makeText(getApplicationContext(), "products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
            }
            return new PurchaseFragment();
        }

        @Override
        public int getCount() {
            return PAGES_NUMBER;
        }
    }


}
