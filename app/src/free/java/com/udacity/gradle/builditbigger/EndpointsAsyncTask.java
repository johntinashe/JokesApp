package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.johntinashe.jokeandroidlibrary.JokeActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

import javax.annotation.Nullable;

public class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {

    private static final String JOKES_KEY = "jokes";
    private static MyApi myApiService = null;
    private Context mContext;
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private InterstitialAd mInterstitialAd;

    public EndpointsAsyncTask(Context context,
                              @Nullable ProgressBar progressBar,
                              @Nullable TextView textView ,
                              @Nullable InterstitialAd mInterstitialAd) {
        this.mContext =context;
        this.mProgressBar = progressBar;
        this.mTextView = textView;
        this.mInterstitialAd = mInterstitialAd;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mProgressBar != null) mProgressBar.setVisibility(View.VISIBLE);
        if (mTextView != null) mTextView.setVisibility(View.INVISIBLE);
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
            return null;
        }
    }

    @Override
    protected void onPostExecute(final String result) {
        if(mProgressBar != null) mProgressBar.setVisibility(View.INVISIBLE);
        final Intent intent = new Intent(mContext, JokeActivity.class);


        if (mInterstitialAd !=null) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                launchActivity(intent,result);
            }
        }else {
            launchActivity(intent,result);
        }


        if (mInterstitialAd != null) {
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                    if (result != null && !result.equalsIgnoreCase("")) {
                        Intent intent = new Intent(mContext, JokeActivity.class);
                        intent.putExtra("jokes", result);
                        mContext.startActivity(intent);
                    }else {
                        mTextView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the interstitial ad is closed.
                    if (result != null && !result.equalsIgnoreCase("")) {
                        Intent intent = new Intent(mContext, JokeActivity.class);
                        intent.putExtra("jokes", result);
                        mContext.startActivity(intent);
                    }else {
                        mTextView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }

    private void launchActivity(Intent intent , String result) {
        if (result != null) {
            intent.putExtra(JOKES_KEY, result);
            mContext.startActivity(intent);
        }else {
            if (mTextView != null)mTextView.setVisibility(View.VISIBLE);
        }
    }
}
