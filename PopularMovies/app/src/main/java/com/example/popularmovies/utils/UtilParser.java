package com.example.popularmovies.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.popularmovies.models.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fares on 7/27/15.
 */
public class UtilParser {
    
    public List<Movie> jsonParserMovies(@NonNull String json) throws JsonParseException {
        Type listType = new TypeToken<List<Movie>>() {}.getType();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(listType, new JsonDeserializerMovieList())
                .create();

        return gson.fromJson(json, listType);
    }

    private class JsonDeserializerMovieList implements JsonDeserializer<List<Movie>> {
        @Override
        public List<Movie> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json == null)
                return null;
            final JsonArray jsonMoviesList = json.getAsJsonObject().getAsJsonArray("results");

            List<Movie> moviesList = new LinkedList<>();
            JsonElement jsonMovie;
            Movie movie;
            Gson gson = new Gson();

//            Log.d("test", "----------------------- parsing in progress:");
            for (int i = 0; i < jsonMoviesList.size(); i++){
                jsonMovie = jsonMoviesList.get(i);
                movie = gson.fromJson(jsonMovie, Movie.class);
//                Log.d("test", movie.toString());
                moviesList.add(movie);
            }
//            Log.d("test", "----------------------- done");
            return moviesList;
        }
    }
}
