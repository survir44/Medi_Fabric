package com.example.medifabric.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.medifabric.SharedPreferenceConfig;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Feedback#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Feedback extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,useremail;

    View rootView;
    String server_url = "http://134.209.152.226:8000";
    //    private SharedPreferenceConfig preferenceConfig;
//    private SharedPreferences mpref;
    private String usrid, usrname = "";
    EditText feedback_text;
    TextView name_text_v;
    Button save_feedback;
    String feedback = null;
    JSONObject jsonObject =new JSONObject();
    private RequestQueue mRequestQueue;


    public Feedback() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Feedback.
     */
    // TODO: Rename and change types and number of parameters
    public static Feedback newInstance(String param1, String param2) {
        Feedback fragment = new Feedback();
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
        ((Manager) getContext()).setActionBarTitle("Feedback");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        getActivity().setTitle("Feedback");

        SharedPreferenceConfig preferenceConfig;
        preferenceConfig = new SharedPreferenceConfig(getActivity().getApplicationContext());
        usrname=preferenceConfig.read_username();
        usrid=preferenceConfig.read_userid();
        useremail=preferenceConfig.read_email();

        feedback_text = rootView.findViewById(R.id.text_feedback);
        name_text_v = rootView.findViewById(R.id.name_feedback);
        name_text_v.setText(usrname);

        save_feedback = rootView.findViewById(R.id.feedback_save);


        save_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = feedback_text.getText().toString();

                //below two lines closes keyborad input on click of save button
                InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getActivity().getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

//                Toast.makeText(getActivity(), "Feedback section save clicked" + feedback, Toast.LENGTH_SHORT).show();
                setJason();
                send_data();
                feedback_text.setText("");

            }
        });
        return rootView;
    }

    public void setJason() {

        if (feedback == null) {
            Toast.makeText(getActivity(), "Enter Feedback", Toast.LENGTH_SHORT).show();
        } else {

            try {
                jsonObject.put("id", usrid); //value from bundle
                jsonObject.put("feedback", feedback);
                jsonObject.put("mail",useremail);
                Log.i("info123", String.valueOf(jsonObject));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    public  void send_data(){
        final String requestBody = jsonObject.toString();
        Log.i("volleyABC123",requestBody);

        //getting response from server starts

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url+"/feedback", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volleyABC", "got response    " + response);
                Toast.makeText(getActivity(), "Thank you for feedback", Toast.LENGTH_SHORT).show();

                //this will close feedback and return to main page
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    Log.i("volleyABC", Integer.toString(error.networkResponse.statusCode));
                    Toast.makeText(getActivity(), "Error:-" + statusCode, Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                } catch(Exception e) {
                    Toast.makeText(getActivity(), "Check Network",Toast.LENGTH_SHORT).show();
                }
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
}