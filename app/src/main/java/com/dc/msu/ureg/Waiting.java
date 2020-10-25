package com.dc.msu.ureg;

public class Waiting {
    String course;
    String student_id;
    String student_name;
    String priority;

    public Waiting(String course, String student_id, String priority) {
        this.course = course;
        this.student_id = student_id;

        this.priority = priority;
    }

    public String getCourse() {
        return course;
    }

    public String getStudent_id() {
        return student_id;
    }



    public String getPriority() {
        return priority;
    }
}
