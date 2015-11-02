package com.example.popularmovies.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.example.popularmovies.fragments.interfaces.CallbackFragmentPopularMovies;
import com.example.popularmovies.models.db.LoaderFavoriteMovies;
import com.example.popularmovies.models.db.ProviderFavouriteMovies;
import com.example.popularmovies.views.adapters.AdapterFavouriteMoviesCursor;
import com.example.popularmovies.views.adapters.AdapterMovies;
import com.example.popularmovies.R;
import com.example.popularmovies.activities.ActivityMain;
import com.example.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fares on 7/27/15.
 */
public class FragmentPopularMovies extends Fragment implements
        ActivityMain.PopularMoviesDataSetChange, View.OnClickListener {

    public static final String SAVED_INSTANCE_MOVIES = "movies";
    public static final int COLUMN_SPAN = 3;

    private RecyclerView mMoviesGrid;
    private AdapterMovies mAdapterMovies;
    private List<Movie> mMoviesList;
    private CallbackFragmentPopularMovies mCallback;
    private LoaderFavoriteMovies mFavoriteMoviesLoader;

    public static Fragment newInstance(@NonNull List<Movie> movies, CallbackFragmentPopularMovies callback){
        FragmentPopularMovies f = new FragmentPopularMovies();
        f.mMoviesList = movies;
        f.mCallback = callback;
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        if (savedInstanceState != null)
            mMoviesList = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_MOVIES);
        if (mMoviesList == null)
            mMoviesList = new LinkedList<>();

        linkedViews(layout);
        setUpMovieGrid();

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFavoriteMoviesLoader != null)
            mFavoriteMoviesLoader.restartLoader();
    }

    // --------- activity callbacks ---------
    @Override
    public void onPopularMoviesDataSetChange(List<Movie> movieList) {
        mMoviesList.clear();
        mMoviesList.addAll(movieList);
        mAdapterMovies.notifyDataSetChanged();
    }
    @Override
    public void onFavoriteMoviesRequested() {
        AdapterFavouriteMoviesCursor adapter = getFavoriteMoviesCursorAdapter();
        mFavoriteMoviesLoader.initLoader();
        setAdapterForMovieList(adapter);
    }
    // --------- clicked movie ---------
    @Override
    public void onClick(View v) {
        mCallback.onMovieClicked( mMoviesList.get(mMoviesGrid.indexOfChild(v)) );
    }
    // --------- --------- --------- ---------

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVED_INSTANCE_MOVIES, new ArrayList<Parcelable>(mMoviesList));
        super.onSaveInstanceState(outState);
    }

    private AdapterFavouriteMoviesCursor getFavoriteMoviesCursorAdapter(){
        Cursor c = getActivity().getContentResolver().query(ProviderFavouriteMovies.FavouriteMovies.CONTENT_URI, null, null, null, null);
        AdapterFavouriteMoviesCursor adapter = new AdapterFavouriteMoviesCursor(getActivity(), c);
        mFavoriteMoviesLoader = LoaderFavoriteMovies.newInstance(this, adapter);
        return adapter;
    }

    private void setUpMovieGrid() {
        mAdapterMovies = new AdapterMovies(getActivity(), mMoviesList, this);
        mMoviesGrid.setLayoutManager(new GridLayoutManager(getActivity(), COLUMN_SPAN));
        setAdapterForMovieList(mAdapterMovies);
    }

    private void setAdapterForMovieList(RecyclerView.Adapter adapter){
        mMoviesGrid.setAdapter(adapter);
    }
    
    private void linkedViews(View layout) {
        mMoviesGrid = (RecyclerView) layout.findViewById(R.id.popularMoviesGrid);
    }


    public void setCallback(CallbackFragmentPopularMovies mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_movies_list, menu);
    }
}
