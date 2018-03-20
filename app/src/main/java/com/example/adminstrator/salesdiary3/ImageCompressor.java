package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 7/23/2016.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageCompressor
{
    //create a method that uses image path to locate and convert image to base64
    public String imageToString(String FileName)
    {
        Bitmap bmp= BitmapFactory.decodeFile(FileName);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        if (FileName.substring(FileName.length()-3,FileName.length()).equals("jpg"))
        {
           bmp.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        }
        else
        {
            bmp.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        }
        byte [] imageByteArray=byteArrayOutputStream.toByteArray();
        String encodedImage= Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        return encodedImage;
    }
    public Bitmap StringToImageBitmap(String encodedImage)
    {
        Bitmap bmp=null;
        try
        {
            //create byte array for storing encodedimage String
            byte[] imageAsBytes = Base64.decode(encodedImage.getBytes(), Base64.DEFAULT);
            bmp= BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        return bmp;
    }
}
