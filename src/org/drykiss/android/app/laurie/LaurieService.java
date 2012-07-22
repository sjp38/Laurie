
package org.drykiss.android.app.laurie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class LaurieService extends Service {
    static final int NOTI_HOME_ID = 1;
    static final int NOTI_RECENT_ID = 2;

    static final String KEY_TURN_ON_HOME_BUTTON = "org.drykiss.android.app.laurie.TURN_ON_HOME_BUTTON";
    static final String KEY_TURN_ON_RECENT_APPS_BUTTON = "org.drykiss.android.app.laurie.TURN_ON_RECENT_APPS_BUTTON";

    private NotificationManager mNotiManager;
    private Notification mHomeNoti;
    private Notification mRecentAppsNoti;

    private SharedPreferences mPrefs;

    private SharedPreferences.OnSharedPreferenceChangeListener mPrefsChangedListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                        String key) {
                    if (KEY_TURN_ON_HOME_BUTTON.equals(key)
                            || KEY_TURN_ON_RECENT_APPS_BUTTON.equals(key)) {
                        turnOnOffFeatures(
                                sharedPreferences.getBoolean(KEY_TURN_ON_HOME_BUTTON, true),
                                sharedPreferences.getBoolean(KEY_TURN_ON_RECENT_APPS_BUTTON, true));
                    }
                }
            };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        Intent homeIntent = new Intent(Intent.ACTION_MAIN, null);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent homeContentIntent = PendingIntent.getActivity(this, 0, homeIntent, 0);

        mHomeNoti = buildLaurieNoti(R.drawable.ic_noti_home, getText(R.string.ticker),
                System.currentTimeMillis(), getText(R.string.home_content_title),
                getText(R.string.home_content), homeContentIntent);

        Intent recentIntent = new Intent(this, RecentAppsActivity.class);
        recentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent recentContentIntent = PendingIntent.getActivity(this, 0, recentIntent, 0);

        mRecentAppsNoti = buildLaurieNoti(R.drawable.ic_noti_recent,
                getText(R.string.ticker),
                System.currentTimeMillis(), getText(R.string.recent_apps_content_title),
                getText(R.string.recent_apps_content), recentContentIntent);

        mNotiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mPrefs.registerOnSharedPreferenceChangeListener(mPrefsChangedListener);
        turnOnOffFeatures(mPrefs.getBoolean(KEY_TURN_ON_HOME_BUTTON, true),
                mPrefs.getBoolean(KEY_TURN_ON_RECENT_APPS_BUTTON, true));
    }

    @Override
    public void onDestroy() {
        mPrefs.unregisterOnSharedPreferenceChangeListener(mPrefsChangedListener);
        mNotiManager.cancelAll();
        super.onDestroy();
    }

    private Notification buildLaurieNoti(int icon, CharSequence tickerText, long when,
            CharSequence title, CharSequence text, PendingIntent intent) {
        Notification noti = new Notification(icon, tickerText, when);
        noti.setLatestEventInfo(getApplicationContext(), title, text,
                intent);
        noti.flags |= Notification.FLAG_NO_CLEAR;

        return noti;
    }

    private void turnOnOffFeatures(boolean turnOnHome, boolean turnOnRecentApps) {
        if (turnOnHome) {
            mNotiManager.notify(NOTI_HOME_ID, mHomeNoti);
        } else {
            mNotiManager.cancel(NOTI_HOME_ID);
        }

        if (turnOnRecentApps) {
            mNotiManager.notify(NOTI_RECENT_ID, mRecentAppsNoti);
        } else {
            mNotiManager.cancel(NOTI_RECENT_ID);
        }
    }
}
