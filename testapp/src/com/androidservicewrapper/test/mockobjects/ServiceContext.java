
package com.androidservicewrapper.test.mockobjects;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Handler;
import android.test.mock.MockContext;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * mock context inherits from context, should use this object to intercept all the context get service call.
 */

public class ServiceContext extends MockContext {
    private static final String TAG = ServiceContext.class.getSimpleName();
    
    private static final long MAX_WAIT_TIME = 1000;
    
    private Context mAndroidContext;
    
    public TestTelephonyManager mTestTelMan = null;
    public TestWifiManager mTestWifiMan = null;
    public TestLocationManager mTestLocMan = null;
    public TestPowerManager mTestPowMan = null;
    public TestAlarmManager mTestAlarmMan = null;
    
    // created db list
    private final List<String> mDbList = new LinkedList<String>();

    private final List<Intent> mIntentList = new LinkedList<Intent>();
    private final List<BroadcastReceiver> mReceiverList = new LinkedList<BroadcastReceiver>();
    
    public ServiceContext(Context context) {
        mAndroidContext = context;
    }
    
    /**
     * get the the android context, the un-mocked one.
     */
    public Context getAndroidContext(){
        return mAndroidContext;
    }
   
    /**
     * pause for several seconds
     */
    public static void waitFor(long ms){
        try{
            Thread.sleep(ms);
        }catch(Exception e){
            Log.d(TAG, "wait: exception: " + e.toString());
        }
    }
    
    public void setTestTelMan(TestTelephonyManager telman){
        mTestTelMan = telman;
    }
    public void setTestWifiMan(TestWifiManager wifiman){
        mTestWifiMan = wifiman;
    }
    public void setTestLocationMan(TestLocationManager locman){
        mTestLocMan = locman;
    }
    public void setTestPowMan(TestPowerManager powman){
        mTestPowMan = powman;
    }
    public void setAlarmMan(TestAlarmManager alarm){
        mTestAlarmMan = alarm;
    }
    
    @Override
    public ApplicationInfo getApplicationInfo() {
        return mAndroidContext.getApplicationInfo();
    }

    @Override
    public String getPackageName() {
        return mAndroidContext.getPackageName();
    }

    @Override
    public Object getSystemService(String name) {
        Log.d(TAG, "context get system service : " + name);
        return mAndroidContext.getSystemService(name);
    }
     
    @Override
    public ContentResolver getContentResolver() {
        return mAndroidContext.getContentResolver();
    }
    
    @Override
    public SQLiteDatabase openOrCreateDatabase(String file, int mode, CursorFactory factory,
                    DatabaseErrorHandler errorHandler) {
        mDbList.add(file);
        return mAndroidContext.openOrCreateDatabase(file, mode, factory, errorHandler);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        synchronized(mReceiverList) {
            if (receiver != null) {
                mReceiverList.add(receiver);
            }
            mReceiverList.notify();
        }
        //return null;  XXX unit test, stub out interaction to platform.
        return mAndroidContext.registerReceiver(receiver, filter);
    }    
    
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler){
        return mAndroidContext.registerReceiver(receiver, filter, broadcastPermission, scheduler);
    }
    
    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        synchronized(mReceiverList) {
            if (receiver != null) {
                mReceiverList.remove(receiver);
            }
            mReceiverList.notify();
        }
        mAndroidContext.unregisterReceiver(receiver);  // behavior test, pass thru to platform.
    }

    public BroadcastReceiver getReceiver() {
        BroadcastReceiver receiver = null;
        synchronized(mReceiverList) {
            if (mReceiverList.isEmpty()) {
                try {
                    mReceiverList.wait(MAX_WAIT_TIME);
                } catch (InterruptedException e) {
                    Log.e(TAG, "getReceiver() interrupted.");
                }
            }
        }
        if (!mReceiverList.isEmpty()) {
            receiver = mReceiverList.remove(0);
        }
        return receiver;
    }

    @Override
    public ComponentName startService(Intent service) {
        // some components may do things on background thread
        synchronized(mIntentList) {
            if (service != null) {
                mIntentList.add(service);
            }
            mIntentList.notify();
        }
        // return null;   XXX unit test stub out the interaction to platform.
        return mAndroidContext.startService(service);
    }
    
    /**
     * @return the intent that was passed in for startService()
     */
    public Intent getStartServiceIntent() {
        Intent intent = null;
        synchronized(mIntentList) {
            if (mIntentList.isEmpty()) {
                try {
                    mIntentList.wait(MAX_WAIT_TIME);
                } catch (InterruptedException e) {
                    Log.e(TAG, "getStartServiceIntent() interrupted.");
                }
            }
        }
        if (!mIntentList.isEmpty()) {
            intent = mIntentList.remove(0);
        }
        return intent;
    }
    
    /**
     * getSharedPreferences
     */
    @Override
    public SharedPreferences getSharedPreferences(String name, int mode){
        return mAndroidContext.getSharedPreferences(name, mode);
    }
    
    /**
     * send broadcast
     */
    @Override
    public void sendBroadcast(Intent intent){
        mAndroidContext.sendBroadcast(intent);
    }
    
    @Override
    public void  sendBroadcast(Intent intent, String receiverPermission){
        mAndroidContext.sendBroadcast(intent, receiverPermission);
    }
    
    @Override
    public Resources getResources(){
        return mAndroidContext.getResources();
    } 
   
    /**
     * send intent thru bcast
     */
    public void sendIntent(Intent i){
        Log.d(TAG, "sendIntent : " + i.getAction());
        mAndroidContext.sendBroadcast(i);
    }
}
