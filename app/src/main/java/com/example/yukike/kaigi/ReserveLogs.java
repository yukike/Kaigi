package com.example.yukike.kaigi;

//予約履歴に表示するアダプタークラス
public class ReserveLogs {
    private Long id;
    private String roomNo;
    private String yyyymmdd;
    private String startHhmm;
    private String endHhmm;
    private String status;

    // id
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    // 会議室名
    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomNo() {
        return this.roomNo;
    }

    // 日時
    public void setYyyymmdd(String yyyymmdd) {
        this.yyyymmdd = yyyymmdd;
    }

    public String getYyyymmdd() {
        return this.yyyymmdd;
    }

    // 開始時間
    public void setStartHhmm(String startHhmm) {
        this.startHhmm = startHhmm;
    }

    public String getStartHhmm() {
        return this.startHhmm;
    }

    // 終了時間
    public void setEndHhmm(String endHhmm) {
        this.endHhmm = endHhmm;
    }

    public String getEndHhmm() {
        return this.endHhmm;
    }

    // ステータス
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
