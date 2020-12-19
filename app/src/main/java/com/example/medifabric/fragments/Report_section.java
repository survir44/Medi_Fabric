package com.example.medifabric.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medifabric.Manager;
import com.example.medifabric.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Report_section#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Report_section extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;

    public Report_section() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Report_section.
     */
    // TODO: Rename and change types and number of parameters
    public static Report_section newInstance(String param1, String param2) {
        Report_section fragment = new Report_section();
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
        ((Manager) getContext()).setActionBarTitle("Report Section");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_report_section,container,false);
        getActivity().setTitle("Reports");

        CardView sugar_reports = rootView.findViewById(R.id.sugar_reports_card);
        sugar_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("report_section","sugar");
//
                ReportList reportList = new ReportList();
                reportList.setArguments(bundle);
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount() > 2){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerID, reportList).addToBackStack(null).commit();
            }
        });

        CardView blood_reports = rootView.findViewById(R.id.blood_reports_card);
        blood_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("report_section","blood");
//
                ReportList reportList = new ReportList();
                reportList.setArguments(bundle);
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount() > 2){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerID, reportList).addToBackStack(null).commit();
            }
        });

        CardView ECG_reports = rootView.findViewById(R.id.ecg_reports_card);
        ECG_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("report_section","ecg");
//
                ReportList reportList = new ReportList();
                reportList.setArguments(bundle);
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount() > 2){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerID, reportList).addToBackStack(null).commit();
            }
        });

        CardView prescription_reports = rootView.findViewById(R.id.prescription_card);
        prescription_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("report_section","prescription");
//
                ReportList reportList = new ReportList();
                reportList.setArguments(bundle);
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount() > 2){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerID, reportList).addToBackStack(null).commit();
            }
        });

        CardView pft_reports = rootView.findViewById(R.id.pft_card);
        pft_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("report_section","pft");
//
                ReportList reportList = new ReportList();
                reportList.setArguments(bundle);
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount() > 2){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerID, reportList).addToBackStack(null).commit();
            }
        });


        return rootView;
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_report_section, container, false);
    }
}