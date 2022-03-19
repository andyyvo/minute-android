package com.example.minute.recording.assemblyai;

public enum authPoints {
    KEY("667dbc33a2ad4e799a0619cd1c1b4619"),
    UPLOAD("https://api.assemblyai.com/v2/upload"),
    TRANSCRIPT("https://api.assemblyai.com/v2/transcript"),
    ;

    private final String authpoint;

    authPoints(String authpoint) {
        this.authpoint = authpoint;
    }

    @Override
    public String toString() {
        return authpoint;
    }
}
