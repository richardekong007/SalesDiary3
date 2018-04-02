package com.example.adminstrator.salesdiary3;

import java.io.Serializable;

/**
 * Created by Adminstrator on 7/10/2016.
 */
public class userProductCatalog implements Serializable
{
    //instance variable declaration
    private int Id;
    private String description;
    private Double costPrice;
    private String imgPath;
    private Integer stock;

    //constructor
    public userProductCatalog()
    {

    }
    public userProductCatalog(int Id, String description, Double costPrice, String imgPath, Integer stock)
    {
        this.Id=Id;
        this.description=description;
        this.costPrice=costPrice;
        this.imgPath=imgPath;
        this.stock=stock;
    }
    public userProductCatalog(String description, Double costPrice, String imgPath, Integer stock)
    {
        this.description=description;
        this.costPrice=costPrice;
        this.imgPath=imgPath;
        this.stock=stock;
    }
    //create getter and setters
    public int getId()
    {
        return this.Id;
    }
    public void setId(int Id)
    {
        this.Id=Id;
    }
    public String getDescription()
    {
        return this.description;
    }
    public void setDescription(String productName)
    {
        productName=this.description;
    }
    public Double getCostPrice()
    {
        return this.costPrice;
    }
    public void setCostPrice(Double price)
    {
        this.costPrice=price;
    }
    public String getImagePath()
    {
        return this.imgPath;
    }
    public void setImagePath(String imgLocation)
    {
        this.imgPath=imgLocation;
    }
    public Integer getStock()
    {
        return this.stock;
    }
    public void setStock(int quantityLeft)
    {
        this.stock=quantityLeft;
    }
}

