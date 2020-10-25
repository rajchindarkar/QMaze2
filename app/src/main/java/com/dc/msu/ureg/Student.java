package com.dc.msu.ureg;

public class Student {
    String id;
    String fname;
    String lname;
    String email;
    String edu;
    String course;

    public Student(String id, String fname, String lname, String email, String edu, String course) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.edu = edu;
        this.course = course;
    }





    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getEdu() {
        return edu;
    }

    public String getCourse() {
        return course;
    }

    public String getId() {
        return id;
    }
}
