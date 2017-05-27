package com.example.ausias.ingotohelper;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;


import com.example.ausias.ingotohelper.DAOS.UserDAO_MySQL;
import com.example.ausias.ingotohelper.Fragments.GmapFragment;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int id;
    public static ArrayList<Integer> petitions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petitions = new ArrayList<>();
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new GmapFragment()).commit();

        //Get the id of the loged user
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        //Load the User Photo
        new GetPhotoTask().execute(id);

        //Load the Notifications of this User
        BackgroundWorker bw = new BackgroundWorker(this);
        bw.execute("getNotifications", id);

    }

    /**
     * This method is called from the AsynkTask with a InputStream obtained from the byte[] of the user Photo,
     * saved in de database, and then creates a BitMap with the InputStream, and put this into ImageView from the
     * lateral menu.
     * @param result InputStream obtained from the AsynkTask Query.
     */
    public void onBackgroundTaskDataObtained(InputStream result) {
        ImageView iw = (ImageView) findViewById(R.id.img);
        Bitmap bitmap = BitmapFactory.decodeStream(result);
        if(bitmap!=null)
            iw.setImageBitmap(Bitmap.createScaledBitmap(bitmap,120,120,false));
        else {
            Drawable defaultProfile = getDrawable(R.drawable.default_profile);
            iw.setImageDrawable(defaultProfile);
        }
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
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getFragmentManager();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_profile) {
            Intent intent = new Intent(this, PerfilActivity.class);
            intent.putExtra("id", getId());
            startActivity(intent);
        }
        else if(id == R.id.nav_maps) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new GmapFragment())
                    .commit();
        }
        else if(id == R.id.nav_manageChild){
            Intent intent = new Intent(this, GestioFillsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id", getId());
            startActivity(intent);
        }
        else if(id == R.id.nav_notification){
            startActivity(new Intent(this, NotificationActivity.class));
        }
        else if(id == R.id.nav_chat){
            startActivity(new Intent(this, XatActivity.class));
        }
        else if(id == R.id.nav_settings){
            //******************************************************************//
        }
        else if(id == R.id.nav_about){
            //******************************************************************//
        }
        else if(id == R.id.nav_ticket) {
            startActivity(new Intent(this, Ticket.class));
        }
        else if(id == R.id.nav_logout) {
            id = 0;
            startActivity(new Intent(this, LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * This AsyncTask receive a ID of a actual User, makes a query to the database and obtain a InputStream with the User photo.
     * On Post Execute, call a function onBackgroundTaskDataObtained with the InputStream.
     */
    private class GetPhotoTask extends AsyncTask<Integer,Void,InputStream> {
        UserDAO_MySQL userDao;

        GetPhotoTask(){
            userDao = new UserDAO_MySQL();
        }

        @Override
        protected InputStream doInBackground(Integer... params) {
            int id = params[0];
            return userDao.getPhoto(id);
        }

        @Override
        protected void onPostExecute(InputStream is) {
            MainActivity.this.onBackgroundTaskDataObtained(is);
        }
    }

    public static int getId() {
        return id;
    }
}

