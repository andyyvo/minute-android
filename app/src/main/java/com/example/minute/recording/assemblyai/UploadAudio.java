package com.example.minute.recording.assemblyai;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadAudio {

    // empty constructor
    public UploadAudio() {}

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

    /**
     * uploadAudio builds POST request for audioFile to AssemblyAI API and parses the JSON
     * request in getUploadAudioURL to obtain audio_url string
     */
    private String audioUrl = "";
    public String uploadAudio(String audioFile) {

        // build body for uploadAudio POST request
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .build();

        // make new http request for uploading audioFile to AssemblyAI URL
        final Request request = new Request.Builder()
                .url(AuthPoints.UPLOAD.toString())
                .addHeader("authorization", AuthPoints.KEY.toString())
                .addHeader("content-type", "application/json")
                .addHeader("transfer-encoding", "chunked")
                .post(requestBody)
                .build();

        // clear the call request if there already is something on it
        cancelCall();

        // makes a new request from the call factory
        mCall = mOkHttpClient.newCall(request);

        // queue up the request
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // required if failed
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // good response
                try {
                    // parse success here
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    audioUrl = getUploadAudioURL(jsonObject.toString());
                } catch (JSONException e) {
                    // failed to parse
                    e.printStackTrace();
                }
            }
        });

        return audioUrl;
    }

    private String getUploadAudioURL(final String jsonText) {

        // Gson obj parses json to java
        Gson gson = new Gson();

        // using uploadJSON.java to map uploadAudio JSON data
        UploadJSON uploadJSON = gson.fromJson(jsonText, UploadJSON.class);

        return uploadJSON.getUpload_url();
    }

    /**
     * uploadURL builds POST request for audio_url from uploadAudio into AssemblyAI API
     * to return an id attribute for a GET request to obtain transcription
     */
    private String audioID = "";
    public String uploadURL(String audioURL) throws IOException {

        // build POST request body
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("audio_url", audioURL)
                .addFormDataPart("disfluencies", "true")
                .build();

        // build GET request for audioURL AssemblyAI API transcribe
        final Request request = new Request.Builder()
                .url(AuthPoints.TRANSCRIPT.toString())
                .addHeader("authorization", AuthPoints.KEY.toString())
                .addHeader("content-type", "application/json")
                .post(requestBody)
                .build();

        // clear the call request if there already is something on it
        cancelCall();

        // makes a new request from the call factory
        mCall = mOkHttpClient.newCall(request);

        // sync call for GET request
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // required if failed
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // good response
                try {
                    // parse success here
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    audioID = getUploadAudioID(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return audioID;
    }

    private String getUploadAudioID(final String jsonText) {

        // similar to getUploadAudioURL
        Gson gson = new Gson();
        TranscriptJSON transcriptJSON = gson.fromJson(jsonText, TranscriptJSON.class);
        return transcriptJSON.getId();
    }
}
