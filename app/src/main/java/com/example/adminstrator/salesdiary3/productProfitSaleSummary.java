package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 9/21/2016.
 */

import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class productProfitSaleSummary extends Fragment
{
    //Instance variable declaration
    private String title;
    private String queryString;
    private String productQuery;
    private String tableName;
    private static final int [] COLORS={Color.rgb(50,50,220), Color.rgb(255,70,20), Color.rgb(220,150,20), Color.rgb(220,10,20)};
    private String[] products;

    private Double[] profit, sales;
    private databaseManager db;
    private SQLiteDatabase sdb;
    private Cursor cursor, distinctProduct;
    private BarChart barchart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle richy)
    {
        final View v=inflater.inflate(R.layout.productprofitsalessummary,container,false);
        //instantiate the database manager
        db=new databaseManager(v.getContext());
        //instantiate SqliteDatabase
        sdb=db.createWritableDb();
        //obtain all bundle objects from previous activities
        Bundle data=this.getArguments();
        tableName=data.getString("SALES RECORD TABLE");
        queryString=data.getString("QUERY");
        title=data.getString("TITLE");
        cursor=sdb.rawQuery(queryString,null);
        //get distinct products from querystring
        productQuery="SELECT DISTINCT DESCRIPTION FROM ("+queryString+")";
        distinctProduct=sdb.rawQuery(productQuery,null);
        //create all required arrays
        products=new String[distinctProduct.getCount()];
        profit=new Double[cursor.getCount()];
        sales=new Double[cursor.getCount()];

        //loop through the cursor
        int index=0;
        if (cursor!=null && cursor.getCount()>0)
        {
            cursor.moveToFirst();
            do
            {
                //Populate the following arrays with its respective value from the database

                //products[index]=cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));
                profit[index]=cursor.getDouble(cursor.getColumnIndexOrThrow("TOTALPROFIT"));
                sales[index]=cursor.getDouble(cursor.getColumnIndexOrThrow("TOTALSALES"));
                index++;
            }
            while(cursor.moveToNext());
        }
        //loop through distinctProduct
        int Productsize=0;
            if (distinctProduct!=null && distinctProduct.getCount()>0)
            {
                distinctProduct.moveToFirst();
                do
                {
                    products[Productsize]=distinctProduct.getString(distinctProduct.getColumnIndexOrThrow("DESCRIPTION"));
                    Productsize++;
                }
                while(distinctProduct.moveToNext());

            }



        //create barchart
        barchart= (BarChart)v.findViewById(R.id.bargraph);
        createBarChart();
        return v;
    }
    public void createBarChart()
    {
        //creating four List to represent 2 groups of barentries
        List<BarEntry> profitEntries=new ArrayList<>(),
                salesEntries=new ArrayList<>();
        //populate these Lists with contents from profit, sales, cost and Loss Arrays
        for (int i=0;i<profit.length;i++)
        {
            profitEntries.add(new BarEntry(i,profit[i].intValue()));
            salesEntries.add(new BarEntry(i,sales[i].intValue()));
        }

        //create two bar dataset objects
        BarDataSet set1=new BarDataSet(profitEntries,"Profit"),
                set2=new BarDataSet(salesEntries,"Sales");
        //add the bar data sets to the salesFigure IBardataSet
        List<IBarDataSet> salesFigures=new ArrayList<>();
        salesFigures.add(set1);
        salesFigures.add(set2);

        //set colors for each set
        set1.setColor(COLORS[0]);
        set2.setColor(COLORS[1]);

        //add profit and sales bar entries to salesfigure barentries big change 1


        //create and assign value for groupSpace, barSpace and barWidth
        float groupSpace=0.02f,
                barspace=0.03f,
                barWidth=0.35f;
        //create barData object  big change2
        BarData data=new BarData(salesFigures);
        //set format for data
        data.setValueFormatter(new MyValueFormatter());
        //set width for each bar
        data.setBarWidth(barWidth);
        //set data for the barchart
        barchart.setData(data);
        //Group bars
        barchart.groupBars(0,groupSpace,barspace);

        //Refresh the Barchart
        barchart.invalidate();
        //enable certain interactions with chart
        barchart.setTouchEnabled(true);
        barchart.setDragEnabled(true);
        barchart.setScaleEnabled(true);
        barchart.fitScreen();
        barchart.setHorizontalScrollBarEnabled(true);
        barchart.setVerticalScrollBarEnabled(true);
        //Animate barchart
        //barchart.animateXY(1400,1400);
        //create legend for the barcharts
        Legend legend=barchart.getLegend();
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        legend.setXEntrySpace(3);
        legend.setYEntrySpace(5);
        legend.setTextColor(Color.BLACK);
        legend.setForm(Legend.LegendForm.LINE);
        //set format for XAXIS
        XAxis xAxis=barchart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisFormatter(products));
        xAxis.setLabelRotationAngle(90f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);


    }

    @Override
    public void onConfigurationChanged(final Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
    }

}

