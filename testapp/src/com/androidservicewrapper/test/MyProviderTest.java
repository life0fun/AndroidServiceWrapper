package com.androidservicewrapper.test;

import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.util.Log;

import com.androidservicewrapper.AndroidServiceWrapper;
import com.androidservicewrapper.test.mockobjects.ServiceContext;
import com.androidservicewrapper.test.mockobjects.TestAlarmManager;
import com.androidservicewrapper.test.mockobjects.TestLocationManager;
import com.androidservicewrapper.test.mockobjects.TestPowerManager;
import com.androidservicewrapper.test.mockobjects.TestTelephonyManager;
import com.androidservicewrapper.test.mockobjects.TestWifiManager;

public class MyProviderTest extends ProviderTestCase2<SearchRecentSuggestionsProvider>{

	private static final String TAG = "LSAPP_TstProv";
	public static String URI = "content://";
	
	private SQLiteDatabase mDb;
    
    MyModuleTest mDetTest = null;
    
    ServiceContext mTestContext;
    TestTelephonyManager mTestTelMan = null;
    TestWifiManager mTestWifiMan = null;
    TestLocationManager mTestLocMan;
    TestPowerManager mTestPowMan = null;
    TestAlarmManager mTestAlarmMan = null;
    
	
    /**
     * TestCase constructor, tell Test Framework to test location provider with the URI.
     */
	public MyProviderTest() {
	    super(SearchRecentSuggestionsProvider.class, URI);
		Log.d(TAG, "MyProviderTest : ");
	}
	
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		
		mTestContext = new ServiceContext(getContext());     
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
	 * test query provider
	 */
	public void testQueryPoi() {
	    String poiuri = URI;
	    Cursor c = mTestContext.getContentResolver().query(Uri.parse(poiuri), null, null, null, null );
	    try{
	        if (c != null && c.moveToFirst()) {
	            do {
                    Log.d(TAG, "testQueryPoi : " + c.getString(1));
                } while (c.moveToNext());
	        }
	    }catch(Exception e){
	        Log.e(TAG, e.toString());
	    }finally{
	        if(c != null)
	            c.close();
	    }
	    Log.d(TAG, "testQueryPoi : done ");
	}
}
