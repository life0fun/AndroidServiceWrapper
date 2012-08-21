package com.androidservicewrapper.test.mockobjects;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.androidservicewrapper.TelephonyManagerWrapper;

import java.util.LinkedList;
import java.util.List;

public class TestTelephonyManager extends TelephonyManagerWrapper {
    private static final String TAG = TestTelephonyManager.class.getSimpleName();
    
    private final List<PhoneStateListener> mListenerList = new LinkedList<PhoneStateListener>();
    private static final long MAX_WAIT_TIME = 1000;

    public TestTelephonyManager(ServiceContext context) {
        super(context.getAndroidContext());
        context.setTestTelMan(this);
    }

    @Override
    public void listen(PhoneStateListener listener, int events) {
        synchronized(mListenerList) {
            if (listener != null) {
                if (events != PhoneStateListener.LISTEN_NONE) {
                    mListenerList.add(listener);
                } else {
                    mListenerList.remove(listener);
                }
            }
            mListenerList.notify();
        }
    }
    
    public PhoneStateListener getListener() {
        PhoneStateListener listener = null;
        synchronized(mListenerList) {
            if (mListenerList.isEmpty()) {
                try {
                    mListenerList.wait(MAX_WAIT_TIME);
                } catch (InterruptedException e) {
                    Log.e(TAG, "getListener() interrupted.");
                }
            }
        }
        if (!mListenerList.isEmpty()) {
            listener = mListenerList.remove(0);
        }
        return listener;
    }
    
    /**
     * override this so we can take location fix in.
     */
    @Override
    public int getDataState(){
        return TelephonyManager.DATA_CONNECTED;
    }

}
