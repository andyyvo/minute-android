package com.example.minute.recording.assemblyai;

public class UploadJSON {
    private String upload_url; // URL to process into transcription

    // empty constructor
    public UploadJSON() {}

    // full constructor
    public UploadJSON(String upload_url) {
        this.upload_url = upload_url;
    }

    // getter setters

    public String getUpload_url() {
        return upload_url;
    }

    public void setUpload_url(String upload_url) {
        this.upload_url = upload_url;
    }
}

// example json
// {
//  "upload_url": "https://bit.ly/3yxKEIY"
// }