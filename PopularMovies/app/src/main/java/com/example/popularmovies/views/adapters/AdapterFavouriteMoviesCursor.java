package com.example.popularmovies.views.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.DatabaseUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.models.db.ColumnsFavouriteMovies;
import com.example.popularmovies.utils.UtilMoviesApi;
import com.squareup.picasso.Picasso;

/**
 * Created by fares on 30.10.15.
 */
public class AdapterFavouriteMoviesCursor extends AdapterRecyclerViewCursor<AdapterFavouriteMoviesCursor.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mMoviePoster;
        private TextView mTitle;

        public ViewHolder(View view){
            super(view);
            mMoviePoster = (ImageView) view.findViewById(R.id.listItemMoviesGridImage);
            mTitle       = (TextView)  view.findViewById(R.id.listItemMoviesGridImageTitle);
        }
    }

    public AdapterFavouriteMoviesCursor(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(AdapterFavouriteMoviesCursor.ViewHolder viewHolder, Cursor cursor) {
        DatabaseUtils.dumpCursor(cursor);
        Picasso.with(mContext)
            .load(UtilMoviesApi.URL_POSTER + cursor.getString(cursor.getColumnIndex(ColumnsFavouriteMovies.POSTER)))
            .into(viewHolder.mMoviePoster);
        viewHolder.mTitle.setText(cursor.getString(cursor.getColumnIndex(ColumnsFavouriteMovies.NAME)));

    }

    @Override
    public AdapterFavouriteMoviesCursor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favourite_movies, parent, false);
        return new ViewHolder(itemView);
    }
}
