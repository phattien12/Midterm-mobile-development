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
import com.mobile.midterm.model.Certification;
import com.mobile.midterm.utils.OnRecyclerViewItemCertificationClickListener;

import java.util.List;

public class SystemCertificationAdapter extends RecyclerView.Adapter<SystemCertificationAdapter.SystemCertificationHolder> {
    private List<Certification> certifications;
    private Context mContext;
    private int layout;
    private OnRecyclerViewItemCertificationClickListener onRecyclerViewItemCertificationClickListener;

    public SystemCertificationAdapter(Context context, List<Certification> certifications, int layout, OnRecyclerViewItemCertificationClickListener on) {
        this.mContext = context;
        this.certifications = certifications;
        this.layout = layout;
        this.onRecyclerViewItemCertificationClickListener = on;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SystemCertificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, parent, false);
        return new SystemCertificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SystemCertificationHolder holder, int position) {
        Certification certification = certifications.get(position);
        holder.name.setText(certification.getName());
        holder.createdTime.setText(certification.getCreatedDate());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecyclerViewItemCertificationClickListener.onItemClickListener(certification);
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecyclerViewItemCertificationClickListener.onItemClickListener(certification);
            }
        });
    }

    @Override
    public int getItemCount() {
        return certifications != null ? certifications.size() : 0;
    }


    class SystemCertificationHolder extends RecyclerView.ViewHolder {
        TextView name, createdTime;
        ImageButton imageButton;
        FrameLayout layout;

        public SystemCertificationHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.certificationItemName);
            createdTime = itemView.findViewById(R.id.certificationItemCreatedTime);
            imageButton = itemView.findViewById(R.id.certificationItemNavigateBtn);
            layout = itemView.findViewById(R.id.systemCertificationItem);
        }
    }
}
