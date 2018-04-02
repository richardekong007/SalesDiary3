package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 7/10/2016.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class createProductCatalog extends Fragment
{
    //instance variable declaration
    int TAKE_PHOTO_CODE=0;
    EditText productDesc;
    EditText imgpath;
    Uri pictureUri;
    Product product;
    signUp signup;
    private  Bundle extras;
    private String tableName;
    productCatalogview productList;
    private View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle b)
    {
        v=inflater.inflate(R.layout.create_product_catalog,container,false);
        //create UI controls

        productDesc=(EditText)v.findViewById(R.id.productdesc);
        final EditText costPrice=(EditText)v.findViewById(R.id.costprice);
        imgpath=(EditText)v.findViewById(R.id.imgpath);
        final EditText stockValue=(EditText)v.findViewById(R.id.stockValue);
        final FloatingActionButton cameraBtn=(FloatingActionButton)v.findViewById(R.id.capturebtn);
        final FloatingActionButton storeBtn=(FloatingActionButton)v.findViewById(R.id.store);
        final databaseManager db=new databaseManager(v.getContext());
        extras =getActivity().getIntent().getExtras();
        //get username from sign up activity
        tableName=extras.getString("USERNAME");
        //set event listener for camera button
        cameraBtn.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v)
           {
            captureImage();
               /**ContentValues values=new ContentValues();
               values.put(MediaStore.Images.Media.TITLE,"Image File name");
               pictureUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
               Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,pictureUri);
               startActivityForResult(cameraIntent,TAKE_PHOTO_CODE);**/
           }
        });
        //set event listener for add button
       storeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    String description = productDesc.getText().toString(), imagePath = imgpath.getText().toString();

                    Double price = Double.parseDouble(costPrice.getText().toString());
                    Integer stock = Integer.parseInt(stockValue.getText().toString());


                    if (!((productDesc.getText().toString().equals("") || costPrice.getText().toString().equals("") ||
                            imgpath.getText().toString().equals("") || stockValue.getText().toString().equals("")) ||
                            (description.length() <= 1 || imagePath.length() <= 1 || !(stock instanceof Integer) ||
                                    !(price instanceof Double)))) {
                        product = new Product(description, price, imagePath, stock);
                        db.addToProductCatalog(product, tableName);
                        AlertDialog.Builder infoDialog = new AlertDialog.Builder(v.getContext());
                        infoDialog.setTitle("MESSAGE");
                        infoDialog.setIcon(R.mipmap.infowhite);
                        infoDialog.setMessage("Product details stored successfully!");
                        infoDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                //clear edit texts
                                clear(productDesc,costPrice,imgpath,stockValue);

                            }
                        });
                        infoDialog.show();
                    }
                    else
                    {
                        final AlertDialog.Builder errorDialog = new AlertDialog.Builder(v.getContext());
                        errorDialog.setTitle("ERROR MESSAGE");
                        errorDialog.setIcon(R.mipmap.errorwhite);
                        errorDialog.setMessage("Invalid product details provided!");
                        errorDialog.setPositiveButton("hints", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                String hint1 = "1.Ensure all product details are provided",
                                        hint2 = "2.Ensure numeric values are provided for cost price and stock",
                                        hint3 = "3.Ensure all values have length size greater than 1";
                                AlertDialog.Builder errorBox=new AlertDialog.Builder(getActivity());
                                errorBox.setTitle("ERROR MESSAGE");
                                errorBox.setIcon(R.mipmap.errorwhite);
                                errorBox.setMessage(hint1 + "\n" + hint2 + "\n" + hint3 + "\n");
                                errorBox.setPositiveButton("Close",new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface d, int which)
                                    {
                                        d.dismiss();
                                    }
                                });
                                errorBox.show();
                            }
                        });
                        errorDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                            }
                        });
                        errorDialog.show();

                    }
                }
                catch(NumberFormatException e)
                {
                    AlertDialog.Builder errorBox=new AlertDialog.Builder(v.getContext());
                    errorBox.setTitle("ERROR MESSAGE");
                    errorBox.setIcon(R.mipmap.errorwhite);
                    errorBox.setMessage("Invalid number format ");
                    errorBox.setPositiveButton("Close",new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface d, int which)
                        {
                            d.dismiss();
                        }
                    });
                    errorBox.show();

                }
                catch (SQLiteException e){}

            }});
           //  final Button ViewCatalog=(Button)v.findViewById(R.id.cat);
            //ViewCatalog.setOnClickListener(new View.OnClickListener()
           // {
              //  @Override
                //public void onClick(View v)
                //{
                    //pass tablename to productCatalogView
                  //  Intent productCatalogViewIntent=new Intent(getActivity(),productCatalogview.class);
                    //productCatalogViewIntent.putExtra("TABLENAME",tableName);
                    //startActivity(productCatalogViewIntent);
                //}
            //});
        return v;
    }
    @Override public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        //save table name data and all other bundle data
        bundle.putString("USERNAME", tableName);
        bundle.putBundle("EXTRAS",extras);
    }

    /**@Override public void onActivityCreated( @Nullable Bundle bundle){
        super.onActivityCreated(bundle);
        //restore all saved properties of the fragment upon recreation
        tableName=bundle.getString("USERNAME");
        Log.v("table name",bundle.getString("USERNAME"));
        extras=bundle.getBundle("EXTRAS");
    }**/

    public void clear(EditText ... editTexts){
        //clear text of all supplied edit text
        for (EditText editText: editTexts){
            editText.setText("");
        }
    }

    public void captureImage()
    {
        /*
        * delegate the responsibilities of capturing images to the the ACTION_IMAGE_CAPTURE intent
        * create a contentValue object to hold image media for subsequent activities that need them
        * IF A CAMERA APP EXISTS DO THE FOLLING BELLOW:
        * create a storage path for sales diary app to hold product images captured by the user
        * if no such directory exists in the specified path, make such directory
        * obtain the image name generated from the user specification
        * create a file object to hold or store the image captured by the user
        * extract the file uri and store in a uri object
        * submit the captured image and its uri
        * start the intent
        * */
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Image File name");
        //pictureUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager())!=null)
        {
            File saleDiaryDir = new File(Environment.getExternalStorageDirectory() + "/Sales Diary");
            if (!(saleDiaryDir.exists()))
                saleDiaryDir.mkdirs();
            String pictureName = getImageName();
            File pictureFile = new File(saleDiaryDir, pictureName);
            pictureUri = Uri.fromFile(pictureFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int ResultCode, Intent data)
    {
        super.onActivityResult(requestCode,ResultCode,data);
        if (requestCode==TAKE_PHOTO_CODE && ResultCode==RESULT_OK)
        {
            final String pictureLocationValue=getRealPathFromUri(pictureUri);
            final AlertDialog.Builder infoDialog=new AlertDialog.Builder(getActivity());
            if (!(pictureLocationValue==null||pictureLocationValue=="")){
                infoDialog.setTitle("MESSAGE");
                infoDialog.setIcon(R.mipmap.infowhite);
                infoDialog.setMessage("image path captured successfully!");
                imgpath.setText(String.valueOf(pictureLocationValue));
            }
            else{
                infoDialog.setTitle("ERROR MESSAGE");
                infoDialog.setIcon(R.mipmap.errorwhite);
                infoDialog.setMessage("Failure capturing image path, try again.");
            }
            infoDialog.setPositiveButton("Ok",new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface d, int which)
                {

                }
            });
            infoDialog.show();
        }
    }
    public String getRealPathFromUri(Uri uri)
    {
        try
        {
            //create external path

            String[] imageProjections={MediaStore.Images.Media.DATA};
            Cursor cursor=getActivity().managedQuery(uri,imageProjections,null,null,null);
            int columnIndex=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        }
        catch(Exception e)
        {
            return uri.getPath();
        }
    }
    public String getImageName()
    {
        /*create a simple date formatter object to get a specified format of the current date and time
          store the formatted value of the current date and time in varaible timestamp
          obtain the user specified product descriotion value and store in the picture Name variable
          return the concatinated  value of the pictureName, timestamp and picture format (in jpg)
        */
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp=simpleDateFormat.format(new Date());
        String pictureName=productDesc.getText().toString();

        return pictureName+"_"+timeStamp+".jpg";
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfiguration)
    {
        super.onConfigurationChanged(newConfiguration);
    }

}
