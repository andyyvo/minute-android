package com.example.minute.recording.assemblyai;

public class TranscriptJSON {
    private String id; // id to get transcription

    // empty constructor
    public TranscriptJSON() {}

    // full constructor
    public TranscriptJSON(String id) {
        this.id = id;
    }

    // getter setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

// example JSON
// {
//  "id": "5551722-f677-48a6-9287-39c0aafd9ac1",
//  "status": "queued",
//  "acoustic_model": "assemblyai_default",
//  "audio_duration": null,
//  "audio_url": "https://bit.ly/3yxKEIY",
//  "confidence": null,
//  "dual_channel": null,
//  "format_text": true,
//  "language_model": "assemblyai_default",
//  "punctuate": true,
//  "text": null,
//  "utterances": null,
//  "webhook_status_code": null,
//  "webhook_url": null,
//  "words": null,
//  ...
// }