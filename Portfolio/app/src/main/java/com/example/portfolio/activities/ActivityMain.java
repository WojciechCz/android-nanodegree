package com.example.portfolio.activities;

import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.portfolio.R;
import com.example.portfolio.fragments.FragmentMain;


public class ActivityMain extends AppCompatActivity {

    // purpose: if try to open same fragment as is currently display -> do nothing
    private static int activeFragment = -1;
    private static final int FRAGMENT_MAIN = 0;

    // view's
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        openFragment(FRAGMENT_MAIN);
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void openFragment(int fragmentType) {

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
        switch (fragmentType) {
            case FRAGMENT_MAIN:
                return FragmentMain.newInstance();
            default:
                Log.d("test", "I cant find fragment to open");
                break;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if (activeFragment == FRAGMENT_MAIN) {
            finish();
        }
        else {
            // return to main fragment
            openFragment(FRAGMENT_MAIN);
        }
    }
}
