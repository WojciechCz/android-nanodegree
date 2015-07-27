package com.example.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.popularmovies.views.adapters.AdapterMovies;
import com.example.popularmovies.R;
import com.example.popularmovies.activities.ActivityMain;
import com.example.popularmovies.models.Movie;

import java.util.List;

/**
 * Created by fares on 7/27/15.
 */
public class FragmentPopularMovies extends Fragment implements ActivityMain.PopularMoviesDataSetChange {

    private GridView mMoviesGrid;
    private AdapterView.OnItemClickListener onMovieClicked;
    private AdapterMovies mAdapterMovies;
    private List<Movie> mMoviesList;

    public static Fragment newInstance(@NonNull List<Movie> movies,@NonNull AdapterView.OnItemClickListener onMovieClicked){
        FragmentPopularMovies f = new FragmentPopularMovies();
        f.mMoviesList = movies;
        f.onMovieClicked = onMovieClicked;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        linkedViews(layout);
        setUpMovieGrid();

        return layout;
    }

    private void setUpMovieGrid() {
        mAdapterMovies = new AdapterMovies(getActivity(), mMoviesList);
        mMoviesGrid.setAdapter(mAdapterMovies);
        mMoviesGrid.setOnItemClickListener(onMovieClicked);
    }

    private void linkedViews(View layout) {
        mMoviesGrid = (GridView) layout.findViewById(R.id.popularMoviesGrid);
    }

    @Override
    public void OnPopularMoviesDataSetChange(List<Movie> movieList) {
        mMoviesList.clear();
        mMoviesList.addAll(movieList);
        mAdapterMovies.notifyDataSetChanged();
    }
}
