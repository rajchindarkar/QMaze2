package com.dc.msu.ureg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.ArrayList;


public class AddStudents extends AppCompatActivity {
    Toolbar toolbar;
    EditText txtFname,txtLname,txtEmail,txtEdu;
    ImageView imgDone;
    AwesomeValidation awesomeValidation;
    String course;
    Spinner spinner;
    ArrayList<String> priorities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);
        initView();
        toolbarSetup();
        course=getIntent().getStringExtra("course");
        validateViews();
        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate() && spinner.getSelectedItemPosition()!=0){
                    final DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
                    boolean isInserted=helper.insertStudent(txtFname.getText().toString(),txtLname.getText().toString(),txtEmail.getText().toString(),txtEdu.getText().toString(),course);

                    if (isInserted) {
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Cursor res=helper.getStudentsByCourse(course);
                                String sid = null;
                                while (res.moveToNext()){
                                    sid=res.getString(0);
                                }
                                helper.insertWaiting(course,Integer.parseInt(sid),priorities.get(spinner.getSelectedItemPosition()));
                                finish();
                                startActivity(new Intent(getApplicationContext(),WaitingList.class));
                            }
                        },0);
                    }else
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();


                }
                else
                    Toast.makeText(getApplicationContext(), "Please select priority", Toast.LENGTH_SHORT).show();

            }
        });
        manageSpinner();
    }

    private void validateViews() {
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.txtFname, "^[/^\\S+$/\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.errfname);
        awesomeValidation.addValidation(this, R.id.txtLname, "^[/^\\S+$/\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.errlname);
        awesomeValidation.addValidation(this, R.id.txtEmail, "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$", R.string.errmail);
        awesomeValidation.addValidation(this, R.id.txtEduLevel, "^[/^\\S+$/\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.erredu);

    }

    private void initView() {
        toolbar=findViewById(R.id.layoutActionBar);
        txtFname=findViewById(R.id.txtFname);
        txtLname=findViewById(R.id.txtLname);
        txtEdu=findViewById(R.id.txtEduLevel);
        txtEmail=findViewById(R.id.txtEmail);
        imgDone=findViewById(R.id.imgDone);
        spinner=findViewById(R.id.spnPriority);
    }

    private void  toolbarSetup() {
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
    private  void manageSpinner(){

        priorities.add("Priority");
        priorities.add("First year");
        priorities.add("Second Year");
        priorities.add("Third year");
        priorities.add("Graduated");
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_item,priorities)
        {
            @Override
            public boolean isEnabled(int position) {
            if (position == 0) {
                return false;
            } else {
                return true;
            }
        }
        };
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
    }
}