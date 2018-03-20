package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 9/2/2016.
 */

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class ReportMenu extends Fragment
{
    private View v;
    private String tableName;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,   Bundle b)
    {
         v=inflater.inflate(R.layout.sale_report_menu,container, false);

        //get sales record table name
        Bundle Extra=getActivity().getIntent().getExtras();
        tableName=getArguments().getString("SALES RECORD TABLE");
        //get views by id
        final ImageButton dailyOption=(ImageButton)v.findViewById(R.id.daily),
                weeklyOption=(ImageButton)v.findViewById(R.id.weekly),
                monthlyOption=(ImageButton)v.findViewById(R.id.monthly),
                quarteroption=(ImageButton)v.findViewById(R.id.quarterly),
                semesterOption=(ImageButton)v.findViewById(R.id.semester),
                yearlyOption=(ImageButton)v.findViewById(R.id.yearly);
        final Button customOption=(Button)v.findViewById(R.id.customReport),
                     generalOtpion=(Button)v.findViewById(R.id.generalsales);
        //set onclick listeners for every button
        dailyOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String yesterday=new salesRecord().getYesterday();
                String queryString="SELECT * FROM "+tableName+" WHERE DATETIME > '"+yesterday+"'";
                Intent generateDailyReport=new Intent(v.getContext(),SalesReport.class);
                //set title of report
                generateDailyReport.putExtra("TITLE","Daily Report");
                generateDailyReport.putExtra("SALES_QUERY",queryString);
                generateDailyReport.putExtra("SALES RECORD TABLE",tableName);
                startActivity(generateDailyReport);
            }
        });
        weeklyOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String lastWeek=new salesRecord().getLastWeek();
                String queryString="SELECT * FROM "+tableName+" WHERE DATETIME > '"+lastWeek+"'";
                Intent generateWeeklyReport=new Intent(v.getContext(),SalesReport.class);
                //set title of report
                generateWeeklyReport.putExtra("TITLE","Weekly Report");
                generateWeeklyReport.putExtra("SALES_QUERY",queryString);
                generateWeeklyReport.putExtra("SALES RECORD TABLE",tableName);
                startActivity(generateWeeklyReport);
            }
        });
        monthlyOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
        {
            String lastMonth=new salesRecord().getLastMonth();
            String queryString="SELECT * FROM "+tableName+" WHERE DATETIME > '"+lastMonth+"'";
            Intent generateMonthly=new Intent(v.getContext(),SalesReport.class);
            //set title of report
            generateMonthly.putExtra("TITLE","Monthly Report");
            generateMonthly.putExtra("SALES_QUERY",queryString);
            generateMonthly.putExtra("SALES RECORD TABLE",tableName);
            startActivity(generateMonthly);
        }
        });
        quarteroption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String lastQuarter=new salesRecord().getLastQuarter();
                String queryString="SELECT * FROM "+tableName+" WHERE DATETIME > '"+lastQuarter+"'";;
                Intent generateQuarter=new Intent(v.getContext(),SalesReport.class);
                //set title of report
                generateQuarter.putExtra("TITLE","Quarter Report");
                generateQuarter.putExtra("SALES_QUERY",queryString);
                generateQuarter.putExtra("SALES RECORD TABLE",tableName);
                startActivity(generateQuarter);
            }

        });
        semesterOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String lastSemester=new salesRecord().getLastSemester();
                String queryString="SELECT * FROM "+tableName+" WHERE DATETIME > '"+lastSemester+"'";
                Intent generateSemester=new Intent(v.getContext(),SalesReport.class);
                //set title of report
                generateSemester.putExtra("TITLE","Semester Report");
                generateSemester.putExtra("SALES_QUERY",queryString);
                generateSemester.putExtra("SALES RECORD TABLE",tableName);
                startActivity(generateSemester);
            }
        });
        yearlyOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String lastyear=new salesRecord().getLastYear();
                String queryString="SELECT * FROM "+tableName+" WHERE DATETIME > '"+lastyear+"'";

                Intent generateYear=new Intent(v.getContext(),SalesReport.class);
                //set title of report
                generateYear.putExtra("TITLE","Annual Report");
                generateYear.putExtra("SALES_QUERY",queryString);
                generateYear.putExtra("SALES RECORD TABLE",tableName);
                 startActivity(generateYear);
            }
        });
        customOption.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                //instantiate datepicker dialog and display the date dialog
                dateDialog dp=new dateDialog(ReportMenu.this,tableName);
            }
        });
        generalOtpion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View V)
            {
                String queryString="SELECT * FROM "+tableName;
                Intent generateReport=new Intent(v.getContext(),SalesReport.class);
                //set title of report
                generateReport.putExtra("TITLE","General Report");
                generateReport.putExtra("SALES_QUERY",queryString);
                generateReport.putExtra("SALES RECORD TABLE",tableName);
                startActivity(generateReport);
            }
        });
        return v;
    }
    @Override
    public void onConfigurationChanged(final Configuration newConfiguration)
    {
        super.onConfigurationChanged(newConfiguration);
    }
}
