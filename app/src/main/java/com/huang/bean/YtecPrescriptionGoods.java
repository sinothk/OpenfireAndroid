package com.huang.bean;

import java.io.Serializable;

/**
 * 膳食配方商品关联表
 *
 * @author huang
 *
 */
public class YtecPrescriptionGoods implements Serializable {

    private static final long serialVersionUID = 7450980149218816726L;

    private int msg_id;// mediumint(8) comment '消息ID',
    private int prescription_id;// mediumint(8) comment '配方ID',
    private int goods_id;// mediumint(8) comment '商品ID',
    private int goods_num;// int COMMENT '商品数量'
    private String shop_price;// ..展示时的价格

    private String name;
    private Img img;

    public YtecPrescriptionGoods() {
    }

    public YtecPrescriptionGoods(int msg_id, int prescription_id, int goods_id,
                                 int goods_num, String name, Img img, String shop_price) {
        this.msg_id = msg_id;
        this.prescription_id = prescription_id;
        this.goods_id = goods_id;
        this.goods_num = goods_num;
        this.name = name;
        this.img = img;
        this.shop_price = shop_price;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Img getImg() {
        return img;
    }

    public void setImg(Img img) {
        this.img = img;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getPrescription_id() {
        return prescription_id;
    }

    public void setPrescription_id(int prescription_id) {
        this.prescription_id = prescription_id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

}
