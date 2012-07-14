
package org.drykiss.android.app.laurie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class LaurieService extends Service {
    static final int NOTI_HOME_ID = 1;
    static final int NOTI_RECENT_ID = 2;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent homeIntent = new Intent(Intent.ACTION_MAIN, null);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent homeContentIntent = PendingIntent.getActivity(this, 0, homeIntent, 0);

        Notification homeNoti = buildLaurieNoti(R.drawable.ic_noti_home, getText(R.string.ticker),
                System.currentTimeMillis(), getText(R.string.home_content_title),
                getText(R.string.home_content), homeContentIntent);

        Intent recentIntent = new Intent(this, RecentAppsActivity.class);
        PendingIntent recentContentIntent = PendingIntent.getActivity(this, 0, recentIntent, 0);

        Notification recentNoti = buildLaurieNoti(R.drawable.ic_noti_recent,
                getText(R.string.ticker),
                System.currentTimeMillis(), getText(R.string.recent_apps_content_title),
                getText(R.string.recent_apps_content), recentContentIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTI_HOME_ID, homeNoti);
        notificationManager.notify(NOTI_RECENT_ID, recentNoti);
    }

    private Notification buildLaurieNoti(int icon, CharSequence tickerText, long when,
            CharSequence title, CharSequence text, PendingIntent intent) {
        Notification noti = new Notification(icon, tickerText, when);
        noti.setLatestEventInfo(getApplicationContext(), title, text,
                intent);
        noti.flags |= Notification.FLAG_NO_CLEAR;

        return noti;
    }
}
