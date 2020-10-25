package com.dc.msu.ureg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.levitnudi.legacytableview.LegacyTableView;

import java.util.ArrayList;

public class WaitingList extends AppCompatActivity {
    Toolbar toolbar;
    TextView btnAdd;
    String course;
    RecyclerView rcWaiting;
    TextView txtTitle;
    ArrayList<Waiting> waitings = new ArrayList<>();
    WaitingAdapter adapter;
    TextView txtEmpty;
    LegacyTableView legacyTableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        initView();
        toolbarSetup();
        btnAdd.setVisibility(View.GONE);
        txtTitle.setText("Waiting List");
        loadStudents();
        rcWaiting.setVisibility(View.GONE);
    }

    private void initView() {
        toolbar = findViewById(R.id.layoutActionBar);
        btnAdd = findViewById(R.id.btnAdd);
        rcWaiting = findViewById(R.id.rcStudents);
        txtEmpty = findViewById(R.id.txtEmpty);
        txtTitle = findViewById(R.id.txtActionTitle);
        legacyTableView = findViewById(R.id.legacy_table_view);
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
        Cursor res = databaseHelper.getWaiting();

        waitings.clear();
        txtEmpty.setText("No student added to waiting list");
        if (res.getCount() < 1) {
            txtEmpty.setVisibility(View.VISIBLE);

        } else
            txtEmpty.setVisibility(View.GONE);
        LegacyTableView.insertLegacyTitle("Course Code", "Student Code","Student Name",
                "Priority");
        while (res.moveToNext()) {
            String cc = res.getString(0);
            String sid = res.getString(1);
            String pr = res.getString(2);
            String n = null;
            Cursor name=databaseHelper.getStudentsid(sid);
            while (name.moveToNext()){
                n=name.getString(1)+" "+name.getString(2);
            }
            LegacyTableView.insertLegacyContent(res.getString(0),
                    res.getString(1), n,res.getString(2));

            waitings.add(new Waiting(cc, sid, pr));
        }
        adapter = new WaitingAdapter(waitings, this, new Callback() {
            @Override
            public void callback() {
                loadStudents();
                if (waitings.size() < 1)
                    txtEmpty.setVisibility(View.VISIBLE);
                else
                    txtEmpty.setVisibility(View.GONE);

            }
        });
        rcWaiting.setVerticalScrollBarEnabled(true);
//        rcWaiting.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcWaiting.setLayoutManager(new GridLayoutManager(this, 2));

        rcWaiting.setAdapter(adapter);
        legacyTableView.setTitle(LegacyTableView.readLegacyTitle());
        legacyTableView.setContentTextSize(35);
        legacyTableView.setTitleTextSize(35);
        legacyTableView.setContent(LegacyTableView.readLegacyContent());
        legacyTableView.setTitleTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//
        //remember to build your table as the last step
        legacyTableView.build();
    }

}