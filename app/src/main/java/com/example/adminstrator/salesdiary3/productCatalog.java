package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 6/23/2016.
 */
import java.util.Date;

public class productCatalog

{
    //declaring instance variable
    private int id;
    private String code;
    private String description;
    private Double quantity;
    private Double quantityLeft;
    private Double costPrice;
    private String imagePath;
    private String timeStamp;
    private String date;

    //salesRecord default constructor
    public productCatalog()
    {

    }
    public productCatalog(String code, String description, Double quantity, Double costPrice)
    {
        this.code=code;
        this.description =description;
        this.quantity=quantity;
        this.costPrice = costPrice;

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
    public Double getQuantityLeft(Double quantitySold)
    {
        quantityLeft=getQuantity()-quantitySold;
        return quantityLeft;
    }
    public Double getCostPrice()
    {
        return this.costPrice;
    }
    public void setCostPrice(Double costPrice)
    {
        this.costPrice = costPrice;
    }
    public String getImagePath()
    {
        return this.imagePath;
    }
    public void setImagePath(String imagePath)
    {
        this.imagePath=imagePath;
    }

    public String getTimeStamp()
    {

        Date d=new Date();
        int HH=(int)d.getHours(),MM=(int)d.getMinutes(),SS=d.getSeconds();
        if (HH>=12)
        {
            HH-=12;
            timeStamp=HH+":"+MM+":"+SS+" PM";
        }
        else
            timeStamp=HH+":"+MM+":"+SS+":"+" AM";
        return timeStamp;
    }
    public void setTimeStamp(int HH, int MM, int SS)
    {
        String time;
        if (HH>=12)
            time=HH+":"+MM+":"+SS+" PM";
        else
            time=HH+":"+MM+":"+SS+":"+" AM";
        timeStamp=time;
    }
    public String getDate()
    {
        Date DATE=new Date();
        int day=(int)DATE.getDate(),month=(int)DATE.getMonth()+1;
        String year= String.valueOf(DATE).substring(String.valueOf(DATE).length()-4, String.valueOf(DATE).length());

        date=day+"-"+month+"-"+year;
        return date;
    }
    public void setDate(int day, int month, int year)
    {
        String d;
        d=day+"-"+month+"-"+year;
        date=d;
    }

}

