
package com.androidservicewrapper.test.mockobjects;

import android.app.PendingIntent;
import android.util.Log;

import com.androidservicewrapper.mock.MockAlarmManager;


public class TestAlarmManager extends MockAlarmManager {
    private static final String TAG = TestAlarmManager.class.getSimpleName();
    
    public TestAlarmManager(ServiceContext context) {
        super(context.getAndroidContext());
        context.setAlarmMan(this);
    }
    
    /**
     * intercept the call
     */
    public void setInexactRepeating(int type, long triggerAtTime, long interval,
            PendingIntent operation) {
        // stub out
        // mAlarmManager.setInexactRepeating(type, triggerAtTime, interval, operation);
        Log.d(TAG, " alarm : setInexactRepeating : " + interval);
        ServiceContext.waitFor(5000);   // wait for 5 seconds
        try{
            operation.send();
        }catch(Exception e){
            Log.e(TAG, e.toString());
        }
    }
}
