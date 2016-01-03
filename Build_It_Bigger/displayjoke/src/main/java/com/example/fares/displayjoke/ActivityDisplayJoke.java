package com.example.fares.displayjoke;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by fares on 03.01.16.
 */
public class ActivityDisplayJoke extends AppCompatActivity {

    public static final String JOKE_EXTRAS = "com.example.fares.displayjoke.intent_extras";
    private String[] mJokes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joke);

        if (getIntent() != null && getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            mJokes = bundle.getStringArray(JOKE_EXTRAS);

            if (mJokes == null || mJokes.length <= 0)
                ((TextView) findViewById(R.id.textViewJoke)).setText(getString(R.string.default_joke_info));
            else
                ((TextView) findViewById(R.id.textViewJoke)).setText(mJokes[0]);
        }
    }
}