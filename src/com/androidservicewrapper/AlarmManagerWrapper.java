package com.androidservicewrapper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

public class AlarmManagerWrapper {
    protected final AlarmManager mAlarmManager;

    public AlarmManagerWrapper(Context context) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * {@link AlarmManager#cancel}
     */
    public void cancel(PendingIntent operation) {
        mAlarmManager.cancel(operation);
    }

    /**
     * {@link AlarmManager#set}
     */
    public void set(int type, long triggerAtTime, PendingIntent operation) {
        mAlarmManager.set(type, triggerAtTime, operation);
    }

    /**
     * {@link AlarmManager#setInexactRepeating}
     */
    public void setInexactRepeating(int type, long triggerAtTime, long interval,
                                    PendingIntent operation) {
        mAlarmManager.setInexactRepeating(type, triggerAtTime, interval, operation);
    }

    /**
     * {@link AlarmManager#setRepeating}
     */
    public void setRepeating(int type, long triggerAtTime, long interval, PendingIntent operation) {
        mAlarmManager.setRepeating(type, triggerAtTime, interval, operation);
    }

    /**
     * {@link AlarmManager#cancel}
     */
    public void setTime(long millis) {
        mAlarmManager.setTime(millis);
    }

    /**
     * {@link AlarmManager#setTimeZone}
     */
    public void setTimeZone(String timeZone) {
        mAlarmManager.setTimeZone(timeZone);
    }

}
