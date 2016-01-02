package com.example;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class ChuckNorris {

    public Joke getChuckNorrisJoke() throws IOException {
        // Create a very simple REST adapter which points the GitHub API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(new StringBuffer().append(JokesService.SERVICE_CHUCK_NORRIS_JOKES_URL).append(JokesService.SERVICE_CHUCK_NORRIS_JOKES_QUERY).toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
        JokesService jokesService = retrofit.create(JokesService.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<Joke> call = jokesService.getJoke();

        // Fetch and print a list of the contributors to the library.
        return call.execute().body();
    }
}
