package com.example.popularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.activities.ActivityMain;
import com.example.popularmovies.fragments.interfaces.CallbackFragmentMovieDetails;
import com.example.popularmovies.models.Movie;
import com.example.popularmovies.models.Review;
import com.example.popularmovies.models.Trailer;
import com.example.popularmovies.utils.ItemClickSupport;
import com.example.popularmovies.utils.UtilDB;
import com.example.popularmovies.utils.UtilMoviesApi;
import com.example.popularmovies.views.adapters.AdapterReviews;
import com.example.popularmovies.views.adapters.AdapterTrailers;
import com.example.popularmovies.views.layouts.WrappingLinearLayoutManager;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class FragmentMovieDetail extends Fragment implements ActivityMain.SelectedMovieChange, ItemClickSupport.OnItemClickListener, View.OnClickListener {

    public static final String SAVED_INSTANCE_MOVIE = "movie";
    private String sortOrder;

    private Movie mMovie;
    private Intent mShareIntent;
    private List<Review> mSelectedMovieReviews;
    private List<Trailer> mSelectedMovieTrailers;

    private TextView mMovieDetailsTitle ;
    private TextView mMovieDetailsReleaseDate;
    private TextView mMovieDetailsOverview;
    private TextView mMovieDetailsVoteAverage;
    private ImageView mMovieDetailsCover;
    private ImageView mMovieDetailsFavouriteButton;
    private RecyclerView mListReviews;
    private RecyclerView mListTrailers;


    private CallbackFragmentMovieDetails mCallback;
    private ShareActionProvider mShareActionProvider;

    public static FragmentMovieDetail newInstance(CallbackFragmentMovieDetails callback, Movie selectedMovie,
                                                  List<Review> reviews, List<Trailer> trailers) {
        FragmentMovieDetail fragment = new FragmentMovieDetail();
        fragment.mMovie = selectedMovie;
        fragment.mSelectedMovieReviews = (reviews == null) ? new LinkedList<Review>() : reviews;
        fragment.mSelectedMovieTrailers = (trailers == null) ? new LinkedList<Trailer>() : trailers;
        fragment.mCallback = callback;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if(savedInstanceState != null){
            mMovie = savedInstanceState.getParcelable(SAVED_INSTANCE_MOVIE);
        }

        linkViews(layout);
        linkListeners();
        fillViewsWithData();
        setUpLists();

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_INSTANCE_MOVIE, mMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSelectedMovieChange(Movie movie, List<Review> reviews, List<Trailer> trailers, Intent shareIntent) {
        mMovie = movie;
        mShareIntent = shareIntent;
        if (mSelectedMovieReviews != null && mSelectedMovieTrailers != null && reviews != null && trailers != null) {
            mSelectedMovieReviews.addAll(reviews);
            mSelectedMovieTrailers.addAll(trailers);

            if (isViewsLinked()) {
                setUpLists();
                fillViewsWithData();


                if (mListReviews != null && mListReviews.getAdapter() != null)
                    ((AdapterReviews) mListReviews.getAdapter()).updateList(mSelectedMovieReviews);
                if (mListTrailers != null && mListTrailers.getAdapter() != null) {
                    ((AdapterTrailers) mListTrailers.getAdapter()).updateList(mSelectedMovieTrailers);

                }
            }
        }
    }



    private void setUpLists(){
        if (mListReviews != null) {
            mListReviews.setLayoutManager(new WrappingLinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
//            mListReviews.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            mListReviews.setNestedScrollingEnabled(false);
            mListReviews.setHasFixedSize(false);
            mListReviews.setAdapter(new AdapterReviews(mSelectedMovieReviews));
            ItemClickSupport.addTo(mListReviews).setOnItemClickListener(this);
        }
        if (mListTrailers != null) {
            mListTrailers.setLayoutManager(new WrappingLinearLayoutManager(getContext()));
            mListTrailers.setNestedScrollingEnabled(false);
            mListTrailers.setHasFixedSize(false);
            mListTrailers.setAdapter(new AdapterTrailers(mSelectedMovieTrailers));
            ItemClickSupport.addTo(mListTrailers).setOnItemClickListener(this);
        }
    }

    private void fillViewsWithData() {
        if (mMovie != null) {
            if (mMovie.getOriginTitle() != null)
                mMovieDetailsTitle.setText(mMovie.getOriginTitle());
            if (mMovie.getReleaseDate() != null)
                mMovieDetailsReleaseDate.setText(mMovie.getReleaseDate());
            if (mMovie.getOverview() != null)
                mMovieDetailsOverview.setText(mMovie.getOverview());
            if (mMovieDetailsCover != null && mMovie.getPosterPath() != null)
                Picasso.with(getActivity())
                        .load(UtilMoviesApi.URL_POSTER + mMovie.getPosterPath())
                        .into(mMovieDetailsCover);


            mMovieDetailsVoteAverage.setText(String.valueOf(mMovie.getVoteAverage()));
        }
    }

    private void linkListeners() {
        mMovieDetailsFavouriteButton.setOnClickListener(this);
    }

    private void linkViews(View layout) {
        mMovieDetailsTitle          = (TextView) layout.findViewById(R.id.movieDetailsTitle);
        mMovieDetailsReleaseDate    = (TextView) layout.findViewById(R.id.movieDetailsReleaseDate);
        mMovieDetailsOverview       = (TextView) layout.findViewById(R.id.movieDetailsOverview);
        mMovieDetailsVoteAverage    = (TextView) layout.findViewById(R.id.movieDetailsVoteAverage);
        mMovieDetailsCover          = (ImageView) layout.findViewById(R.id.movieDetailsCover);
        mMovieDetailsFavouriteButton= (ImageView) layout.findViewById(R.id.movieDetailsFavouriteButton);
        mListReviews    = (RecyclerView) layout.findViewById(R.id.movieDetailsReviews);
        mListTrailers   = (RecyclerView) layout.findViewById(R.id.movieDetailsTrailers);

        if (mMovie != null && checkIfFavorite())
            favoriteButtonSrc(R.mipmap.app_favourite_movie_marked);
    }

    private boolean isViewsLinked(){
        return (mMovieDetailsTitle != null) && (mMovieDetailsReleaseDate != null) && (mMovieDetailsOverview != null) && (mMovieDetailsVoteAverage != null);
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
       if (recyclerView.getId() == R.id.movieDetailsReviews) {

       }
       if (recyclerView.getId() == R.id.movieDetailsTrailers) {
           mCallback.onTrailerClicked(mSelectedMovieTrailers.get(position).getKey());
       }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.movieDetailsFavouriteButton){
            if (!checkIfFavorite()) {
                favoriteButtonSrc(R.mipmap.app_favourite_movie_marked);
                mCallback.onFavouriteButtonClicked(mMovie);
            }
        }
    }

    private boolean checkIfFavorite(){
        Movie m = new UtilDB().getMovieFromDB(getActivity(), mMovie.getId());
        return m != null;
    }

    // ------- favorite button change src -------
    private void favoriteButtonSrc(int imageResourceId){
        mMovieDetailsFavouriteButton.setImageResource(imageResourceId);
    }

    // ----------- toolbar options menu ---------
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_movie_details, menu);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));
        if (!mSelectedMovieTrailers.isEmpty() && mSelectedMovieTrailers.get(0) != null )
            if (mSelectedMovieTrailers.get(0).getKey() != null)
                if (mShareActionProvider != null && mShareIntent != null){
                    setUpShareActionProvider(mShareIntent);
                }
    }

    private void setUpShareActionProvider(Intent shareIntent){
        mShareActionProvider.setShareIntent(shareIntent);
        mShareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
            @Override
            public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
                return false;
            }
        });
    }
    // ------- ------- ------- ------- ------- --
}