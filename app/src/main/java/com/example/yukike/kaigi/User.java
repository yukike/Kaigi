package com.example.yukike.kaigi;

public class User {
    private String userid;
    private boolean nfcflag;

    public void setUserId(String id) {
        userid = id;
    }

    public String getUserId() {
        return userid;
    }

    public void setNfcflag(boolean flag) {
        nfcflag = flag;
    }

    public boolean getNfcflag() {
        return nfcflag;
    }
}
