package com.aditya.marquee;

public class CloudSavesData {

    String prompt, date, imageLink;

    public CloudSavesData(String prompt, String date, String imageLink) {
        this.prompt = prompt;
        this.date = date;
        this.imageLink = imageLink;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getDate() {
        return date;
    }

    public String getImageLink() {
        return imageLink;
    }
}
