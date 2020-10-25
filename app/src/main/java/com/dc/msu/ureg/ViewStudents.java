package com.dc.msu.ureg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.levitnudi.legacytableview.LegacyTableView;

import java.util.ArrayList;

public class ViewStudents extends AppCompatActivity {
    Toolbar toolbar;
    TextView btnAdd;
    String course;
    RecyclerView rcStudents;
    ArrayList<Student> students = new ArrayList<>();
    StudentAdapter adapter;
    TextView txtEmpty;
    LegacyTableView legacyTableView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        initView();
        toolbarSetup();
        course = getIntent().getStringExtra("course");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddStudents.class).putExtra("course", course));
            }
        });
        loadStudents();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnAdd.setBackground(MainActivity.getBackgroundDrawable(Color.WHITE,getResources().getDrawable(R.drawable.rounded_corners)));
        }
        legacyTableView.setVisibility(View.GONE);
    }

    private void initView() {
        toolbar = findViewById(R.id.layoutActionBar);
        btnAdd = findViewById(R.id.btnAdd);
        rcStudents = findViewById(R.id.rcStudents);
        txtEmpty=findViewById(R.id.txtEmpty);
        legacyTableView=findViewById(R.id.legacy_table_view);
    }

    private void toolbarSetup() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void loadStudents() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        Cursor res = databaseHelper.getStudentsByCourse(course);
        Log.d("diaryFragHome", String.valueOf(res.getCount()));
        students.clear();
        if (res.getCount()<1)
            txtEmpty.setVisibility(View.VISIBLE);
        else
            txtEmpty.setVisibility(View.GONE);

        while (res.moveToNext()) {
            String id = res.getString(0);
            String fn = res.getString(1);
            String ln = res.getString(2);
            String em = res.getString(3);
            String el = res.getString(4);
            String cc = res.getString(5);
            students.add(new Student(id,fn, ln, em, el, cc));
        }
        adapter = new StudentAdapter(students, this, new Callback() {
            @Override
            public void callback() {
                loadStudents();
                if (students.size()<1)
                    txtEmpty.setVisibility(View.VISIBLE);
                else
                    txtEmpty.setVisibility(View.GONE);

            }
        });
        rcStudents.setVerticalScrollBarEnabled(true);
        rcStudents.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rcStudents.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }
}