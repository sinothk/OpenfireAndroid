package com.sinothk.openfire.android.demo.xmpp.database;


import android.content.Context;

import com.sinothk.openfire.android.demo.xmpp.database.model.greendao.DaoMaster;
import com.sinothk.openfire.android.demo.xmpp.database.model.greendao.DaoSession;

/**
 * 项目名：   GreenDaoDB-maseter
 * 包名：     com.text.xuhong.greendaodb_maseter
 * 文件名：   DBManager
 * 创建者：   xuhong
 * 创建时间： 2017/8/29 16:52
 * 描述：   TODO
 */

public class DBManager {

    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;

    private DBManager() {
    }

    public static DBManager init (Context mContext) {

        if (mInstance == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(mContext, "user_db.db", null);//此处为自己需要处理的表
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }

        if (mInstance == null) {
            //保证异步处理安全操作
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager();
                }
            }
        }
        return mInstance;
    }


    public static DBManager getmInstance() {
        return mInstance;
    }
    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

}
