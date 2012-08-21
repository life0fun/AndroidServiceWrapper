package com.androidservicewrapper.test.mockobjects;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Looper;
import android.util.Log;

import com.androidservicewrapper.LocationManagerWrapper;


/**
 * extend directly from wifi manager wrapper
 */
public class TestLocationManager extends LocationManagerWrapper {
    private static final String TAG = TestLocationManager.class.getSimpleName();
    
    private static final String TEST_PROVIDER = "ls_test";
    
    public Location mMocLoc;
    
    public TestLocationManager(ServiceContext context) {
        super(context.getAndroidContext());
        context.setTestLocationMan(this);
        Log.d(TAG, "TestLocationManager : constructor " );
    }
    
    /**
     * set the moc location for use
     */
    public Location setMocLocation(double lat, double lng, float accu){
        mMocLoc = new Location(LocationManager.NETWORK_PROVIDER);
        mMocLoc.setLatitude(lat);
        mMocLoc.setLongitude(lng);
        mMocLoc.setAccuracy(accu);
        return mMocLoc;
    }

    public LocationProvider getProvider(String name){
        return super.getProvider(name);
    }
    
    public void addTestProvider() {
        if( getProvider(TEST_PROVIDER) == null){
            addTestProvider(TEST_PROVIDER, false, false, false, false, false, false, false, 0, 0);
            Log.d(TAG, " TestLocationManager : add test provider " );
        }
    }
    
    /**
     * remove the test provider
     */
    public void removeTestProvider(){
        super.removeTestProvider(TEST_PROVIDER);
    }
    
    /**
     * intercept the request location update using test provider
     */
    @Override
    public void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener, Looper looper){
        setTestProviderEnabled(TEST_PROVIDER, true);
        if( mMocLoc != null){
            Log.d(TAG, " requestLocationUpdates :  " + provider + " loc : " + mMocLoc.toString());
            setTestProviderLocation(TEST_PROVIDER, mMocLoc);
            //setTestProviderLocation(LocationManager.NETWORK_PROVIDER, mMocLoc);
            listener.onLocationChanged(mMocLoc);    // direct call back the registered listener.
        }
    }
   
}
