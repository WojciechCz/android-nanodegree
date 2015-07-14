package com.example.portfolio.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.portfolio.R;

public class FragmentMain extends Fragment implements View.OnClickListener {

    private Button mSpotifyStreamer;
    private Button mScoresApp;
    private Button mLibraryApp;
    private Button mBuildItBigger;
    private Button mXyzReader;
    private Button mCapstone;

    public static FragmentMain newInstance() {
        FragmentMain fragment = new FragmentMain();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);

        setUpButtons(layout);

        return layout;
    }

    private void setUpButtons(View layout) {
        linkedViews(layout);
        setOnClick();
    }

    private void setOnClick() {
        if (mSpotifyStreamer!=null)
            mSpotifyStreamer.setOnClickListener(this);
        if (mScoresApp!=null)
            mScoresApp.setOnClickListener(this);
        if (mLibraryApp!=null)
            mLibraryApp.setOnClickListener(this);
        if (mBuildItBigger!=null)
            mBuildItBigger.setOnClickListener(this);
        if (mXyzReader!=null)
            mXyzReader.setOnClickListener(this);
        if (mCapstone!=null)
            mCapstone.setOnClickListener(this);
    }

    private void linkedViews(View layout) {
        mSpotifyStreamer= (Button) layout.findViewById(R.id.mainButtonSpotifyStreamer);
        mScoresApp      = (Button) layout.findViewById(R.id.mainButtonScoresApp);
        mLibraryApp     = (Button) layout.findViewById(R.id.mainButtonLibraryApp);
        mBuildItBigger  = (Button) layout.findViewById(R.id.mainButtonBuildItBigger);
        mXyzReader      = (Button) layout.findViewById(R.id.mainButtonXyzReader);
        mCapstone       = (Button) layout.findViewById(R.id.mainButtonCapstone);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String toDisplay = getActivity().getResources().getString(R.string.onclick_toast);
        if (id == R.id.mainButtonSpotifyStreamer){
            toDisplay+= getActivity().getResources().getString(R.string.main_button_spotify_streamer);
            makeToast(toDisplay);
        }
        else if (id == R.id.mainButtonScoresApp){
            toDisplay+= getActivity().getResources().getString(R.string.main_button_scores_app);
            makeToast(toDisplay);
        }
        else if (id == R.id.mainButtonLibraryApp){
            toDisplay+= getActivity().getResources().getString(R.string.main_button_library_app);
            makeToast(toDisplay);
        }
        else if (id == R.id.mainButtonBuildItBigger){
            toDisplay+= getActivity().getResources().getString(R.string.main_button_build_it_bigger);
            makeToast(toDisplay);
        }
        else if (id == R.id.mainButtonXyzReader){
            toDisplay+= getActivity().getResources().getString(R.string.main_button_xyz_reader);
            makeToast(toDisplay);
        }
        else if (id == R.id.mainButtonCapstone){
            toDisplay+= getActivity().getResources().getString(R.string.main_button_capstone_my_own_app);
            makeToast(toDisplay);
        }
    }

    private void makeToast(@NonNull String toDisplay) {
        Toast.makeText(getActivity(), toDisplay, Toast.LENGTH_SHORT).show();
    }
}
