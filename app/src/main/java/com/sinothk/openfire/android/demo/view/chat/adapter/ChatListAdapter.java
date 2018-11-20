package com.sinothk.openfire.android.demo.view.chat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sinothk.comm.utils.DateUtil;
import com.sinothk.openfire.android.demo.R;
import com.sinothk.openfire.android.demo.model.bean.LastMessage;
import com.sinothk.widget.tipView.style1.BadgeView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ContactsViewHolder> {
    private Context mContext;

    private List<LastMessage> dataList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public ChatListAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_list_item, null);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, final int position) {
        final LastMessage lastMsg = dataList.get(position);

        holder.nameTv.setText(lastMsg.getName());
        holder.contentTv.setText(lastMsg.getMsgTxt());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, lastMsg);
                }
            }
        });

        holder.timeTv.setText(DateUtil.getFriendlyDate(new Date(lastMsg.getMsgTime())));

        int unreadNum = lastMsg.getMsgUnread();
        if (unreadNum > 0) {
            holder.unreadNumTv.setText(String.valueOf(unreadNum));
            holder.unreadNumTv.setVisibility(View.VISIBLE);
        } else {
            holder.unreadNumTv.setVisibility(View.INVISIBLE);
        }

        if (lastMsg.getAvatar() != null) {
            Glide.with(mContext).load(lastMsg.getAvatar()).into(holder.avatarIv);
//            holder.avatarIv.setImageDrawable(lastMsg.getAvatar());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(ArrayList<LastMessage> data) {
        dataList.clear();
        dataList.addAll(data == null ? new ArrayList<LastMessage>() : data);
        notifyDataSetChanged();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout itemLayout;

        ImageView avatarIv;
        TextView nameTv, contentTv, timeTv;
        BadgeView unreadNumTv;

        ContactsViewHolder(View itemView) {
            super(itemView);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.itemLayout);
            avatarIv = (ImageView) itemView.findViewById(R.id.avatarIv);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            contentTv = (TextView) itemView.findViewById(R.id.contentTv);
            timeTv = (TextView) itemView.findViewById(R.id.timeTv);

            unreadNumTv = (BadgeView) itemView.findViewById(R.id.unreadNumTv);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Object object);
    }
}


