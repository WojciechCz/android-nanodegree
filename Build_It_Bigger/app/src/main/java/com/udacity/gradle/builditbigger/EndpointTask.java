package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.Joke;
import com.example.fares.builditbigger.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by fares on 03.01.16.
 */
public class EndpointTask extends AsyncTask<Void, Void, List<String>> {
    private static MyApi myApiService = null;
    private Context mContext;
    private EndpointTaskCallback mEndpointTaskCallback;
    private ProgressBar mProgressBar;

    public EndpointTask(@NonNull Context context, @NonNull EndpointTaskCallback endpointTaskCallback, ProgressBar mProgressBar) {
        this.mContext = context;
        this.mProgressBar = mProgressBar;
        this. mEndpointTaskCallback = endpointTaskCallback;
    }

    @Override
    protected void onPreExecute() {
        if (mProgressBar != null)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        if(myApiService == null) {
            MyApi.Builder builder =
                    new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://build-it-bigger-endpoint.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        try {
//            myApiService.sayHi(name).execute().getData();
            return myApiService.getJoke().execute().getAll();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<String> jokes) {
        if (mProgressBar != null)
            mProgressBar.setVisibility(View.GONE);

        mEndpointTaskCallback.onJokesDownloaded(jokes);
    }

    public interface EndpointTaskCallback{
        void onJokesDownloaded(List<String> jokes);
    }
}