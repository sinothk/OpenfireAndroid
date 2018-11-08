package com.sinothk.dbHelper;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {

    private volatile static Context context;
    private volatile static DBHelper singleton;
    private volatile static DbUtils dbUtils;

    public static DBHelper with(Context mContext) {
        context = mContext;
        if (singleton == null) {
            synchronized (DBHelper.class) {
                if (singleton == null) {
                    singleton = new DBHelper();
                }
                if (dbUtils == null) {
                    dbUtils = DbUtils.create(context);
                }
            }
        }

        return singleton;
    }

    public DbUtils db() {
        if (dbUtils == null) {
            dbUtils = DbUtils.create(context);
        }
        return dbUtils;
    }

    public boolean saveOrUpdate(Object entity) {
        try {
            db().saveOrUpdate(entity);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<?> findMyLastMsg(Class<?> cls) {
        try {
            return db().findAll(cls);
        } catch (DbException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

