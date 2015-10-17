package com.example.popularmovies.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.models.Movie;
import com.example.popularmovies.utils.UtilMoviesApi;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fares on 7/27/15.
 */
public class AdapterMovies extends BaseAdapter {

    private Context mContext;
    private List<Movie> movies;


    public AdapterMovies(Context mContext, List<Movie> movies) {
        this.mContext = mContext;
        if (movies != null)
            this.movies = movies;
        else
            this.movies = new LinkedList<>();
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_movies_grid, parent, false);

            holder = new Holder();
            holder.mMoviePoster = (ImageView) convertView.findViewById(R.id.listItemMoviesGridImage);
            holder.mTitle       = (TextView) convertView.findViewById(R.id.listItemMoviesGridImageTitle);

            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }

        holder.mTitle.setText(movies.get(position).getmOriginTitle());

        Picasso.with(mContext)
                .load(UtilMoviesApi.URL_POSTER + movies.get(position).getmPosterPath())
                .into(holder.mMoviePoster);

        return convertView;
    }

    private class Holder {
        ImageView mMoviePoster;
        TextView mTitle;
    }
}
