package com.aditya.marquee;

public class ActivityData {

    String activityName, moreInfo;
    int image;

    public ActivityData(String activityName, String moreInfo, int image) {
        this.activityName = activityName;
        this.moreInfo = moreInfo;
        this.image = image;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public int getImage() {
        return image;
    }
}
