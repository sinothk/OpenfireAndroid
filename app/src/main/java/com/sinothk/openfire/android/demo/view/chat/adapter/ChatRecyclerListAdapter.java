package com.sinothk.openfire.android.demo.view.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huang.adapter.holer.TextHolder;
import com.sinothk.openfire.android.bean.IMConstant;
import com.sinothk.openfire.android.bean.IMMessage;
import com.sinothk.openfire.android.demo.R;

import java.util.ArrayList;

//import com.huang.adapter.holer.AudioHolder;

/**
 * 聊天列表的适配器
 *
 * @author huang
 */
public class ChatRecyclerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 数据源
    private ArrayList<IMMessage> list;
    private String doctor_id;
    private Context context;

    public ChatRecyclerListAdapter(Context context, String doctor_id) {
        super();
        this.doctor_id = doctor_id;
        this.context = context;

        this.list = new ArrayList<>();
    }

    public void addData(ArrayList<IMMessage> newList) {
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateData(IMMessage imMessage) {
        list.add(imMessage);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {

        IMMessage imMessage = list.get(position);

        if (imMessage.getFromType().equals(IMConstant.FromType.SEND)) {// 发送者id等于本人id,那么显示右边布局
            switch (imMessage.getContentType()) {
                case IMConstant.ContentType.TEXT:
                    return 10;
//                case 1:
////                    return 11;
////                case 2:
////                    return 12;
////                case 3:
////                    return 13;
////                case 4:
////                    return 14;
////                case 5:
////                    return 15;
            }
        } else if (imMessage.getFromType().equals(IMConstant.FromType.RECEIVE)) {// 接受者id等于本人id,那么显示左边布局
            switch (imMessage.getContentType()) {
                case IMConstant.ContentType.TEXT:
                    return 20;
//                case 1:
//                    return 21;
//                case 2:
//                    return 22;
//                case 3:
//                    return 23;
//                case 4:
//                    return 24;
//                case 5:
//                    return 25;
            }
        }
        return 0;

//        if (list.get(position).getFrom().equals(doctor_id)) {// 发送者id等于本人id,那么显示右边布局
//            switch (list.get(position).getContentType()) {
//                case IMConstant.ContentType.TEXT:
//                    return 10;
////                case 1:
//////                    return 11;
//////                case 2:
//////                    return 12;
//////                case 3:
//////                    return 13;
//////                case 4:
//////                    return 14;
//////                case 5:
//////                    return 15;
//            }
//        } else if (list.get(position).getTo().equals(doctor_id)) {// 接受者id等于本人id,那么显示左边布局
//            switch (list.get(position).getContentType()) {
//                case IMConstant.ContentType.TEXT:
//                    return 20;
////                case 1:
////                    return 21;
////                case 2:
////                    return 22;
////                case 3:
////                    return 23;
////                case 4:
////                    return 24;
////                case 5:
////                    return 25;
//            }
//        }
//        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder Holder = null;
        switch (viewType) {
            case 10:// 0:右文本
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chat_right, null);
                Holder = new TextHolder(view, false);
                break;
            case 20:// 0:左文本
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chat, null);
                Holder = new TextHolder(view, true);
                break;
//            case 11:// 1:右图片
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chat_imge_right, null);
//                Holder = new ImageHolder(view, false);
//                break;
//            case 21:// 1:图片
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chat_imge, null);
//                Holder = new ImageHolder(view, true);
//                break;
//            case 12:// 2:文章
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chat_article_right, null);
//                Holder = new ArticleHolder(view, false);
//                break;
//            case 22:// 2:文章
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chat_article, null);
//                Holder = new ArticleHolder(view, true);
//                break;
//            case 13:// 3:开方
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chat_prescription_right, null);
//                Holder = new PrescriptionHolder(view, false);
//                break;
//            case 23:// 3:开方
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chat_prescription, null);
//                Holder = new PrescriptionHolder(view, true);
//                break;
//            case 14:// 4:用户结束本次对话
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chat_finish_right, null);
//                Holder = new FinishHolder(view, false);
//                break;
//            case 24:// 4:用户结束本次对话
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chat_finish, null);
//                Holder = new FinishHolder(view, true);
//                break;
//            case 15:// 5:语音
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chat_audio_right, null);
//                Holder = new AudioHolder(view, false);
//                break;
//            case 25:// 5:语音
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chat_audio, null);
//                Holder = new AudioHolder(view, true);
//                break;
        }
        return Holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IMMessage imMessage = list.get(position);
        int itemType = getItemViewType(position);

        switch (itemType) {
            case 10:// 0:文本
                TextHolder textHolder = (TextHolder) holder;
                textHolder.doSomething(imMessage, doctor_id, textHolder);
                break;
            case 20:// 0:文本
                TextHolder textHolder_2 = (TextHolder) holder;
                textHolder_2.doSomething(imMessage, doctor_id, textHolder_2);
                break;
//            case 11:// 1:图片
//                ImageHolder imageHolder = (ImageHolder) holder;
//                imageHolder.doSomething(list.get(position), doctor_id, imageHolder, context);
//                break;
//            case 21:// 1:图片
//                ImageHolder imageHolder_2 = (ImageHolder) holder;
//                imageHolder_2.doSomething(list.get(position), doctor_id, imageHolder_2, context);
//                break;
//            case 12:// 2:文章
//                ArticleHolder articleHolder = (ArticleHolder) holder;
//                articleHolder.doSomething(list.get(position), doctor_id, articleHolder, context);
//                break;
//            case 22:// 2:文章
//                ArticleHolder articleHolder_2 = (ArticleHolder) holder;
//                articleHolder_2.doSomething(list.get(position), doctor_id, articleHolder_2, context);
//                break;
//            case 13:// 3:开方
//                PrescriptionHolder prescriptionHolder = (PrescriptionHolder) holder;
//                prescriptionHolder.doSomething(list.get(position), doctor_id, prescriptionHolder);
//                break;
//            case 23:// 3:开方
//                PrescriptionHolder prescriptionHolder_2 = (PrescriptionHolder) holder;
//                prescriptionHolder_2.doSomething(list.get(position), doctor_id, prescriptionHolder_2);
//                break;
//            case 14:// 4:用户结束本次对话
//                FinishHolder finishHolder = (FinishHolder) holder;
//                finishHolder.doSomething(list.get(position), doctor_id, finishHolder);
//                break;
//            case 24:// 4:用户结束本次对话
//                FinishHolder finishHolder_2 = (FinishHolder) holder;
//                finishHolder_2.doSomething(list.get(position), doctor_id, finishHolder_2);
//                break;
//            case 15:// 5:语音
//                AudioHolder audioHolder = (AudioHolder) holder;
//                audioHolder.doSomething(list.get(position), doctor_id, audioHolder);
//                break;
//            case 25:// 5:语音
//                AudioHolder audioHolder_2 = (AudioHolder) holder;
//                audioHolder_2.doSomething(list.get(position), doctor_id, audioHolder_2);
//                break;
        }
    }


}