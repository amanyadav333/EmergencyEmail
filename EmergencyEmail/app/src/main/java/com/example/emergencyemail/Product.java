package com.example.emergencyemail;

public class Product {
    private int id;
    private String start;
    private String end;
    private String weekday;

    public Product(int id, String start, String end, String weekday) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.weekday = weekday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }
}
