package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 7/20/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class
CustomCursorAdapter extends CursorAdapter
{

    public CustomCursorAdapter(Context context, Cursor cursor)
    {
        super(context,cursor);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        //when the view will be created for the first time
        //we need to tell the adapter how each item will look
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.productcatalogfields,parent,false);
        return v;
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        //setting our data from database using the cursor
        //that means, take the data from the cursor and put it in views
        //TextView Id=(TextView)view.findViewById(R.id.text0);


        //creating an image Bitmap to decode image files from the database

        //finding views to binded to cursor
        TextView IdHolder=(TextView)view.findViewById(R.id.text0);
        TextView DescriptionHolder=(TextView)view.findViewById(R.id.text1);
        ImageView imageHolder=(ImageView)view.findViewById(R.id.image);
        TextView priceHolder=(TextView)view.findViewById(R.id.text2);
        TextView stockHolder= (TextView)view.findViewById(R.id.text3);


        //Binding views with contents from database
            int id=cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));
            Double price = cursor.getDouble(cursor.getColumnIndexOrThrow("COSTPRICE"));
            int stock = cursor.getInt(cursor.getColumnIndexOrThrow("STOCK"));
            IdHolder.setText(String.valueOf(id));
            DescriptionHolder.setText(String.valueOf(description));
            imageHolder.setImageBitmap(BitmapLoader.loadBitmap(cursor.getString(cursor.getColumnIndexOrThrow("IMAGEPATH")),100,100));
            priceHolder.setText(String.valueOf(price));
            stockHolder.setText(String.valueOf(stock));


    }

}
    /**class BitmapLoader
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
                    scale=Math.round((float)originalWidth/requiredHeight);
                else
                    scale=Math.round((float)originalHieght/requiredHeight);
            }
            return scale;
        }
        public static BitmapFactory.Options getOptions(String filePath,int requiredWidth, int requiredHeight)
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
        public static  Bitmap loadBitmap(String filePath,int requiredWidth, int requiredHeight)
        {
            BitmapFactory.Options Options=getOptions(filePath, requiredWidth,requiredHeight);
            return BitmapFactory.decodeFile(filePath,Options);
        }
    }**/