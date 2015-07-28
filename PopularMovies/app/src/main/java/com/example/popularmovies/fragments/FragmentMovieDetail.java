package com.example.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.models.Movie;

public class FragmentMovieDetail extends Fragment {

    public static final String SAVED_INSTANCE_MOVIE = "movie";

    private Movie selectedMovie;
    private TextView mMovieDetailsTitle ;
    private TextView mMovieDetailsReleaseDate;
    private TextView mMovieDetailsOverview;
    private TextView mMovieDetailsVoteAverage;

    public static FragmentMovieDetail newInstance(Movie selectedMovie) {
        FragmentMovieDetail fragment = new FragmentMovieDetail();
        fragment.selectedMovie = selectedMovie;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if(savedInstanceState != null){
            selectedMovie = savedInstanceState.getParcelable(SAVED_INSTANCE_MOVIE);
        }

        linkViews(layout);

        fillViewsWithData();

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_INSTANCE_MOVIE, selectedMovie);
        super.onSaveInstanceState(outState);
    }

    private void fillViewsWithData() {
        if (selectedMovie.getmOriginTitle() != null)
            mMovieDetailsTitle.setText(selectedMovie.getmOriginTitle());
        if (selectedMovie.getmReleaseDate() != null)
            mMovieDetailsReleaseDate .setText(selectedMovie.getmReleaseDate());
        if (selectedMovie.getmOverview() != null)
            mMovieDetailsOverview.setText(selectedMovie.getmOverview());

        mMovieDetailsVoteAverage.setText(String.valueOf(selectedMovie.getmVoteAverage()));
    }

    private void linkViews(View layout) {
        mMovieDetailsTitle          = (TextView) layout.findViewById(R.id.movieDetailsTitle);
        mMovieDetailsReleaseDate    = (TextView) layout.findViewById(R.id.movieDetailsReleaseDate);
        mMovieDetailsOverview       = (TextView) layout.findViewById(R.id.movieDetailsOverview);
        mMovieDetailsVoteAverage    = (TextView) layout.findViewById(R.id.movieDetailsVoteAverage);
    }

}
