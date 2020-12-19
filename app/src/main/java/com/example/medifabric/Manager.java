package com.example.medifabric;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
//import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;




import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.medifabric.fragments.Feedback;
import com.google.android.material.navigation.NavigationView;
import com.example.medifabric.SharedPreferenceConfig;
import com.example.medifabric.fragments.profile;
import com.squareup.picasso.Picasso;


//This contains java code for navigation drawer activity

public class Manager extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String uid, uname, urole, uProfile,profileurl;
    TextView UNAME, UID, UROLE;
    View mView;
    ImageView imageView;
    Handler handler;
    SharedPreferenceConfig preferenceConfig;
    Toolbar toolbar;

    @Override
    public void onResume(){
        super.onResume();
        toolbar.setTitle("Manager");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_activity_manager);
        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());  //....6/6/2019

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Manger");

        //get data sent by Mainactivity.java startsact
        Intent intent = getIntent();
//        uid = intent.getStringExtra(MainActivity.EXTRA_UID);
        uname=preferenceConfig.read_username();
        urole=preferenceConfig.read_email();
        uid=preferenceConfig.read_userid();
        uProfile=preferenceConfig.read_contact();
        profileurl=preferenceConfig.read_uprofileurl();

        Log.i("tracking uid","manager when received "+uid+uname+urole+uProfile);
        //get data sent by Mainactivity.java ends



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mView = navigationView.getHeaderView(0); //created an object for HeaderView in navigationView

        UID = (TextView)mView.findViewById(R.id.uid_final);
        UNAME = (TextView)mView.findViewById(R.id.uname_final);
        UROLE = (TextView)mView.findViewById(R.id.uroll_final);

        UID.setText(uid); //Setting data of user into the header of navigation view
        UNAME.setText(uname);
        UROLE.setText(urole);

        navigationView.setNavigationItemSelectedListener(this);

        imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        loadImageUrl(profileurl);

        if(savedInstanceState == null)
        {
            Bundle bundle = new Bundle();
            bundle.putString("uRole",urole);

            handler = new Handler();
//            ActivityManager activityManager;
//            activityManager= new ActivityManager(getApplicationContext(),handler);
//            activityManagr.setArguments(bundle);
//
//            getSupportFragmentManager().beginTransaction().replace(R.id.containerID, activityManager).addToBackStack(null).commit();
            ActivityManager activityManager;
            activityManager = ActivityManager.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.containerID, activityManager).addToBackStack(null).commit();
        }
    }

     public void setSupportActionBar(Toolbar toolbar) {
    }

    void loadImageUrl(String url) {
        Picasso.get().load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView, new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {
                        Log.i("picasso", "got response");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("picasso", "got error "+e.toString());

                    }
                });
    }

    //below mathod id used to close the drawer... if its is open while pressing backpress key
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(getSupportFragmentManager().getBackStackEntryCount() > 1){
                getSupportFragmentManager().popBackStack();
            }
            else {
                //finish();
                finishAffinity();
                //super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manager, menu);
        return true;
    }

 //   @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_profile) {

            Bundle bundle = new Bundle();
            bundle.putString("userid","123456");
//                            Log.i("tracking uid","in manager sending to minute ");

            profile profile_f = new profile();
            profile_f.setArguments(bundle);
            if(getSupportFragmentManager().getBackStackEntryCount() > 1){
                getSupportFragmentManager().popBackStack();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.containerID, profile_f).addToBackStack(null).commit();

        } else if (id == R.id.nav_activity_manager) {
            //Toast.makeText(Manager.this,String.valueOf(getSupportFragmentManager().getBackStackEntryCount()),Toast.LENGTH_SHORT).show();
            if(getSupportFragmentManager().getBackStackEntryCount() > 1){
                getSupportFragmentManager().popBackStack();
            }
            else if(getSupportFragmentManager().getBackStackEntryCount() != 1){
                Bundle bundle = new Bundle();
                bundle.putString("uRole",urole);


            }
        } else if (id == R.id.nav_developers) {

        } else if (id == R.id.nav_Feedback) {
            Feedback feedback = new Feedback();
            if(getSupportFragmentManager().getBackStackEntryCount() > 1){
                getSupportFragmentManager().popBackStack();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.containerID, feedback).addToBackStack(null).commit();



        } else if (id == R.id.nav_about_us) {


        } else if (id == R.id.log_out) {
            //....6/6/2019
            preferenceConfig.writeLoginStatus(false,"","",urole,"","","","","","","","");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            //....6/6/2019
            Toast.makeText(Manager.this,"Logged Out", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setActionBarTitle(String title) {
        toolbar.setTitle(title);
    }
}
