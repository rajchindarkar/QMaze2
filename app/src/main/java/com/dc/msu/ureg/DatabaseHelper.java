package com.dc.msu.ureg;;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="waiting.db";
    public static final String TABLE_NAME="course_table";
    public static final String TABLE_NAME2="student_table";
    public static final String TABLE_NAME3="waiting_table";
    public static final String col1="COURSE_CODE";
    public static final String col2="TITLE";
    public static final String col3="CREDITS";
    public static final String col4="SEATS";
    public static final String cl1="STUDENTD_ID";
    public static final String cl2="FNAME";
    public static final String cl3="LNAME";
    public static final String cl4="EMAIL";
    public static final String cl5="EDU_LEVEL";
    public static final String cl6="COURSE_CODE";
    public static final String c7="PRIORITY";
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" (COURSE_CODE text primary key ,TITLE text,CREDITS INTEGER,SEATS INTEGER)");
        db.execSQL("create table "+TABLE_NAME2+" (STUDENTD_ID INTEGER primary key AUTOINCREMENT,FNAME TEXT,LNAME text,EMAIL TEXT,EDU_LEVEL TEXT,COURSE_CODE TEXT)");
        db.execSQL("create table "+TABLE_NAME3+" (COURSE_CODE text ,STUDENTD_ID INTEGER,PRIORITY TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME3);

    }
    public boolean insert(String code,String title, int credit, int seats){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col1,code);
        contentValues.put(col2,title);
        contentValues.put(col3,credit);
        contentValues.put(col4,seats);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
            return  false;
        else
            return true;
    }
    public boolean insertWaiting(String coursecode,int sid, String p){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col1,coursecode);
        contentValues.put(cl1,sid);
        contentValues.put(c7,p);

        long result=db.insert(TABLE_NAME3,null,contentValues);
        if(result==-1)
            return  false;
        else
            return true;
    }
    public boolean insertStudent(String f,String l, String e, String ed,String cc){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(cl2,f);
        contentValues.put(cl3,l);
        contentValues.put(cl4,e);
        contentValues.put(cl5,ed);
        contentValues.put(cl6,cc);
        try {
            long result=db.insert(TABLE_NAME2,null,contentValues);
            if(result==-1)
                return  false;
            else
                return true;
        }catch (Exception wx){
            Log.d("FailInsert", String.valueOf(wx));
            return false;
        }

    }
    public boolean updateData(String f,String l, String e, String ed,String cc, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cl2,f);
        contentValues.put(cl3,l);
        contentValues.put(cl4,e);
        contentValues.put(cl5,ed);
        contentValues.put(cl6,cc);
        db.update(TABLE_NAME2, contentValues, "STUDENTD_ID=?", new String[]{id});

        return true;
    }
    public boolean updatePriority(String p,String sid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(c7,p);

        db.update(TABLE_NAME3, contentValues, "STUDENTD_ID=?", new String[]{sid});

        return true;
    }
    public int DeleteStudent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME2, "STUDENTD_ID = ?", new String[]{id});
    }
    public int DeleteWaiting(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME3, "STUDENTD_ID = ?", new String[]{id});
    }
    public Cursor getCourse(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from course_table",null);
        return res;
    }
    public Cursor getWaiting(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from waiting_table",null);
        return res;
    }
    public boolean getWaitingStudent(String sid){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from waiting_table WHERE STUDENTD_ID=" + sid , null);
        if (res.getCount()>0)
            return true;
        else
            return false;
    }
    public Cursor getWaitingStudents(String sid){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from waiting_table WHERE STUDENTD_ID=" + sid , null);
       return res;
    }
    public Cursor getStudentsByCourse(String course){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from student_table WHERE COURSE_CODE='" + course + "'", null);
        return res;
    }
    public Cursor getStudentsid(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from student_table WHERE STUDENTD_ID='" + id + "'", null);
        return res;
    }
    public String getName(String sid){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from student_table WHERE STUDENTD_ID=" + sid , null);
        Log.d("countSt", String.valueOf(res.getCount()));
        String name = null;
        while (res.moveToNext()){
            name =res.getString(1)+" "+res.getString(2);
        
        }
        return name;
    }
}
