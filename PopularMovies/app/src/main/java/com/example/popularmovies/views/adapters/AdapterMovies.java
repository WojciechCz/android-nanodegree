package com.example.popularmovies.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.models.Movie;
import com.example.popularmovies.utils.UtilMoviesApi;
import com.example.popularmovies.views.adapters.callbacks.AdapterMoviesListener;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fares on 7/27/15.
 */
public class AdapterMovies extends RecyclerView.Adapter<AdapterMovies.Holder> {

    private Context mContext;
    private List<Movie> movies;
    private AdapterMoviesListener mCallback;

    public AdapterMovies(Context mContext, List<Movie> movies, AdapterMoviesListener callback) {
        this.mContext = mContext;
        this.mCallback = callback;
        if (movies != null)
            this.movies = movies;
        else
            this.movies = new LinkedList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movies_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.mTitle.setText(movies.get(position).getOriginTitle());

        Picasso.with(mContext)
                .load(UtilMoviesApi.URL_POSTER + movies.get(position).getPosterPath())
                .into(holder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mMoviePoster;
        TextView mTitle;

        public Holder(View itemView) {
            super(itemView);
            mMoviePoster = (ImageView) itemView.findViewById(R.id.listItemMoviesGridImage);
            mTitle       = (TextView)  itemView.findViewById(R.id.listItemMoviesGridImageTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCallback.onItemClicked(getAdapterPosition(), v, movies.get(getAdapterPosition()));
        }
    }
}
