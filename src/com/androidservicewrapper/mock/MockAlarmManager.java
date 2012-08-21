package com.androidservicewrapper.mock;

import android.app.PendingIntent;
import android.content.Context;

import com.androidservicewrapper.AlarmManagerWrapper;

/**
 * All methods are non-functional and throw {@link java.lang.UnsupportedOperationException}.
 * You can use this to inject other dependencies, mocks, or monitors into the classes you are
 * testing.
 */
public class MockAlarmManager extends AlarmManagerWrapper {

    public MockAlarmManager(Context context) {
        super(context);
    }

    @Override
    public void cancel(PendingIntent operation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int type, long triggerAtTime, PendingIntent operation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setInexactRepeating(int type, long triggerAtTime, long interval,
                                    PendingIntent operation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRepeating(int type, long triggerAtTime, long interval, PendingIntent operation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTime(long millis) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTimeZone(String timeZone) {
        throw new UnsupportedOperationException();
    }
}