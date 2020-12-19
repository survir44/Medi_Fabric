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
import com.android.volley.DefaultRetryPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocketFactory;
import java.security.cert.CertificateFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.*;
import java.security.cert.Certificate;
import javax.security.cert.X509Certificate;
import java.security.KeyStore;
import java.security.*;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.*;
import java.security.cert.*;
import java.security.cert.CertificateException;
import java.security.NoSuchAlgorithmException;
import 	java.io.FileNotFoundException;
import com.android.volley.toolbox.HurlStack;

import com.example.medifabric.SharedPreferenceConfig;





public class RegisterActivity extends AppCompatActivity {

    private List<String> gender_list;
    private AppCompatSpinner gender_spinner;
    private Button register;
    private String server_url="http://134.209.152.226:8000"; //Main Server URL
    private String uname="",uemail="",uaddress="",ucontact="",ugender="",udob="",upass="",uconfirmpass="",uage="22";
    private EditText uname_r,uemail_r,uaddress_r,uconatct_r,udob_r,upass_r,uconfirm_r;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Pattern date_pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
    private SharedPreferences mpref;
    private SharedPreferenceConfig preferenceConfig;
    private Integer flag=0,count=0;
    RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        mpref=getSharedPreferences("",MODE_PRIVATE);

        requestQueue= Volley.newRequestQueue(this);


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

//


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
                }else if (! (uemail.matches(emailPattern))) {
                    Toast.makeText(RegisterActivity.this,"Enter Valid Email address",Toast.LENGTH_SHORT).show();
                }else if(uaddress.length()==0){
                    Toast.makeText(RegisterActivity.this,"Enter Address",Toast.LENGTH_SHORT).show();
                }else if(ucontact.length()==0){
                    Toast.makeText(RegisterActivity.this,"Enter Contact Number",Toast.LENGTH_SHORT).show();
                }else if(ucontact.length()!=10){
                    Toast.makeText(RegisterActivity.this,"Enter Valid Contact Number",Toast.LENGTH_SHORT).show();
                }else if(ugender.length()==0){
                    Toast.makeText(RegisterActivity.this,"Enter Gender",Toast.LENGTH_SHORT).show();
                }else if(udob.length()==0){
                    Toast.makeText(RegisterActivity.this,"Enter Date of Birth",Toast.LENGTH_SHORT).show();
                }else if(!date_pattern.matcher(udob).matches()){
                    Toast.makeText(RegisterActivity.this,"Enter Valid Date YYYY-MM-DD",Toast.LENGTH_SHORT).show();
                }else if(upass.length()==0){
                    Toast.makeText(RegisterActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
                }else if(upass.length()<8){
                    Toast.makeText(RegisterActivity.this,"Password too short \n minimum 8 characters",Toast.LENGTH_SHORT).show();
                }else if(uconfirmpass.length()==0){
                    Toast.makeText(RegisterActivity.this,"Confirm password feild empty ",Toast.LENGTH_SHORT).show();
                }else if(!upass.equals(uconfirmpass)){
                    Toast.makeText(RegisterActivity.this,"Enter correct password",Toast.LENGTH_SHORT).show();
                }else if(flag==0){
                 createJson_send();
                }
            }
        });



    }


    
    private void createJson_send(){
        flag = 1;

        final JSONObject jsonObject = new JSONObject();
        try {
            Log.i("volleyABC", "Creating jason");
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url+"/register", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                flag=0;
                Log.i("volleyABC", "onResponse:edit reached "+response);
                String uid="",name="",pubkey="",prikey="";
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    uid = jsonObject1.getString("id");
                    name=jsonObject1.getString("name");
                    pubkey=jsonObject1.getString("public");
                    prikey=jsonObject1.getString("private");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegisterActivity.this,"Registred Succesfully",Toast.LENGTH_SHORT).show();
                preferenceConfig.writeLoginStatus(true,uid,upass,uemail,ucontact,uaddress,ugender,udob,name,pubkey,prikey,"http://134.209.152.226:8000/images/"+uid+".jpg");

                    Intent manager = new Intent(RegisterActivity.this, Manager.class);
                    startActivity(manager);
                    finish();
                
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                flag=0;
                try{
                    Toast.makeText(RegisterActivity.this,"Error"+error.toString(),Toast.LENGTH_SHORT).show();

                    if(error.networkResponse.statusCode == 400) {
                        Toast.makeText(RegisterActivity.this, "Email already Registred!!", Toast.LENGTH_SHORT).show();
                    }
                    error.printStackTrace();
                }
                catch (Exception e)
                {
                    Log.i("volleyABC" ,"exception"+e.toString());
                    Toast.makeText(RegisterActivity.this,"Check Network"+e.toString(),Toast.LENGTH_LONG).show();}
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
                return "application/json";
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        count++;
//        Toast.makeText(RegisterActivity.this,"sending request count"+count.toString(),Toast.LENGTH_SHORT).show();

        requestQueue.add(stringRequest);
        Log.i("volleyABC", "addd string request ");


    }

//        RequestQueue requestQueue= Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
//    private SSLSocketFactory getSocketFactory() {
//
//        CertificateFactory cf = null;
//        try {
//
//            cf = CertificateFactory.getInstance("X.509");
//            InputStream caInput = getResources().openRawResource(R.raw.cert_name);
//            Certificate ca;
//            try {
//
//                ca = cf.generateCertificate(caInput);
////                Log.e("CERT", "ca=" + ((X509Certificate) ca).getSubjectDN());
//            } finally {
//                caInput.close();
//            }
//
//
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);
//
//
//            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//
//                    Log.e("CipherUsed", session.getCipherSuite());
////                    return hostname.compareTo("134.209.152.226")==0; //The Hostname of your server.
//                    return  true;
//
//                }
//            };
//
//
//            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//            SSLContext context = null;
//            context = SSLContext.getInstance("TLS");
//
////            HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> {
////                if(hostname.equals("134.209.152.226")) return true;
////                return false;
////            });
//
//            context.init(null, tmf.getTrustManagers(), null);
//            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
//
//            SSLSocketFactory sf = context.getSocketFactory();
//
//
//            return sf;
//
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        }
//         catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//
//        return  null;
//    }



}