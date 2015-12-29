package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilities;
import barqsoft.footballscores.data.DatabaseContract;

/**
 * Created by fares on 12/27/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ServiceWidgetRemoteViewsScoresList extends RemoteViewsService {

    public final String LOG_TAG = ServiceWidgetRemoteViewsScoresList.class.getSimpleName();

    private static final String[] SCORES_COLUMNS = {
            DatabaseContract.ScoresEntry.HOME_COL,
            DatabaseContract.ScoresEntry.AWAY_COL,
            DatabaseContract.ScoresEntry.HOME_GOALS_COL,
            DatabaseContract.ScoresEntry.AWAY_GOALS_COL,
            DatabaseContract.ScoresEntry.DATE_COL,
            DatabaseContract.ScoresEntry.LEAGUE_COL,
            DatabaseContract.ScoresEntry.MATCH_DAY,
            DatabaseContract.ScoresEntry.MATCH_ID,
            DatabaseContract.ScoresEntry.TIME_COL
    };

    private static final int INDEX_HOME       = 0;
    private static final int INDEX_AWAY       = 1;
    private static final int INDEX_HOME_GOALS = 2;
    private static final int INDEX_AWAY_GOALS = 3;
    private static final int INDEX_DATE       = 4;
    private static final int INDEX_LEAGUE     = 5;
    private static final int INDEX_MATCH_DAY  = 6;
    private static final int INDEX_MATCH_ID   = 7;
    private static final int INDEX_TIME       = 8;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor mCursor;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (mCursor != null)
                    mCursor.close();
                final long identityToken = Binder.clearCallingIdentity();
                Uri scoresUri = DatabaseContract.ScoresEntry.buildScoreWithDate();

                Date date = new Date(System.currentTimeMillis()+((1-2)*86400000));
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

//                viewFragments[i].setFragmentDate(mformat.format(fragmentdate));

                mCursor = getContentResolver()
                        .query(scoresUri,
                                SCORES_COLUMNS,
                                DatabaseContract.ScoresEntry.DATE_COL,
                                new String[]{formattedDate},
                                DatabaseContract.ScoresEntry.DATE_COL + " ASC");

                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (mCursor != null) {
                    mCursor.close();
                    mCursor = null;
                }
            }

            @Override
            public int getCount() {
                return mCursor == null ? 0 : mCursor.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION || mCursor == null || !mCursor.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.list_item_widget);


                String league    = mCursor.getString(INDEX_LEAGUE);
                String date      = mCursor.getString(INDEX_DATE);
                String time      = mCursor.getString(INDEX_TIME);
                String home      = mCursor.getString(INDEX_HOME);
                String away      = mCursor.getString(INDEX_AWAY);
                int home_goals   = mCursor.getInt(INDEX_HOME_GOALS);
                int away_goals   = mCursor.getInt(INDEX_AWAY_GOALS);
                String match_id  = mCursor.getString(INDEX_MATCH_ID);
                String match_day = mCursor.getString(INDEX_MATCH_DAY);

                int homeImg = Utilities.getTeamCrestByTeamName(home);
                int awayImg = Utilities.getTeamCrestByTeamName(away);

                Bitmap crestImageHome = null, crestImageAway = null;

                try {
                    crestImageHome = Glide.with(ServiceWidgetRemoteViewsScoresList.this)
                            .load(homeImg)
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                try {
                    crestImageAway = Glide.with(ServiceWidgetRemoteViewsScoresList.this)
                            .load(awayImg)
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }




                views.setTextViewText(R.id.home_name, home);
                views.setTextViewText(R.id.away_name, away);
                views.setTextViewText(R.id.data_textview, date);
                views.setTextViewText(R.id.score_textview, Utilities.getScores(home_goals, away_goals));
                views.setTextViewText(R.id.score_textview, Utilities.getScores(home_goals, away_goals));
                if (crestImageHome != null)
                    views.setImageViewBitmap(R.id.home_crest, crestImageHome);
                if (crestImageAway != null)
                    views.setImageViewBitmap(R.id.away_crest, crestImageAway);

//                Intent fillIntent = new Intent();

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.list_item_widget);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (mCursor.moveToPosition(position))
                    return mCursor.getLong(INDEX_MATCH_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}
