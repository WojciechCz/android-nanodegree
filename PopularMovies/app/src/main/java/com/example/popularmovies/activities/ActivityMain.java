package com.example.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.popularmovies.fragments.FragmentMovieDetail;
import com.example.popularmovies.fragments.FragmentPopularMovies;
import com.example.popularmovies.R;
import com.example.popularmovies.models.Movie;
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
public class ActivityMain extends AppCompatActivity implements UtilMoviesApi.PopularMovies {

    public static final String LOG_DEBUG = "saarna";

    public static final String SAVED_INSTANCE_MOVIE = "movie";
    public static final String SAVED_INSTANCE_MOVIES = "movies";
    public static final String SAVED_INSTANCE_FRAGMENT = "fragment";
    public static final String SAVED_INSTANCE_ACTIVE_FRAGMENT = "active_fragment";
    // purpose: if try to open same fragment as is currently display -> do nothing
    private static int activeFragment = -1;


    public static final int FRAGMENT_POPULAR_MOVIES = 0;
    public static final int FRAGMENT_MOVIE_DETAIL   = 1;

    public static final int MOVIES_SORT_POPULAR = 1;
    public static final int MOVIES_SORT_RATED   = 2;

    private Toolbar toolbar;
    private ImageView collapsingToolbarImage;
    private boolean collapsingToolbarImageVisible;

    private List<Movie> movies;
    private Movie selectedMovie;

    private int sortOrder;

    private PopularMoviesDataSetChange mDataSetChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        // default display order
        setSortOrderFromPrefs();


        if (savedInstanceState != null) {
            activeFragment = savedInstanceState.getInt(SAVED_INSTANCE_ACTIVE_FRAGMENT);
            selectedMovie = savedInstanceState.getParcelable(SAVED_INSTANCE_MOVIE);
            if (movies == null || movies.isEmpty())
                movies = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_MOVIES);
            mDataSetChange = (FragmentPopularMovies) getSupportFragmentManager().getFragment(savedInstanceState, SAVED_INSTANCE_FRAGMENT);
            if (selectedMovie != null && activeFragment == FRAGMENT_MOVIE_DETAIL)
                toolbarImageShow();
        }
        else {
            openFragment(FRAGMENT_POPULAR_MOVIES);
            downloadMovies();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVED_INSTANCE_ACTIVE_FRAGMENT, activeFragment);
        if (activeFragment == FRAGMENT_MOVIE_DETAIL)
            outState.putParcelable(SAVED_INSTANCE_MOVIE, selectedMovie);
        outState.putParcelableArrayList(SAVED_INSTANCE_MOVIES, new ArrayList<Parcelable>(movies));
        getSupportFragmentManager().putFragment(outState, SAVED_INSTANCE_FRAGMENT, (FragmentPopularMovies)mDataSetChange);
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
                        float diff = one.getmPopularity() - two.getmPopularity();
                        if (diff > 0)
                            return 1;
                        else if (diff < 0)
                            return -1;
                        return 0;
                    }
                });
                mDataSetChange.OnPopularMoviesDataSetChange(movies);
            } else if (sortOrder == MOVIES_SORT_RATED) {
                Collections.sort(movies, new Comparator<Movie>() {
                    public int compare(Movie one, Movie two) {
                        float diff = one.getmVoteCount() - two.getmVoteCount();
                        if (diff > 0)
                            return 1;
                        else if (diff < 0)
                            return -1;
                        return 0;
                    }
                });
                mDataSetChange.OnPopularMoviesDataSetChange(movies);
            }
        }
    }

    private void toolbarImageShow() {
        if (selectedMovie != null && collapsingToolbarImage != null){
            Picasso.with(this)
                    .load(UtilMoviesApi.URL_POSTER + selectedMovie.getmPosterPath())
                    .into(collapsingToolbarImage);
            collapsingToolbarImage.setVisibility(View.VISIBLE);
            collapsingToolbarImageVisible = true;
            toolbar.setBackgroundColor(Color.TRANSPARENT);
        }
    }
    private void toolbarImageHide() {
        if (collapsingToolbarImageVisible && selectedMovie != null && collapsingToolbarImage != null){
            collapsingToolbarImage.setVisibility(View.GONE);
            collapsingToolbarImageVisible = false;
            toolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
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
        return super.onOptionsItemSelected(item);
    }

    private void downloadMovies() {
        UtilMoviesApi utilMoviesApi = new UtilMoviesApi();
        utilMoviesApi.getPopularMoviesJson(this, getString(R.string.MOVIE_API_KEY));
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarImage = (ImageView) findViewById(R.id.collapsingToolbarImage);
    }

    public void openFragment(int fragmentType) {
        // start transaction when want to display different fragment then current
        if (!(activeFragment == fragmentType)) {
            activeFragment = fragmentType;

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment f = getFragment(fragmentType);
            fragmentTransaction.replace(R.id.mainScreenFragmentContainer, f);

            fragmentTransaction.commit();
        }
    }

    private Fragment getFragment(int fragmentType) {
        Fragment frag;
        switch (fragmentType) {
            case FRAGMENT_POPULAR_MOVIES:
                if (movies == null)
                    movies = new LinkedList<>();
                frag = FragmentPopularMovies.newInstance(movies);
                mDataSetChange = (FragmentPopularMovies) frag;
                return frag;
            case FRAGMENT_MOVIE_DETAIL:
                toolbarImageShow();
                return FragmentMovieDetail.newInstance(selectedMovie);
            default:
                Log.d("test", "I cant find fragment to open");
                break;
        }
        return null;
    }

    public void setSelectedMovie(Movie movie){
        this.selectedMovie = movie;
    }

    public interface PopularMoviesDataSetChange {
        void OnPopularMoviesDataSetChange(List<Movie> movieList);
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

    @Override
    public void OnPopularMoviesJsonReceived(String json) {
        UtilParser movieParser = new UtilParser();

        try {
            movies = movieParser.jsonParserMovies(json);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        
        Log.d(LOG_DEBUG, "DOWNLOADED & PARSED MOVIES");
//        if (movies != null)
//            for (Movie m : movies)
//                Log.d(LOG_DEBUG, m.toString());

        if (movies != null && mDataSetChange != null){
            mDataSetChange.OnPopularMoviesDataSetChange(movies);
            changeSortOrderAndSort();
        }
    }
}
