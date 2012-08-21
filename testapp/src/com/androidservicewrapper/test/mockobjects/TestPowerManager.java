package com.androidservicewrapper.test.mockobjects;

import com.androidservicewrapper.mock.MockPowerManager;

public class TestPowerManager extends MockPowerManager {
    private static final String TAG = "LSAPP_TstPow";
    private boolean mScreenOn = false;
    
    public TestPowerManager(ServiceContext context) {
        super(context.getAndroidContext());
        context.setTestPowMan(this);
    }
    
    @Override
    public boolean isScreenOn() {
        return mScreenOn;
    }

    public void setScreenOn(boolean screenOn) {
        mScreenOn = screenOn;
    }

}
