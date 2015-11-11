package com.example.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import com.example.popularmovies.R;
import com.example.popularmovies.fragments.FragmentMovieDetail;
import com.example.popularmovies.fragments.FragmentPopularMovies;
import com.example.popularmovies.fragments.interfaces.CallbackFragmentMovieDetails;
import com.example.popularmovies.models.Movie;
import com.example.popularmovies.models.Review;
import com.example.popularmovies.models.Trailer;
import com.example.popularmovies.utils.UtilDB;
import com.example.popularmovies.utils.UtilFragment;
import com.example.popularmovies.utils.UtilMoviesApi;
import com.example.popularmovies.utils.UtilParser;
import com.example.popularmovies.utils.Utilities;
import com.google.gson.JsonParseException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fares on 10.11.15.
 */
public class ActivityDetail extends AppCompatActivity
        implements CallbackFragmentMovieDetails, UtilMoviesApi.PopularMovieDetails {

    private static final String LOG_DEBUG = ActivityDetail.class.getSimpleName();
    private static int activeFragment = -1;

    private Toolbar toolbar;
    private ImageView collapsingToolbarImage;
    private boolean collapsingToolbarImageVisible;

    private ActivityMain.SelectedMovieChange mSelectedMovieChange;
    private Movie selectedMovie;
    private List<Review> mSelectedMovieReviews;
    private List<Trailer> mSelectedMovieTrailers;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpToolbar();

        Intent intent = getIntent();
        if (intent != null){
            selectedMovie = intent.getExtras().getParcelable(Utilities.INTENT_MOVIE_KEY);
        }
        openFragment(UtilFragment.FRAGMENT_MOVIE_DETAIL);
    }

    public void openFragment(int fragmentType) {
        // start transaction when want to display different fragment then current
        if (!(activeFragment == fragmentType)) {
            activeFragment = fragmentType;
            UtilFragment.setCollapsingToolbarBehaviour(this, false, activeFragment);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment f = getFragment(fragmentType);
            fragmentTransaction.replace(R.id.mainScreenFragmentContainer, f);

            fragmentTransaction.commit();
        }
    }

    @Nullable
    private Fragment getFragment(int fragmentType) {
        Fragment frag;
        switch (fragmentType) {
            case UtilFragment.FRAGMENT_MOVIE_DETAIL:
                UtilFragment.toolbarImageShow(this, selectedMovie, collapsingToolbarImage, toolbar);
                frag = FragmentMovieDetail.newInstance(this, selectedMovie, mSelectedMovieReviews, mSelectedMovieTrailers);
                mSelectedMovieChange = (FragmentMovieDetail) frag;
                return frag;
            default:
                Log.d("test", "I cant find fragment to open");
                break;
        }
        return null;
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarImage = (ImageView) findViewById(R.id.collapsingToolbarImage);
        toolbar.setLogo(R.mipmap.app_logo);
        toolbar.setTitle(getString(R.string.toolbarMainText));
        toolbar.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // ------------- movie details callback -------------
    @Override
    public void onTrailerClicked(String youtubeVideoID) {
        Utilities.watchYoutubeVideo(this, youtubeVideoID);
    }

    @Override
    public void onFavouriteButtonClicked(Movie movie) {
        UtilDB db = new UtilDB();
        try {
            db.insertMovieToFavourites(this, movie);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    // ------------- parser callbacks -------------
        @Override
    public void onMovieDetailsReceived(String[] jsons) {
        try {
            if (mSelectedMovieReviews != null) {
                mSelectedMovieReviews.clear();
                mSelectedMovieReviews.addAll(new UtilParser().jsonParserReviews(jsons[0]));
            }
            else {
                mSelectedMovieReviews = new UtilParser().jsonParserReviews(jsons[0]);
            }

            if (mSelectedMovieTrailers != null){
                mSelectedMovieTrailers.clear();
                mSelectedMovieTrailers.addAll(new UtilParser().jsonParserTrailers(jsons[1]));
            }
            else {
                mSelectedMovieTrailers = new UtilParser().jsonParserTrailers(jsons[1]);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        Log.d(LOG_DEBUG, "DOWNLOADED & PARSED MOVIES");
    }
}
