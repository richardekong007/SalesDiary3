package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 7/20/2016.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class productCatalogview extends Fragment implements ProductAdapter.ProductDetailClickListener {
    //instance variable declaration
    private ProductAdapter adapter;
    private List<Product> products = new ArrayList<>();
    private databaseManager db;
    private Product catalog;
    @BindView(R.id.product_list)
    RecyclerView recyclerView;

    @BindView(R.id.search)
    SearchView searchView;

    private static String tableName;
    private static String description;
    private static int selectedItemId;
    private Uri pictureUri;
    private int TAKE_PHOTO_CODE = 0;
    private Product product;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        v = inflater.inflate(R.layout.productcatalog, container, false);
        ButterKnife.bind(this, v);
        final Bundle extra = getActivity().getIntent().getExtras();
        //get tableName from create product Catalog fragment
        tableName = extra.getString("USERNAME");//obtain the username as the catalogue table name to be viewed
        db = new databaseManager(v.getContext());

        new Handler().post(() -> {
            //determine if the table name exist
            if (!(tableName == null || tableName.equals(""))) {
                products = db.getListOfCatalog(tableName);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                adapter = new ProductAdapter(products);
                adapter.setProductDetailClickListener(this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                Toast.makeText(getActivity(), "Contains " + db.getCountOfProductsInCatalog(tableName), Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setOnCreateContextMenuListener(this);
        return v;
    }

    public void reQueryProductList(final String table_name) {
        new Handler().post(() ->
        {
            ListView listView = (ListView) v.findViewById(R.id.product_list);
            databaseManager db = new databaseManager(v.getContext());
            SQLiteDatabase sqliteDb = db.getReadableDatabase();
            String sql = "SELECT * FROM " + table_name;
            Cursor c = sqliteDb.rawQuery(sql, null);
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        createMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //obtain details about selected product
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //increment selectedItemId by 1, because menu Info position is zero indexed
        selectedItemId = info.position;
        View view = info.targetView;
        return menuChoice(item);
    }

    private void createMenu(Menu menu) {
        MenuItem New = menu.add(0, 0, 0, "New");
        New.setIcon(R.mipmap.add_icon);
        MenuItem Edit = menu.add(0, 1, 1, "Edit");
        Edit.setIcon(R.mipmap.edit_icon);
        MenuItem Delete = menu.add(0, 2, 2, "Delete");
        Delete.setIcon(R.mipmap.delete_icon);
        MenuItem Refresh = menu.add(0, 3, 3, "Refresh");


    }

    private boolean menuChoice(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                addProductDetails(tableName);
                break;
            case 1:
                editProductDetail(selectedItemId, tableName);
                break;
            case 2:
                deleteProductDetail(selectedItemId, tableName);
                break;
            case 3:
                reQueryProductList(tableName);
        }
        return true;
    }

    EditText imgpathedit;

    public void addProductDetails(String username) {
        //Create a dialog bix for adding new product detail
        final Dialog newbox = new Dialog(v.getContext());
        newbox.setTitle("Add New Product");
        //associate the dialog with a layout file
        newbox.setContentView(R.layout.new_dialog);
        //finding dialog components by id
        TextView decritionText =  newbox.findViewById(R.id.newdesctext),
                pricetext =  newbox.findViewById(R.id.newpricetext),
                imgpathtext =  newbox.findViewById(R.id.newimgpathtext),
                stocktext = newbox.findViewById(R.id.newstocktext);
        final EditText descriptionedit =  newbox.findViewById(R.id.newdescedit),
                priceedit =  newbox.findViewById(R.id.newpriceedit),
                stockedit =  newbox.findViewById(R.id.newstockedit);
        imgpathedit =  newbox.findViewById(R.id.newimgpathedit);
        ImageButton capture =  newbox.findViewById(R.id.newcapturebtn);
        Button addbtn =  newbox.findViewById(R.id.newaddbtn);
        ImageButton exitbtn =  newbox.findViewById(R.id.newexitbtn);
        //setting event listener for capture button
        capture.setOnClickListener((view) -> captureImage());
        //setting event listener for add button
        addbtn.setOnClickListener((view)-> {
                try {
                    String description = descriptionedit.getText().toString(), imagePath = imgpathedit.getText().toString();

                    Double price = Double.parseDouble(priceedit.getText().toString());
                    Integer stock = Integer.parseInt(stockedit.getText().toString());


                    if (!((descriptionedit.getText().toString().equals("")) || (priceedit.getText().toString().equals("")) ||
                            (imgpathedit.getText().toString().equals("")) || (stockedit.getText().toString().equals("")) ||
                            (description.length() <= 1) || (imagePath.length() <= 1))) {
                        product = new Product(description, price, imagePath, stock);
                        db.addToProductCatalog(product, tableName);
                        Toast.makeText(v.getContext(), "Product details stored successfully!", Toast.LENGTH_LONG).show();
                        //clear inputs
                        descriptionedit.setText("");
                        priceedit.setText("");
                        imgpathedit.setText("");
                        stockedit.setText("");
                    } else {
                        Toast.makeText(v.getContext(), "Invalid product details provided!", Toast.LENGTH_LONG).show();
                        String hint1 = "1.Ensure all product details are provided",
                                hint2 = "2.Ensure numeric values are provided for cost price and stock",
                                hint3 = "3.Ensure all values have length size greater than 1";
                        Toast.makeText(v.getContext(), hint1 + "\n" + hint2 + "\n" + hint3 + "\n", Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), "Invalid number format ", Toast.LENGTH_LONG).show();
                } catch (SQLiteException e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //reflect realtime changes to the products in catalog
                reQueryProductList(tableName);
        });
        exitbtn.setOnClickListener((view)-> newbox.dismiss());
        //make dialog visible
        newbox.show();
    }

    public void editProductDetail(final int position, final String table_name) {
        //create an edit Dialog box
        final Dialog editbox = new Dialog(v.getContext());
        editbox.setTitle("Edit Product Details");
        editbox.setContentView(R.layout.edit_dialog);
        final TextView productNo = (TextView) editbox.findViewById(R.id.editID),
                descriptionText = (TextView) editbox.findViewById(R.id.Edittext),
                priceText = (TextView) editbox.findViewById(R.id.pricetext),
                stockText = (TextView) editbox.findViewById(R.id.stocktext);
        final EditText description = (EditText) editbox.findViewById(R.id.edit_description),
                price = (EditText) editbox.findViewById(R.id.editPrice),
                stock = (EditText) editbox.findViewById(R.id.editstock);
        Button edit = (Button) editbox.findViewById(R.id.editButton);
        ImageView Dialogphoto = (ImageView) editbox.findViewById(R.id.Editimage);
        ImageButton closeBtn = (ImageButton) editbox.findViewById(R.id.closebtn);
        //querying product details from the productCatalog database
        String sqlite = "SELECT * FROM " + table_name;
        final SQLiteDatabase sqliteDb = db.createWritableDb();
        final Cursor c = sqliteDb.rawQuery(sqlite, null);
        //holds the value for product ID
        int id = 0;
        //obtaining each column seperately
        if (c.moveToPosition(position)) {
            id = c.getInt(c.getColumnIndexOrThrow("ID"));
            String productDescription = c.getString(c.getColumnIndexOrThrow("DESCRIPTION"));
            Double productPrice = c.getDouble(c.getColumnIndexOrThrow("COSTPRICE"));
            int productStock = c.getInt(c.getColumnIndexOrThrow("STOCK"));
            String imagePath = c.getString(c.getColumnIndexOrThrow("IMAGEPATH"));
            //set product image bitmap
            Dialogphoto.setImageBitmap(BitmapLoader.loadBitmap(imagePath, 100, 100));

            //set product details to be edited
            productNo.setText(String.valueOf(id));
            description.setText(productDescription);
            price.setText(String.valueOf(productPrice));
            stock.setText(String.valueOf(productStock));
        }
        //set event listener for close button
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editbox.dismiss();
            }
        });
        //set event listener for editButton
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get contents of dialog input fields

                try {
                    int id = Integer.parseInt(productNo.getText().toString());
                    String productDescription = description.getText().toString();
                    Double productPrice = Double.parseDouble(price.getText().toString());
                    int productStock = Integer.parseInt(stock.getText().toString());
                    //Updating the selected product details
                    if (!((productDescription.equals("")) || (productDescription.length() < 2))) {

                        ContentValues values = new ContentValues();
                        values.put("DESCRIPTION", productDescription);
                        values.put("COSTPRICE", productPrice);
                        values.put("STOCK", productStock);
                        sqliteDb.update(table_name, values, "ID=?", new String[]{String.valueOf(id)});
                    } else
                        Toast.makeText(v.getContext(), "Provide Valid product details", Toast.LENGTH_LONG).show();
                } catch (SQLiteException sqle) {
                    Toast.makeText(v.getContext(), sqle.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (IllegalFormatException ife) {
                    Toast.makeText(v.getContext(), ife.getMessage(), Toast.LENGTH_LONG).show();
                }
                reQueryProductList(table_name);//reflect realtime updates made to the product catalog
                editbox.dismiss();
            }
        });
        editbox.show();//make the dialog box visible
    }

    public void deleteProductDetail(final int position, final String table_name) {
        //create a delete alert dialog box
        AlertDialog.Builder deleteAlert = new AlertDialog.Builder(v.getContext());
        deleteAlert.setTitle("CONFIRMATION MESSAGE");
        deleteAlert.setIcon(R.mipmap.confirm2);
        deleteAlert.setMessage("Sure you want to Delete product Record?");
        deleteAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                //get actual id and description of product being deleted from the database
                String sqlite = "SELECT * FROM " + table_name;
                databaseManager db = new databaseManager(v.getContext());
                SQLiteDatabase sdb = db.createWritableDb();
                Cursor c = sdb.rawQuery(sqlite, null);
                String description = "";
                int realId = 0;

                if (c.moveToPosition(position)) {
                    realId = c.getInt(c.getColumnIndexOrThrow("ID"));
                    description = c.getString(c.getColumnIndexOrThrow("DESCRIPTION"));

                }

                //delete product details
                sdb.delete(table_name, "ID=?", new String[]{String.valueOf(realId)});
                Toast.makeText(v.getContext(), description + " deleted successfully!" + " at " + position, Toast.LENGTH_LONG).show();
                //reflect changes to database
                reQueryProductList(table_name);

            }
        });
        deleteAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                d.dismiss();
            }
        });
        deleteAlert.show();


    }

    public void captureImage() {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Image File name");
            pictureUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        } catch (SecurityException se) {
            Toast.makeText(v.getContext(), se.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int ResultCode, Intent data) {
        super.onActivityResult(requestCode, ResultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && ResultCode == RESULT_OK) {
            final String pictureLocationValue = getRealPathFromUri(pictureUri);
            final AlertDialog.Builder infoDialog = new AlertDialog.Builder(v.getContext());
            if (!(pictureLocationValue == null || pictureLocationValue == "")) {
                Toast.makeText(v.getContext(), "image path captured successfully!", Toast.LENGTH_LONG).show();
                imgpathedit.setText(String.valueOf(pictureLocationValue));
            } else {
                Toast.makeText(v.getContext(), "Failure capturing image path, try again.", Toast.LENGTH_LONG).show();
            }

        }
    }

    public String getRealPathFromUri(Uri uri) {
        try {
            String[] imageProjections = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().managedQuery(uri, imageProjections, null, null, null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        } catch (Exception e) {
            return uri.getPath();
        }
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
    }

    @Override
    public void onDetailClick(Product product) {

    }
}
