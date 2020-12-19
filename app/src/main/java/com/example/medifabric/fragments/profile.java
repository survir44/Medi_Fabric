package com.example.medifabric.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medifabric.ActivityManager;
import com.example.medifabric.Manager;
import com.example.medifabric.R;
import com.example.medifabric.SharedPreferenceConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import android.provider.MediaStore;
import android.net.Uri;
import android.graphics.Bitmap;
import android.content.Context;

import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;
import java.util.Base64.*;
import android.util.Base64OutputStream;
import com.example.medifabric.MultipartRequest;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,stored_usrid ,UProfile;
    String server_url="http://134.209.152.226:8000";  //Main Server URL
    View rootView;
    ImageView imageButton;
    private SharedPreferenceConfig preferenceConfig;
    ImageButton add_dp;
    private Uri imageUri;
    private Bitmap thumbnail;
    ContentValues values;
    Bitmap bitmap;
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private byte[] multipartBody2;

    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ((Manager) getContext()).setActionBarTitle("Manager");
    }

    @Override
    public void onResume(){
        super.onResume();

        // Set title bar
//        ( getActivity()).setActionBarTitle("Your title");
        ((Manager) getContext()).setActionBarTitle("Profile");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile,container,false);

        getActivity().setTitle("My Profile");
//        getActivity().getActionBar().setTitle("My Profile");
//        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("Your Title");


        preferenceConfig = new SharedPreferenceConfig(getActivity().getApplicationContext());
        TextView id = rootView.findViewById(R.id.id_p);
        stored_usrid=preferenceConfig.read_userid();
        UProfile=preferenceConfig.read_uprofileurl();
        Log.i("picasso camera", "image url "+UProfile);
        id.setText(stored_usrid);
        get_data();//fetch data from server

        imageButton = rootView.findViewById(R.id.profile_photo);
        loadImageUrl(UProfile);
        add_dp = rootView.findViewById(R.id.add_dp_p);
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "MyPicture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());

//        checkPermission("android.permission.WRITE_EXTERNAL_STORAGE",0);



        add_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(takePicture, 0);//zero can be replaced with any action code (called requestCode)
                if(!checkPermission(new String[] {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.CAMERA"},0)){
                    Toast.makeText(getActivity(),"Please Grant permisiions",Toast.LENGTH_SHORT);
                }
                else{
                imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,0);
                }
            }
        });



        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Log.i("picasso camera", "got response"+Integer.toString(resultCode)+Integer.toString(requestCode));
        Toast.makeText(getActivity(),"text"+Integer.toString(resultCode),Toast.LENGTH_SHORT);
        Log.i("picasso camera", "got response 3rd"+Integer.toString(resultCode)+Integer.toString(requestCode));
        if(resultCode == -1){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                Log.i("picasso camera", "image size before"+bitmap.getAllocationByteCount());
                imageButton.setImageBitmap(bitmap);
//                send_image(stored_usrid,bitmap);
                send_image2(stored_usrid,bitmap);
//                send_image2(stored_usrid,b);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void loadImageUrl(String url) {
        Picasso.get().load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageButton, new com.squareup.picasso.Callback(){

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
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        Log.i("picasso camera", "image size after in getstring"+encodedImage.length());
        return encodedImage;
    }

    private void send_image (String userid, Bitmap image ) {

        String URL = server_url+"/upload";
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());


        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("id", userid);
        postParam.put("pic",getStringImage(image));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "multipart/form-data;boundary=" + boundary;
            }

        };
        mRequestQueue.add(jsonObjReq);
    }

    private void send_image2(String userid, final Bitmap image){

        byte[] multipartBody;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        multipartBody = baos.toByteArray();
        try {

            buildPart(dos,multipartBody, userid+".png");

            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            multipartBody = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MultipartRequest multipartRequest = new MultipartRequest(server_url+"/upload", null, mimeType, multipartBody, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Toast.makeText(getActivity(), "Upload successfully!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Upload failed!\r\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(multipartRequest);
//        Volley.getInstance(getActivity()).addToRequestQueue(multipartRequest);
    }




    void buildPart(DataOutputStream dataOutputStream, byte[] fileData, String fileName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
                + fileName + "\"" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

    void get_data() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id",stored_usrid); //actual value shud be id_s
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonObject.toString();
        Log.i("volleyABC ", requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url+"/profile", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("volleyABC response", response);
                //Toast.makeText(MainActivity.this,response, Toast.LENGTH_SHORT).show();
                set_data(response);//set data in textiles
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    //String statusCode = String.valueOf(error.networkResponse.statusCode);
                    Log.i("volleyABC" ,Integer.toString(error.networkResponse.statusCode));
                    Toast.makeText(getActivity(),"Error in API",Toast.LENGTH_SHORT).show();//it will not occur as authenticating at start
                    error.printStackTrace();}
                catch (Exception e)
                {
                    Log.i("volleyABC" ,"exception");
                    Toast.makeText(getActivity(),"Check Network",Toast.LENGTH_SHORT).show();} //occur if connection not get estabilished
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
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    void set_data(String data)
    {
        Log.i("volleyABC", "set_data called"+data);


        TextView name = rootView.findViewById(R.id.profile_name_p);
        TextView email = rootView.findViewById(R.id.email_p);
        TextView phn = rootView.findViewById(R.id.phn_P);
        TextView dob = rootView.findViewById(R.id.dob_p);
        TextView address = rootView.findViewById(R.id.address_p);
        TextView gender = rootView.findViewById(R.id.gender_p);

        JSONObject fetchedData ;
        try {
            fetchedData= new JSONObject(data);
            fetchedData = fetchedData.getJSONObject("profile");


            name.setText(fetchedData.getString("name"));
            email.setText(fetchedData.getString("email"));
            phn.setText(fetchedData.getString("phone"));
            dob.setText(fetchedData.getString("dob"));
            address.setText(fetchedData.getString("address"));
            gender.setText(fetchedData.getString("gender"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPermission(String[] permission, int requestCode)
    {
        ArrayList<String> to_be_taken = new ArrayList<String>();

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(getActivity(), permission[0]) == PackageManager.PERMISSION_DENIED) {
            Log.i("permission check" ,"taking ");
            to_be_taken.add(permission[0]);

        }
        if (ContextCompat.checkSelfPermission(getActivity(), permission[1]) == PackageManager.PERMISSION_DENIED) {
            Log.i("permission check" ,"taking ");
            to_be_taken.add(permission[1]);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), permission[2]) == PackageManager.PERMISSION_DENIED) {
            Log.i("permission check" ,"taking ");
            to_be_taken.add(permission[2]);

        }
        if(to_be_taken.size() > 0){
            String[] p ;
            p=to_be_taken.toArray(new String[0]);
            ActivityCompat.requestPermissions(getActivity(),p, requestCode);
            return false;
        }
        else{
            Log.i("permission check" ,"taking done");
            return true;
        }
    }


}































