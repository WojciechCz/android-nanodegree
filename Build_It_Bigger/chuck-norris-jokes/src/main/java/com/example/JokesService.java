package com.example;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by fares on 02.01.16.
 */
public interface JokesService {

    public static final String SERVICE_CHUCK_NORRIS_JOKES_URL = "http://api.icndb.com/jokes/random";
    public static final String SERVICE_CHUCK_NORRIS_JOKES_QUERY = "?firstName=Chuck&amp;lastName=Norris";

    @GET
    Call<Joke> getJoke();
}
