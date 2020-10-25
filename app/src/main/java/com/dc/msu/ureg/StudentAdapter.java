package com.dc.msu.ureg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    ArrayList<Student> students;
    Context context;
    LayoutInflater inflater;
    Callback callback;

    public StudentAdapter(ArrayList<Student> students, Context context, Callback callback) {
        this.students = students;
        this.context = context;
        this.callback = callback;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.student_row, parent, false);
        return new StudentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Student student = students.get(position);
        holder.txtName.setText(student.getFname() + " " + student.getLname());
        holder.txtEmail.setText(student.getEmail());
        holder.txtEdu.setText(student.getEdu());
        holder.layoutStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, EditStudent.class).putExtra("course", student.getCourse())
                        .putExtra("fname", student.getFname())
                        .putExtra("lname", student.getLname())
                        .putExtra("email", student.getEmail())
                        .putExtra("id",student.getId())
                        .putExtra("edu", student.getEdu()));
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteStudent(student.getId());
            }
        });
        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                if (databaseHelper.getWaitingStudent(student.getId()))
                    Toast.makeText(context, "Student already added to waiting list", Toast.LENGTH_SHORT).show();
                else
                    showPriority(student.getCourse(), student.getId());
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.imgDelete.setBackground(MainActivity.getBackgroundDrawable(Color.BLACK, context.getResources().getDrawable(R.drawable.circle)));
            holder.imgAdd.setBackground(MainActivity.getBackgroundDrawable(Color.BLACK, context.getResources().getDrawable(R.drawable.circle)));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.layoutStud.setForeground(MainActivity.getBackgroundDrawable(Color.BLACK, context.getResources().getDrawable(R.drawable.rounded_edittext_white)));
                holder.layoutStud.setForegroundTintList(ColorStateList.valueOf(Color.parseColor("#0Dffffff")));
            }
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEdu, txtEmail;
        RelativeLayout layoutStud;
        ImageView imgAdd, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEdu = itemView.findViewById(R.id.txtEduLevel);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtName = itemView.findViewById(R.id.txtName);
            layoutStud = itemView.findViewById(R.id.layoutStudent);
            imgAdd = itemView.findViewById(R.id.imgAdd);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

    public void DeleteStudent(final String id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        SharedPreferences preferences = context.getSharedPreferences("PREFS", 0);
        final String color = preferences.getString("bgcolor", "#000000");
        builder.setMessage((Html.fromHtml("<font color=" + color + ">Are you sure you want to delete this entry?</font>")));

        builder.setPositiveButton((Html.fromHtml("<font color=" + color + ">Yes</font>")), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                int deleted = databaseHelper.DeleteStudent(id);
                databaseHelper.DeleteWaiting(id);
                if (deleted > 0) {
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    callback.callback();
                } else
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

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

    public void showPriority(final String cc, final String sid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Priority");
        final String[] options = {"First year", "Second year", "Third Year", "Graduated"};
        final int[] pos = new int[1];
        // cow
        builder.setSingleChoiceItems(options, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                Log.d("Clicked", String.valueOf(which));
                pos[0] = which;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                Boolean isInserted = databaseHelper.insertWaiting(cc, Integer.parseInt(sid), options[pos[0]]);
                if (isInserted)
                    Toast.makeText(context, "Added to waiting list", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
