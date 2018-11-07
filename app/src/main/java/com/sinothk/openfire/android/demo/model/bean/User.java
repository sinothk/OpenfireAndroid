package com.sinothk.openfire.android.demo.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 项目名：   GreenDaoDemo
 * 包名：     com.xuhong.greendaodemo.db
 * 文件名：   User
 * 创建者：   xuhong
 * 创建时间： 2017/9/13 16:51
 * 描述：   TODO
 */

@Entity
public class User {

    //id
    @Id
    private Long key ;
    //名字
    private String Name;

    //年龄
    private int age;

    //性别
    private String sex;

    public User(String name, String sex, int age) {
        Name = name;
        this.sex = sex;
        this.age = age;
    }

    @Generated(hash = 1875574585)
    public User(Long key, String Name, int age, String sex) {
        this.key = key;
        this.Name = Name;
        this.age = age;
        this.sex = sex;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getKey() {
        return this.key;
    }

    public void setKey(Long key) {
        this.key = key;
    }
}
