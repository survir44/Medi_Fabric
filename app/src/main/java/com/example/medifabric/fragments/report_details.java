package com.example.medifabric.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medifabric.Manager;
import com.example.medifabric.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;


import java.io.UnsupportedEncodingException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link report_details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class report_details extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "section";
    private String server_url="http://134.209.152.226:8000"; //Main Server URL
    View rootview;

    // TODO: Rename and change types of parameters
    private String mParam1,id,section;
    private String mParam2;

    public report_details() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment report_details.
     */
    // TODO: Rename and change types and number of parameters
    public static report_details newInstance(String param1, String param2) {
        report_details fragment = new report_details();
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
            id = getArguments().getString(ARG_PARAM1);
            section = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_report_details,container,false);
        getActivity().setTitle("Report Details");
        TextView title = rootview.findViewById(R.id.rd_title);
        title.setText(section.toUpperCase()+"REPORT");
        get_data();
        return  rootview;
    }
    private void get_data(){

        final JSONObject jsonObject = new JSONObject();
        try {
            Log.i("volleyABC", "Creating jason");
            jsonObject.put("id", id);
            Log.i("volleyABC", "Created jason");
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.i("volleyABC", "error in jason creation");
        }

        final String requestBody = jsonObject.toString();
        Log.i("volleyABC", requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,server_url+"/report/"+section+"/desc",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                Log.i("volleyABC" ,"got response    "+response);
                try {
                    JSONObject reponse_json = new JSONObject(response);
                    JSONArray jsonArray = reponse_json.getJSONArray("report");
                    reponse_json=jsonArray.getJSONObject(0);

//                    Set<String> s =  reponse_json.keys();
                    Integer dd=0;
                    Iterator<?> i = reponse_json.keys();
                    do{
                        dd++;
                        String k = i.next().toString();
                        String idd="rd_d"+dd.toString();
                        TextView dtemp= rootview.findViewById(getResources().getIdentifier(idd, "id", getActivity().getPackageName()));
                        dtemp.setVisibility(View.VISIBLE);
                        dtemp.setText(k+" : "+reponse_json.getString(k));

                    }while(i.hasNext() && dd<20);


                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },new Response.ErrorListener()  {

            @Override
            public void onErrorResponse(VolleyError error) {

                try{
                    Log.i("volleyABC" ,Integer.toString(error.networkResponse.statusCode));
                    if(error.networkResponse.statusCode==400) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show(); //This method is used to show pop-up on the screen if user gives wrong uid
                    }

                    error.printStackTrace();}
                catch (Exception e)
                {
                    Toast.makeText(getActivity(),"Check Network",Toast.LENGTH_SHORT).show();}
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

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest); // get response from server

    }
}