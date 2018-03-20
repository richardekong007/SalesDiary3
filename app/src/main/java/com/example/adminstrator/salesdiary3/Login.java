package com.example.adminstrator.salesdiary3;

/**
 * Created by Adminstrator on 6/16/2016.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class Login extends AppCompatActivity
{
    private AlertDialog.Builder infoDialog;
    private AlertDialog.Builder errorDialog;
    private AlertDialog.Builder confirmDialog;
    private String salesRecordName;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final EditText username = (EditText) findViewById(R.id.username2),
              password = (EditText) findViewById(R.id.password2);
        final Button login = (Button) findViewById(R.id.Login2),
                signup = (Button) findViewById(R.id.signup2);
        final databaseManager db = new databaseManager(this);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String uname = username.getText().toString().trim();
                final String PASSWORD = password.getText().toString().trim();
                salesRecordName=username.getText().toString().trim()+"_Sales_Records";
                final String InvalidUsername = "User name doesn't exist";

                if (!((uname.equals("") || uname.length() <= 1) || (PASSWORD.equals("") || PASSWORD.length() <= 1))) {
                    if (db.getPassword(uname).equals(InvalidUsername)) {
                        errorDialog = new AlertDialog.Builder(Login.this);
                        errorDialog.setTitle("ERROR MESSAGE");
                        errorDialog.setIcon(R.mipmap.errorwhite);
                        errorDialog.setMessage(InvalidUsername);
                        errorDialog.setPositiveButton("Close",null);
                        errorDialog.show();
                    }
                    if (PASSWORD.equals(db.getPassword(uname))) {
                        infoDialog = new AlertDialog.Builder(Login.this);
                        infoDialog.setTitle("MESSAGE");
                        infoDialog.setIcon(R.mipmap.infowhite);
                        infoDialog.setMessage("Logging in ...");
                        infoDialog.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which)
                            {

                            if (db.getCountOfProductsInCatalog(uname)>0)
                            {
                                Intent gotoMainActivity = new Intent(Login.this, MainActivity.class);
                                gotoMainActivity.putExtra("USERNAME",uname);
                                gotoMainActivity.putExtra("SALES RECORD TABLE",salesRecordName);
                                startActivity(gotoMainActivity);

                            }
                            else
                            {
                                AlertDialog.Builder exceptionBox=new AlertDialog.Builder(Login.this);
                                exceptionBox.setTitle("ERROR MESSAGE");
                                exceptionBox.setIcon(R.mipmap.errorwhite);
                                exceptionBox.setMessage("No Product Catalog available!");
                                exceptionBox.setPositiveButton("Ok",new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface d, int which)
                                    {
                                        AlertDialog.Builder confirmBox=new AlertDialog.Builder(Login.this);
                                        confirmBox.setTitle("CONFIRMATION MESSAGE");
                                        confirmBox.setIcon(R.mipmap.confirm2);
                                        confirmBox.setMessage("Do you wish to create a product catalog now");
                                        confirmBox.setPositiveButton("Yes",new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface d, int which)
                                            {

                                                Intent mainActivity=new Intent(Login.this,MainActivity.class);
                                                mainActivity.putExtra("USERNAME",uname);
                                                mainActivity.putExtra("SALES RECORD TABLE",salesRecordName);
                                                startActivity(mainActivity);
                                                }
                                        });
                                        confirmBox.setNegativeButton("No", null);
                                        confirmBox.show();
                                    }
                                });exceptionBox.show();
                            }
                                //clear the username and password values
                                clear(username, password);
                            }
                        });
                        infoDialog.show();
                    }
                    else
                    {
                        errorDialog = new AlertDialog.Builder(Login.this);
                        errorDialog.setTitle("ERROR MESSAGE");
                        errorDialog.setIcon(R.mipmap.errorwhite);
                        errorDialog.setMessage("Wrong password!");
                        errorDialog.setPositiveButton("Close",null);
                        errorDialog.show();
                    }
                }
                else
                    {
                        errorDialog = new AlertDialog.Builder(Login.this);
                        errorDialog.setTitle("ERROR MESSAGE");
                        errorDialog.setIcon(R.mipmap.errorwhite);
                        errorDialog.setMessage("Invalid or no Username and Password specified");
                        errorDialog.setPositiveButton("Close",new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface d, int which)
                            {

                            }
                        });
                        errorDialog.show();

                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog = new AlertDialog.Builder(Login.this);
                confirmDialog.setTitle("CONFIRMATION MESSAGE");
                confirmDialog.setIcon(R.mipmap.confirm2);
                confirmDialog.setMessage("Are you sure?");
                confirmDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface d, int which)
                    {
                        Intent gotoSignup = new Intent(Login.this, signUp.class);
                        clear(username,password);
                        startActivity(gotoSignup);

                    }
                });
                confirmDialog.setNegativeButton("No",null);
                confirmDialog.show();
            }
        });

    }

    @Override
    public void onConfigurationChanged(final Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
    }
    public void clear(EditText...editTexts){
       //clear all editTet arguments
       for(EditText editText:editTexts){
           editText.setText("");
       }
    }
}
