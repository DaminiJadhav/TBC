package com.truckbhejob2b.truckbhejocustomer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.truckbhejob2b.truckbhejocustomer.Model.NotificationModel;
import com.truckbhejob2b.truckbhejocustomer.*;

import java.util.List;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {
    private Context mContext;
    private List<NotificationModel> notificationList;

    public AdapterNotification(Context mContext, List<NotificationModel> notificationList) {
        this.mContext = mContext;
        this.notificationList = notificationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_notifications, parent, false);

    return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel notification = notificationList.get(position);
        holder.tv_NotificationTitle.setText(notification.getnTitle());
        holder.tv_NotificationDesc.setText(notification.getnDesc());
        holder.tv_NotificationTime.setText(notification.getnDate());

        holder.notificationModel = notification;

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_NotificationTitle, tv_NotificationType, tv_NotificationDesc, tv_NotificationTime;
        private NotificationModel notificationModel;
        public ViewHolder(@NonNull View view) {
            super(view);
            tv_NotificationTitle = (TextView) view.findViewById(R.id.tv_NotificationTitle);
            tv_NotificationType = (TextView) view.findViewById(R.id.tv_NotificationType);
            tv_NotificationDesc = (TextView) view.findViewById(R.id.tv_NotificationDesc);
            tv_NotificationTime = (TextView) view.findViewById(R.id.tv_NotificationTime);
        }
    }
}
