package com.example.medifabric.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.medifabric.MainActivity;
import com.example.medifabric.Manager;
import com.example.medifabric.R;
import com.example.medifabric.SharedPreferenceConfig;
import com.example.medifabric.mAdapter.ExampleAdapter;
import com.example.medifabric.mAdapter.ExampleItem;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportList extends Fragment implements ExampleAdapter.OnItemClickedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "report_section";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferenceConfig preferenceConfig;
    private String server_url="http://134.209.152.226:8000"; //Main Server URL

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String report_section;

    public static final String EXTRA_AGENDA = "agenda";
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_TIME = "time";
    public static final String EXTRA_CREATOR = "creator";
    public static final String EXTRA_POINTS = "points";
    public static final String EXTRA_TASK = "task";
    public static final String EXTRA_PERSON = "person";

    private RecyclerView rv;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;
    private View rootView;
    EditText SearchInput;
    private String  UID;
    CharSequence search="";
    SwipeRefreshLayout swipeRefreshLayout;
    String userid,uname;

    View rootview;

    public ReportList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportList.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportList newInstance(String param1, String param2) {
        ReportList fragment = new ReportList();
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
            report_section = getArguments().getString(ARG_PARAM1);
        }
        preferenceConfig = new SharedPreferenceConfig(getActivity().getApplicationContext());
        uname=preferenceConfig.read_email();
        userid=preferenceConfig.read_userid();
//        Toast.makeText(getActivity(), "clicked"+userid+uname+report_section, Toast.LENGTH_SHORT).show();

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
        ((Manager) getContext()).setActionBarTitle("Report Section");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_report_list,null);
        getActivity().setTitle("Reports");
        Bundle bundle = getArguments();
        UID = this.getArguments().getString("userid");
        //SerachBar
        SearchInput = rootView.findViewById(R.id.search_bar);
        //We are getting User ID from navigation manager to this fragment

        //REFERENCE
        rv = (RecyclerView) rootView.findViewById(R.id.recycler_view_RV);

        //LAYOUT MANAGER
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipe(); //This method is used to add swipe refresh Layout

        mExampleList = new ArrayList<>();

//        mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        parseJSON(); //This method is used to get list of Agendas from server
        //Adapter
//        set_recycler("");
        createJson_send(report_section);
        rv.setAdapter(new ExampleAdapter(getActivity(), mExampleList));
        SearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mExampleAdapter.getFilter().filter(s);
                search = s;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return rootView;
    }
    public String toString() {
        return "Reports";
    }


    void swipe() {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresher);
        //swipeRefreshLayout.setColorSchemeResources(R.color.Red,R.color.OrangeRed,R.color.Yellow,R.color.GreenYellow,R.color.BlueViolet);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                        int min = 65;
                        int max = 95;

//                        parseJSON();
//                        set_recycler("");
                        createJson_send(report_section);
                    }
                },1000);
            }
        });
    }
    private void createJson_send(String report_section){

        final JSONObject jsonObject = new JSONObject();
        try {
            Log.i("volleyABC", "Creating jason");
            jsonObject.put("id", userid);
            Log.i("volleyABC", "Created jason");
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.i("volleyABC", "error in jason creation");
        }

        final String requestBody = jsonObject.toString();
        Log.i("volleyABC", requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,server_url+"/report/"+report_section,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                Log.i("volleyABC" ,"got response    "+response);
                set_recycler(response);

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

    public void set_recycler(String response){
        mExampleList.clear(); //We should clear our arraylist
        mExampleAdapter = new ExampleAdapter(getActivity(), mExampleList);
        TextView no_reports = rootView.findViewById(R.id.no_reports_yet);
        no_reports.setVisibility(View.GONE);
//        response= "[{\"agenda\": \"a\",\"da_te\": \"20/12/12\",\"ti_me\": \"bdjbs\",\"creator\": \"agcs\",\"minute\": \"dd\",\"work\": \"dhdbdj\"},{\"agenda\": \"a\",\"da_te\": \"20/12/12\",\"ti_me\": \"bdjbs\",\"creator\": \"agcs\",\"minute\": \"dd\",\"work\": \"dhdbdj\"}]";
        try {
            JSONObject reponse_json = new JSONObject(response);
            JSONArray jsonArray = reponse_json.getJSONArray("report");

            for(int i=0; i< jsonArray.length(); i++) {
                JSONObject minutes = jsonArray.getJSONObject(i);

                String report_id = minutes.getString("id");
                String date = minutes.getString("da_te");
                String issued_by = minutes.getString("lab_name");
                String type = minutes.getString("type");



                //in the above variable date we are not getting date in DD:MM:YYYYY
                //so we are creating new variable date1 to get our desire format
                String date1 = date.substring(8,10) + "/" + date.substring(5,7) + "/" + date.substring(0,4);
                mExampleList.add(new ExampleItem(report_id, date1,"test_time",issued_by, type));
                }

            mExampleAdapter.notifyDataSetChanged();
            rv.setAdapter(mExampleAdapter);
            mExampleAdapter.setOnItemClickListener(ReportList.this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        ExampleItem clickedItem = mExampleList.get(position);
        String rid=clickedItem.getAgenda();
        Toast.makeText(getActivity(), "clicked"+rid, Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("id",rid);
        bundle.putString("section",report_section);
        report_details report_details_f = new report_details();
        report_details_f.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerID, report_details_f).addToBackStack(null).commit();


    }
}