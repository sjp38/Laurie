
package org.drykiss.android.app.laurie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class LaurieService extends Service {
    static final int NOTI_ID = 1;
    static boolean mNotiShown = false;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.drawable.ic_menu_home;
        CharSequence tickerText = getText(R.string.ticker);
        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, tickerText, when);

        CharSequence contentTitle = getText(R.string.content_title);
        CharSequence contentText = getText(R.string.content);
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notification.setLatestEventInfo(getApplicationContext(), contentTitle, contentText,
                contentIntent);
        notification.flags |= Notification.FLAG_NO_CLEAR;

        notificationManager.notify(NOTI_ID, notification);
    }
}
