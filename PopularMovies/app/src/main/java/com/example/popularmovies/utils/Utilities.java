package com.example.popularmovies.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.popularmovies.R;

/**
 * Created by fares on 11/10/15.
 */
public class Utilities {

    public static final String INTENT_MOVIE_KEY = "movieKey";

    public static void watchYoutubeVideo(Context context, String videoID){
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.youtube_app_link) + videoID)));
        }
        catch (ActivityNotFoundException e){
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.youtube_web_link) + videoID)));
        }
    }

    public static Intent createShareIntent(@NonNull String shareString){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
        return shareIntent;
    }
}
