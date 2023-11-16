package com.mobile.midterm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.midterm.R;
import com.mobile.midterm.model.User;
import com.mobile.midterm.utils.OnRecyclerViewItemClickListener;

import java.util.List;

public class SystemUserAdapter extends RecyclerView.Adapter<SystemUserAdapter.SysetemUserHolder> {
    private List<User> users;
    private Context mContext;
    private int layout;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public SystemUserAdapter(Context context, List<User> users, int layout, OnRecyclerViewItemClickListener on) {
        this.mContext = context;
        this.users = users;
        this.layout = layout;
        this.onRecyclerViewItemClickListener = on;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SysetemUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, parent, false);
        return new SysetemUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SysetemUserHolder holder, int position) {
        User user = users.get(position);
        holder.fullName.setText(user.getFullName());
        holder.role.setText(user.getRole().toUpperCase());
        holder.phoneNum.setText(user.getPhoneNum());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecyclerViewItemClickListener.onItemClickListener(user);
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecyclerViewItemClickListener.onItemClickListener(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }


    class SysetemUserHolder extends RecyclerView.ViewHolder {
        TextView fullName, role, phoneNum;
        ImageButton imageButton;
        FrameLayout layout;

        public SysetemUserHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.userItemFullName);
            role = itemView.findViewById(R.id.userItemRole);
            phoneNum = itemView.findViewById(R.id.userItemPhoneNum);
            imageButton = itemView.findViewById(R.id.userItemNavigateBtn);
            layout = itemView.findViewById(R.id.systemUserItem);
        }
    }
}
