package com.example.android.sunshine.app.wearable;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by fares on 11.02.16.
 */
public class UtilWearable {

    private static final String WEATHER = "/today-weather";
    private static final String DATA_ITEM_HIGH = "high";
    private static final String DATA_ITEM_LOW = "low";
    private static final String DATA_ITEM_IMAGE = "image";


    private GoogleApiClient mGoogleApiClient;
    private Context mContext;

    public UtilWearable(Context context) {
        mContext = context;
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Wearable.API)
                .build();

    }

    public void sendWeatherInfo(Double tempHigh, Double tempLow, Bitmap bitmap){
        if (bitmap != null && mGoogleApiClient.isConnected())
            sendDataItems(tempHigh, tempLow, toAsset(bitmap));
    }

    /**
    * Sends weather data info to wearable app.
     * @param tempHigh - string with formatted max temp in day.
     * @param tempLow - string with formatted min temp in day.
     * @param asset - asset which contains image to send.
     */
    private void sendDataItems(Double tempHigh, Double tempLow, Asset asset){
        if (mGoogleApiClient == null)
            return;

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(WEATHER);

        putDataMapRequest.getDataMap().putDouble(DATA_ITEM_HIGH, tempHigh);
        putDataMapRequest.getDataMap().putDouble(DATA_ITEM_LOW, tempLow);
        putDataMapRequest.getDataMap().putAsset(DATA_ITEM_IMAGE, asset);

        mGoogleApiClient.connect();
        if (!mGoogleApiClient.isConnected())
            return;

        Wearable.DataApi.putDataItem(mGoogleApiClient, putDataMapRequest.asPutDataRequest());
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

}
