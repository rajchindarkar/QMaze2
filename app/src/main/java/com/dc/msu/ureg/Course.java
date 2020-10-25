package com.dc.msu.ureg;

public class Course {
    String code,title,seats,credits;

    public Course(String code, String title, String seats, String credits) {
        this.code = code;
        this.title = title;
        this.seats = seats;
        this.credits = credits;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getSeats() {
        return seats;
    }

    public String getCredits() {
        return credits;
    }
}
