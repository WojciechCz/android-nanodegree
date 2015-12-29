package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

/**
 * Created by fares on 12/27/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ProviderWidgetScores extends AppWidgetProvider {

    public static final String ACTION_DATA_UPDATED = "barqsoft.footballscores.app.ACTION_DATA_UPDATED";

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            views.setOnClickPendingIntent(R.id.widget_toolbar, pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                setRemoteAdapter(context, views);
            else
                setRemoteAdapterV11(context, views);

            Intent intentTemplate = new Intent(context, MainActivity.class);
            PendingIntent pendingIntentTemplate = TaskStackBuilder
                    .create(context)
                    .addNextIntentWithParentStack(intentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setPendingIntentTemplate(R.id.widget_list, pendingIntentTemplate);
            views.setEmptyView(R.id.widget_list, R.id.widget_empty);

            manager.updateAppWidget(appWidgetId, views);
        }
    }

    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_list, new Intent(context, ServiceWidgetRemoteViewsScoresList.class));
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list, new Intent(context, ServiceWidgetRemoteViewsScoresList.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }
}
