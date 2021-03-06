package com.example.popularmovies.utils;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.popularmovies.models.Movie;
import com.example.popularmovies.models.db.ColumnsFavouriteMovies;
import com.example.popularmovies.models.db.LoaderFavoriteMovies;
import com.example.popularmovies.models.db.ProviderFavouriteMovies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by fares on 02.11.15.
 */
public class UtilDB {

    public void insertMovieToFavourites(Context context, Movie m) throws RemoteException, OperationApplicationException, IllegalArgumentException {

        if (m.getId() <= 0 || m.getTitle() == null || m.getPosterPath() == null)
            throw new IllegalArgumentException("Wrong movie data. Can't save to favorite!");

        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ProviderFavouriteMovies.FavouriteMovies.CONTENT_URI);
        builder.withValue(ColumnsFavouriteMovies.KEY, m.getId());
        builder.withValue(ColumnsFavouriteMovies.NAME, m.getTitle());
        builder.withValue(ColumnsFavouriteMovies.POSTER, m.getPosterPath());

        builder.withValue(ColumnsFavouriteMovies.BACKDROP_PATH,   (m.getBackdropPath() == null)   ? "" : m.getBackdropPath());
        builder.withValue(ColumnsFavouriteMovies.ORIGIN_LANGUAGE, (m.getOriginLanguage() == null) ? "" : m.getOriginLanguage());
        builder.withValue(ColumnsFavouriteMovies.ORIGIN_TITLE,    (m.getOriginTitle() == null)    ? "" : m.getOriginTitle());
        builder.withValue(ColumnsFavouriteMovies.OVERVIEW,        (m.getOverview() == null)       ? "" : m.getOverview());
        builder.withValue(ColumnsFavouriteMovies.RELEASE_DATE,    (m.getReleaseDate() == null)    ? "" : m.getReleaseDate());

        builder.withValue(ColumnsFavouriteMovies.POPULARITY,   m.getPopularity());
        builder.withValue(ColumnsFavouriteMovies.VOTE_AVERAGE, m.getVoteAverage());
        builder.withValue(ColumnsFavouriteMovies.VOTE_COUNT,   m.getVoteCount());
        builder.withValue(ColumnsFavouriteMovies.VOTE_COUNT,   m.getVoteCount());


        ContentProviderOperation operation = builder.build();
        String s = ProviderFavouriteMovies.AUTHORITY;
        context.getContentResolver().applyBatch(s, new ArrayList<>(Collections.singletonList(operation)));
    }

    @Nullable
    public Movie getMovieFromDB(Context context, int movieId){
        Cursor c = context.getContentResolver().query(ProviderFavouriteMovies.FavouriteMovies.CONTENT_URI,
                null,
                ColumnsFavouriteMovies.KEY + "=?",
                new String[] {String.valueOf(movieId)},
                null);

        if (c != null && c.moveToFirst()){
             return new Movie.Builder()
                    .id(c.getInt(c.getColumnIndex(ColumnsFavouriteMovies.KEY)))
                    .posterPath(c.getString(c.getColumnIndex(ColumnsFavouriteMovies.POSTER)))
                    .title(c.getString(c.getColumnIndex(ColumnsFavouriteMovies.NAME)))
                    .build();
        }
        return null;
    }

}
