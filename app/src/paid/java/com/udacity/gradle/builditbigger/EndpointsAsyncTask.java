package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

    private static MyApi myApiService = null;
    private Context context;
    private ProgressBar progressBar;
    private TextView textView;

    public EndpointsAsyncTask(Context context,ProgressBar progressBar,TextView textView) {
        this.context =context;
        this.progressBar = progressBar;
        this.textView = textView;
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
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });

            myApiService = builder.build();
        }

        try {
            return myApiService.getJokesApi().execute().getData();
        } catch (IOException e) {
            return "";
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        textView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String result) {
        progressBar.setVisibility(View.INVISIBLE);

        if (result != null && !result.equalsIgnoreCase("")) {
            Intent intent = new Intent(context, JokeActivity.class);
            intent.putExtra("jokes", result);
            context.startActivity(intent);
        }else {
            textView.setVisibility(View.VISIBLE);
        }


    }
}
