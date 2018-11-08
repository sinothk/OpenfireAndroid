package com.huang.bean;

import java.io.Serializable;

/**
 * 商品对应的图片
 *
 * @author huang
 *
 */
public class Img implements Serializable {

    private String small;
    private String thumb;// ..展示时的图片
    private String url;

    public Img() {
    }

    public Img(String small, String thumb, String url) {
        this.small = small;
        this.thumb = thumb;
        this.url = url;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

