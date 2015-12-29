package it.jaschke.alexandria;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by fares on 12/27/15.
 */
public class Utility {
    public static boolean isConnected(@NonNull Context context){
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

    public static void showNoInternetConnectionAlert(@NonNull Context context, @NonNull String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
