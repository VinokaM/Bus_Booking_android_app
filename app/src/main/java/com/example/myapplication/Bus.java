package com.example.myapplication;

public class Bus {

    // Declare variables for bus information
    private String busId;
    private String busNumber;
    private String startCity;
    private String endCity;
    private int seatCount;

    private String time;
    private String userId;


    public Bus() {
        // Default constructor required for calls to DataSnapshot.getValue(Bus.class)
    }

    // Constructor to initialize all fields
    public Bus(String busId, String busNumber, String startCity, String endCity, int seatCount, String time, String userId) {
        this.busId = busId;
        this.busNumber = busNumber;
        this.startCity = startCity;
        this.endCity = endCity;
        this.seatCount = seatCount;
        this.time = time;
        this.userId = userId;
    }

    // Getter and setter methods for each field
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

