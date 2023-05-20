package com.aditya.marquee;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class CloudSavesAdapter extends RecyclerView.Adapter<CloudSavesAdapterViewHolder > {

    private Context mContext;
    int lastPosition = -1;
    private List<CloudSavesData> cloudSavesDataList;

    CloudSavesAdapter(Context mContext, List< CloudSavesData > cloudSavesDataList) {
        this.mContext = mContext;
        this.cloudSavesDataList = cloudSavesDataList;
    }

    @Override
    public CloudSavesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cloud_saves_row_item, parent, false);
        return new CloudSavesAdapterViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final CloudSavesAdapterViewHolder holder, int position) {
        holder.promptName.setText(cloudSavesDataList.get(position).getPrompt());
        holder.dateInfo.setText(cloudSavesDataList.get(position).getDate());
        Glide.with(mContext)
                .load(cloudSavesDataList.get(position).getImageLink())
                .into(holder.logoImage);
        final int pos = position;

        holder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager downloadmanager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(cloudSavesDataList.get(pos).getImageLink());

                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle(cloudSavesDataList.get(pos).getPrompt());
                request.setDescription("Downloading");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,cloudSavesDataList.get(pos).getPrompt());
                downloadmanager.enqueue(request);
            }
        });

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
        return cloudSavesDataList.size();
    }
}


class CloudSavesAdapterViewHolder extends RecyclerView.ViewHolder {

    TextView promptName, dateInfo;
    ImageView logoImage;
    Button downloadButton;

    CloudSavesAdapterViewHolder(View itemView) {
        super(itemView);

        promptName = itemView.findViewById(R.id.promptName);
        dateInfo = itemView.findViewById(R.id.dateInfo);
        logoImage = itemView.findViewById(R.id.logoImage);
        downloadButton = itemView.findViewById(R.id.downloadButton);
    }
}
