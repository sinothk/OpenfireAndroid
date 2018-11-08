package com.huang.adapter.holer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huang.bean.YtecConsultMsgBean;
import com.huang.bean.YtecPrescriptionGoods;
import com.huang.utils.CommonUtils;
import com.sinothk.openfire.android.demo.R;

import java.util.List;

/**
 * 3:开方
 * @author huang
 *
 */
public class PrescriptionHolder extends RecyclerView.ViewHolder {
    public LinearLayout chatLinearlayoutRight_prescription;
    public TextView chatTimeTextviewRight_prescription;
    public ImageView chatImageviewRight_prescription;
    public LinearLayout chatLinearlayoutLeft_prescription;
    public TextView chatTimeTextviewLeft_prescription;
    public ImageView chatImageviewLeft_prescription;

    public TextView tx_symptom;
    public TextView tx_tag;
    public TextView tx_taboo;
    public TextView tx_fit_use;
    public TextView tx_suggest;
    public TextView tx_meals;
    public TextView tx_look_meals;
    public TextView tx_goods;

    public PrescriptionHolder(View view, Boolean flag) {
        super(view);
        if (flag) {// 左
            this.chatLinearlayoutLeft_prescription = (LinearLayout) view.findViewById(R.id.listview_chat_linearlayout_left_prescription);// 左布局
            this.chatTimeTextviewLeft_prescription = (TextView) view.findViewById(R.id.listview_chat_time_textview_left_prescription);// 左时间
            this.chatImageviewLeft_prescription = (ImageView) view.findViewById(R.id.listview_chat_imageview_left_prescription);// 左头像
        }else {
            this.chatLinearlayoutRight_prescription = (LinearLayout) view.findViewById(R.id.listview_chat_linearlayout_right_prescription);// 右布局
            this.chatTimeTextviewRight_prescription = (TextView) view.findViewById(R.id.listview_chat_time_textview_right_prescription);// 右时间
            this.chatImageviewRight_prescription = (ImageView) view.findViewById(R.id.listview_chat_imageview_right_prescription);// 右头像

            this.tx_symptom = (TextView) view.findViewById(R.id.tx_symptom);
            this.tx_tag = (TextView) view.findViewById(R.id.tx_tag);
            this.tx_taboo = (TextView) view.findViewById(R.id.tx_taboo);
            this.tx_fit_use = (TextView) view.findViewById(R.id.tx_fit_use);
            this.tx_suggest = (TextView) view.findViewById(R.id.tx_suggest);
            this.tx_meals = (TextView) view.findViewById(R.id.tx_meals);
            this.tx_look_meals = (TextView) view.findViewById(R.id.tx_look_meals);
            this.tx_goods = (TextView) view.findViewById(R.id.tx_goods);
        }
    }

    public void doSomething(final YtecConsultMsgBean bean, int doctor_id, PrescriptionHolder holder) {
        if (bean.getSender_id() == doctor_id) {// 发送者id等于本人id,那么显示右边布局
            CommonUtils.setImage(bean.getSender_photo(), holder.chatImageviewRight_prescription);// 设置头像
            holder.chatTimeTextviewRight_prescription.setText(bean.getSend_time());
            holder.chatLinearlayoutRight_prescription.setVisibility(View.VISIBLE);

            if (bean.getPrescription() != null) {
                holder.tx_symptom.setText(bean.getPrescription().getAnalysis());
                holder.tx_tag.setText(bean.getPrescription().getKeywords());
                holder.tx_taboo.setText(bean.getPrescription().getAvoid());
                holder.tx_fit_use.setText(bean.getPrescription().getSuit());
                holder.tx_suggest.setText(bean.getPrescription().getSuggestion());
                holder.tx_meals.setText(bean.getPrescription().getContent());

                String goods_str = "";// 膳食商品
                List<YtecPrescriptionGoods> prescription_goods = bean.getPrescription().getPrescription_goods();
                if (prescription_goods != null) {
                    for (int i = 0; i < prescription_goods.size(); i++) {
                        goods_str = goods_str + prescription_goods.get(i).getName() + "\n";
                    }
                    holder.tx_goods.setText(goods_str);
                }

                holder.tx_look_meals.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("ChatListAdapter.bean="+bean);
                    }
                });
            }
        } else if (bean.getReceiver_id() == doctor_id) {// 接受者id等于本人id,那么显示左边布局
            CommonUtils.setImage(bean.getSender_photo(), holder.chatImageviewLeft_prescription);// 设置头像
            holder.chatTimeTextviewLeft_prescription.setText(bean.getSend_time());
            holder.chatLinearlayoutLeft_prescription.setVisibility(View.VISIBLE);
        }
    }
}
