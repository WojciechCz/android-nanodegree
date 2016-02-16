/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.fares.wearableapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.text.format.DateFormat.*;

/**
 * Digital watch face with seconds. In ambient mode, the seconds aren't displayed. On devices with
 * low-bit ambient mode, the text is drawn without anti-aliasing in ambient mode.
 */
public class WatchFace extends CanvasWatchFaceService {
    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SERIF, Typeface.NORMAL);
    private static final Typeface BOLD_TYPEFACE   = Typeface.create(Typeface.SERIF, Typeface.BOLD);
    /**
     * Update rate in milliseconds for interactive mode. We update once a second since seconds are
     * displayed in interactive mode.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);
    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private static class EngineHandler extends Handler {
        private final WeakReference<WatchFace.Engine> mWeakReference;

        public EngineHandler(WatchFace.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            WatchFace.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }

    private class Engine extends CanvasWatchFaceService.Engine
            implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener {

        final Handler mUpdateTimeHandler = new EngineHandler(this);

        boolean mRegisteredTimeZoneReceiver = false;
        boolean mAmbient;

        private Paint mBackgroundPaint, mPaintLine;
        private Paint mPaintHours, mPaintTimeColon, mPaintMinutes,
                mPaintTempHigh, mPaintTempLow, mPaintDate;

        Calendar mTime;
        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTime.setTimeZone(TimeZone.getDefault());
            }
        };

        float mXOffset;
        float mYOffset;

        private static final String WEATHER = "/today-weather";
        private static final String DATA_ITEM_HIGH = "high";
        private static final String DATA_ITEM_LOW = "low";
        private static final String DATA_ITEM_IMAGE = "image";
        private Double mTempHigh, mTempLow;
        private Bitmap mWeatherImage;

        private float mLineOffsetX, mLineOffsetY, mImageOffsetX;

        private float mTextSize, mTextSizeDate, mTextSizeTemp;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        boolean mLowBitAmbient;


        private GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(WatchFace.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(WatchFace.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());
            Resources resources = WatchFace.this.getResources();
            mYOffset      = resources.getDimension(R.dimen.digital_y_offset);
            mLineOffsetX  = resources.getDimension(R.dimen.line_x_offset);
            mLineOffsetY  = resources.getDimension(R.dimen.line_y_offset);
            mImageOffsetX = resources.getDimension(R.dimen.image_x_offset);

            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(getColor(R.color.primary));
            mPaintLine = new Paint();
            mPaintLine.setColor(getColor(R.color.secondary_text));

            mPaintHours     = createTextPaint(getColor(R.color.primary_text), BOLD_TYPEFACE);
            mPaintTimeColon = createTextPaint(getColor(R.color.secondary_text), NORMAL_TYPEFACE);
            mPaintMinutes   = createTextPaint(getColor(R.color.secondary_text), NORMAL_TYPEFACE);
            mPaintTempHigh  = createTextPaint(getColor(R.color.primary_text), BOLD_TYPEFACE);
            mPaintTempLow   = createTextPaint(getColor(R.color.secondary_text), NORMAL_TYPEFACE);
            mPaintDate      = createTextPaint(getColor(R.color.secondary_text), NORMAL_TYPEFACE);

            mWeatherImage = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            mTime = Calendar.getInstance();
        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }

        private Paint createTextPaint(int textColor, Typeface typeface) {
            Paint paint = new Paint();
            paint.setColor(textColor);
            paint.setTypeface(typeface);
            paint.setAntiAlias(true);
            return paint;
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();
                mTime.setTimeZone(TimeZone.getDefault());
                mGoogleApiClient.connect();
            } else {
                unregisterReceiver();
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    Wearable.DataApi.removeListener(mGoogleApiClient, this);
                    mGoogleApiClient.disconnect();
                }
            }
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver)
                return;

            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            WatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver)
                return;

            mRegisteredTimeZoneReceiver = false;
            WatchFace.this.unregisterReceiver(mTimeZoneReceiver);
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);

            Resources resources = WatchFace.this.getResources();
            boolean isRound = insets.isRound();
            mTextSize     = resources.getDimension(isRound ? R.dimen.digital_text_size_round : R.dimen.digital_text_size);
            mTextSizeDate = resources.getDimension(isRound ? R.dimen.digital_text_size_round_small : R.dimen.digital_text_size_small);
            mTextSizeTemp = resources.getDimension(isRound ? R.dimen.digital_text_size_round_temp : R.dimen.digital_text_size_temp);

            mXOffset = resources.getDimension(isRound ? R.dimen.digital_x_offset_round : R.dimen.digital_x_offset);

            mPaintHours     .setTextSize(mTextSize);
            mPaintTimeColon .setTextSize(mTextSize);
            mPaintMinutes   .setTextSize(mTextSize);
            mPaintDate      .setTextSize(mTextSizeDate);
            mPaintTempHigh  .setTextSize(mTextSizeTemp);
            mPaintTempLow   .setTextSize(mTextSizeTemp);
//            mTextPaint.setTextSize(resources.getDimension(isRound ? R.dimen.digital_text_size_round : R.dimen.digital_text_size));
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (mAmbient != inAmbientMode) {
                mAmbient = inAmbientMode;
                if (mLowBitAmbient) {
                    mPaintHours   .setAntiAlias(!inAmbientMode);
                    mPaintMinutes .setAntiAlias(!inAmbientMode);
                    mPaintTempHigh.setAntiAlias(!inAmbientMode);
                    mPaintTempLow .setAntiAlias(!inAmbientMode);
                    mPaintDate    .setAntiAlias(!inAmbientMode);
                }
                invalidate();
            }
            updateTimer();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            // init time
            final boolean is24HourSpan = is24HourFormat(WatchFace.this);
            final float centerX = bounds.width() / 2f;
            final float centerY = bounds.height() / 2f;
            final long currentTimeMillis = System.currentTimeMillis();
            mTime.setTimeInMillis(currentTimeMillis);

            // Draw the background.
            if (isInAmbientMode()) {
                canvas.drawColor(Color.BLACK);
            } else {
                canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundPaint);
            }

            // Draw hours and minutes
            String hours = String.format("%02d", mTime.get(Calendar.HOUR) == 0 ? 12 : mTime.get(Calendar.HOUR));
            String minutes = String.format("%02d", mTime.get(Calendar.MINUTE));
            String date = getFormattedDate(new Date(currentTimeMillis));

            final float offsetXHours = centerX-mPaintHours.measureText(hours);
            final float offsetXColon = centerX;
            final float offsetXMinutes= centerX+mPaintTimeColon.measureText(":");
            final float offsetXDate = (bounds.width()-mPaintDate.measureText(date)) / 2;
            Rect colonBounds = new Rect();
            mPaintTimeColon.getTextBounds(";", 0, 1, colonBounds);
            final float offsetYDate = mYOffset + colonBounds.height();

            final float offsetXLineLeft  = centerX - (mLineOffsetX/2);
            final float offsetXLineRight = centerX + (mLineOffsetX/2);
            final float offsetYLine      = centerY + mLineOffsetY;
            canvas.drawLine(offsetXLineLeft, offsetYLine, offsetXLineRight, offsetYLine, mPaintLine);

            canvas.drawText(hours, offsetXHours, mYOffset, mPaintHours);
            canvas.drawText(":"    , offsetXColon  , mYOffset   , mPaintTimeColon);
            canvas.drawText(minutes, offsetXMinutes, mYOffset   , mPaintMinutes);
            canvas.drawText(date   , offsetXDate   , offsetYDate, mPaintDate);

            final float offsetXImageLeft = centerX - mImageOffsetX;
            final float offsetXImageTop  = offsetYLine + mLineOffsetY;
            if (mWeatherImage != null)
                canvas.drawBitmap(mWeatherImage, offsetXImageLeft, offsetXImageTop, null);
            
            if (mTempHigh != null && mTempLow != null) {
                final String tempHigh = mTempHigh + "\u00b0";
                final String tempLow = mTempLow + "\u00b0";
                Rect tempBounds = new Rect();
                mPaintTempHigh.getTextBounds("1", 0, 1, tempBounds);
                final float tempHighOffsetX = offsetXImageLeft + mWeatherImage.getWidth();
                final float tempLowOffsetX = tempHighOffsetX + mWeatherImage.getWidth();
                final float tempOffsetY = offsetXImageTop - tempBounds.height() + mWeatherImage.getHeight();

                canvas.drawText(tempHigh, tempHighOffsetX, tempOffsetY, mPaintTempHigh);
                canvas.drawText(tempLow, tempLowOffsetX, tempOffsetY, mPaintTempLow);
            }
        }

        private String getFormattedDate(Date date){
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM F yyyy");
            return sdf.format(date);
        }

        /**
         * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage() {
            invalidate();
            if (shouldTimerBeRunning()) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS
                        - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }

        @Override
        public void onDataChanged(DataEventBuffer dataEventBuffer) {
            for (DataEvent data : dataEventBuffer){
                if (data.getType() == DataEvent.TYPE_CHANGED){
                    String path = data.getDataItem().getUri().getPath();

                    if (WEATHER.equals(path)){
                        DataMap dataMap = DataMapItem.fromDataItem(data.getDataItem()).getDataMap();
                        if (dataMap.containsKey(DATA_ITEM_HIGH))
                            mTempHigh = dataMap.getDouble(DATA_ITEM_HIGH);
                        if (dataMap.containsKey(DATA_ITEM_LOW))
                            mTempLow = dataMap.getDouble(DATA_ITEM_LOW);
                        if (dataMap.containsKey(DATA_ITEM_IMAGE)) {
                            InputStream assetInputStream = Wearable.DataApi.getFdForAsset(mGoogleApiClient, dataMap.getAsset(DATA_ITEM_IMAGE)).await().getInputStream();
                            mWeatherImage = BitmapFactory.decodeStream(assetInputStream);
                        }
                    }

                    invalidate();
                }
            }
        }

        @Override
        public void onConnected(Bundle bundle) {

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }
    }
}
