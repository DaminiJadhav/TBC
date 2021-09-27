package com.truckbhejob2b.truckbhejocustomer.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.truckbhejob2b.truckbhejocustomer.DashboardActivity;
import com.truckbhejob2b.truckbhejocustomer.Fragments.OrderDetailsFragment;
import com.truckbhejob2b.truckbhejocustomer.Fragments.TrackOrderFragment;
import com.truckbhejob2b.truckbhejocustomer.Model.Dashboard;
import com.truckbhejob2b.truckbhejocustomer.Model.Trips;
import com.truckbhejob2b.truckbhejocustomer.R;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestKeys;

import java.util.List;

public class AdapterTrips extends RecyclerView.Adapter<AdapterTrips.ViewHolder> {
    private Context mContext;
    private List<Trips> tripsList;

    public AdapterTrips(Context mContext, List<Trips> tripsList) {
        this.mContext = mContext;
        this.tripsList = tripsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_mytrips, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Trips trips = tripsList.get(position);
        holder.mTxtViewOrderId.setText(trips.getOrderId());
        holder.mTxtViewFromAdd.setText(trips.getFromLocation());
        holder.mTxtViewToAdd.setText(trips.getToLocation());
        holder.mTxtViewOrderDate.setText(trips.getOrderDate());
        holder.mTxtViewVehicleType.setText(trips.getVehicleType());
        holder.mTxtViewProductType.setText(trips.getProductType());
        if (trips.getVehicleNumber().equals("null") || trips.getVehicleNumber().equals("NULL")) {
            holder.mTXtViewVehicleNumber.setText("");
        } else {
            holder.mTXtViewVehicleNumber.setText(trips.getVehicleNumber());
        }

        switch (trips.getOrderStatus()) {
            case "0":
              //  holder.mLinearLayout.setVisibility(View.GONE);
                //holder.view.setVisibility(View.GONE);
                if (!trips.getPodStatus().equals("NO")) {
//                    Log.d("TAG", "onBindViewHolder: "+trips.getPodStatus());
                    if (trips.getPodStatus().equals("8")) {

                        holder.mTxtViewOrderStatus.setText(mContext.getString(R.string.trips_delivered));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                holder.mTxtViewOrderStatus.setBackground(mContext.getDrawable(R.drawable.rectgreenbox));
                            }
                        }
                    } else {
                        holder.mTxtViewOrderStatus.setText(mContext.getString(R.string.trips_delivered));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                holder.mTxtViewOrderStatus.setBackground(mContext.getDrawable(R.drawable.rectgreenbox));
                            }
                        }
                    }
                }
                break;
            case "1":
                holder.mTxtViewOrderStatus.setText(mContext.getString(R.string.trips_Pending));
                holder.mTXtViewVehicleNumber.setText("");

              //  holder.mLinearLayout.setVisibility(View.GONE);
               // holder.view.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mTxtViewOrderStatus.setBackground(mContext.getDrawable(R.drawable.rectyellowbox));
                    holder.markerImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_icons_13));
                }
                break;
            case "2":
                holder.mTxtViewOrderStatus.setText(mContext.getString(R.string.trips_accepted));
                break;
            case "3":
                holder.mTxtViewOrderStatus.setText(mContext.getString(R.string.trips_canceled));
                break;
            case "4":
                //holder.mLinearLayout.setVisibility(View.VISIBLE);
               // holder.view.setVisibility(View.VISIBLE);

                holder.mTxtViewOrderStatus.setText(mContext.getString(R.string.trips_running));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.mTxtViewOrderStatus.setBackground(mContext.getDrawable(R.drawable.rectgreenbox));
                    }
                }
                if (trips.getTrackStatus().equals("NO")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.markerImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_icons_13));
                    }
                } else {
                    if (trips.getTrackStatus().equals("1")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.markerImage.setImageDrawable(mContext.getDrawable(R.mipmap.green_location));
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.markerImage.setImageDrawable(mContext.getDrawable(R.mipmap.red_location));
                        }
                    }
                }
                break;
        }
        holder.trips = trips;

    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        private ImageView mImageViewMoreEvent;
        private ImageView markerImage;
        private TextView mTxtViewOrderId, mTxtViewOrderStatus, mTxtViewOrderDate, mTxtViewFromAdd, mTxtViewToAdd, mTxtViewVehicleType, mTXtViewVehicleNumber, mTxtViewProductType;
        private LinearLayout mLinearLayout;
        private View view;
        private CardView mCardView;
        private Trips trips;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            mImageButtonTrack = itemView.findViewById(R.id.imageBtnLocation);
//            mImageButtonCall = itemView.findViewById(R.id.imageBtnCall);
            markerImage = itemView.findViewById(R.id.imageView12);
//            mLinearLayout = itemView.findViewById(R.id.linearLayoutView);
            mTxtViewOrderId = itemView.findViewById(R.id.txtViewOrderId);
            mTxtViewOrderDate = itemView.findViewById(R.id.txtViewOrderDate);
            mTxtViewOrderStatus = itemView.findViewById(R.id.txtViewStatus);
            mTxtViewFromAdd = itemView.findViewById(R.id.txtViewFromLocation);
            mTxtViewToAdd = itemView.findViewById(R.id.txtViewToLocation);
            mTxtViewVehicleType = itemView.findViewById(R.id.txtViewVehicleType);
            mTXtViewVehicleNumber = itemView.findViewById(R.id.txtViewVehicleNumber);
            mTxtViewProductType = itemView.findViewById(R.id.txtViewProduct);
//            view = itemView.findViewById(R.id.view);
            mCardView = itemView.findViewById(R.id.cardViewRunningOrder);
//            mImageViewMoreEvent = itemView.findViewById(R.id.imageViewMoreEvent);
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!trips.getOrderStatus().equals("1")){
                        OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(RestKeys.RES_TRIPS_ORDER_ID, trips.getOrderId());
                        orderDetailsFragment.setArguments(bundle);
                        ((DashboardActivity)mContext).changeFragment(orderDetailsFragment,"FragmentOrderDetails");
                    }
                }
            });

        }
    }


}
