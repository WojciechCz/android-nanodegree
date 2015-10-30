package com.example.popularmovies.models.db;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by fares on 30.10.15.
 */
public interface ColumnsFavouriteMovies {
    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(DataType.Type.INTEGER) @NotNull String KEY = "key";
    @DataType(DataType.Type.TEXT) @NotNull    String NAME = "name";
    @DataType(DataType.Type.TEXT) @NotNull    String POSTER = "poster";
}
