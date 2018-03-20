package com.example.adminstrator.salesdiary3;

import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adminstrator on 9/20/2016.
 */
public class OutFlowSummaryReport extends Fragment {
    //instance variable declaration
    private String title;
    private String queryString;
    private String tableName;
    private int [] COLOR={Color.rgb(255,123,0), Color.rgb(30,120,10), Color.rgb(255,70,10), Color.rgb(90,150,200), Color.rgb(25,200,150)};
    private String[] products;
    private Double[] profit, sales, cost, loss;
    private databaseManager db;
    private SQLiteDatabase sdb;
    private Cursor cursor;
    private Double grandProfit, grandSales, grandCost, grandLoss;
    private PieChart pieChart;
    private TextView chartTitle;
    private String[] label={"Cost","loss"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle richy)
    {
       final View v=inflater.inflate(R.layout.outflowreportsummary,container,false);

        //Instantiating the data base manager
        db=new databaseManager(v.getContext());
        sdb=db.createWritableDb();

        //obtain all bundle objects from previous activities
        Bundle data=this.getArguments();
        tableName=data.getString("SALES RECORD TABLE");
        queryString=data.getString("QUERY");
        title=data.getString("TITLE");
        //excute query string from previous activity
        cursor=sdb.rawQuery(queryString,null);
        products=new String[cursor.getCount()];
        profit=new Double[cursor.getCount()];
        sales=new Double[cursor.getCount()];
        cost=new Double[cursor.getCount()];
        loss=new Double[cursor.getCount()];
        //loop through the cursor
        int index=0;
        if (cursor!=null && cursor.getCount()>0)
        {
            cursor.moveToFirst();
            do
            {
                //Populate the following arrays with its respective value from the database
                cost[index]=cursor.getDouble(cursor.getColumnIndexOrThrow("TOTALCOST"));
                loss[index]=cursor.getDouble(cursor.getColumnIndexOrThrow("TOTALLOSS"));
                index++;
            }
            while(cursor.moveToNext());
        }

        //grand total of profit, sales, cost and loss respectively
        grandCost=sum(cost);
        grandLoss= Math.abs(sum(loss));

        //Instantiate textViews
                chartTitle=(TextView)v.findViewById(R.id.charttitle);
                TextView costField=(TextView)v.findViewById(R.id.costfield),
                lossField=(TextView)v.findViewById(R.id.lossfield);

        //set title
        chartTitle.setText(title);
        //set Grand values to TextView
        costField.setText(grandCost.toString());
        lossField.setText(grandLoss.toString());

        pieChart= (PieChart)v.findViewById(R.id.pie);
        //enable rotation for the pie chart
        pieChart.setRotationEnabled(true);


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e==null)
                    return;
                Toast.makeText(v.getContext(),h.getDataIndex()+" is "+e.getY()+" ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        createPieChart();
        return v;
    }

    public void createPieChart()
    {
        //create pie entries
        List<PieEntry> Entries=new ArrayList<>();
        List<String> products=new ArrayList<>();
        //populate the cost and loss entries with data
        Entries.add(new PieEntry(grandCost.intValue(),label[0]));
        Entries.add(new PieEntry(grandLoss.intValue(),label[1]));

        //create pie data set
        String pieCenterPhrase=chartTitle.getText().toString().substring(0,chartTitle.getText().toString().indexOf(" "))+" Outflow";
        PieDataSet set=new PieDataSet(Entries,pieCenterPhrase);
        //set properties for the piedata set
        set.setSliceSpace(3);
        set.setSelectionShift(5);
        //add colors to pieChart
        ArrayList<Integer> colors=new ArrayList<Integer>();
        for (int c: COLOR)
            colors.add(c);
        set.setColors(colors);

        PieData data=new PieData(set);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        //undo all highligth
        pieChart.highlightValue(null);
        //update or refresh piechart
        pieChart.invalidate();
        pieChart.setTouchEnabled(true);

        //animate piechart
        pieChart.animateXY(1400,1400);
        //set text at center
        pieChart.setCenterText(pieCenterPhrase);
        pieChart.setCenterTextSize(10f);
        //create Legend
        Legend l=pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
        l.setTextColor(Color.BLACK);
        l.setTextSize(40f);
        l.setForm(Legend.LegendForm.CIRCLE);

    }
    public Double sum(Double[] array)
    {
        Double total=0.0;
        if (array.length<1)
            throw new ArrayIndexOutOfBoundsException();
        for (int i=0;i<array.length;i++)
        {
            total+=array[i];
        }
        return total;
    }
    @Override
    public void onConfigurationChanged(final Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
    }

}
