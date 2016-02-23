package com.example.android.sunshine.wearableapp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by fares on 16.02.16.
 */
public class DataItemResultCallback implements ResultCallback<DataApi.DataItemResult> {

    private static final String LOGTAG = DataItemResultCallback.class.getSimpleName();
    private static final String WEATHER = "/today-weather";
    private static final String DATA_ITEM_HIGH = "high";
    private static final String DATA_ITEM_LOW = "low";
    private static final String DATA_ITEM_IMAGE = "image";
    private static final String DATA_ITEM_TEST = "test_time";

    private final FetchConfigDataMapCallback mCallback;

    public DataItemResultCallback(FetchConfigDataMapCallback callback) {
        mCallback = callback;
    }


    @Override
    public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
        if (dataItemResult.getStatus().isSuccess()) {
            if (dataItemResult.getDataItem() != null) {
                DataItem configDataItem = dataItemResult.getDataItem();
                DataMapItem dataMapItem = DataMapItem.fromDataItem(configDataItem);
                DataMap config = dataMapItem.getDataMap();
                mCallback.onConfigDataMapFetched(config);
            } else {
                mCallback.onConfigDataMapFetched(new DataMap());
            }
        }
    }

    public interface FetchConfigDataMapCallback {
        void onConfigDataMapFetched(DataMap config);
    }
}