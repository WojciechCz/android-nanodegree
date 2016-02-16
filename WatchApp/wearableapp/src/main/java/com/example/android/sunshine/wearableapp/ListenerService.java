package com.example.android.sunshine.wearableapp;

import android.util.Log;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by fares on 16.02.16.
 */
public class ListenerService extends WearableListenerService {

    private static final String LOGTAG = ListenerService.class.getSimpleName();
    private static final String WEATHER = "/today-weather";
    private static final String DATA_ITEM_HIGH = "high";
    private static final String DATA_ITEM_LOW = "low";
    private static final String DATA_ITEM_IMAGE = "image";
    private static final String DATA_ITEM_TEST = "test_time";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(LOGTAG, "------- RECEIVER DATA");
        DataMap dataMap;
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (path.equals(WEATHER)) {
                    dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                    String tempHigh    = dataMap.getString(DATA_ITEM_HIGH);
                    String tempLow     = dataMap.getString(DATA_ITEM_LOW);
                    Asset weatherImage = dataMap.getAsset(DATA_ITEM_IMAGE);
                    Log.v("myTag", "DataMap received on watch: " + dataMap);
                }
            }
        }
    }
}