package com.example.popularmovies.fragments.interfaces;

import com.example.popularmovies.models.Movie;

/**
 * Created by fares on 27.10.15.
 */
public interface CallbackFragmentMovieDetails {
    void onTrailerClicked(String youtubeVideoID);
    void onFavouriteButtonClicked(Movie movie);
}
