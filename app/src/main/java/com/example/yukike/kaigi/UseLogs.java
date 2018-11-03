package com.example.yukike.kaigi;

public class UseLogs {
    private Long id;
    private String roomNo;
    private String yyyymmdd;
    private String startHhmm;

    public Long getId() {
        return id;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getYyyymmdd() {
        return yyyymmdd;
    }

    public String getStartHhmm() {
        return startHhmm;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public void setYyyymmdd(String yyyymmdd) {
        this.yyyymmdd = yyyymmdd;
    }

    public void setStartHhmm(String startHhmm) {
        this.startHhmm = startHhmm;
    }
}
