package com.example.popularmovies.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.example.popularmovies.fragments.FragmentMovieDetail;
import com.example.popularmovies.fragments.FragmentPopularMovies;
import com.example.popularmovies.R;
import com.example.popularmovies.fragments.interfaces.CallbackFragmentMovieDetails;
import com.example.popularmovies.fragments.interfaces.CallbackFragmentPopularMovies;
import com.example.popularmovies.models.Movie;
import com.example.popularmovies.models.Review;
import com.example.popularmovies.models.Trailer;
import com.example.popularmovies.models.db.ProviderFavouriteMovies;
import com.example.popularmovies.utils.UtilDB;
import com.example.popularmovies.utils.UtilMoviesApi;
import com.example.popularmovies.utils.UtilParser;
import com.google.gson.JsonParseException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fares on 07.06.15.
 */
public class ActivityMain extends AppCompatActivity implements
        UtilMoviesApi.PopularMovies, CallbackFragmentPopularMovies, CallbackFragmentMovieDetails {

    public static final String LOG_DEBUG = "saarna";

    public static final String SAVED_INSTANCE_MOVIE = "movie";
    public static final String SAVED_INSTANCE_MOVIES = "movies";
    public static final String SAVED_INSTANCE_FRAGMENT = "fragment";
    public static final String SAVED_INSTANCE_ACTIVE_FRAGMENT = "active_fragment";
    // purpose: if try to open same fragment as is currently display -> do nothing
    private static int activeFragment = -1;
    private Fragment currentFratgment;


    public static final int FRAGMENT_POPULAR_MOVIES = 0;
    public static final int FRAGMENT_MOVIE_DETAIL   = 1;

    public static final int MOVIES_SORT_POPULAR = 1;
    public static final int MOVIES_SORT_RATED   = 2;

    private Toolbar toolbar;
    private ImageView collapsingToolbarImage;
    private boolean collapsingToolbarImageVisible;
    private boolean mTwoPane;

    private List<Movie> movies;
    private Movie selectedMovie;
    private List<Review> mSelectedMovieReviews;
    private List<Trailer> mSelectedMovieTrailers;

    private int sortOrder;
    private boolean mIsDisplayingFavorite = false;

    private ShareActionProvider mShareActionProvider;



    private PopularMoviesDataSetChange mDataSetChange;
    private SelectedMovieChange mSelectedMovieChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        // default display order
        setSortOrderFromPrefs();

        FragmentPopularMovies fpm = (FragmentPopularMovies) getSupportFragmentManager().findFragmentById(R.id.mainScreenFragmentPopularMovies);

        // show TwoPane layout if there is popular movies fragment present
        if (fpm != null) {
            mTwoPane = true;

            if (savedInstanceState != null) {
                activeFragment = savedInstanceState.getInt(SAVED_INSTANCE_ACTIVE_FRAGMENT);
                selectedMovie = savedInstanceState.getParcelable(SAVED_INSTANCE_MOVIE);
                if (movies == null || movies.isEmpty())
                    movies = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_MOVIES);
                if (selectedMovie != null && activeFragment == FRAGMENT_MOVIE_DETAIL)
                    toolbarImageShow();
            }
            else {
                fpm.setCallback(this);
                // set callback popular movies and start download movies
                mDataSetChange = fpm;
                // open detail
                openFragment(FRAGMENT_MOVIE_DETAIL);
                downloadMovies();
            }
        }
        // one pane layout
        else {
            mTwoPane = false;
            openFragment(FRAGMENT_POPULAR_MOVIES);
            downloadMovies();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (movies == null || movies.isEmpty())
            downloadMovies();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVED_INSTANCE_ACTIVE_FRAGMENT, activeFragment);
        if (activeFragment == FRAGMENT_MOVIE_DETAIL)
            outState.putParcelable(SAVED_INSTANCE_MOVIE, selectedMovie);
        outState.putParcelableArrayList(SAVED_INSTANCE_MOVIES, new ArrayList<Parcelable>(movies));
//        getSupportFragmentManager().putFragment(outState, SAVED_INSTANCE_FRAGMENT, (FragmentPopularMovies)mDataSetChange);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( (movies == null || movies.isEmpty()) && !isNetworkAvailable()){
            Toast.makeText(this, "No internet connection !", Toast.LENGTH_LONG).show();
        }
        setSortOrderFromPrefs();
        changeSortOrderAndSort();
    }

    // ------------- Fragment callbacks ------------------
    @Override
    public void onMovieClicked(Movie m) {
        selectedMovie = m;
        if (mIsDisplayingFavorite)
            updateMovieDetails();
        else
            downloadMovieDetails(String.valueOf(m.getId()));

    }
    @Override
    public void onTrailerClicked(String youtubeVideoID) {
        watchYoutubeVideo(youtubeVideoID);
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
    @Override
    public void registerShareProvider(ShareActionProvider provider){
        mShareActionProvider = provider;
    }
    // ------------- ------------------ -------------
    public void watchYoutubeVideo(String videoID){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoID)));
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoID)));
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setSortOrderFromPrefs(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortingOrder = prefs.getString(getResources().getString(R.string.sorting_order_key), "");
        if (sortingOrder != null) {
            if (sortingOrder.equals(getResources().getStringArray(R.array.entries_values_sorting_order)[0])) {
                sortOrder = MOVIES_SORT_POPULAR;
            }
            else if (sortingOrder.equals(getResources().getStringArray(R.array.entries_values_sorting_order)[1])) {
                sortOrder = MOVIES_SORT_RATED;
            }
        }
    }

    private void changeSortOrderAndSort() {
        if (movies != null && mDataSetChange != null) {
            if (sortOrder == MOVIES_SORT_POPULAR) {
                Collections.sort(movies, new Comparator<Movie>() {
                    public int compare(Movie one, Movie two) {
                        float diff = one.getPopularity() - two.getPopularity();
                        if (diff > 0)
                            return 1;
                        else if (diff < 0)
                            return -1;
                        return 0;
                    }
                });
                mDataSetChange.onPopularMoviesDataSetChange(movies);
            } else if (sortOrder == MOVIES_SORT_RATED) {
                Collections.sort(movies, new Comparator<Movie>() {
                    public int compare(Movie one, Movie two) {
                        float diff = one.getVoteCount() - two.getVoteCount();
                        if (diff > 0)
                            return 1;
                        else if (diff < 0)
                            return -1;
                        return 0;
                    }
                });
                mDataSetChange.onPopularMoviesDataSetChange(movies);
            }
        }
    }

    private void toolbarImageShow() {
        if (selectedMovie != null && collapsingToolbarImage != null){
            Picasso.with(this)
                    .load(UtilMoviesApi.URL_POSTER + selectedMovie.getPosterPath())
                    .into(collapsingToolbarImage);
            collapsingToolbarImage.setVisibility(View.VISIBLE);
            collapsingToolbarImageVisible = true;
            toolbar.setBackgroundColor(Color.TRANSPARENT);
            toolbar.setLogo(android.R.color.transparent);
        }
    }
    private void toolbarImageHide() {
        if (collapsingToolbarImageVisible && selectedMovie != null && collapsingToolbarImage != null){
            collapsingToolbarImage.setVisibility(View.GONE);
            collapsingToolbarImageVisible = false;
            toolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
            toolbar.setLogo(R.mipmap.app_logo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, ActivitySettings.class));
            return true;
        }
        else if (id == R.id.action_see_favorites) {
            mIsDisplayingFavorite = !mIsDisplayingFavorite;
            if (mIsDisplayingFavorite) {
                showFavoritesMovies();
                item.setTitle(getString(R.string.title_activity_popular));
            }
            else {
                showPopularMovies();
                item.setTitle(getString(R.string.title_activity_favorites));
            }
            return true;
        }
        else if (id == R.id.action_share) {

        }
        return super.onOptionsItemSelected(item);
    }

    private void showFavoritesMovies(){
        mDataSetChange.onFavoriteMoviesRequested();
    }

    private void showPopularMovies(){
        mDataSetChange.onPopularMoviesRequest();
    }

    private void downloadMovies() {
        new UtilMoviesApi().getPopularMoviesJson(this, getString(R.string.MOVIE_API_KEY));
    }
    private void downloadMovieDetails(String movieId) {
        new UtilMoviesApi().getDetailsJson(this, getString(R.string.MOVIE_API_KEY), movieId);
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarImage = (ImageView) findViewById(R.id.collapsingToolbarImage);
        toolbar.setLogo(R.mipmap.app_logo);
        toolbar.setTitle(getString(R.string.toolbarMainText));
        toolbar.invalidate();
    }

    private void forceOpenFragment(int fragId){
        activeFragment = -1;
        openFragment(fragId);
    }

    public void openFragment(int fragmentType) {
        // start transaction when want to display different fragment then current
        if (!(activeFragment == fragmentType)) {
            activeFragment = fragmentType;
            setCollapsingToolbarBehaviour();

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
            case FRAGMENT_POPULAR_MOVIES:
                if (movies == null)
                    movies = new LinkedList<>();
                frag = FragmentPopularMovies.newInstance(movies, this);
                mDataSetChange = (FragmentPopularMovies) frag;
                return frag;
            case FRAGMENT_MOVIE_DETAIL:
                toolbarImageShow();
                frag = FragmentMovieDetail.newInstance(this, selectedMovie, mSelectedMovieReviews, mSelectedMovieTrailers);
                mSelectedMovieChange = (FragmentMovieDetail) frag;
                return frag;
            default:
                Log.d("test", "I cant find fragment to open");
                break;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if (activeFragment == FRAGMENT_POPULAR_MOVIES) {
            finish();
        }
        else {
            if (collapsingToolbarImageVisible){
                toolbarImageHide();
            }
            // return to main fragment
            openFragment(FRAGMENT_POPULAR_MOVIES);
        }
    }

    public void setCollapsingToolbarBehaviour(){
        if (!mTwoPane) {
            if (activeFragment == FRAGMENT_MOVIE_DETAIL)
                ((AppBarLayout) findViewById(R.id.appBarLayout)).setExpanded(true);
            if (activeFragment == FRAGMENT_POPULAR_MOVIES)
                ((AppBarLayout) findViewById(R.id.appBarLayout)).setExpanded(false);
        }
    }

    // ---------------- HTTP REQUEST RESULT ----------------
    @Override
    public void onPopularMoviesJsonReceived(String json) {
        UtilParser movieParser = new UtilParser();

        try {
            movies = movieParser.jsonParserMovies(json);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        Log.d(LOG_DEBUG, "DOWNLOADED & PARSED MOVIES");

        updateData();
    }

    @Override
    public void onMovieDetailsReceived(String[] jsons) {
        try {
            mSelectedMovieReviews = new UtilParser().jsonParserReviews(jsons[0]);
            mSelectedMovieTrailers = new UtilParser().jsonParserTrailers(jsons[1]);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        Log.d(LOG_DEBUG, "DOWNLOADED & PARSED MOVIES");
        if (!mTwoPane)
            openFragment(FRAGMENT_MOVIE_DETAIL);
        updateMovieDetails();
    }
    // ---------------- HTTP REQUEST RESULT ----------------

    private void updateData(){
        if (movies != null && mDataSetChange != null){

            // for movie detail in tablet layout
            if (mTwoPane){
                updateMovieDetails();
            }
            // for movie list
            mDataSetChange.onPopularMoviesDataSetChange(movies);
            changeSortOrderAndSort();
        }
    }

    private void updateMovieDetails(){
        if (movies != null && !movies.isEmpty()) {
            if (selectedMovie == null)
                selectedMovie = movies.get(0);
            if (mSelectedMovieChange != null)
                mSelectedMovieChange.onSelectedMovieChange(selectedMovie, mSelectedMovieReviews, mSelectedMovieTrailers);
        }
        forceOpenFragment(FRAGMENT_MOVIE_DETAIL);
    }

    public interface PopularMoviesDataSetChange {
        void onPopularMoviesDataSetChange(List<Movie> movieList);
        void onFavoriteMoviesRequested();
        void onPopularMoviesRequest();
    }
    public interface SelectedMovieChange {
        void onSelectedMovieChange(Movie movie, List<Review> reviews, List<Trailer> trailers);
    }
}
