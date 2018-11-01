package com.sinothk.openfire.android.demo.view.contacts.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinothk.openfire.android.demo.R;
import com.sinothk.openfire.android.demo.model.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjz on 9/4/16.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ContactsViewHolder> {

    private List<UserBean> contacts = new ArrayList<>();
    private int layoutId;
    private OnItemClickListener onItemClickListener;

    public GroupListAdapter(int layoutId) {
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
        final UserBean contact = contacts.get(position);
//        if (position == 0 || !contacts.get(position - 1).getIndex().equals(contact.getIndex())) {
//            holder.tvIndex.setVisibility(View.VISIBLE);
//            holder.tvIndex.setText(contact.getIndex());
//        } else {
            holder.tvIndex.setVisibility(View.GONE);
//        }

        holder.tvName.setText(contact.getName());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, contact);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(ArrayList<UserBean> friendData) {
        contacts.clear();
        contacts.addAll(friendData == null ? new ArrayList<UserBean>() : friendData);
        notifyDataSetChanged();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout itemLayout;

        TextView tvIndex;
        ImageView ivAvatar;
        TextView tvName;

        ContactsViewHolder(View itemView) {
            super(itemView);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.itemLayout);
            tvIndex = (TextView) itemView.findViewById(R.id.tv_index);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Object object);
    }
}