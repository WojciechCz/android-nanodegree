package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.example.Joke;
import com.example.fares.builditbigger.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by fares on 03.01.16.
 */
public class EndpointTask extends AsyncTask<Pair<Context, String>, Void, Joke> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected Joke doInBackground(Pair<Context, String>... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder =
                    new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://build-it-bigger-endpoint.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        context = params[0].first;
        String name = params[0].second;

        try {
            myApiService.sayHi(name).execute().getData();
//            return myApiService.getJoke();
            return new Joke();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Joke joke) {
        Toast.makeText(context, joke.getNext(), Toast.LENGTH_LONG).show();
    }
}