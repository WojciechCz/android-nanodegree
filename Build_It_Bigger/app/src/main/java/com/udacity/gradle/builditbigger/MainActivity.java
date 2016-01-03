package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fares.displayjoke.ActivityDisplayJoke;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements MainActivityFragment.MainFragmentCallback, EndpointTask.EndpointTaskCallback {

    private List<String> mJokes;
    private UpdateViewMainFragment mUpdateViewMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onAttachFragment(Fragment fragment) throws ClassCastException {
        mUpdateViewMainFragment = (UpdateViewMainFragment) fragment;
        super.onAttachFragment(fragment);
    }

    @Override
    public void onDownloadJokesRequest(ProgressBar progressBar) {
        mUpdateViewMainFragment.onDownloadStart();
        new EndpointTask(this, this, progressBar).execute();
    }

    @Override
    public void onJokesDownloaded(List<String> jokes) {
        mUpdateViewMainFragment.onDownloadEnd();
        this.mJokes = jokes;
        startActivityDisplayJoke(jokes);
    }

    private void startActivityDisplayJoke(List<String> jokes){
        Intent showJoke = new Intent(this, ActivityDisplayJoke.class);
        showJoke.putExtra(ActivityDisplayJoke.JOKE_EXTRAS, jokes.toArray(new String[jokes.size()]));
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
