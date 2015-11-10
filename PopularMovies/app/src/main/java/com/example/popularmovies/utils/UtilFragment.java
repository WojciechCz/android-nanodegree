package com.example.popularmovies.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.popularmovies.R;
import com.example.popularmovies.fragments.FragmentMovieDetail;
import com.example.popularmovies.fragments.FragmentPopularMovies;
import com.example.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

/**
 * Created by fares on 10.11.15.
 */
public class UtilFragment {

    public static final int FRAGMENT_POPULAR_MOVIES = 0;
    public static final int FRAGMENT_MOVIE_DETAIL   = 1;
//
//    public static void openFragment(Activity context, boolean isTwoPane, int activeFragment, int fragmentType, FragmentManager fragmentManager) {
//        // start transaction when want to display different fragment then current
//        if (!(activeFragment == fragmentType)) {
//            activeFragment = fragmentType;
//            setCollapsingToolbarBehaviour(context, isTwoPane, activeFragment);
//
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//            Fragment f = getFragment(fragmentType);
//            fragmentTransaction.replace(R.id.mainScreenFragmentContainer, f);
//
//            fragmentTransaction.commit();
//        }
//    }
//
//    @Nullable
//    private Fragment getFragment(int fragmentType) {
//        Fragment frag;
//        switch (fragmentType) {
//            case FRAGMENT_POPULAR_MOVIES:
//                if (movies == null)
//                    movies = new LinkedList<>();
//                frag = FragmentPopularMovies.newInstance(movies, this);
//                mDataSetChange = (FragmentPopularMovies) frag;
//                return frag;
//            case FRAGMENT_MOVIE_DETAIL:
//                toolbarImageShow();
//                frag = FragmentMovieDetail.newInstance(this, selectedMovie, mSelectedMovieReviews, mSelectedMovieTrailers);
//                mSelectedMovieChange = (FragmentMovieDetail) frag;
//                return frag;
//            default:
//                Log.d("test", "I cant find fragment to open");
//                break;
//        }
//        return null;
//    }

    public static boolean toolbarImageShow(Context context, Movie selectedMovie, ImageView collapsingToolbarImage, Toolbar toolbar) {
        if (selectedMovie != null && collapsingToolbarImage != null){
            Picasso.with(context)
                    .load(UtilMoviesApi.URL_POSTER + selectedMovie.getPosterPath())
                    .into(collapsingToolbarImage);
            collapsingToolbarImage.setVisibility(View.VISIBLE);
            toolbar.setBackgroundColor(Color.TRANSPARENT);
            toolbar.setLogo(android.R.color.transparent);
        }
        return true;
    }

    public static boolean toolbarImageHide(Movie selectedMovie, ImageView collapsingToolbarImage,
                                     Toolbar toolbar, boolean collapsingToolbarImageVisible, int color) {
        if (collapsingToolbarImageVisible && selectedMovie != null && collapsingToolbarImage != null){
            collapsingToolbarImage.setVisibility(View.GONE);
            toolbar.setBackgroundColor(color);
            toolbar.setLogo(R.mipmap.app_logo);
        }
        return true;
    }

    public static void setCollapsingToolbarBehaviour(Activity context, boolean isTwoPane, int activeFragment){
        if (!isTwoPane) {
            if (activeFragment == FRAGMENT_MOVIE_DETAIL)
                ((AppBarLayout) context.findViewById(R.id.appBarLayout)).setExpanded(true);
            if (activeFragment == FRAGMENT_POPULAR_MOVIES)
                ((AppBarLayout) context.findViewById(R.id.appBarLayout)).setExpanded(false);
        }
    }
}
