package com.dc.msu.ureg;;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
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

public class WaitingAdapter extends RecyclerView.Adapter<WaitingAdapter.ViewHolder> {
    ArrayList<Waiting> waitings;
    Context context;
    LayoutInflater inflater;
    Callback callback;

    public WaitingAdapter(ArrayList<Waiting> waitings, Context context, Callback callback) {
        this.waitings = waitings;
        this.context = context;
        this.callback = callback;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.waiting_list_row, parent, false);
        return new WaitingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Waiting waiting = waitings.get(position);
        holder.txtPriority.setText(waiting.getPriority());
        holder.txtCourse.setText(waiting.getCourse());
        holder.txtStud.setText(waiting.getStudent_id());
        DatabaseHelper databaseHelper=new DatabaseHelper(context);
        holder.txtName.setText(databaseHelper.getName(waiting.getStudent_id()));
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteStudent(waiting.getStudent_id());
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.imgDelete.setBackground(MainActivity.getBackgroundDrawable(Color.BLACK, context.getResources().getDrawable(R.drawable.circle)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.layoutWait.setForeground(MainActivity.getBackgroundDrawable(Color.BLACK, context.getResources().getDrawable(R.drawable.rounded_edittext_white)));
                holder.layoutWait.setForegroundTintList(ColorStateList.valueOf(Color.parseColor("#0Dffffff")));
            }
        }
    }

    @Override
    public int getItemCount() {
        return waitings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCourse, txtStud, txtPriority,txtName;
        RelativeLayout layoutWait;
        ImageView  imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCourse = itemView.findViewById(R.id.txtCode);
            txtStud = itemView.findViewById(R.id.txtStudentID);
            txtPriority = itemView.findViewById(R.id.txtPriority);
            layoutWait = itemView.findViewById(R.id.layoutWait);
            txtName=itemView.findViewById(R.id.txtName);
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
                int deleted = databaseHelper.DeleteWaiting(id);
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


}
