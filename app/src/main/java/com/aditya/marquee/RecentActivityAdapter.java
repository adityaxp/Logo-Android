package com.aditya.marquee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;


import java.util.ArrayList;
import java.util.List;


public class RecentActivityAdapter extends RecyclerView.Adapter< RecentActivityViewHolder > {

    private Context mContext;
    int lastPosition = -1;
    private List<ActivityData> activityDataList;

    RecentActivityAdapter(Context mContext, List< ActivityData > receiptDataList) {
            this.mContext = mContext;
            this.activityDataList = receiptDataList;
        }

        @Override
        public RecentActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_activity_recycle_row_item, parent, false);
            return new RecentActivityViewHolder(mView);
        }

        @Override
        public void onBindViewHolder(final RecentActivityViewHolder holder, int position) {
            holder.activityName.setText(activityDataList.get(position).getActivityName());
            holder.moreInfo.setText(activityDataList.get(position).getMoreInfo());
            holder.logoImage.setImageResource(activityDataList.get(position).getImage());


            setAnimation(holder.itemView, position);

        }

    public void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(1500);
            view.startAnimation(scaleAnimation);
            lastPosition = position;
        }
    }

        @Override
        public int getItemCount() {
            return activityDataList.size();
        }
    }


    class RecentActivityViewHolder extends RecyclerView.ViewHolder {

        TextView activityName, moreInfo;
        ImageView logoImage;

        RecentActivityViewHolder(View itemView) {
            super(itemView);

            activityName = itemView.findViewById(R.id.activityName);
            moreInfo = itemView.findViewById(R.id.moreInfo);
            logoImage = itemView.findViewById(R.id.logoImage);

        }
    }
