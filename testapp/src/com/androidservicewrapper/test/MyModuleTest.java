package com.androidservicewrapper.test;

import android.util.Log;

import com.androidservicewrapper.test.mockobjects.ServiceContext;

public class MyModuleTest {
	
	private static final String TAG = MyModuleTest.class.getSimpleName();
	
	ServiceContext mTestContext;
	
	private TestSuite mTestSuite;
	
	public MyModuleTest(ServiceContext testctx) {
	    mTestContext = testctx;
		mTestSuite = new TestSuite(mTestContext);
		Log.d(TAG, " MyModuleTest Constructor done...created test suite!");
	}
	
	/**
	 * run all the test cases in the test suites
	 */
	public void runTestSuite() {
		Log.d(TAG, " runTestSuite : ");
		mTestSuite.run();
	}
}
