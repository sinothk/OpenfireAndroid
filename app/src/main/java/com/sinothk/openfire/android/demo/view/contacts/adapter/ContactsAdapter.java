package com.sinothk.openfire.android.demo.view.contacts.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinothk.openfire.android.bean.IMUser;
import com.sinothk.openfire.android.demo.R;
import com.sinothk.openfire.android.demo.model.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjz on 9/4/16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private List<IMUser> contacts = new ArrayList<>();
    private int layoutId;
    private OnItemClickListener onItemClickListener;

    public ContactsAdapter(int layoutId) {
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
        final IMUser userBean = contacts.get(position);
        if (position == 0 || !contacts.get(position - 1).getIndex().equals(userBean.getIndex())) {
            holder.tvIndex.setVisibility(View.VISIBLE);
            holder.tvIndex.setText(userBean.getIndex());
        } else {
            holder.tvIndex.setVisibility(View.GONE);
        }

        holder.tvName.setText(userBean.getName());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, userBean);
                }
            }
        });


        if (userBean.getUserAvatar() != null) {
            holder.avatarIv.setImageDrawable(userBean.getUserAvatar());
        }

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(ArrayList<IMUser> friendData) {
        contacts.clear();
        contacts.addAll(friendData == null ? new ArrayList<IMUser>() : friendData);
        notifyDataSetChanged();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout itemLayout;

        TextView tvIndex;
        ImageView avatarIv;
        TextView tvName;

        ContactsViewHolder(View itemView) {
            super(itemView);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.itemLayout);
            tvIndex = (TextView) itemView.findViewById(R.id.tv_index);
            avatarIv = (ImageView) itemView.findViewById(R.id.avatarIv);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Object object);
    }
}