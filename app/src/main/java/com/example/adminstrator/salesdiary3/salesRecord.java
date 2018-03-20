package com.example.adminstrator.salesdiary3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Adminstrator on 7/9/2016.
 */

public class salesRecord
{

        //declaring instance variable
        private int id;
        private String code;
        private String description;
        private Double quantity;
        private Double quantityLeft;
        private Double profit;
        private Double salesPrice;
        private String imagePath;
        private String timeStamp;
        private String date;
        private Random RandInt=new Random();
        //salesRecord default constructor
        public salesRecord()
        {

        }
        public salesRecord(String code, String description, Double quantity, Double salesPrice)
        {
            this.code=code;
            this.description =description;
            this.quantity=quantity;
            this.salesPrice = salesPrice;

        }
        //implement setter and getter methods
        public String getCode()
        {
            return this.code;
        }
        public void setCode(String code){
            this.code=code;
        }
        public String getDescription()
        {
            return this.description;
        }
        public void setDescription(String description)
        {
            this.description = description;
        }
        public Double getQuantity()
        {
            return this.quantity;
        }
        public void setQuantity(Double quantity)
        {
            this.quantity=quantity;
        }
        public int getId()
        {
            int min=1000000, max=10000000;
            id=RandInt.nextInt((max-min)+1)+min;
            return id;
        }
        public Double getQuantityLeft(Double quantitySold)
        {
            quantityLeft=getQuantity()-quantitySold;
            return quantityLeft;
        }
        public Double getProfit(Double costPrice)
        {
            Double sellingPrice=getSalesPrice();
            profit=sellingPrice-costPrice;
            return profit;
        }
        public Double getSalesPrice()
        {
            return this.salesPrice;
        }
        public void setSalesPrice(Double salesPrice)
        {
            this.salesPrice = salesPrice;

        }
        public String getImagePath()
        {
            return this.imagePath;
        }
        public void setImagePath(String imagePath)
        {
            this.imagePath=imagePath;

        }

        public String getDateTime()
        {
            //compute the current
            Date date=new Date();

            //specify the yyyy-MM-dd hh:mm:ss format
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            // return Datetime value in yyyy-MM-dd hh:mm:ss
            return simpleDateFormat.format(date);
        }
        public String getYesterday()
        {

        Date d = new Date();
        SimpleDateFormat s=new SimpleDateFormat("dd");
        String Time=s.format(d);
        int yesterdayFigure= Integer.parseInt(Time)-2;
        String yesterday="";
        yesterday=yesterdayFigure<10?"0"+ String.valueOf(yesterdayFigure): String.valueOf(yesterdayFigure);
        s=new SimpleDateFormat("yyyy-MM-"+yesterday+" hh:mm:ss");
        return s.format(d);

        }
        public String getLastWeek()
        {
        Date d=new Date();
        SimpleDateFormat s=new SimpleDateFormat("dd");
        String Time=s.format(d);
        int lastWeek= Integer.parseInt(Time)-7;
        String lastWeekDate="";

        lastWeekDate=lastWeek<10?"0"+ String.valueOf(lastWeek): String.valueOf(lastWeek);

        s=new SimpleDateFormat("yyyy-MM-"+lastWeekDate+" hh:mm:ss");

        return s.format(d);

        }
        public String getLastMonth()
        {
        Date d=new Date();
        SimpleDateFormat s=new SimpleDateFormat("MM");
        String Time=s.format(d);
        int lastMonthFigure= Integer.parseInt(Time)-1;
        String lastMonthDate="";

            lastMonthDate=lastMonthFigure<10?"0"+ String.valueOf(lastMonthFigure): String.valueOf(lastMonthFigure);

        s=new SimpleDateFormat("yyyy-"+lastMonthDate+"-dd hh:mm:ss");
        return s.format(d);
    }
    public String getLastQuarter()
    {
        Date d=new Date();
        SimpleDateFormat s=new SimpleDateFormat("MM");
        String Time=s.format(d);
        int lastQuarterFigure= Integer.parseInt(Time)-3;
        String lastQuarterDate="";

        lastQuarterDate=lastQuarterFigure<10?"0"+ String.valueOf(lastQuarterFigure): String.valueOf(lastQuarterFigure);
        s=new SimpleDateFormat("yyyy-"+lastQuarterDate+"-dd hh:mm:ss");
        return s.format(d);
    }
    public String getLastSemester()
    {
        Date d=new Date();
        SimpleDateFormat s=new SimpleDateFormat("MM");
        String Time=s.format(d);
        int lastSemesterFigure= Integer.parseInt(Time)-6;
        String lastSemesterDate="";

        lastSemesterDate=lastSemesterFigure<10?"0"+ String.valueOf(lastSemesterFigure): String.valueOf(lastSemesterFigure);
        s=new SimpleDateFormat("yyyy-"+lastSemesterDate+"-dd hh:mm:ss");
        return s.format(d);
    }

    public String getLastYear()
    {
        Date d=new Date();
        SimpleDateFormat s=new SimpleDateFormat("yyyy");
        String Time=s.format(d);
        int lastYearFigure= Integer.parseInt(Time)-1;
        s=new SimpleDateFormat(lastYearFigure+"-MM-dd hh:mm:ss");
        return s.format(d);
    }
    public String getCustomDateTime(int year, int month, int day)
    {
        String dateTime="";
        if(month<10)
        {
            dateTime=new String(year+"-"+"0"+month+"-"+day);
        }
        else if (day<10)
        {
            dateTime=new String(year+"-"+month+"-"+"0"+day);
        }
        else if (month<10 && day<10 )
        {
            dateTime=new String(year+"-"+"0"+month+"-"+"0"+day);
        }
        else
        {

        }
        return dateTime;
    }
    }



