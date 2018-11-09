package com.huang.adapter.holer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huang.bean.YtecConsultMsgBean;
import com.huang.utils.CommonUtils;
import com.sinothk.comm.utils.DateUtil;
import com.sinothk.openfire.android.bean.IMMessage;
import com.sinothk.openfire.android.demo.R;

import java.util.Date;

/**
 * 文本
 * @author huang
 *
 */
public class TextHolder extends RecyclerView.ViewHolder {
    public LinearLayout chatLinearlayoutRight;
    public TextView chatTimeTextviewRight;
    public ImageView chatImageviewRight;
    public LinearLayout chatLinearlayoutLeft;
    public TextView chatTimeTextviewLeft;
    public ImageView chatImageviewLeft;

    public TextView chatContentTextviewLeft;
    public TextView chatContentTextviewRight;

    public TextHolder(View view, Boolean flag) {
        super(view);
        if (flag) {// 左
            this.chatLinearlayoutLeft = (LinearLayout) view.findViewById(R.id.listview_chat_linearlayout_left);// 左布局
            this.chatTimeTextviewLeft = (TextView) view.findViewById(R.id.listview_chat_time_textview_left);// 左时间
            this.chatImageviewLeft = (ImageView) view.findViewById(R.id.listview_chat_imageview_left);// 左头像

            this.chatContentTextviewLeft = (TextView) view.findViewById(R.id.listview_chat_content_textview_left);// 左内容
        }else {
            this.chatLinearlayoutRight  = (LinearLayout) view.findViewById(R.id.listview_chat_linearlayout_right);// 右布局
            this.chatTimeTextviewRight = (TextView) view.findViewById(R.id.listview_chat_time_textview_right);// 右时间
            this.chatImageviewRight = (ImageView) view.findViewById(R.id.listview_chat_imageview_right);// 右头像

            this.chatContentTextviewRight = (TextView) view.findViewById(R.id.listview_chat_content_textview_right);// 右内容
        }
    }

    public void doSomething(final IMMessage bean, String doctor_id , TextHolder holder){
        if (bean.getFrom().equals(doctor_id)) {// 发送者id等于本人id,那么显示右边布局
//            CommonUtils.setImage(bean.getSender_photo(), holder.chatImageviewRight);// 设置头像
            holder.chatTimeTextviewRight.setText(DateUtil.getFriendlyDate(new Date(bean.getMsgTime())));
            holder.chatLinearlayoutRight.setVisibility(View.VISIBLE);

            holder.chatContentTextviewRight.setText(bean.getMsgTxt());

        }else if (bean.getTo().equals(doctor_id)) {// 接受者id等于本人id,那么显示左边布局
//            CommonUtils.setImage(bean.getSender_photo(), holder.chatImageviewLeft);// 设置头像
            holder.chatTimeTextviewLeft.setText(DateUtil.getFriendlyDate(new Date(bean.getMsgTime())));
            holder.chatLinearlayoutLeft.setVisibility(View.VISIBLE);

            holder.chatContentTextviewLeft.setText(bean.getMsgTxt());
        }
    }

}
