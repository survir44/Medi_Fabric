package com.example.medifabric;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import com.example.medifabric.SharedPreferenceConfig;

public class MainActivity extends AppCompatActivity {

    private String uemail="",upass="";
    private EditText uemail_r,upass_r;
    private String server_url="http://134.209.152.226:8000"; //Main Server URL
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private SharedPreferenceConfig preferenceConfig;
    private SharedPreferences mpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        if(preferenceConfig.readLoginStatus()!=""){
            Intent manager = new Intent(MainActivity.this, Manager.class);
            startActivity(manager);
            finish();
        }

        uemail_r=(EditText) findViewById(R.id.userid_l);
        upass_r=(EditText) findViewById(R.id.password_l);

        mpref=getSharedPreferences("",MODE_PRIVATE);
        String stored_usremail=mpref.getString("useremail","");
        uemail_r.setText(stored_usremail);
        Log.i("shared pref","stored email"+stored_usremail);


        //contact us button code
       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Contact Us", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //login button code
        Button login_button = findViewById(R.id.Login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent manager = new Intent(MainActivity.this, Manager.class);
//                String UROLE="", USERNAME="", ProfileURL="";
//                startActivity(manager);

                uemail=uemail_r.getText().toString();
                upass = upass_r.getText().toString();

                if(uemail.length()==0){
                    Toast.makeText(MainActivity.this,"Enter Email address",Toast.LENGTH_SHORT).show();
                }else if (! (uemail.matches(emailPattern))) {
                    Toast.makeText(MainActivity.this,"Enter Valid Email address",Toast.LENGTH_SHORT).show();
                }else if(upass.length()==0){
                    Toast.makeText(MainActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
                }else if(upass.length()<8){
                    Toast.makeText(MainActivity.this,"Password too short \n minimum 8 characters",Toast.LENGTH_SHORT).show();
                }else{
                    //for testing of login status
//                    preferenceConfig.writeLoginStatus(true,uemail,"",uemail,"ucontact_test","uaddress_test","ugender_test","udob_test");
//                    SharedPreferences.Editor editor=mpref.edit();
//                    editor.putString("useremail",uemail);
//                    editor.putString("userid","test_id");
//                    editor.putString("password","test_pwd");
//                    editor.putString("usercontact","test_conct");
//                    editor.putString("useraddress","test_address");
//                    editor.putString("usergender","test_gender");
//                    editor.putString("userdob","test_dob");
//                    editor.apply();
//                    Intent manager = new Intent(MainActivity.this, Manager.class);
//                    startActivity(manager);
//                    finish();
                    //testing end here


                    createJson_send();
                }

            }
        });

        //register button code
        Button register_button = findViewById(R.id.Register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });

    }

    private void createJson_send(){

        final JSONObject jsonObject = new JSONObject();
        try {
            Log.i("volleyABC", "Creating jason");
            jsonObject.put("username", uemail);
            jsonObject.put("password", upass);
            Log.i("volleyABC", "Created jason");
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.i("volleyABC", "error in jason creation");
        }

        final String requestBody = jsonObject.toString();
        Log.i("volleyABC", requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,server_url+"/login",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                Log.i("volleyABC" ,"got response    "+response);
                Toast.makeText(MainActivity.this, "Logged IN", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor=mpref.edit();
                    editor.putString("useremail",uemail);
                    editor.putString("password",upass);
                    editor.apply();
                    Intent manager = new Intent(MainActivity.this, Manager.class);
                    startActivity(manager);
                    finish();

            }
        },new Response.ErrorListener()  {

            @Override
            public void onErrorResponse(VolleyError error) {

                try{
                    Log.i("volleyABC" ,Integer.toString(error.networkResponse.statusCode));
                    if(error.networkResponse.statusCode==400) {
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show(); //This method is used to show pop-up on the screen if user gives wrong uid
                    }

                    error.printStackTrace();}
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this,"Check Network",Toast.LENGTH_SHORT).show();}
            }
        }){
            //sending JSONOBJECT String to server starts
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        //sending JSONOBJECT String to server ends

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest); // get response from server
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}