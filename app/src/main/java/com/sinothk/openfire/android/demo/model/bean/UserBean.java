package com.sinothk.openfire.android.demo.model.bean;

import com.sinothk.openfire.android.bean.IMCode;
import com.sinothk.openfire.android.bean.IMUser;
import com.sinothk.openfire.android.demo.PinYinUtil;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserBean implements Serializable {

    private String index;

    private String jid;
    private String name;
    private String userName;
    private String email;
    private String password;

    private String sex;
    private int age;

    public UserBean() {
    }

    public UserBean(String name, String sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public UserBean(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static void sort(ArrayList<UserBean> users) {

        for (int i = 0; i < users.size(); i++) {
            String nameIndex = PinYinUtil.getChineseFirstSpell(users.get(i).getName());
            users.get(i).setIndex(nameIndex.substring(0, 1));
        }

        Collections.sort(users, new Comparator<UserBean>() {
            @Override
            public int compare(UserBean o1, UserBean o2) {
                return o1.index.compareTo(o2.index);
            }
        });
    }

//    public IMUser getIMUserBean() {
//        IMUser imUser = new IMUser();
//        imUser.setJid(jid);
//        imUser.setUserName(u);
//        imUser.setName();
//        imUser.setEmail();
//
//        return imUser;
//    }
}
