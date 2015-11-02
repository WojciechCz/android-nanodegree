package com.example.popularmovies.models.db;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.popularmovies.views.adapters.AdapterFavouriteMoviesCursor;

/**
 * Created by fares on 02.11.15.
 */
public class LoaderFavoriteMovies implements LoaderManager.LoaderCallbacks<Cursor>  {
    private static final int CURSOR_LOADER_ID = 0;
    private Fragment mAttachedFragment;
    private AdapterFavouriteMoviesCursor mAdapter;

    private LoaderFavoriteMovies() {
    }

    public static LoaderFavoriteMovies newInstance(@NonNull Fragment fragment, @NonNull AdapterFavouriteMoviesCursor adapter){
        LoaderFavoriteMovies element = new LoaderFavoriteMovies();
        element.mAttachedFragment = fragment;
        element.mAdapter = adapter;
        return element;
    }

    public void initLoader(){
        if (mAttachedFragment != null)
            mAttachedFragment.getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    public void restartLoader(){
        if (mAttachedFragment != null)
            mAttachedFragment.getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mAttachedFragment != null && mAttachedFragment.getActivity() != null){
            return new CursorLoader(mAttachedFragment.getActivity(), ProviderFavouriteMovies.FavouriteMovies.CONTENT_URI,
                    null, null, null, null);
        }
        return  null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter != null)
            mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null)
            mAdapter.swapCursor(null);
    }
}
