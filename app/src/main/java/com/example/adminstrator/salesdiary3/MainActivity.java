package com.example.adminstrator.salesdiary3;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Bundle usernameBundle;
    private Bundle salesRecord;
    private String salesRecordName;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm=getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame,new createProductCatalog()).commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //obtain database user name from signup activity to create a product catalog database with the obtained username
        usernameBundle=getIntent().getExtras();
        //obtain sales record table name from the database manager
        salesRecord=getIntent().getExtras();
        salesRecordName=getIntent().getExtras().getString("SALES RECORD TABLE");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        //save data from the instance variable
        bundle.putString("USERNAME",username);
        bundle.putString("SALES RECORD TABLE",salesRecordName);
        bundle.putBundle("USERNAME BUNDLE",usernameBundle);
        bundle.putBundle("SALE RECORD BUNDLE",salesRecord);
    }
    @Override public void onRestoreInstanceState(Bundle bundle){
        super.onRestoreInstanceState(bundle);
        username=bundle.getString("USERNAME");
        salesRecordName=bundle.getString("SALES RECORD TABLE");
        usernameBundle=bundle.getBundle("USERNAME BUNDLE");
        salesRecord=bundle.getBundle("SALE RECORD BUNDLE");
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment=null;
        FragmentManager fm=getFragmentManager();
        databaseManager db=new databaseManager(this);
        if (id == R.id.nav_addProducts) {
            fragment=new createProductCatalog();
            //pass the username to the add product content. username will serve as the database table name for newly created catalog
            fragment.setArguments(usernameBundle);
            fm.beginTransaction().replace(R.id.content_frame,fragment).commit();
        }
        else if (id == R.id.nav_catalogue) {
            fragment=new productCatalogview();
            fragment.setArguments(usernameBundle);//pass the username as the catalogue name to view after creating it.
            fm.beginTransaction().replace(R.id.content_frame,fragment).commit();
        }
         else if (id == R.id.nav_salesrecord) {
            fragment=new recordScreen();
            fragment.setArguments(usernameBundle);
            fm.beginTransaction().replace(R.id.content_frame,fragment).commit();
        } else if (id == R.id.nav_reportmenu) {
            fragment=new ReportMenu();
            fragment.setArguments(usernameBundle);
            fm.beginTransaction().replace(R.id.content_frame,fragment).commit();
        } else if (id == R.id.nav_credits) {

        } else if (id == R.id.nav_logout) {
            //navigate ot the logout section
            Intent logout=new Intent(MainActivity.this,Login.class);
            startActivity(logout);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
