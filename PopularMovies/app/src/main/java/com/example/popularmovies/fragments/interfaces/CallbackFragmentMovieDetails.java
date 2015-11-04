package com.example.popularmovies.fragments.interfaces;


import android.support.v7.widget.ShareActionProvider;

import com.example.popularmovies.models.Movie;

/**
 * Created by fares on 27.10.15.
 */
public interface CallbackFragmentMovieDetails {
    void onTrailerClicked(String youtubeVideoID);
    void onFavouriteButtonClicked(Movie movie);
}
