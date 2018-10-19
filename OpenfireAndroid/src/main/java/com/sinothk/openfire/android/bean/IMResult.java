package com.sinothk.openfire.android.bean;

/**
 * @ author LiangYT
 * @ create 2018/10/19 9:27
 * @ Describe
 */
public class IMResult {
    private int code;
    private String tip;
    private String msg;

    public IMResult() {
    }

    public IMResult(int code, String tip) {
        this.code = code;
        this.tip = tip;
    }

    public IMResult(int code, String tip, String msg) {
        this.code = code;
        this.tip = tip;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
