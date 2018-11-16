package com.huang.adapter.holer;

import android.content.Context;
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
 * 图片
 *
 * @author huang
 */
public class ImageHolder extends RecyclerView.ViewHolder {
    public LinearLayout chatLinearlayoutRight_imge;
    public TextView chatTimeTextviewRight_imge;
    public ImageView chatImageviewRight_imge;
    public LinearLayout chatLinearlayoutLeft_imge;
    public TextView chatTimeTextviewLeft_imge;
    public ImageView chatImageviewLeft_imge;

    public ImageView chatContentImageViewLeft;
    public ImageView chatContentImageViewRight;

    public ImageHolder(View view, Boolean flag) {
        super(view);
        if (flag) {// 左
            this.chatLinearlayoutLeft_imge = (LinearLayout) view.findViewById(R.id.listview_chat_linearlayout_left_imge);// 左布局
            this.chatTimeTextviewLeft_imge = (TextView) view.findViewById(R.id.listview_chat_time_textview_left_imge);// 左时间
            this.chatImageviewLeft_imge = (ImageView) view.findViewById(R.id.listview_chat_imageview_left_imge);// 左头像

            this.chatContentImageViewLeft = (ImageView) view.findViewById(R.id.listview_chat_content_imageView_left);
        } else {
            this.chatLinearlayoutRight_imge = (LinearLayout) view.findViewById(R.id.listview_chat_linearlayout_right_imge);// 右布局
            this.chatTimeTextviewRight_imge = (TextView) view.findViewById(R.id.listview_chat_time_textview_right_imge);// 右时间
            this.chatImageviewRight_imge = (ImageView) view.findViewById(R.id.listview_chat_imageview_right_imge);// 右头像

            this.chatContentImageViewRight = (ImageView) view.findViewById(R.id.listview_chat_content_imageView_right);
        }
    }

    public void doSomething(final IMMessage bean, String doctor_id, ImageHolder holder, final Context context) {
        if (bean.getFromJid().equals(doctor_id)) {// 发送者id等于本人id,那么显示右边布局
//            CommonUtils.setImage(bean.getSender_photo(), holder.chatImageviewRight_imge);// 设置头像
            holder.chatTimeTextviewRight_imge.setText(DateUtil.getFriendlyDate(new Date(bean.getMsgTime())));
            holder.chatLinearlayoutRight_imge.setVisibility(View.VISIBLE);

            // 图片点击放大
            holder.chatContentImageViewRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent( context , ChatShowPictureActivity.class);
//                    intent.putExtra("chatPictureUrl", bean.getImg_path());
//                    context.startActivity(intent);
                }
            });

            // 设置图片
//            CommonUtils.setImage(bean.getImg_path(), holder.chatContentImageViewRight);

        } else if (bean.getToJid().equals(doctor_id)) {// 接受者id等于本人id,那么显示左边布局
//            CommonUtils.setImage(bean.getSender_photo(), holder.chatImageviewLeft_imge);// 设置头像
            holder.chatTimeTextviewLeft_imge.setText(DateUtil.getFriendlyDate(new Date(bean.getMsgTime())));
            holder.chatLinearlayoutLeft_imge.setVisibility(View.VISIBLE);

            // 图片点击放大
            holder.chatContentImageViewLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent( context , ChatShowPictureActivity.class);
//                    intent.putExtra("chatPictureUrl", bean.getImg_path());
//                    context.startActivity(intent);
                }
            });

            // 设置图片
//            CommonUtils.setImage(bean.getImg_path(), holder.chatContentImageViewRight);
        }
    }
}
