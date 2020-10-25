package com.dc.msu.ureg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.ArrayList;


public class EditStudent extends AppCompatActivity {
    Toolbar toolbar;
    EditText txtFname,txtLname,txtEmail,txtEdu;
    ImageView imgDone;
    AwesomeValidation awesomeValidation;
    TextView txtActionTitle;
    String course,id;
    String fname,lname,edu,email;
    DatabaseHelper helper;
    ArrayList<String> priorities=new ArrayList<>();
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);
        initView();
        toolbarSetup();
        initData();
        setData();
        validateViews();
        helper=new DatabaseHelper(getApplicationContext());
        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate() && spinner.getSelectedItemPosition()!=0)
                {
                    Boolean isUpdated=helper.updateData(txtFname.getText().toString(),txtLname.getText().toString(),txtEmail.getText().toString(),txtEdu.getText().toString(),course,id);
                    helper.updatePriority(priorities.get(spinner.getSelectedItemPosition()),id);
                    if (isUpdated) {
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

                    }else
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                    finish();
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
    private void setData() {
        txtActionTitle.setText("Edit");
        txtFname.setText(fname);
        txtLname.setText(lname);
        txtEdu.setText(edu);
        txtEmail.setText(email);
    }

    private void initData() {
        course=getIntent().getStringExtra("course");
        fname=getIntent().getStringExtra("fname");
        lname=getIntent().getStringExtra("lname");
        email=getIntent().getStringExtra("email");
        edu=getIntent().getStringExtra("edu");
        id=getIntent().getStringExtra("id");
    }

    private void initView() {
        toolbar=findViewById(R.id.layoutActionBar);
        txtFname=findViewById(R.id.txtFname);
        txtLname=findViewById(R.id.txtLname);
        txtEdu=findViewById(R.id.txtEduLevel);
        txtEmail=findViewById(R.id.txtEmail);
        imgDone=findViewById(R.id.imgDone);
        spinner=findViewById(R.id.spnPriority);
        txtActionTitle=findViewById(R.id.txtActionTitle);

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
        DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
        Cursor res=databaseHelper.getWaitingStudents(id);
        String p=null;
        while (res.moveToNext()){
            p=res.getString(2);
        }
        spinner.setSelection(priorities.indexOf(p));
    }


}