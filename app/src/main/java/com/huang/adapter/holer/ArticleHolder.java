package com.huang.adapter.holer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huang.bean.YtecConsultMsgBean;
import com.huang.utils.CommonUtils;
import com.sinothk.openfire.android.demo.R;

/**
 * 2:文章
 *
 * @author huang
 */
public class ArticleHolder extends RecyclerView.ViewHolder {
    public LinearLayout chatLinearlayoutRight_article;
    public TextView chatTimeTextviewRight_article;
    public ImageView chatImageviewRight_article;
    public LinearLayout chatLinearlayoutLeft_article;
    public TextView chatTimeTextviewLeft_article;
    public ImageView chatImageviewLeft_article;

    public TextView chatArticleTextviewLeft;
    public TextView chatArticleTextviewRight;
    public TextView title_textview_left;
    public TextView see_textview_left;
    public TextView title_textview_right;
    public TextView see_textview_right;

    public ArticleHolder(View view, Boolean flag) {
        super(view);
        if (flag) {// 左
//			this.chatLinearlayoutLeft_article = (LinearLayout) view.findViewById(R.id.listview_chat_linearlayout_left_article);// 左布局
//			this.chatTimeTextviewLeft_article = (TextView) view.findViewById(R.id.listview_chat_time_textview_left_article);// 左时间
//			this.chatImageviewLeft_article = (ImageView) view.findViewById(R.id.listview_chat_imageview_left_article);// 左头像
//
//			this.chatArticleTextviewLeft = (TextView) view.findViewById(R.id.listview_chat_article_textview_left);
//			this.title_textview_left = (TextView) view.findViewById(R.id.listview_chat_article_title_textview_left);
//			this.see_textview_left = (TextView) view.findViewById(R.id.listview_chat_article_see_textview_left);
        } else {
            this.chatLinearlayoutRight_article = (LinearLayout) view.findViewById(R.id.listview_chat_linearlayout_right_article);// 右布局
            this.chatTimeTextviewRight_article = (TextView) view.findViewById(R.id.listview_chat_time_textview_right_article);// 右时间
            this.chatImageviewRight_article = (ImageView) view.findViewById(R.id.listview_chat_imageview_right_article);// 右头像

            this.chatArticleTextviewRight = (TextView) view.findViewById(R.id.listview_chat_article_textview_right);
            this.title_textview_right = (TextView) view.findViewById(R.id.listview_chat_article_title_textview_right);
            this.see_textview_right = (TextView) view.findViewById(R.id.listview_chat_article_see_textview_right);
        }
    }

    public void doSomething(final YtecConsultMsgBean bean, int doctor_id, ArticleHolder holder, final Context context) {
        if (bean.getSender_id() == doctor_id) {// 发送者id等于本人id,那么显示右边布局
            CommonUtils.setImage(bean.getSender_photo(), holder.chatImageviewRight_article);// 设置头像
            holder.chatTimeTextviewRight_article.setText(bean.getSend_time());
            holder.chatLinearlayoutRight_article.setVisibility(View.VISIBLE);

            // 文章点击进入单个文章页面
            holder.see_textview_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
//                    Intent intent = new Intent(context, NutritionArticleActivity.class);
//                    intent.putExtra("article_id", bean.getArticle_id() + "");
//                    intent.putExtra("article_tag", "");
//                    context.startActivity(intent);
                }
            });

            holder.chatArticleTextviewRight.setText(bean.getContent());
            holder.title_textview_right.setText(bean.getArticle_title());
        } else if (bean.getReceiver_id() == doctor_id) {// 接受者id等于本人id,那么显示左边布局
            CommonUtils.setImage(bean.getSender_photo(), holder.chatImageviewLeft_article);// 设置头像
            holder.chatTimeTextviewLeft_article.setText(bean.getSend_time());
            holder.chatLinearlayoutLeft_article.setVisibility(View.VISIBLE);

            // 文章点击进入单个文章页面
            holder.see_textview_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
//                    Intent intent = new Intent(context, NutritionArticleActivity.class);
//                    intent.putExtra("article_id", bean.getArticle_id() + "");
//                    intent.putExtra("article_tag", "");
//                    context.startActivity(intent);
                }
            });
            holder.chatArticleTextviewLeft.setText(bean.getContent());
            holder.title_textview_left.setText(bean.getArticle_title());
        }
    }
}