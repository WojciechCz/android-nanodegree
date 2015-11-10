package com.example.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.popularmovies.models.Movie;
import com.example.popularmovies.utils.UtilFragment;

import java.util.LinkedList;

/**
 * Created by fares on 10.11.15.
 */
public class ActivityDetail extends AppCompatActivity {

    private static int activeFragment = -1;

    private Movie selectedMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null){
            intent.getData().get
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
