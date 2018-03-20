package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 8/31/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SalesReport extends AppCompatActivity {

    private String tableName;
    private String queryString;
    private String titleString;
    private String customDate;
    private Cursor c;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.reportview);

        //get sales record table name
        Bundle Extra = getIntent().getExtras();
        tableName = Extra.getString("SALES RECORD TABLE");
        //get query string
        final Bundle extra = getIntent().getExtras();
        //get title string
        final Bundle extra1=getIntent().getExtras();
        //get custom date for custom report
        final Bundle extra2=getIntent().getExtras();
               customDate= extra2.getString("CUSTOMDATE");
        //instantiate header
        final TextView header=(TextView)findViewById(R.id.header);
        //instantiating listview for list of products sold
        final ListView salesList = (ListView)findViewById(R.id.reportlistLayout);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                databaseManager db = new databaseManager(SalesReport.this);
                SQLiteDatabase sqLiteDatabase = db.createWritableDb();
                //get queryString Bundle
                queryString = extra.getString("SALES_QUERY");
                //determine if the query string is empty or not
                try{
                    c = sqLiteDatabase.rawQuery(queryString, null);
                    //get title string Bundle to set the bundle of each report category
                    titleString=extra1.getString("TITLE");
                    //set titlestring as title of generted report category
                    header.setText(titleString);
                    //create customed cursor adapter and associate with a listView
                    customAdapter2 customCursorAdapter = new customAdapter2(SalesReport.this, c);
                    //determine if a report category has a record or not
                    if (c.getCount()<1)
                    {
                        final AlertDialog.Builder dialog=new AlertDialog.Builder(SalesReport.this);
                        dialog.setTitle("INFORMATION MESSAGE");
                        dialog.setIcon(R.mipmap.infowhite);
                        dialog.setMessage("Sorry, no Record was found for this time frame!");
                        dialog.setPositiveButton("OK",new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface d, int which)
                            {
                                d.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    else
                    {
                        salesList.setAdapter(customCursorAdapter);
                    }
                }
                catch(Exception e){}

            }
        });
        //create summary Button
        Button sumary = (Button)findViewById(R.id.summary);
        sumary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //link to Report summary class
                //query String
                String queryString;
                //create an intent
                Intent reportSummary = new Intent(SalesReport.this, ReportSummaryFragment.class);
                //determine if tableName is not null
                if (tableName==null || tableName.equals("")){
                    //determine the current sales report category
                    if (c.getCount() > 0) {
                        if (header.getText().toString().equals("Daily Report")) {
                            queryString = "SELECT DISTINCT DESCRIPTION,SUM(PROFIT) AS TOTALPROFIT,SUM(SALESPRICE)" +
                                    " AS TOTALSALES,SUM(COSTPRICE) AS TOTALCOST,SUM(LOSS) AS TOTALLOSS" +
                                    " FROM " + tableName + " WHERE DATETIME >'" + new salesRecord().getYesterday() + "'" +
                                    " GROUP BY DESCRIPTION";
                            String title = "Yesterday's Report";
                            reportSummary.putExtra("QUERY", queryString);
                            reportSummary.putExtra("TITLE", title);
                            startActivity(reportSummary);
                        } else if (header.getText().toString().equals("Weekly Report")) {
                            queryString = "SELECT DISTINCT DESCRIPTION,SUM(PROFIT) AS TOTALPROFIT,SUM(SALESPRICE)" +
                                    " AS TOTALSALES,SUM(COSTPRICE) AS TOTALCOST,SUM(LOSS) AS TOTALLOSS" +
                                    " FROM " + tableName + " WHERE DATETIME >'" + new salesRecord().getLastWeek() + "'" +
                                    " GROUP BY DESCRIPTION";
                            String title = "Weekly Report";
                            reportSummary.putExtra("QUERY", queryString);
                            reportSummary.putExtra("TITLE", title);
                            startActivity(reportSummary);

                        } else if (header.getText().toString().equals("Monthly Report")) {
                            queryString = "SELECT DISTINCT DESCRIPTION,SUM(PROFIT) AS TOTALPROFIT,SUM(SALESPRICE)" +
                                    " AS TOTALSALES,SUM(COSTPRICE) AS TOTALCOST,SUM(LOSS) AS TOTALLOSS" +
                                    " FROM " + tableName + " WHERE DATETIME >'" + new salesRecord().getLastMonth() + "'" +
                                    " GROUP BY DESCRIPTION";
                            String title = "Monthly Report";
                            reportSummary.putExtra("QUERY", queryString);
                            reportSummary.putExtra("TITLE", title);
                            startActivity(reportSummary);

                        } else if (header.getText().toString().equals("Quarter Report")) {
                            queryString = "SELECT DISTINCT DESCRIPTION,SUM(PROFIT) AS TOTALPROFIT,SUM(SALESPRICE)" +
                                    " AS TOTALSALES,SUM(COSTPRICE) AS TOTALCOST,SUM(LOSS) AS TOTALLOSS" +
                                    " FROM " + tableName + " WHERE DATETIME >'" + new salesRecord().getLastQuarter() + "'" +
                                    " GROUP BY DESCRIPTION";

                            String title = "Quarter Report";
                            reportSummary.putExtra("QUERY", queryString);
                            reportSummary.putExtra("TITLE", title);
                            startActivity(reportSummary);


                        } else if (header.getText().toString().equals("Semester Report")) {
                            queryString = "SELECT DISTINCT DESCRIPTION,SUM(PROFIT) AS TOTALPROFIT,SUM(SALESPRICE)" +
                                    " AS TOTALSALES,SUM(COSTPRICE) AS TOTALCOST,SUM(LOSS) AS TOTALLOSS" +
                                    " FROM " + tableName + " WHERE DATETIME >'" + new salesRecord().getLastSemester() + "'" +
                                    " GROUP BY DESCRIPTION";
                            reportSummary.putExtra("QUERY", queryString);

                            String title = "Semester Report";
                            reportSummary.putExtra("QUERY", queryString);
                            reportSummary.putExtra("TITLE", title);
                            startActivity(reportSummary);

                        } else if (header.getText().toString().equals("Annual Report")) {
                            queryString = "SELECT DISTINCT DESCRIPTION,SUM(PROFIT) AS TOTALPROFIT,SUM(SALESPRICE)" +
                                    " AS TOTALSALES,SUM(COSTPRICE) AS TOTALCOST,SUM(LOSS) AS TOTALLOSS" +
                                    " FROM " + tableName + " WHERE DATETIME >'" + new salesRecord().getLastYear() + "'" +
                                    " GROUP BY DESCRIPTION";

                            String title = "Annual Report";
                            reportSummary.putExtra("QUERY", queryString);
                            reportSummary.putExtra("TITLE", title);
                            startActivity(reportSummary);
                        }
                        else if (header.getText().toString().contains("Report for "+customDate))
                        {
                            queryString = "SELECT DISTINCT DESCRIPTION,SUM(PROFIT) AS TOTALPROFIT,SUM(SALESPRICE)" +
                                    " AS TOTALSALES,SUM(COSTPRICE) AS TOTALCOST,SUM(LOSS) AS TOTALLOSS" +
                                    " FROM " + tableName + " WHERE DATETIME LIKE '"+"%"+customDate+"%"+"'" +
                                    " GROUP BY DESCRIPTION";

                            String title = "Report for "+customDate;
                            reportSummary.putExtra("QUERY", queryString);
                            reportSummary.putExtra("TITLE", title);
                            startActivity(reportSummary);
                        }
                        else {
                            queryString = "SELECT DISTINCT DESCRIPTION,SUM(PROFIT) AS TOTALPROFIT,SUM(SALESPRICE)" +
                                    " AS TOTALSALES,SUM(COSTPRICE) AS TOTALCOST,SUM(LOSS) AS TOTALLOSS" +
                                    " FROM " + tableName + " GROUP BY DESCRIPTION";
                            reportSummary.putExtra("QUERY", queryString);
                            reportSummary.putExtra("TITLE", "General Report");
                            startActivity(reportSummary);

                        }

                    }
                    else
                    {
                        final AlertDialog.Builder dialog=new AlertDialog.Builder(v.getContext());
                        dialog.setTitle("INFORMATION MESSAGE");
                        dialog.setIcon(R.mipmap.infowhite);
                        dialog.setMessage("Sorry, you can't do that!");
                        dialog.setPositiveButton("OK",new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface d, int which)
                            {
                                d.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
    }

    class customAdapter2 extends CursorAdapter {

        private customAdapter2(Context ctx, Cursor cursor) {
            super(ctx, cursor);
        }

        @Override
        public View newView(Context ctx, Cursor cursor, ViewGroup vroot) {
            LayoutInflater layoutInflater = LayoutInflater.from(vroot.getContext());
            View v = layoutInflater.inflate(R.layout.report_field, vroot, false);
            return v;
        }

        @Override
        public void bindView(View v, Context ctx, Cursor cursor) {
            ImageView productImage = (ImageView) v.findViewById(R.id.product_report_pictext);
            TextView RidData = (TextView) v.findViewById(R.id.Riddata),
                    descdata = (TextView) v.findViewById(R.id.desc_reportdata),
                    productcodedata = (TextView) v.findViewById(R.id.productCodedata),
                    Stockleftdata = (TextView) v.findViewById(R.id.stockleftdata),
                    StockSolddata = (TextView) v.findViewById(R.id.stocksoldtextdata),
                    InitialStock=(TextView)v.findViewById(R.id.initialstockdata),
                    costdata = (TextView) v.findViewById(R.id.costpricedata),
                    salesdata = (TextView) v.findViewById(R.id.salespricedata),
                    profitdata = (TextView) v.findViewById(R.id.profitdata),
                    lossdata = (TextView) v.findViewById(R.id.lossdata),
                    dateTime = (TextView) v.findViewById(R.id.Datetime);
            //Associate the views with the cursor
            String imgPath = cursor.getString(cursor.getColumnIndexOrThrow("IMAGEPATH"));
            productImage.setImageBitmap(BitmapLoader.loadBitmap(imgPath, 100, 100));
            RidData.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("RID"))));
            descdata.setText((cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"))));
            productcodedata.setText(cursor.getString(cursor.getColumnIndexOrThrow("PRODUCTCODE")));
            StockSolddata.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("RECORDED_STOCK"))));
            InitialStock.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("INITIAL_STOCK"))));
            Stockleftdata.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("STOCK_LEFT"))));

            costdata.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow("COSTPRICE"))));
            salesdata.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow("SALESPRICE"))));
            profitdata.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"))));
            lossdata.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow("LOSS"))));
            dateTime.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATETIME")));
        }
    }
}