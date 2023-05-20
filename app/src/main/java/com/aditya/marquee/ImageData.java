package com.aditya.marquee;

public class ImageData {
    String date, imageURL, prompt;

    public ImageData(){}

    public ImageData(String date, String imageURL, String prompt) {
        this.date = date;
        this.imageURL = imageURL;
        this.prompt = prompt;
    }

    public String getDate() {
        return date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPrompt() {
        return prompt;
    }
}
