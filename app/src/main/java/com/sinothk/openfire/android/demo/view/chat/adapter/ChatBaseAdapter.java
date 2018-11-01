package com.sinothk.openfire.android.demo.view.chat.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.sinothk.openfire.android.bean.IMMessage;

import java.util.ArrayList;

public abstract class ChatBaseAdapter extends BaseAdapter {

    protected Context mContext = null;
    protected ArrayList<IMMessage> list = new ArrayList<>();

    public ChatBaseAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public IMMessage getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //文本
    protected final int TYPE_SEND_TXT = 0;
    protected final int TYPE_RECEIVE_TXT = 1;

    // 图片
    protected final int TYPE_SEND_IMAGE = 2;
    protected final int TYPE_RECEIVER_IMAGE = 3;

    //文件
    protected final int TYPE_SEND_FILE = 4;
    protected final int TYPE_RECEIVE_FILE = 5;
    // 语音
    protected final int TYPE_SEND_VOICE = 6;
    protected final int TYPE_RECEIVER_VOICE = 7;
    // 位置
    protected final int TYPE_SEND_LOCATION = 8;
    protected final int TYPE_RECEIVER_LOCATION = 9;
    //群成员变动
    protected final int TYPE_GROUP_CHANGE = 10;
    //视频
    protected final int TYPE_SEND_VIDEO = 11;
    protected final int TYPE_RECEIVE_VIDEO = 12;
    //自定义消息
    protected final int TYPE_CUSTOM_TXT = 13;

}
