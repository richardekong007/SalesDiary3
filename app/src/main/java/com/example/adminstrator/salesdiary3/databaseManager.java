package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 6/16/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class databaseManager extends SQLiteOpenHelper {
    //instance variable
    private static final int databaseVersion = 10;
    //fields for user table
    private static final String table = "User";
    private static final String databaseName = "salesDiaryDatabase";
    private static final String key_id = "id";
    private static final String key_userName = "username";
    private static final String key_email = "email";
    private static final String key_password = "password";

    //fields for salesRecord table
    private static final String table2 = "Products";
    private static final String product_id = "id";
    private static final String product_code = "UPC";
    private static final String product_name = "Name";
    private static final String quantity = "Quantity";
    private static final String price = "Price";
    private static final String imagePath = "ImagePath";
    private static final String date = "Date";
    private static final String time = "Time";


    public databaseManager(Context ctx) {
        super(ctx, databaseName, null, databaseVersion);
    }

    //Overriding the onCreate method
    @Override
    public void onCreate(SQLiteDatabase db) {
        //table creation statement for User
        String createTable = "CREATE TABLE " + table + "(" + key_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                key_userName + " TEXT," + key_email + " VARCHAR," + key_password + " VARCHAR"
                + ")";
        db.execSQL(createTable);
        //table creation statement for salesRecord
        String createTable2 = "CREATE TABLE " + table2 + "(" + product_id + " INTEGER," + product_code + " VARCHAR PRIMARY KEY," + product_name +
                " TEXT," + quantity + " DECIMAL," + price + " DECIMAL," + imagePath + " TEXT," + date + " VARCHAR," + time + " VARCHAR" + ")";
        db.execSQL(createTable2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + table);
        db.execSQL("DROP TABLE IF EXISTS " + table2);
        //create the table again
        onCreate(db);

    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(key_userName, user.getUsername());
        values.put(key_email, user.getEmail());
        values.put(key_password, user.getPassword());

        db.insert(table, null, values);
        db.close();

    }

    //add salesRecord sales record
    public void addProductSalesRecord(productCatalog productCatalog) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //put data into ContentValues object
        values.put(product_code, productCatalog.getCode());
        values.put(product_name, productCatalog.getDescription());
        values.put(quantity, productCatalog.getQuantity());
        values.put(price, productCatalog.getCostPrice());
        values.put(imagePath, productCatalog.getImagePath());
        values.put(date, productCatalog.getCostPrice());
        values.put(time, productCatalog.getTimeStamp());
        //insert values into the database
        db.insert(table2, null, values);
        db.close();
    }

    //getting single User
    User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table, new String[]{key_id, key_userName, key_email},
                key_id + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        cursor.close();
        return user;
    }

    //getting all contacts
    public List<User> getAllUsers() {
        List<User> Users = new ArrayList<User>();
        //select all query
        String SelectQuery = "SELECT * FROM " + table;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SelectQuery, null);
        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setemail(cursor.getString(2));
                //adding contact to list
                Users.add(user);
            }
            while (cursor.moveToNext());
        }
        //return contactList
        return Users;
    }

    //Deleting contact
    String getPassword(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(table, new String[]{key_id, key_userName, key_email, key_password},
                key_userName + "=?", new String[]{username}, null, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return "User name doesn't exist";
        }
        cursor.moveToFirst();
        String Password = cursor.getString(cursor.getColumnIndex(key_password));
        cursor.close();
        return Password;

    }

    public void deleteContact(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, key_id + "=?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //updating single contact
    public int updateContact(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(key_userName, user.getUsername());
        values.put(key_email, user.getEmail());
        values.put(key_password,user.getPassword());
        db.close();

        return db.update(table, values, key_id + "=?", new String[]{String.valueOf(user.getId())});
    }

    //getting contact counts
    public int getContactCount() {
        String countQuery = "SELECT * FROM " + table;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    public void createUserProductCatalog(String tableName) {
        SQLiteDatabase db=this.getWritableDatabase();
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName +
                "(" + "_id,"
                +"ID integer primary key autoincrement,"
                + "DESCRIPTION TEXT UNIQUE,"
                + "COSTPRICE DECIMAL,"
                + "STOCK INTEGER,"
                + "IMAGEPATH VARCHAR"
                + ")";
        db.execSQL(createTableStatement);

    }
    public void createRecordBase(String tableName)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        String createTableStatement="CREATE TABLE IF NOT EXISTS " + tableName + " ("+
                "_id,"+
                "RID INTEGER PRIMARY KEY,"+
                "PRODUCTCODE VARCHAR NOT NULL,"+
                "DESCRIPTION VARCHAR NOT NULL,"+
                "IMAGEPATH VARCHAR NOT NULL,"+
                "INITIAL_STOCK INTEGER NOT NULL,"+
                "RECORDED_STOCK INTEGER NOT NULL,"+
                "STOCK_LEFT INTEGER NOT NULL,"+
                "COSTPRICE DECIMAL NOT NULL,"+
                "SALESPRICE DECIMAL NOT NULL,"+
                "PROFIT DECIMAL NOT NULL,"+
                "LOSS DECIMAL NOT NULL,"+
                "DATETIME DATETIME DEFAULT (CURRENT_TIMESTAMP) NOT NULL" +")";
        db.execSQL(createTableStatement);
    }
    public void addToProductCatalog(Product product, String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();
        String description = "DESCRIPTION";
        String STOCK = "STOCK";
        String COSTPRICE = "COSTPRICE";
        String imageLocation = "IMAGEPATH";

        ContentValues values = new ContentValues();
        values.put(description, product.getDescription());
        values.put(COSTPRICE, product.getCostPrice());
        values.put(STOCK, product.getStock());
        values.put(imageLocation, product.getImagePath());
        //insert values into the Product
        db.insert(tableName, null, values);

    }

    public Cursor getAllProductsInCatalog(String tableName) {
        String statement = "SELECT * FROM " + tableName;
        //List<Product> products = new ArrayList<Product>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(statement, null);

        //loop through items in table
       /** if (cursor.moveToFirst()) {
            do {
                Product productCatalog = new Product();
                productCatalog.setDescription(cursor.getString(1));
                productCatalog.setImagePath(cursor.getString(1));
                productCatalog.setCostPrice(cursor.getDouble(2));
                productCatalog.setStock(cursor.getInt(3));
                products.add(productCatalog);
            }
            while (cursor.moveToNext());
        }**/
        return cursor;

    }

    public List<Product> getListOfCatalog(String tableName){
        List <Product> catalog = new ArrayList<>();
        String statement = "SELECT * FROM " + tableName;
        try(SQLiteDatabase db = this.getReadableDatabase()){
            try(Cursor cursor = db.rawQuery(statement,null)){
                if (cursor.moveToFirst()){
                    do{
                        Product product = new Product(cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                                cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")),
                                cursor.getDouble(cursor.getColumnIndexOrThrow("COSTPRICE")),
                                cursor.getString(cursor.getColumnIndexOrThrow("IMAGEPATH")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("STOCK")));
                        catalog.add(product);
                    } while(cursor.moveToNext());
                }
            }

        }
        return catalog;
    }

    public int updateProductInCatalog(String tableName) {
        Product product=new Product();
        String description = "DESCRIPTION";
        String STOCK = "STOCK";
        String COSTPRICE = "COSTPRICE";
        String imageLocation = "IMAGEPATH";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STOCK, product.getStock());
        values.put(COSTPRICE, product.getCostPrice());
        values.put(imageLocation, product.getImagePath());
        db.close();
        return db.update(tableName, values, description + "=?", new String[]{String.valueOf(product.getDescription())});
    }
    public void deleteProductFromCatalog(int id,String tableName)
    {
        //Product product=new Product();
        String ID="ID";
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(tableName,ID+"=?",new String[]{String.valueOf(id)});
    }
    public int getCountOfProductsInCatalog(String tableName)
    {
        String query="SELECT * FROM "+tableName;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);

        return cursor.getCount();
    }
    public boolean verifyUsername (String userName)
    {
        boolean value=false;
        String sql="SELECT "+key_userName+" FROM "+table;
        SQLiteDatabase db=this.getWritableDatabase();
        List<String> usernames=new ArrayList<String>();
        Cursor c=db.rawQuery(sql,null);
        //populate usernames with usernames from database
        if (c.moveToFirst())
        {
            do
                {
                    usernames.add(c.getString(0));
                }
                while(c.moveToNext());
        }
        //loop through list of available usernames to determine if userName exits
        for (String username:usernames)
        {
            if (userName.equals(username))
                value=true;
            else
                value=false;
        }
        return value;
    }
    public SQLiteDatabase createWritableDb()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db;
    }
}