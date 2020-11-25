package com.example.medifabric;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;


public class RegisterActivity extends AppCompatActivity {

    private List<String> gender_list;
    private AppCompatSpinner gender_spinner;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set spinner array
        gender_list = new ArrayList<>();
        gender_list.add("Male");
        gender_list.add("Female");
        gender_spinner = (AppCompatSpinner) findViewById(R.id.gender_spinner_r);
        ArrayAdapter adapter =new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gender_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(adapter);

        //register button
        register=(Button) findViewById(R.id.Register_button_r);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manager = new Intent(RegisterActivity.this, Manager.class);
                String UROLE="", USERNAME="", ProfileURL="";
                startActivity(manager);
            }
        });






    }

}