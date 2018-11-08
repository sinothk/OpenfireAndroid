package com.huang.bean;

import java.io.Serializable;

/**
 * 在线咨询消息表
 *
 * @author huang
 *
 */
public class YtecConsultMsgBean implements Serializable {

    private static final long serialVersionUID = 1798462548852603450L;

    private int msg_id;// mediumint(8) auto_increment primary key,
    private int msg_type;// TINYINT(1) DEFAULT 0 COMMENT
    // '消息类型0:文本,1:图片,2:文章,3:开方',4:'用户结束本次对话',5:'语音'
    private boolean is_in_time;// TINYINT(1) DEFAULT 0 COMMENT '是否是及时消息
    // 0:否,1:是',
    private int parentId;// mediumint(8) DEFAULT 0 COMMENT '消息父id',
    private int sender_id;// mediumint(8) not null comment '发送者id',
    private int sender_type;// TINYINT(1) DEFAULT 0 COMMENT '发送者类型0:用户,1:医生',
    private String sender_name;// varchar(60) comment '用户昵称或医生姓名',
    private String sender_nick_name;// 昵称
    private String sender_photo;// varchar(225) comment '发送者头像',
    private int receiver_id;// mediumint(8) not null comment '接受者id',
    private int receiver_type;// TINYINT(1) DEFAULT 0 COMMENT
    // '接受者类型0:用户,1:医生,2:大厅,3:团队',
    private String content;// varchar(500) comment '文本内容',
    private String img_path;// varchar(255) comment '图片消息 图片路径',
    private int article_id;// mediumint(8) COMMENT '文章消息 文章id',
    private String article_title;// 文章标题,文章描述使用content字段
    private boolean readed;// TINYINT(1) default 0 comment '是否已读 0:否 1:是',
    private String read_time;// datetime comment '读取时间',
    private boolean sender_deleted;// TINYINT(1) default 0 comment '用户是否已删除
    // 0:否1:是',
    private boolean receiver_deleted;// TINYINT(1) default 0 comment '医生是否已删除
    // 0:否1:是',
    private String send_time;// datetime not null
    private String doctor_photo;// 医生头像图片
    private String link;// 发文章的带上一个链接

    private String audio;// 语音网络地址
    private String audio_time;// 语音的时长

    private YtecPrescription prescription;

    public YtecConsultMsgBean() {
    }

    // 发送语音
    public YtecConsultMsgBean(int msg_type, int sender_id, int sender_type,
                              String sender_name, String sender_nick_name, String sender_photo,
                              int receiver_id, int receiver_type, String audio, String audio_time,
                              String send_time, boolean is_in_time, String doctor_photo) {
        this.msg_type = msg_type;
        this.sender_id = sender_id;
        this.sender_type = sender_type;
        this.sender_name = sender_name;
        this.sender_nick_name = sender_nick_name;
        this.sender_photo = sender_photo;
        this.receiver_id = receiver_id;
        this.receiver_type = receiver_type;
        this.audio = audio;
        this.audio_time = audio_time;
        this.send_time = send_time;
        this.is_in_time = is_in_time;
        this.doctor_photo = doctor_photo;
    }

    // 发送文本
    public YtecConsultMsgBean(int msg_type, String content, int sender_id,
                              int sender_type, String sender_name, String sender_nick_name,
                              String sender_photo, int receiver_id, int receiver_type,
                              String send_time, boolean is_in_time, String doctor_photo) {
        this.msg_type = msg_type;
        this.content = content;
        this.sender_id = sender_id;
        this.sender_type = sender_type;
        this.sender_name = sender_name;
        this.sender_nick_name = sender_nick_name;
        this.sender_photo = sender_photo;
        this.receiver_id = receiver_id;
        this.receiver_type = receiver_type;
        this.send_time = send_time;
        this.is_in_time = is_in_time;
        this.doctor_photo = doctor_photo;
    }

    // 发送图片
    public YtecConsultMsgBean(int msg_type, int sender_id, int sender_type,
                              String sender_name, String sender_nick_name, String sender_photo,
                              int receiver_id, int receiver_type, String img_path,
                              String send_time, boolean is_in_time, String doctor_photo) {
        this.msg_type = msg_type;
        this.sender_id = sender_id;
        this.sender_type = sender_type;
        this.sender_name = sender_name;
        this.sender_nick_name = sender_nick_name;
        this.sender_photo = sender_photo;
        this.receiver_id = receiver_id;
        this.receiver_type = receiver_type;
        this.img_path = img_path;
        this.send_time = send_time;
        this.is_in_time = is_in_time;
        this.doctor_photo = doctor_photo;
    }

    // 发送文章
    public YtecConsultMsgBean(int msg_type, int article_id, String content,
                              String article_title, int sender_id, int sender_type,
                              String sender_name, String sender_nick_name, String sender_photo,
                              int receiver_id, int receiver_type, String send_time,
                              boolean is_in_time, String doctor_photo, String link) {
        this.msg_type = msg_type;
        this.content = content;
        this.article_title = article_title;
        this.sender_id = sender_id;
        this.sender_type = sender_type;
        this.sender_name = sender_name;
        this.sender_nick_name = sender_nick_name;
        this.sender_photo = sender_photo;
        this.receiver_id = receiver_id;
        this.receiver_type = receiver_type;
        this.send_time = send_time;
        this.is_in_time = is_in_time;
        this.doctor_photo = doctor_photo;
        this.link = link;
    }

    // 发送开方信息
    public YtecConsultMsgBean(int msg_type, int sender_id, int sender_type,
                              String sender_name, String sender_nick_name, String sender_photo,
                              int receiver_id, int receiver_type, YtecPrescription prescription,
                              String send_time, boolean is_in_time, String doctor_photo) {
        this.msg_type = msg_type;
        this.sender_id = sender_id;
        this.sender_type = sender_type;
        this.sender_name = sender_name;
        this.sender_nick_name = sender_nick_name;
        this.sender_photo = sender_photo;
        this.receiver_id = receiver_id;
        this.receiver_type = receiver_type;
        this.prescription = prescription;
        this.send_time = send_time;
        this.is_in_time = is_in_time;
        this.doctor_photo = doctor_photo;
    }

    public YtecConsultMsgBean(int msg_id, int msg_type, boolean is_in_time,
                              int parentId, int sender_id, int sender_type, String sender_name,
                              String sender_nick_name, String sender_photo, int receiver_id,
                              int receiver_type, String content, String img_path, int article_id,
                              String article_title, boolean readed, String read_time,
                              boolean sender_deleted, boolean receiver_deleted, String send_time,
                              YtecPrescription prescription, String doctor_photo, String link) {
        super();
        this.msg_id = msg_id;
        this.msg_type = msg_type;
        this.is_in_time = is_in_time;
        this.parentId = parentId;
        this.sender_id = sender_id;
        this.sender_type = sender_type;
        this.sender_name = sender_name;
        this.sender_nick_name = sender_nick_name;
        this.sender_photo = sender_photo;
        this.receiver_id = receiver_id;
        this.receiver_type = receiver_type;
        this.content = content;
        this.img_path = img_path;
        this.article_id = article_id;
        this.article_title = article_title;
        this.readed = readed;
        this.read_time = read_time;
        this.sender_deleted = sender_deleted;
        this.receiver_deleted = receiver_deleted;
        this.send_time = send_time;
        this.prescription = prescription;
        this.doctor_photo = doctor_photo;
        this.link = link;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudio_time() {
        return audio_time;
    }

    public void setAudio_time(String audio_time) {
        this.audio_time = audio_time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDoctor_photo() {
        return doctor_photo;
    }

    public void setDoctor_photo(String doctor_photo) {
        this.doctor_photo = doctor_photo;
    }

    public String getSender_nick_name() {
        return sender_nick_name;
    }

    public void setSender_nick_name(String sender_nick_name) {
        this.sender_nick_name = sender_nick_name;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public boolean isIs_in_time() {
        return is_in_time;
    }

    public void setIs_in_time(boolean is_in_time) {
        this.is_in_time = is_in_time;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getSender_type() {
        return sender_type;
    }

    public void setSender_type(int sender_type) {
        this.sender_type = sender_type;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_photo() {
        return sender_photo;
    }

    public void setSender_photo(String sender_photo) {
        this.sender_photo = sender_photo;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public int getReceiver_type() {
        return receiver_type;
    }

    public void setReceiver_type(int receiver_type) {
        this.receiver_type = receiver_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public String getRead_time() {
        return read_time;
    }

    public void setRead_time(String read_time) {
        this.read_time = read_time;
    }

    public boolean isSender_deleted() {
        return sender_deleted;
    }

    public void setSender_deleted(boolean sender_deleted) {
        this.sender_deleted = sender_deleted;
    }

    public boolean isReceiver_deleted() {
        return receiver_deleted;
    }

    public void setReceiver_deleted(boolean receiver_deleted) {
        this.receiver_deleted = receiver_deleted;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public YtecPrescription getPrescription() {
        return prescription;
    }

    public void setPrescription(YtecPrescription prescription) {
        this.prescription = prescription;
    }

}
