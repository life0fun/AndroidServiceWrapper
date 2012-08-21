package com.androidservicewrapper.test;

import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.androidservicewrapper.test.mockobjects.ServiceContext;
import com.androidservicewrapper.test.mockobjects.TestLocationManager;
import com.androidservicewrapper.test.mockobjects.TestWifiManager;

public class TestSuite {

    private static final String TAG = TestSuite.class.getSimpleName();
    
    ServiceContext mTestContext;
    public TestWifiManager mTestWifiMan;
    public TestLocationManager mTestLocMan;
    
    
    /**
     * constructor, dep injection of lsman and detection.
     */
    public TestSuite(ServiceContext testctx){
        mTestContext = testctx;
        
        mTestWifiMan = mTestContext.mTestWifiMan;
        mTestLocMan = mTestContext.mTestLocMan;
        
        Log.d(TAG, "TestSuite constructed...");
    }
    
    public void run() {
        Log.d(TAG, "TestSuite : a series test cases start running...");
        testLocationFix();
    }
    
    /**
     * locaiton fix request and processing
     */
    public void testLocationFix() {
        // first, mock the location fix
        mTestContext.mTestLocMan.setMocLocation(10.0, 10.0, 60);
        Log.d(TAG, " testLocationFix : ");
        ServiceContext.waitFor(5000);
    }
}
