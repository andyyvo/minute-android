package com.example.minute.recording.assemblyai;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class uploadAudio {

    // Square's HTTP client for JVM/Android
    // call perform HTTP client requests at a high level with GET URL
    // https://square.github.io/okhttp/calls/
    private Call mCall;

    // okHTTPClient is the factory for calls which are used to send http requests and read responses
    // only need one instance for all HTTP calls
    // each client holds its own connection/thread pools so we can reuse them and save memory
    // https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/
    private final OkHttpClient mOkHttpClient = new OkHttpClient();

    // cancel the current HTTP call request
    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    private String uploadAudio(String audioFile) {

        // build body for uploadAudio POST request
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("audio_url", audioFile)
                .addFormDataPart("disfluencies", "true")
                .build();

        // make new http request for uploading audioFile to AssemblyAI URL
        final Request request = new Request.Builder()
                .url(authPoints.UPLOAD.toString())
                .addHeader("authorization", authPoints.KEY.toString())
                .addHeader("content-type", "application/json")
                .post(requestBody)
                .build();

        // clear the call request if there already is something on it
        cancelCall();

        

        return "apple";
    }
}
