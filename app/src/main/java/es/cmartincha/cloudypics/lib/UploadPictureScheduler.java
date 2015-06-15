package es.cmartincha.cloudypics.lib;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import es.cmartincha.cloudypics.service.UploadPictureService;

/**
 * Created by Carlos on 15/06/2015.
 */
public class UploadPictureScheduler {
    private static final int TRIGGER_TIME_MILLIS = 1000;

    public static void scheduleNewStart(Context context, Class<? extends UploadPictureService> serviceClass, String token) {
        Intent intentService = new Intent(context, serviceClass);
        intentService.putExtra("token", token);

        if (PendingIntent.getService(context, 0, intentService, PendingIntent.FLAG_NO_CREATE) == null) {
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarm.set(
                    AlarmManager.RTC,
                    System.currentTimeMillis() + TRIGGER_TIME_MILLIS,
                    PendingIntent.getService(context, 0, intentService, 0)
            );
        }
    }
}
