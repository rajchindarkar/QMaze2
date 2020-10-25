package com.dc.msu.ureg;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class CourseAdapter extends  RecyclerView.Adapter<CourseAdapter.ViewHolder>{
    ArrayList<Course> courses;
    Context context;
    LayoutInflater inflater;

    public CourseAdapter(ArrayList<Course> courses, Context context) {
        this.courses = courses;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.course_row, parent, false);
        return new CourseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Course course=courses.get(position);
        holder.txtTitle.setText(course.getTitle());
        holder.txtCode.setText(course.getCode());
        holder.txtSeats.setText("Seats : "+course.getSeats());
        holder.txtCredits.setText("Credits : "+course.getCredits());
        holder.layoutCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,ViewStudents.class).putExtra("course",course.getCode()));
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.layoutCourse.setForeground(MainActivity.getBackgroundDrawable(Color.BLACK, context.getResources().getDrawable(R.drawable.rounded_edittext_white)));
                holder.layoutCourse.setForegroundTintList(ColorStateList.valueOf(Color.parseColor("#0Dffffff")));
            }
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtCode,txtTitle,txtSeats, txtCredits;
        LinearLayout layoutCourse;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCode=itemView.findViewById(R.id.txtCode);
            txtCredits=itemView.findViewById(R.id.txtCredits);
            txtSeats=itemView.findViewById(R.id.txtSeats);
            txtTitle=itemView.findViewById(R.id.txtTitle);
            layoutCourse=itemView.findViewById(R.id.layoutCourse);
        }
    }
}
