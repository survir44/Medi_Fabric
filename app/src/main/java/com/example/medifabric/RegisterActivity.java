package com.example.medifabric;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;


public class RegisterActivity extends AppCompatActivity {

    private List<String> gender_list;
    private AppCompatSpinner gender_spinner;
    private Button register;
    private String server_url="https://165.22.210.37:8000"; //Main Server URL
    private String uname="",uemail="",uaddress="",ucontact="",ugender="",udob="",upass="",uconfirmpass="",uage="22";
    private EditText uname_r,uemail_r,uaddress_r,uconatct_r,udob_r,upass_r,uconfirm_r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        uname_r = (EditText) findViewById(R.id.name_r);
        uemail_r = (EditText) findViewById(R.id.email_r);
        uaddress_r = (EditText) findViewById(R.id.address_r);
        uconatct_r = (EditText) findViewById(R.id.phone_number_r);
        udob_r = (EditText) findViewById(R.id.dob_r);
        upass_r = (EditText) findViewById(R.id.password_r);
        uconfirm_r = (EditText) findViewById(R.id.confirm_password_r);

        //set spinner array
        gender_list = new ArrayList<>();
        gender_list.add("Male");
        gender_list.add("Female");
        gender_spinner = (AppCompatSpinner) findViewById(R.id.gender_spinner_r);
        ArrayAdapter adapter =new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_list_item_1, gender_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(adapter);

        //register button
        register=(Button) findViewById(R.id.Register_button_r);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname=uname_r.getText().toString();
                uemail=uemail_r.getText().toString();
                uaddress = uaddress_r.getText().toString();
                ucontact = uconatct_r.getText().toString();
                udob = udob_r.getText().toString();
                upass = upass_r.getText().toString();
                uconfirmpass = uconfirm_r.getText().toString();
                ugender=gender_spinner.getSelectedItem().toString();
                Log.i("volleyABC", "got values"+uname+uemail+uaddress+ucontact+udob+upass+uconfirmpass+ugender);



                if(uname.length()==0){
                    Toast.makeText(RegisterActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }else if(uemail.length()==0){
                    Toast.makeText(RegisterActivity.this,"Enter Email address",Toast.LENGTH_SHORT).show();
                }else if(uaddress.length()==0){
                    Toast.makeText(RegisterActivity.this,"Enter Address",Toast.LENGTH_SHORT).show();
                }else if(ucontact.length()==0){
                    Toast.makeText(RegisterActivity.this,"Enter Contact Number",Toast.LENGTH_SHORT).show();
                }else if(ugender.length()==0){
                    Toast.makeText(RegisterActivity.this,"Enter Gender",Toast.LENGTH_SHORT).show();
                }else if(udob.length()==0){
                    Toast.makeText(RegisterActivity.this,"Enter Date of Birth",Toast.LENGTH_SHORT).show();
                }else if(upass.length()==0){
                    Toast.makeText(RegisterActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
                }else if(uconfirmpass.length()==0){
                    Toast.makeText(RegisterActivity.this,"Confirm password feild empty ",Toast.LENGTH_SHORT).show();
                }else if(!upass.equals(uconfirmpass)){
                    Toast.makeText(RegisterActivity.this,"Enter correct password",Toast.LENGTH_SHORT).show();
                }else{
                 createJson_send();
                }
            }
        });


    }
    
    private void createJson_send(){

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", uname);
            jsonObject.put("email", uemail);
            jsonObject.put("gender", ugender);
            jsonObject.put("age", uage);
            jsonObject.put("dob", udob);
            jsonObject.put("phone", ucontact);
            jsonObject.put("address", uaddress);
            jsonObject.put("password", upass);
            Log.i("volleyABC", "Created jason");
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.i("volleyABC", "error in jason creation");
        }

        final String requestBody = jsonObject.toString();
        Log.i("volleyABC", requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volleyABC", "onResponse:edit reached "+response);
                Toast.makeText(RegisterActivity.this,"got response"+response,Toast.LENGTH_SHORT).show();
                
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    //String statusCode = String.valueOf(error.networkResponse.statusCode);
                    Log.i("volleyABC" ,"edit"+Integer.toString(error.networkResponse.statusCode));
                    Toast.makeText(RegisterActivity.this,"Invalid api call",Toast.LENGTH_SHORT).show();
                    error.printStackTrace();}
                catch (Exception e)
                {
                    Log.i("volleyABC" ,"exception");
                    Toast.makeText(RegisterActivity.this,"Check Network",Toast.LENGTH_SHORT).show();}

            }
        }){

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
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



}