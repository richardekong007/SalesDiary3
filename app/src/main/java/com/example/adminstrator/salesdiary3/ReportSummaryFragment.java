package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 9/20/2016.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;

import java.util.Locale;

public class ReportSummaryFragment extends AppCompatActivity implements ActionBar.TabListener
{




        /**
         * The {@link android.support.v4.view.PagerAdapter} that will provide
         * fragments for each of the sections. We use a
         * {@link FragmentPagerAdapter} derivative, which will keep every
         * loaded fragment in memory. If this becomes too memory intensive, it
         * may be best to switch to a
         * {@link android.support.v4.app.FragmentStatePagerAdapter}.
         */
        SectionsPagerAdapter mSectionsPagerAdapter;

        /**
         * The {@link ViewPager} that will host the section contents.
         */
        ViewPager mViewPager;
    private String title;
    private String queryString;
    private String tableName;
    private static final int [] COLORS={Color.rgb(50,50,220), Color.rgb(120,250,20), Color.rgb(220,150,20), Color.rgb(220,10,20)};
    private String[] products;
    private Double[] profit, sales, cost, loss;
    private databaseManager db;
    private SQLiteDatabase sdb;
    private Cursor cursor;
    private Double grandProfit, grandSales, grandCost, grandLoss;
    private BarChart barchart;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.reportsumarryfragment);

            //create ActionBar
            final ActionBar actionBar=getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                }
            });
            for (int i=0;i<mSectionsPagerAdapter.getCount();i++)
            {
                actionBar.addTab(actionBar.newTab().setText("\t"+mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
            }


        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.

            return true;
        }
        @Override
        public void onTabSelected(ActionBar.Tab tab , FragmentTransaction ft)
        {
            mViewPager.setCurrentItem(tab.getPosition());
        }
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
        {

        }
        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
        {

        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement


            return super.onOptionsItemSelected(item);
        }


        /**
         * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
         * one of the sections/tabs/pages.
         */
        public class SectionsPagerAdapter extends FragmentPagerAdapter {

            public SectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                // getItem is called to instantiate the fragment for the given page.
                // Return a PlaceholderFragment (defined as a static inner class below).
                Fragment fragment=null;
                Bundle data=getIntent().getExtras();
                switch(position)
                {
                    case 0:
                        fragment=new InflowSummaryReport();
                        fragment.setArguments(data);
                        break;
                    case 1:
                        fragment=new OutFlowSummaryReport();
                        fragment.setArguments(data);
                        break;
                    case 2:
                        fragment=new productProfitSaleSummary();
                        fragment.setArguments(data);
                        break;


                }
                return fragment;
            }

            @Override
            public int getCount() {
                // Show 5 total pages.
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                Locale l = Locale.getDefault();
                switch (position) {
                    case 0:
                        return getString(R.string.inflowsummaryreport).toUpperCase(l);
                    case 1:
                        return getString(R.string.outflowsummaryreport).toUpperCase(l);
                    case 2:
                        return "Product Info".toUpperCase(l);

                }
                return null;
            }

        }

    }

