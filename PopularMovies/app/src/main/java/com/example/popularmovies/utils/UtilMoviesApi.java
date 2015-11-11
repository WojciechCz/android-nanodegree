package com.example.popularmovies.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by fares on 7/27/15.
 */
public class UtilMoviesApi {

    private static final String MOVIE_API = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";
    private static final String REVIEWS_API = "http://api.themoviedb.org/3/movie/%s/reviews?api_key=";
    private static final String TRAILERS_API = "http://api.themoviedb.org/3/movie/%s/videos?api_key=";
    public static final String URL_POSTER = "http://image.tmdb.org/t/p/w185";


    public void getPopularMoviesJson(@NonNull PopularMovies callback, @NonNull String apiK){
        new GetPopularMoviesJson(callback).execute(apiK);
    }

    public void getDetailsJson(@NonNull PopularMovieDetails callback, @NonNull String apiK, @NonNull String movieId){
        new GetMovieDetails(callback).execute(apiK, movieId);
    }

    public interface PopularMovieDetails{
        void onMovieDetailsReceived(String[] jsons);
    }

    public interface PopularMovies extends PopularMovieDetails{
        void onPopularMoviesJsonReceived(String jsons);
    }


    private class GetPopularMoviesJson extends AsyncTask<String, Void, String> {

        private PopularMovies mCallback;

        public GetPopularMoviesJson(@NonNull PopularMovies mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        protected String doInBackground(String... param) {
            String url = MOVIE_API + param[0];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            if (json != null)
                mCallback.onPopularMoviesJsonReceived(json);
        }
    }

    private class GetMovieDetails extends AsyncTask<String, Void, String[]> {

        private PopularMovieDetails mCallback;

        public GetMovieDetails(@NonNull PopularMovieDetails mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        protected String[] doInBackground(String... param) {
            String movieId = param[1];
            String finalUrlReviews = String.format(REVIEWS_API, movieId) + param[0];
            String finalUrlTrailers = String.format(TRAILERS_API, movieId) + param[0];

            OkHttpClient client = new OkHttpClient();
            Request requestReviews = new Request.Builder()
                    .url(finalUrlReviews)
                    .build();
            Request requestTrailers = new Request.Builder()
                    .url(finalUrlTrailers)
                    .build();

            try {
                Response responseReviews = client.newCall(requestReviews).execute();
                Response responseTrailers = client.newCall(requestTrailers).execute();
                return new String[] { responseReviews.body().string(), responseTrailers.body().string() };
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] jsons) {
            if (jsons != null)
                mCallback.onMovieDetailsReceived(jsons);
        }
    }
}
