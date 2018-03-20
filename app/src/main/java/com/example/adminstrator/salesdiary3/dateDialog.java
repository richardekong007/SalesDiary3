package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 11/19/2016.
 */

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;

public class dateDialog

{
    private String tableName;
    private Fragment app;

    public dateDialog(Fragment app, String tableName)
    {
        this.app=app;
        this.tableName=tableName;
        showDialog();
    }
    public void showDialog()
    {
        //create dialog
        final Dialog dialog=new Dialog(app.getActivity());
        dialog.setTitle("Select a date and touch done ");
        dialog.setContentView(R.layout.date_dialog);
        //create controls
        final DatePicker dp=(DatePicker)dialog.findViewById(R.id.datepicker);
        dp.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                passDate(dp);
                dialog.dismiss();
            }
        });
       /** ImageButton done=(ImageButton)dialog.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               passDate(dp);
               dialog.dismiss();
            }
        });**/
        dialog.show();
    }
    public void passDate(DatePicker dp)
    {
        //String customDate=dp.getYear()+"-"+(dp.getMonth()+1)+"-"+dp.getDayOfMonth();
        salesRecord salesRecord=new salesRecord();
        String customDate=salesRecord.getCustomDateTime(dp.getYear(),dp.getMonth()+1,dp.getDayOfMonth());
        String queryString="SELECT * FROM "+tableName+" WHERE DATETIME LIKE '"+"%"+customDate+"%"+"'";

        Intent intent=new Intent(app.getActivity(),SalesReport.class);
        //set title of report
        intent.putExtra("TITLE"," Report for "+customDate);
        intent.putExtra("SALES_QUERY",queryString);
        intent.putExtra("SALES RECORD TABLE",tableName);
        intent.putExtra("CUSTOMDATE",customDate);
        app.startActivity(intent);
    }

}
