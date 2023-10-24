package com.example.evault;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Upload {
    public String name;
    public String description;
    public String url;
    public String FileExtension;

    // Default constructor required for calls to DataSnapshot.getValue(Upload.class)
    public Upload() {
    }

    public Upload(String name, String description, String url,String FileExtension) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.FileExtension= FileExtension;
    }
}
