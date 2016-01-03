package com.udacity.gradle.builditbigger;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by fares on 03.01.16.
 */
public class Utilities {

    public static boolean isConnectedToInternet(@NonNull Context context){
        boolean isConnected;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            isConnected = isConnectedV23(context);
        else
            isConnected = isConnected(context);

        return isConnected;
    }

    @SuppressWarnings("deprecation")
    private static boolean isConnected(@NonNull Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static boolean isConnectedV23(@NonNull Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = connectivity.getAllNetworks();
        NetworkInfo networkInfo;
        for (Network mNetwork : networks) {
            networkInfo = connectivity.getNetworkInfo(mNetwork);
            if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                return true;
            }
        }
        return false;
    }

    public static void showNoInternetConnectionAlert(@NonNull Context context, @NonNull String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
