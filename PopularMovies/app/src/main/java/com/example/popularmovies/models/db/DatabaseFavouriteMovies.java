package com.example.popularmovies.models.db;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by fares on 30.10.15.
 */
@Database(version = DatabaseFavouriteMovies.VERSION)
public class DatabaseFavouriteMovies {
    private DatabaseFavouriteMovies(){}

    public static final int VERSION = 2;

    @Table(ColumnsFavouriteMovies.class) public static final String FAVOURITE_MOVIES = "favouriteMovies";
}
