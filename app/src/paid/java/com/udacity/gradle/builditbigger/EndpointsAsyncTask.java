package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.johntinashe.jokeandroidlibrary.JokeActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

public class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {

    private static final String JOKES_KEY = "jokes";
    private static MyApi myApiService = null;
    private Context mContext;
    private ProgressBar mProgressBar;
    private TextView mTextView;

    public EndpointsAsyncTask(Context context, @Nullable ProgressBar progressBar, @Nullable TextView textView, @Nullable String name) {
        this.mContext = context;
        this.mProgressBar = progressBar;
        this.mTextView = textView;
    }

    @Override
    protected String doInBackground(Void...params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });

            myApiService = builder.build();
        }

        try {
            return myApiService.getJokesApi().execute().getData();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mProgressBar != null) mProgressBar.setVisibility(View.VISIBLE);
        if (mTextView != null) mTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPostExecute(String result) {
        if (mProgressBar != null) mProgressBar.setVisibility(View.INVISIBLE);

        if (result != null) {
            Intent intent = new Intent(mContext, JokeActivity.class);
            intent.putExtra(JOKES_KEY, result);
            mContext.startActivity(intent);
        }else {
            if (mTextView != null) mTextView.setVisibility(View.VISIBLE);
        }


    }
}
