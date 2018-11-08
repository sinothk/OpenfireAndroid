package com.huang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 膳食配方表
 *
 * @author huang
 *
 */
public class YtecPrescription implements Serializable{

    private static final long serialVersionUID = -209373792532990053L;


    private int prescription_id;// mediumint(8) auto_increment primary key,
    private int msg_id;// mediumint(8) comment '消息ID',
    private String analysis;// varchar(500) comment '用户症状分析',
    private String keywords;// varchar(255) comment '标签',
    private String avoid;// varchar(500) comment '禁忌',
    private String suit;// varchar(500) comment '适用',
    private String suggestion;// varchar(500) comment '建议',
    private String content;// varchar(500) comment '配方内容',
    private String add_time;// datetime not null

    private List<YtecPrescriptionGoods> prescription_goods;

    public YtecPrescription() {
    }

    public YtecPrescription(int prescription_id, int msg_id, String analysis,
                            String keywords, String avoid, String suit, String suggestion,
                            String content, String add_time) {
        this.prescription_id = prescription_id;
        this.msg_id = msg_id;
        this.analysis = analysis;
        this.keywords = keywords;
        this.avoid = avoid;
        this.suit = suit;
        this.suggestion = suggestion;
        this.content = content;
        this.add_time = add_time;
    }

    public int getPrescription_id() {
        return prescription_id;
    }

    public void setPrescription_id(int prescription_id) {
        this.prescription_id = prescription_id;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAvoid() {
        return avoid;
    }

    public void setAvoid(String avoid) {
        this.avoid = avoid;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public List<YtecPrescriptionGoods> getPrescription_goods() {
        return prescription_goods;
    }

    public void setPrescription_goods(List<YtecPrescriptionGoods> prescription_goods) {
        this.prescription_goods = prescription_goods;
    }



}

