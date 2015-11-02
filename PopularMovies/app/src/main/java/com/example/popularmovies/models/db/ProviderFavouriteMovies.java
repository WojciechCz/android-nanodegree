package com.example.popularmovies.models.db;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by fares on 30.10.15.
 */
@ContentProvider(authority = ProviderFavouriteMovies.AUTHORITY, database = DatabaseFavouriteMovies.class)
public class ProviderFavouriteMovies {
    public static final String AUTHORITY = "com.example.popularmovies.models.db.ProviderFavouriteMovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String FAVOURITE_MOVIES = "favouriteMovies";
    }

    private static Uri buildUri(String ... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = DatabaseFavouriteMovies.FAVOURITE_MOVIES) public static class FavouriteMovies{
        @ContentUri(
                path = Path.FAVOURITE_MOVIES,
                type = "vnd.android.cursor.dir/favouriteMovies",
                defaultSort = ColumnsFavouriteMovies._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.FAVOURITE_MOVIES);

        @InexactContentUri(
                name = "FAVOURITE_MOVIE_ID",
                path = Path.FAVOURITE_MOVIES + "/#",
                type = "vnd.android.cursor.item/favouriteMovie",
                whereColumn = ColumnsFavouriteMovies._ID,
                pathSegment = 1)

        public static Uri withId(long id){
            return buildUri(Path.FAVOURITE_MOVIES, String.valueOf(id));
        }
    }
}
