package com.example.android.sunshine.app.wearable;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by fares on 11.02.16.
 */
public class UtilWearable implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOGTAG = UtilWearable.class.getSimpleName();
    private static final String WEATHER = "/today-weather";
    private static final String DATA_ITEM_HIGH = "high";
    private static final String DATA_ITEM_LOW = "low";
    private static final String DATA_ITEM_IMAGE = "image";
    private static final String DATA_ITEM_TEST = "test_time";


    private GoogleApiClient mGoogleApiClient;
    private PutDataMapRequest mPutDataMapRequest;
    private Context mContext;

    public UtilWearable(Context context) {
        mContext = context;
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    public void sendWeatherInfo(Double tempHigh, Double tempLow, Bitmap bitmap){
        if (mGoogleApiClient == null)
            return;

        if (bitmap != null)
            prepareDataItem(tempHigh, tempLow, toAsset(bitmap));

        if (!tryToSendDataItems() && !mGoogleApiClient.isConnecting())
            mGoogleApiClient.connect();
    }
    /**
     * Sends weather data info to wearable app.
     * @param tempHigh - string with formatted max temp in day.
     * @param tempLow - string with formatted min temp in day.
     * @param asset - asset which contains image to send.
     */
    private void prepareDataItem(Double tempHigh, Double tempLow, Asset asset){
        mPutDataMapRequest = PutDataMapRequest.create(WEATHER);
        mPutDataMapRequest.getDataMap().putDouble(DATA_ITEM_HIGH, tempHigh);
        mPutDataMapRequest.getDataMap().putDouble(DATA_ITEM_LOW, tempLow);
        mPutDataMapRequest.getDataMap().putAsset(DATA_ITEM_IMAGE, asset);
        Log.d(LOGTAG, "Sending data at: " + System.currentTimeMillis());
        mPutDataMapRequest.getDataMap().putLong(DATA_ITEM_TEST, System.currentTimeMillis());
    }


    private boolean tryToSendDataItems(){
        boolean isConnected = mGoogleApiClient.isConnected();
        if (isConnected) {
            new Thread(() -> {
                DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mGoogleApiClient, mPutDataMapRequest.asPutDataRequest().setUrgent()).await();
                if (result.getStatus().isSuccess()) {
                    Log.v(LOGTAG, "DataMap: " + mPutDataMapRequest.asPutDataRequest() + " sent successfully to data layer ");
                }
            }).start();
        }

        return isConnected;
    }

    /**
     * Builds an {@link com.google.android.gms.wearable.Asset} from a bitmap. The image that we get
     * back from the camera in "data" is a thumbnail size. Typically, your image should not exceed
     * 320x320 and if you want to have zoom and parallax effect in your app, limit the size of your
     * image to 640x400. Resize your image before transferring to your wearable device.
     */
    private static Asset toAsset(Bitmap bitmap) {
        ByteArrayOutputStream byteStream = null;
        try {
            byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            return Asset.createFromBytes(byteStream.toByteArray());
        } finally {
            if (null != byteStream) {
                try {
                    byteStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        tryToSendDataItems();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(LOGTAG, connectionResult.toString());
    }
}
