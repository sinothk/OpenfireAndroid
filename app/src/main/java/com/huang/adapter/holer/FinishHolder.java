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
 * 4:用户结束本次对话
 *
 * @author huang
 */
public class FinishHolder extends RecyclerView.ViewHolder {
    public LinearLayout chatLinearlayoutRight_finish;
    public TextView chatTimeTextviewRight_finish;
    public ImageView chatImageviewRight_finish;
    public LinearLayout chatLinearlayoutLeft_finish;
    public TextView chatTimeTextviewLeft_finish;
    public ImageView chatImageviewLeft_finish;

    public TextView chatContentTextviewLeft_finish;
    public TextView chatContentTextviewRight_finish;

    public FinishHolder(View view, Boolean flag) {
        super(view);
        if (flag) {// 左
            this.chatLinearlayoutLeft_finish = (LinearLayout) view.findViewById(R.id.listview_chat_linearlayout_left_finish);// 左布局
            this.chatTimeTextviewLeft_finish = (TextView) view.findViewById(R.id.listview_chat_time_textview_left_finish);// 左时间
            this.chatImageviewLeft_finish = (ImageView) view.findViewById(R.id.listview_chat_imageview_left_finish);// 左头像

            this.chatContentTextviewLeft_finish = (TextView) view.findViewById(R.id.listview_chat_finish_content_textview_left);// 左内容
        } else {
            this.chatLinearlayoutRight_finish = (LinearLayout) view.findViewById(R.id.listview_chat_linearlayout_right_finish);// 右布局
            this.chatTimeTextviewRight_finish = (TextView) view.findViewById(R.id.listview_chat_time_textview_right_finish);// 右时间
            this.chatImageviewRight_finish = (ImageView) view.findViewById(R.id.listview_chat_imageview_right_finish);// 右头像

            this.chatContentTextviewRight_finish = (TextView) view.findViewById(R.id.listview_chat_finish_content_textview_right);// 右内容
        }
    }

    public void doSomething(final IMMessage bean, String doctor_id, FinishHolder holder) {
        if (bean.getFrom().equals(doctor_id)) {// 发送者id等于本人id,那么显示右边布局
//            CommonUtils.setImage(bean.getSender_photo(), holder.chatImageviewRight_finish);// 设置头像
            holder.chatTimeTextviewRight_finish.setText(DateUtil.getFriendlyDate(new Date(bean.getMsgTime())));
            holder.chatLinearlayoutRight_finish.setVisibility(View.VISIBLE);

            holder.chatContentTextviewRight_finish.setText("用户已退出咨询，对话结束。");
        } else if (bean.getTo().equals(doctor_id)) {// 接受者id等于本人id,那么显示左边布局
//            CommonUtils.setImage(bean.getSender_photo(), holder.chatImageviewLeft_finish);// 设置头像
            holder.chatTimeTextviewLeft_finish.setText(DateUtil.getFriendlyDate(new Date(bean.getMsgTime())));
            holder.chatLinearlayoutLeft_finish.setVisibility(View.VISIBLE);

            holder.chatContentTextviewLeft_finish.setText("用户已退出咨询，对话结束。");
        }
    }
}
