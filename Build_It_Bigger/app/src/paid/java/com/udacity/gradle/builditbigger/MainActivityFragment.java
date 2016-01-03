package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements UpdateViewMainFragment {

    private MainFragmentCallback mMainFragmentCallback;

    @Bind(R.id.progressBar) ProgressBar mProgressBar;
    @Bind(R.id.tellJoke)    Button mTellJoke;

    @Override
    public void onAttach(Context context) throws ClassCastException {
        super.onAttach(context);
        mMainFragmentCallback = (MainFragmentCallback) context;
    }

    public static MainActivityFragment getInstance(MainFragmentCallback mainFragmentCallback){
        MainActivityFragment f = new MainActivityFragment();
        f.mMainFragmentCallback = mainFragmentCallback;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, root);

        return root;
    }

    @OnClick(R.id.tellJoke)
    public void onClickShowJoke(Button button){
        if (mProgressBar != null && mMainFragmentCallback != null){
            mMainFragmentCallback.onDownloadJokesRequest(mProgressBar);
        }
    }

    @Override
    public void onDownloadStart() {
        mTellJoke.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDownloadEnd() {
        mTellJoke.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    public interface MainFragmentCallback {
        void onDownloadJokesRequest(ProgressBar progressBar);
    }
}
