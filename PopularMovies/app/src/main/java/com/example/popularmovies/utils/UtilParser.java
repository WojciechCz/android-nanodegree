package com.example.popularmovies.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.popularmovies.models.Movie;
import com.example.popularmovies.models.Review;
import com.example.popularmovies.models.Trailer;
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

    public List<Trailer> jsonParserTrailers(@NonNull String json) throws JsonParseException {
        Type listType = new TypeToken<List<Trailer>>() {}.getType();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(listType, new JsonDeserializerMovieTrailers())
                .create();

        return gson.fromJson(json, listType);
    }

    public List<Review> jsonParserReviews(@NonNull String json) throws JsonParseException {
        Type listType = new TypeToken<List<Review>>() {}.getType();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(listType, new JsonDeserializerMovieReview())
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

            if (jsonMoviesList != null && jsonMoviesList.size() > 0) {
                for (int i = 0; i < jsonMoviesList.size(); i++) {
                    jsonMovie = jsonMoviesList.get(i);
                    movie = gson.fromJson(jsonMovie, Movie.class);
                    moviesList.add(movie);
                }
            }
            return moviesList;
        }
    }

    private class JsonDeserializerMovieReview implements JsonDeserializer<List<Review>> {
        @Override
        public List<Review> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json == null)
                return null;
            final JsonArray jsonReviewsList = json.getAsJsonObject().getAsJsonArray("results");

            List<Review> reviewList = new LinkedList<>();
            JsonElement jsonReview;
            Review review;
            Gson gson = new Gson();

            if (jsonReviewsList != null && jsonReviewsList.size() > 0) {
                for (int i = 0; i < jsonReviewsList.size(); i++){
                    jsonReview = jsonReviewsList.get(i);
                    review = gson.fromJson(jsonReview, Review.class);
                    reviewList.add(review);
                }
            }
            return reviewList;
        }
    }

    private class JsonDeserializerMovieTrailers implements JsonDeserializer<List<Trailer>> {
        @Override
        public List<Trailer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json == null)
                return null;
            final JsonArray jsonTrailersList = json.getAsJsonObject().getAsJsonArray("results");

            List<Trailer> trailerList = new LinkedList<>();
            JsonElement jsonTrailer;
            Trailer trailer;
            Gson gson = new Gson();

            if (jsonTrailersList != null && jsonTrailersList.size() > 0) {
                for (int i = 0; i < jsonTrailersList.size(); i++) {
                    jsonTrailer = jsonTrailersList.get(i);
                    trailer = gson.fromJson(jsonTrailer, Trailer.class);
                    trailerList.add(trailer);
                }
            }
            return trailerList;
        }
    }

}
