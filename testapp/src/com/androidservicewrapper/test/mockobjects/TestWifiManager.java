package com.androidservicewrapper.test.mockobjects;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.androidservicewrapper.WifiManagerWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class TestWifiManager extends WifiManagerWrapper {
    private static final String TAG = "LSAPP_TstWifi";
    
    public static final String LS_JSON_WIFISSID = "wifissid";
    public static final String LS_JSON_WIFIBSSID = "wifibssid";
    public static final String LS_JSON_WIFISS = "ss";
    
    public ServiceContext mServiceContext;
    public int mWifiState;
    public List<ScanResult> mScanResult = new ArrayList<ScanResult>();
    
    // Android is really not test friendly. why you can not open up constructor ?
    static Constructor<ScanResult> mScanResultConstructor = null;
    static{
        try{
            mScanResultConstructor = ScanResult.class.getDeclaredConstructor(String.class, String.class, String.class,Integer.TYPE, Integer.TYPE);
        }catch(Exception e){
            Log.d(TAG, "TestWifiManager : fail to find ScanResult constructor");
        }
    }
    
    public TestWifiManager(ServiceContext context) {
        super(context.getAndroidContext());
        mServiceContext = context;
        context.setTestWifiMan(this);
    }
    
    /**
     * override the start scan API to fake a scan result available intent.
     */
    @Override
    public boolean startScan(){
        Log.d(TAG, "startScan : send fake scan result available intent");
        mServiceContext.sendIntent(new Intent(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        return true;
    }   

    /**
     * override the get scan results and inject mock scan result here.
     */
    @Override
    public List<ScanResult> getScanResults() {
        Log.d(TAG, "getScanResults : return mock scan result");
        return mScanResult;
    }
    
    /**
     * set the scan result 
     */
    public List<ScanResult> setScanResult(String wifiJsonArray){
        mScanResult.clear();
        if( wifiJsonArray != null ){
            try{
                JSONArray wifissid = new JSONArray(wifiJsonArray);
                JSONObject entry = null;
                for (int i=0; i<wifissid.length(); i++) {
                    entry = wifissid.getJSONObject(i);
                    ScanResult sr = scanResultFactory(entry.getString(LS_JSON_WIFISSID), entry.getString(LS_JSON_WIFIBSSID), null, -10, 0);
                    mScanResult.add(sr);
                    Log.d(TAG, "setScanResult : ScanResult : " + entry.toString());
                }
            }catch(Exception e) {
                Log.d(TAG, "setScanResult : " + wifiJsonArray + " : " + e.toString());
            }
        }
        Log.d(TAG, "setScanResult : done populating scan result");
        return mScanResult;
    }
    
    /**
     * static factory to make a scan result.
     */
    public static ScanResult scanResultFactory(String ssid, String bssid, String caps, int level, int frequency){
        ScanResult sr = null;
        try{
            sr = (ScanResult)mScanResultConstructor.newInstance(ssid, bssid, caps, level, frequency);
        }catch(Exception e){
            Log.d(TAG, "makeScanResult : failed : " + e.toString());
        }
        Log.d(TAG, "makeScanResult : success : " + sr.toString());
        return sr;
    }
    
    /**
     * get wifi connection info
     */
    @Override
    public WifiInfo getConnectionInfo(){
        Log.d(TAG, "getConnectionInfo : ");
        return super.getConnectionInfo();
    }
    
   
    
    @Override
    public boolean isWifiEnabled(){
        Log.d(TAG, "isWifiEnabled : ");
        return super.isWifiEnabled();
    }
    
    @Override
    public int getWifiState(){
        Log.d(TAG, "getWifiState : ");
        return mWifiState;
    }
    
    protected void setWifiState(int state){
        Log.d(TAG, "setWifiState : ");
        mWifiState = state; 
    }
}
