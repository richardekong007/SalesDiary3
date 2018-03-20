package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 9/17/2016.
 */

import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class ReportSummary extends AppCompatActivity
{
    //instance variable declaration
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
    //override the oncreate menthod
    @Override
    protected void onCreate(Bundle b)
    {
        super.onCreate(b);
        setContentView(R.layout.reportsummary);
        //instantiate the database manager
        db=new databaseManager(this);
        //instantiate SqliteDatabase
        sdb=db.createWritableDb();
        //obtain all bundle objects from previous activities
        Bundle b1=getIntent().getExtras();
        tableName=b1.getString("SALES RECORD TABLE");
        queryString=b1.getString("QUERY");
        title=b1.getString("TITLE");
        cursor=sdb.rawQuery(queryString,null);
        //create all required arrays
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

                products[index]=cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));
                profit[index]=cursor.getDouble(cursor.getColumnIndexOrThrow("TOTALPROFIT"));
                sales[index]=cursor.getDouble(cursor.getColumnIndexOrThrow("TOTALSALES"));
                cost[index]=cursor.getDouble(cursor.getColumnIndexOrThrow("TOTALCOST"));
                loss[index]=cursor.getDouble(cursor.getColumnIndexOrThrow("TOTALLOSS"));
                index++;
            }
            while(cursor.moveToNext());
            }
        //grand total of profit, sales, cost and loss respectively
            grandProfit=sum(profit);
            grandSales=sum(sales);
            grandCost=sum(cost);
            grandLoss=sum(loss);
        //Instantiate textViews
        TextView chartTitle=(TextView)findViewById(R.id.charttitle),
                profittext=(TextView)findViewById(R.id.profitField),
                salesField=(TextView)findViewById(R.id.salesfield),
                costfield=(TextView)findViewById(R.id.costfield),
                lossfield=(TextView)findViewById(R.id.lossfield);

        //set title
        chartTitle.setText(title);
        //set Grand values to TextView
        profittext.setText(grandProfit.toString());
        salesField.setText(grandSales.toString());
        costfield.setText(grandCost.toString());
        lossfield.setText(grandLoss.toString());
        //create barchart
        barchart= (BarChart) findViewById(R.id.bargraph);
        barchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e==null)
                    return;
                Toast.makeText(ReportSummary.this,products[h.getDataSetIndex()]+" is "+e.getData()+" ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        setBarChartData();
        }
        public void setBarChartData()
        {
           //creating four List to represent 4 groups of barentries
            List<BarEntry> profitEntries=new ArrayList<>(),
            salesEntries=new ArrayList<>(),
            costEntries=new ArrayList<>(),
            lossEntries=new ArrayList<>();
            //populate these Lists with contents from profit, sales, cost and Loss Arrays
            for (int i=0;i<profit.length;i++)
            {
                profitEntries.add(new BarEntry(i,profit[i].intValue()));
                salesEntries.add(new BarEntry(i,sales[i].intValue()));
                costEntries.add(new BarEntry(i,cost[i].intValue()));
                lossEntries.add(new BarEntry(i,loss[i].intValue()));
            }
            //create four bar dataset objects
            BarDataSet set1=new BarDataSet(profitEntries,"Profit"),
                    set2=new BarDataSet(salesEntries,"Sales"),
                      set3=new BarDataSet(costEntries,"Cost"),
                    set4=new BarDataSet(lossEntries,"Loss");
            //set colors for each set
            set1.setColor(COLORS[0]);
            set2.setColor(COLORS[1]);
            set3.setColor(COLORS[2]);
            set4.setColor(COLORS[3]);
            //create and assign value for groupSpace, barSpace and barWidth
            float groupSpace=0.01f,
                    barspace=0.02f,
                    barWidth=0.15f;
            //create barData object
            BarData data=new BarData(set1,set2,set3,set4);
            //set width for each bar
            data.setBarWidth(barWidth);
            //set data for the barchart
            barchart.setData(data);
            //Group bars
            barchart.groupBars(0.01f,groupSpace,barspace);

            //Refresh the Barchart
            barchart.invalidate();
            //enable certain interactions with chart
            barchart.setTouchEnabled(true);
            barchart.setDragEnabled(true);
            barchart.setScaleEnabled(true);
            barchart.fitScreen();

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
        }
        public void setPieChart()
        {

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

class MyXAxisFormatter implements IAxisValueFormatter
{
    //declare instance variable
    private String[] mlabel;
    //create constructor
    public MyXAxisFormatter(String[] label)
    {
        this.mlabel=label;
    }
    @Override
    public String getFormattedValue(float value, AxisBase axisBase)
    {
        return mlabel[(int)value];
    }
     public int getDecimalDigits()
    {
        return 0;
    }
}

