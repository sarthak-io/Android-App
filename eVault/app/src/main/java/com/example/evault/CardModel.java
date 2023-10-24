package com.example.evault;

public class CardModel {
    private String title;
    private String fileUrl;
    String fileExtension;


    public CardModel(String title, String fileUrl, String fileExtension) {
        this.title = title;
        this.fileUrl = fileUrl;
        this.fileExtension = fileExtension;
    }

    public String getTitle() {
        return title;
    }

    public String getFileUrl() {
        return fileUrl;
    }
    public String getFileExtension() {
        return fileExtension;
    }
}

