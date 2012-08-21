package com.androidservicewrapper.test;

import android.content.Context;
import android.content.Intent;
import android.service.wallpaper.WallpaperService;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.androidservicewrapper.AndroidServiceWrapper;
import com.androidservicewrapper.test.mockobjects.ServiceContext;
import com.androidservicewrapper.test.mockobjects.TestAlarmManager;
import com.androidservicewrapper.test.mockobjects.TestLocationManager;
import com.androidservicewrapper.test.mockobjects.TestPowerManager;
import com.androidservicewrapper.test.mockobjects.TestTelephonyManager;
import com.androidservicewrapper.test.mockobjects.TestWifiManager;

public class MyServiceTest extends ServiceTestCase<WallpaperService> {

	private static final String TAG = MyServiceTest.class.getSimpleName();
	
	WallpaperService mService = null;
	MyModuleTest mDetTest = null;
	
	ServiceContext mTestContext;
	TestTelephonyManager mTestTelMan = null;
    TestWifiManager mTestWifiMan = null;
    TestLocationManager mTestLocMan;
    TestPowerManager mTestPowMan = null;
    TestAlarmManager mTestAlarmMan = null;
	
	public MyServiceTest() {
		super(WallpaperService.class);    // constructor note down which class object we test against. 
	}
	
	 @Override
	 protected void setUp() throws Exception {
	     super.setUp();
	     
	     mTestContext = new ServiceContext(getSystemContext());	 
	     mTestTelMan = new TestTelephonyManager(mTestContext);
	     mTestLocMan = new TestLocationManager(mTestContext);
	     mTestWifiMan = new TestWifiManager(mTestContext);
	     mTestPowMan = new TestPowerManager(mTestContext);
	     mTestAlarmMan = new TestAlarmManager(mTestContext);
	     
	     mTestLocMan.addTestProvider();
	
	     AndroidServiceWrapper.setMockSystemService(Context.TELEPHONY_SERVICE, mTestTelMan);
	     AndroidServiceWrapper.setMockSystemService(Context.WIFI_SERVICE, mTestWifiMan);
	     AndroidServiceWrapper.setMockSystemService(Context.ALARM_SERVICE, mTestAlarmMan);
	     AndroidServiceWrapper.setMockSystemService(Context.LOCATION_SERVICE, mTestLocMan);
	     
	     setContext(mTestContext);
	 }
	 
	 /**
	  * unit test tear down
	  */
	 @Override
	 protected void tearDown() throws Exception {
	     // do I need to clean up anything ? Yes, remove the mock location provider
	     assertNotNull(mTestLocMan);
	     mTestLocMan.removeTestProvider();
	 
	     mTestTelMan = null;
	     mTestLocMan = null;
	     mTestWifiMan = null;
	     mTestAlarmMan = null;
	     mTestPowMan = null;
	     
	     super.tearDown();
	 }
	 
	 /**
	  * ensure test setup properly before running test
	  */
	 @Override
	 public void testServiceTestCaseSetUpProperly() throws Exception {
	     //super.testServiceTestCaseSetUpProperly();  // can not call this.
	     assertNotNull(mTestContext);
	     assertNotNull(mTestWifiMan);
	     
	     assertEquals(mTestWifiMan, AndroidServiceWrapper.getSystemService(mTestContext, Context.WIFI_SERVICE));
	     assertEquals(mTestTelMan, AndroidServiceWrapper.getSystemService(mTestContext, Context.TELEPHONY_SERVICE));
	     assertEquals(mTestLocMan, AndroidServiceWrapper.getSystemService(mTestContext, Context.LOCATION_SERVICE));
	     assertEquals(mTestAlarmMan, AndroidServiceWrapper.getSystemService(mTestContext, Context.ALARM_SERVICE));
	 }
	 
	/**
	 * each small unit test case must be testXXX()
	 * adb shell am instrument -w -e size small com.android.foo/android.test.InstrumentationTestRunner
	 */
	@SmallTest
	public void testStartService() {
	    Intent startIntent = new Intent();
	    startIntent.setClass(getContext(), WallpaperService.class);
	    startService(startIntent);
	    Log.d(TAG, "testStartable : started service." );
	    mService = (WallpaperService)getService();
	    assertNotNull(getService());
	    ServiceContext.waitFor(2000);
	    Log.d(TAG, "testStartable : done testing..." );
	}
}
