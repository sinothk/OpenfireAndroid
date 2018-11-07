package com.sinothk.openfire.android.demo.view.contacts.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinothk.openfire.android.bean.IMChatRoom;
import com.sinothk.openfire.android.bean.IMUser;
import com.sinothk.openfire.android.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjz on 9/4/16.
 */
public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ContactsViewHolder> {

    private List<IMChatRoom> data = new ArrayList<>();
    private int layoutId;
    private OnItemClickListener onItemClickListener;

    public RoomListAdapter(int layoutId) {
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, final int position) {
        final IMChatRoom itemEntity = data.get(position);
//        if (position == 0 || !contacts.get(position - 1).getIndex().equals(contact.getIndex())) {
//            holder.tvIndex.setVisibility(View.VISIBLE);
//            holder.tvIndex.setText(contact.getIndex());
//        } else {
        holder.tvIndex.setVisibility(View.GONE);
//        }

        holder.tvName.setText(itemEntity.getRoomName());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, itemEntity);
                }
            }
        });

        // 是否加密
        if (itemEntity.isPasswordProtected()) {
            holder.clockIv.setVisibility(View.VISIBLE);
        } else {
            holder.clockIv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(ArrayList<IMChatRoom> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout itemLayout;

        TextView tvIndex;
        ImageView ivAvatar, clockIv;
        TextView tvName;


        ContactsViewHolder(View itemView) {
            super(itemView);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.itemLayout);
            tvIndex = (TextView) itemView.findViewById(R.id.tv_index);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            clockIv = (ImageView) itemView.findViewById(R.id.clockIv);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Object object);
    }
}