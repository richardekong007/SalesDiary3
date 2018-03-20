package com.example.adminstrator.salesdiary3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Adminstrator on 8/15/2016.
 */
public class BitmapLoader
{
    public static int getScale(int originalWidth, int originalHieght, final int requiredWidth, final int requiredHeight)
    {
        //a scale of 1 means the original dimension of the images are maintained
        int scale=1;

        //calculate scale onlly if the original height or width exceeds required value
        if ((originalWidth>requiredWidth) || (originalHieght>requiredHeight))
        {
            //calculate the scale with respect to the small dimension
            if (originalWidth>originalHieght)
                scale= Math.round((float)originalWidth/requiredHeight);
            else
                scale= Math.round((float)originalHieght/requiredHeight);
        }
        return scale;
    }
    public static BitmapFactory.Options getOptions(String filePath, int requiredWidth, int requiredHeight)
    {
        BitmapFactory.Options options=new BitmapFactory.Options();
        //etting inJustDecodeBounds to true ensures that we are able to measure the image dimension without
        //actually allocating it memory
        options.inJustDecodeBounds=true;
        //decode the file for measurement
        BitmapFactory.decodeFile(filePath,options);
        //obtaining the inSampleSize for loading the scaled down version of the image
        // the image . option.outwiddth and option.outHieght are ,easured dimension of the original image
        options.inSampleSize=getScale(options.outWidth, options.outHeight, requiredWidth, requiredHeight);
        //set inJustDecodeBounds to false allocates the Bitmap to some memory
        options.inJustDecodeBounds=false;
        return options;

    }
    public static Bitmap loadBitmap(String filePath, int requiredWidth, int requiredHeight)
    {
        BitmapFactory.Options Options=getOptions(filePath, requiredWidth,requiredHeight);
        return BitmapFactory.decodeFile(filePath,Options);
    }
}
