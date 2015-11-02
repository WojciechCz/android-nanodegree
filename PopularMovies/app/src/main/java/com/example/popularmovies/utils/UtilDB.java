package com.example.popularmovies.utils;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.example.popularmovies.models.Movie;
import com.example.popularmovies.models.db.ColumnsFavouriteMovies;
import com.example.popularmovies.models.db.ProviderFavouriteMovies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by fares on 02.11.15.
 */
public class UtilDB {

    public void insertMovieToFavourites(Context context, Movie m) throws RemoteException, OperationApplicationException {
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ProviderFavouriteMovies.FavouriteMovies.CONTENT_URI);
        builder.withValue(ColumnsFavouriteMovies.KEY, m.getId());
        builder.withValue(ColumnsFavouriteMovies.NAME, m.getTitle());
        builder.withValue(ColumnsFavouriteMovies.POSTER, m.getPosterPath());

        ContentProviderOperation operation = builder.build();
        context.getContentResolver().applyBatch(ProviderFavouriteMovies.AUTHORITY, new ArrayList<>(Collections.singletonList(operation)));
    }

}
