package com.sinothk.openfire.android.demo.view.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sinothk.openfire.android.bean.IMConstant;
import com.sinothk.openfire.android.bean.IMMessage;
import com.sinothk.openfire.android.demo.R;
import com.sinothk.openfire.android.demo.view.base.adapter.ChatBaseAdapter;

import java.util.ArrayList;

public class ChatAdapter extends ChatBaseAdapter<IMMessage> {

    private LayoutInflater mInflater;

    public ChatAdapter(Context context) {
        super(context);
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(ListView contentListView, ArrayList<IMMessage> chatList) {
        list.clear();
        list.addAll(chatList);

        notifyDataSetChanged();

        contentListView.setSelection(list.size());
    }

    @Override
    public int getItemViewType(int position) {
        IMMessage msg = list.get(position);

        //是文字类型或者自定义类型（用来显示群成员变化消息）
        switch (msg.getContentType()) {
            case IMConstant.ContentType.CONTENT_TEXT:
                return msg.getFromType().equals(IMConstant.FromType.SEND) ? TYPE_SEND_TXT : TYPE_RECEIVE_TXT;
//            case image:
//                return msg.getDirect() == MessageDirect.send ? TYPE_SEND_IMAGE
//                        : TYPE_RECEIVER_IMAGE;
//            case file:
//                String extra = msg.getContent().getStringExtra("video");
//                if (!TextUtils.isEmpty(extra)) {
//                    return msg.getDirect() == MessageDirect.send ? TYPE_SEND_VIDEO
//                            : TYPE_RECEIVE_VIDEO;
//                } else {
//                    return msg.getDirect() == MessageDirect.send ? TYPE_SEND_FILE
//                            : TYPE_RECEIVE_FILE;
//                }
//            case voice:
//                return msg.getDirect() == MessageDirect.send ? TYPE_SEND_VOICE
//                        : TYPE_RECEIVER_VOICE;
//            case location:
//                return msg.getDirect() == MessageDirect.send ? TYPE_SEND_LOCATION
//                        : TYPE_RECEIVER_LOCATION;
//            case eventNotification:
//            case prompt:
//                return TYPE_GROUP_CHANGE;
            default:
                return TYPE_CUSTOM_TXT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final IMMessage msg = list.get(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = createViewByType(msg, position);

            holder.msgTime = (TextView) convertView.findViewById(R.id.jmui_send_time_txt);

            holder.headIcon = (ImageView) convertView.findViewById(R.id.jmui_avatar_iv);

            holder.displayName = (TextView) convertView.findViewById(R.id.jmui_display_name_tv);
//            holder.emoTextContent = (EmoticonsTextView)convertView.findViewById(R.id.jmui_msg_content_new);
            holder.txtContent = (TextView) convertView.findViewById(R.id.jmui_msg_content);
            holder.sendingIv = (ImageView) convertView.findViewById(R.id.jmui_sending_iv);
            holder.resend = (ImageButton) convertView.findViewById(R.id.jmui_fail_resend_ib);
//            holder.ivDocument = (ImageView) convertView.findViewById(R.id.iv_document);
            holder.text_receipt = (TextView) convertView.findViewById(R.id.text_receipt);

            switch (msg.getContentType()) {
                case IMConstant.ContentType.CONTENT_TEXT:
                    holder.ll_businessCard = (LinearLayout) convertView.findViewById(R.id.ll_businessCard);
                    holder.business_head = (ImageView) convertView.findViewById(R.id.business_head);
                    holder.tv_nickUser = (TextView) convertView.findViewById(R.id.tv_nickUser);
                    holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
                    break;

//                case image:
//                    holder.picture = (ImageView) convertView.findViewById(R.id.jmui_picture_iv);
//                    holder.progressTv = (TextView) convertView.findViewById(R.id.jmui_progress_tv);
//                    break;
//                case file:
//                    String extra = msg.getContent().getStringExtra("video");
//                    if (!TextUtils.isEmpty(extra)) {
//                        holder.picture = (ImageView) convertView.findViewById(R.id.jmui_picture_iv);
//                        holder.progressTv = (TextView) convertView.findViewById(R.id.jmui_progress_tv);
//                        holder.videoPlay = (LinearLayout) convertView.findViewById(R.id.message_item_video_play);
//                    } else {
//                        holder.progressTv = (TextView) convertView.findViewById(R.id.jmui_progress_tv);
//                        holder.contentLl = (LinearLayout) convertView.findViewById(R.id.jmui_send_file_ll);
//                        holder.sizeTv = (TextView) convertView.findViewById(R.id.jmui_send_file_size);
//                        holder.alreadySend = (TextView) convertView.findViewById(R.id.file_already_send);
//                    }
//                    if (msg.getDirect().equals(MessageDirect.receive)) {
//                        holder.fileLoad = (TextView) convertView.findViewById(R.id.jmui_send_file_load);
//                    }
//                    break;
//                case voice:
//                    holder.voice = (ImageView) convertView.findViewById(R.id.jmui_voice_iv);
//                    holder.voiceLength = (TextView) convertView.findViewById(R.id.jmui_voice_length_tv);
//                    holder.readStatus = (ImageView) convertView.findViewById(R.id.jmui_read_status_iv);
//                    break;
//                case location:
//                    holder.location = (TextView) convertView.findViewById(R.id.jmui_loc_desc);
//                    holder.picture = (ImageView) convertView.findViewById(R.id.jmui_picture_iv);
//                    holder.locationView = convertView.findViewById(R.id.location_view);
//                    break;
//                case custom:
//                case prompt:
//                case eventNotification:
//                    holder.groupChange = (TextView) convertView.findViewById(R.id.jmui_group_content);
//                break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtContent.setText(msg.getMsgTxt());
        holder.txtContent.setVisibility(View.VISIBLE);

        return convertView;
    }

    private View createViewByType(IMMessage msg, int position) {
        // 会话类型
        switch (msg.getContentType()) {
            case IMConstant.ContentType.CONTENT_TEXT:
                return getItemViewType(position) == TYPE_SEND_TXT ?
                        mInflater.inflate(R.layout.chat_item_text_send, null) :
                        mInflater.inflate(R.layout.chat_item_text_receive, null);

//            case image:
//                return getItemViewType(position) == TYPE_SEND_IMAGE ?
//                        mInflater.inflate(R.layout.jmui_chat_item_send_image, null) :
//                        mInflater.inflate(R.layout.jmui_chat_item_receive_image, null);
//            case file:
//                String extra = msg.getContent().getStringExtra("video");
//                if (!TextUtils.isEmpty(extra)) {
//                    return getItemViewType(position) == TYPE_SEND_VIDEO ?
//                            mInflater.inflate(R.layout.jmui_chat_item_send_video, null) :
//                            mInflater.inflate(R.layout.jmui_chat_item_receive_video, null);
//                } else {
//                    return getItemViewType(position) == TYPE_SEND_FILE ?
//                            mInflater.inflate(R.layout.jmui_chat_item_send_file, null) :
//                            mInflater.inflate(R.layout.jmui_chat_item_receive_file, null);
//                }
//            case voice:
//                return getItemViewType(position) == TYPE_SEND_VOICE ?
//                        mInflater.inflate(R.layout.jmui_chat_item_send_voice, null) :
//                        mInflater.inflate(R.layout.jmui_chat_item_receive_voice, null);
//            case location:
//                return getItemViewType(position) == TYPE_SEND_LOCATION ?
//                        mInflater.inflate(R.layout.jmui_chat_item_send_location, null) :
//                        mInflater.inflate(R.layout.jmui_chat_item_receive_location, null);
//            case eventNotification:
//            case prompt:
//                if (getItemViewType(position) == TYPE_GROUP_CHANGE) {
//                    return mInflater.inflate(R.layout.jmui_chat_item_group_change, null);
//                }
            default:
                return mInflater.inflate(R.layout.chat_item_group_change, null);
        }
    }

    public static class ViewHolder {
        public TextView msgTime;
        public ImageView headIcon;
        //        public ImageView ivDocument;
        public TextView displayName;
        public TextView txtContent;
        //        public EmoticonsTextView emoTextContent;
        public ImageView picture;
        public TextView progressTv;
        public ImageButton resend;
        public TextView voiceLength;
        public ImageView voice;
        public ImageView readStatus;
        public TextView location;
        public TextView groupChange;
        public ImageView sendingIv;
        public LinearLayout contentLl;
        public TextView sizeTv;
        public LinearLayout videoPlay;
        public TextView alreadySend;
        public View locationView;
        public LinearLayout ll_businessCard;
        public ImageView business_head;
        public TextView tv_nickUser;
        public TextView tv_userName;
        public TextView text_receipt;
        public TextView fileLoad;
    }
}
