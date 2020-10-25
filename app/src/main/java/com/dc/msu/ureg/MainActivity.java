package com.dc.msu.ureg;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper courseHelper;
    Toolbar toolbar;
    androidx.recyclerview.widget.RecyclerView rcCourse;
    ArrayList <Course> courses=new ArrayList<>();
    ImageView showWaiting;
    CourseAdapter recyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarSetup();
        initView();
        Log.d("pmname",getPackageName());
        courseHelper = new DatabaseHelper(getApplicationContext());
        if (courseHelper.getCourse().getCount()<1){
            insertCourses();
        }
        loadCourses();
        showWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),WaitingList.class));
            }
        });
    }

    private void initView() {
        toolbar=findViewById(R.id.layoutActionBar);
        rcCourse=findViewById(R.id.rcCourse);
        showWaiting=findViewById(R.id.imgShowWaiting);
    }

    private void insertCourses() {
        courseHelper.insert("C001", "Human-Computer Interaction", 3, 50);
        courseHelper.insert("C002", "Web Development", 3, 50);
        courseHelper.insert("C003", "Computer Networks", 3, 50);
        courseHelper.insert("C004", "Database Systems", 3, 50);
        courseHelper.insert("C005", "Computer Security", 3, 50);
        courseHelper.insert("C006", "Presentation Skills For Business", 3, 50);
        courseHelper.insert("C007", "Cyberlaw", 3, 50);
        courseHelper.insert("C008", "Information Technology Project Management", 3, 50);
        courseHelper.insert("C009", "Software Process Management", 3, 50);
        courseHelper.insert("C010", "Software Engineering", 3, 50);
        courseHelper.insert("C011", "Operating System", 3, 50);
        courseHelper.insert("C012", "Mobile Computing", 3, 50);
        courseHelper.insert("C012", "Advanced Database Systems", 3, 50);

    }
    private void  toolbarSetup() {
        setSupportActionBar(toolbar);

    }
    private void loadCourses(){
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        Cursor res = databaseHelper.getCourse();
        Log.d("diaryFragHome", String.valueOf(res.getCount()));
        courses.clear();



        while (res.moveToNext()) {
            String id = res.getString(0);
            String title = res.getString(1);
            String seats = res.getString(2);
            String credits = res.getString(3);

          courses.add(new Course(id,title,credits,seats));
        }
        recyclerViewAdapter = new CourseAdapter(courses,MainActivity.this);
        rcCourse.setVerticalScrollBarEnabled(true);
        rcCourse.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rcCourse.setAdapter(recyclerViewAdapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static RippleDrawable getBackgroundDrawable(int pressedColor, Drawable backgroundDrawable) {
        return new RippleDrawable(getPressedState(pressedColor), backgroundDrawable, null);
    }

    public static ColorStateList getPressedState(int pressedColor) {
        return new ColorStateList(new int[][]{new int[]{}}, new int[]{pressedColor});
    }
    public void showExitPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        String color = preferences.getString("bgcolor", "#000000");
        builder.setMessage((Html.fromHtml("<font color=" + color + ">Are you sure to exit?</font>")));

        builder.setPositiveButton((Html.fromHtml("<font color=" + color + ">Yes</font>")), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });

        builder.setNegativeButton((Html.fromHtml("<font color=" + color + ">NO</font>")), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {

        showExitPopup();
    }
}