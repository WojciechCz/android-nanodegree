package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.fares.displayjoke.ActivityDisplayJoke;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements MainActivityFragment.MainFragmentCallback, EndpointTask.EndpointTaskCallback {

    private List<String> mJokes;
    private UpdateViewMainFragment mUpdateViewMainFragment;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                startActivityDisplayJoke();
            }
        });

        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(BuildConfig.TEST_DEVICE_ID)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private void showInterstitialAd(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            startActivityDisplayJoke();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) throws ClassCastException {
        mUpdateViewMainFragment = (UpdateViewMainFragment) fragment;
        super.onAttachFragment(fragment);
    }

    @Override
    public void onDownloadJokesRequest(ProgressBar progressBar) {
        if (Utilities.isConnectedToInternet(this)){
            mUpdateViewMainFragment.onDownloadStart();
            new EndpointTask(this, this, progressBar).execute();
        }
        else {
            Utilities.showNoInternetConnectionAlert(this, getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onJokesDownloaded(List<String> jokes) {
        mUpdateViewMainFragment.onDownloadEnd();
        this.mJokes = jokes;
        showInterstitialAd();
    }

    private void startActivityDisplayJoke(){
        Intent showJoke = new Intent(this, ActivityDisplayJoke.class);
        showJoke.putExtra(ActivityDisplayJoke.JOKE_EXTRAS, mJokes.toArray(new String[mJokes.size()]));
        startActivity(showJoke);
    }

    // ---------------- fragMain ------------------



    // ---------------- menu ------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
