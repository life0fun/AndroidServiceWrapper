package com.androidservicewrapper.mock;

import android.content.Context;
import android.os.PowerManager.WakeLock;

import com.androidservicewrapper.PowerManagerWrapper;

/**
 * All methods are non-functional and throw {@link java.lang.UnsupportedOperationException}.
 * You can use this to inject other dependencies, mocks, or monitors into the classes you are
 * testing.
 */
public class MockPowerManager extends PowerManagerWrapper {

    public MockPowerManager(Context context) {
        super(context);
    }

    @Override
    public void goToSleep(long time) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isScreenOn() {
        throw new UnsupportedOperationException();
    }

    @Override
    public WakeLock newWakeLock(int flags, String tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reboot(String reason) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void userActivity(long when, boolean noChangeLights) {
        throw new UnsupportedOperationException();
    }
}
