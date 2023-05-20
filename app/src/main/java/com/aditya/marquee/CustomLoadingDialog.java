package com.aditya.marquee;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class CustomLoadingDialog {


    Activity activity;
    AlertDialog alertDialog;
    LottieAnimationView lottieAnimationView;
    TextView progressTitleTextView;

    public CustomLoadingDialog(Activity myActivity) {
        activity = myActivity;

    }

    void customLoadingDialogShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_progress_dialog, null));
        builder.setCancelable(true);

        View view = activity.getLayoutInflater().inflate(R.layout.custom_progress_dialog, null);

        progressTitleTextView = view.findViewById(R.id.progressTitleTextView);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);


        alertDialog = builder.create();
        alertDialog.show();
    }

    void  customLoadingDialogDismiss(){
        alertDialog.dismiss();
    }
}
