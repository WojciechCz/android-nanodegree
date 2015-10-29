package com.example.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.activities.ActivityMain;
import com.example.popularmovies.fragments.interfaces.CallbackFragmentMovieDetails;
import com.example.popularmovies.models.Movie;
import com.example.popularmovies.models.Review;
import com.example.popularmovies.models.Trailer;
import com.example.popularmovies.utils.ItemClickSupport;
import com.example.popularmovies.views.adapters.AdapterReviews;
import com.example.popularmovies.views.adapters.AdapterTrailers;
import com.example.popularmovies.views.layouts.WrappingLinearLayoutManager;

import java.util.LinkedList;
import java.util.List;

public class FragmentMovieDetail extends Fragment implements ActivityMain.SelectedMovieChange, ItemClickSupport.OnItemClickListener {

    public static final String SAVED_INSTANCE_MOVIE = "movie";
    private String sortOrder;

    private Movie selectedMovie;
    private List<Review> mSelectedMovieReviews;
    private List<Trailer> mSelectedMovieTrailers;

    private TextView mMovieDetailsTitle ;
    private TextView mMovieDetailsReleaseDate;
    private TextView mMovieDetailsOverview;
    private TextView mMovieDetailsVoteAverage;
    private RecyclerView mListReviews;
    private RecyclerView mListTrailers;

    private CallbackFragmentMovieDetails mCallback;

    public static FragmentMovieDetail newInstance(Movie selectedMovie, CallbackFragmentMovieDetails callback) {
        FragmentMovieDetail fragment = new FragmentMovieDetail();
        fragment.selectedMovie = selectedMovie;
        fragment.mSelectedMovieReviews = new LinkedList<>();
        fragment.mSelectedMovieTrailers = new LinkedList<>();
        fragment.mCallback = callback;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if(savedInstanceState != null){
            selectedMovie = savedInstanceState.getParcelable(SAVED_INSTANCE_MOVIE);
        }

        linkViews(layout);
        fillViewsWithData();
        setUpLists();

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_INSTANCE_MOVIE, selectedMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSelectedMovieChange(Movie movie, List<Review> reviews, List<Trailer> trailers) {
        selectedMovie = movie;
        if (mSelectedMovieReviews != null && mSelectedMovieTrailers != null && reviews != null && trailers != null) {
            mSelectedMovieReviews.addAll(reviews);
            mSelectedMovieTrailers.addAll(trailers);
            if (isViewsLinked()) {
                setUpLists();
                fillViewsWithData();


                if (mListReviews != null && mListReviews.getAdapter() != null)
                    ((AdapterReviews) mListReviews.getAdapter()).updateList(mSelectedMovieReviews);
                if (mListTrailers != null && mListTrailers.getAdapter() != null)
                    ((AdapterTrailers) mListTrailers.getAdapter()).updateList(mSelectedMovieTrailers);
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
        if (selectedMovie != null) {
            if (selectedMovie.getmOriginTitle() != null)
                mMovieDetailsTitle.setText(selectedMovie.getmOriginTitle());
            if (selectedMovie.getmReleaseDate() != null)
                mMovieDetailsReleaseDate.setText(selectedMovie.getmReleaseDate());
            if (selectedMovie.getmOverview() != null)
                mMovieDetailsOverview.setText(selectedMovie.getmOverview());

            mMovieDetailsVoteAverage.setText(String.valueOf(selectedMovie.getmVoteAverage()));
        }
    }

    private void linkViews(View layout) {
        mMovieDetailsTitle          = (TextView) layout.findViewById(R.id.movieDetailsTitle);
        mMovieDetailsReleaseDate    = (TextView) layout.findViewById(R.id.movieDetailsReleaseDate);
        mMovieDetailsOverview       = (TextView) layout.findViewById(R.id.movieDetailsOverview);
        mMovieDetailsVoteAverage    = (TextView) layout.findViewById(R.id.movieDetailsVoteAverage);
        mListReviews    = (RecyclerView) layout.findViewById(R.id.movieDetailsReviews);
        mListTrailers   = (RecyclerView) layout.findViewById(R.id.movieDetailsTrailers);
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
}
