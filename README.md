# Android Service Wrapper

Android Service Wrapper is a wrapper library on top of android framework service to support writing clean code for use case testing.

## Android platform is not friendly for testing.

We have been watching clean code talks from google; However, Android implementation is not test friendly.

* System service lookup for android framework singleton services.
* private constructors for many service objects and state objects, e.g., Location, CellLocation. etc.

Because of the singleton service object lookup and the lacking of public default constructor, it is hard to write either isolated unit testing code as well as use case testing code.

## The solution

"All problems in computer science can be solved by another level of indirection"

Android Service Wrapper introduce an abstract layer on top of Android platform system layer to provide an mechanism to facilitate writing use case testing code. 
It intercepts getSystemService() API calls and returns a wrapped service object to the Application. 
Following calls from application to service through the service object can either be delegated to the real system service when running in normally case or to the mocked system service when running in use case testing case.
Android Service Wrapper is a library and currently wraps some common Android platform system services, including LocationService, WifiManagerService, TelephonyService, AlarmService, etc. 

In application code, to use a Android platform service, rather than calling standard Android API getSystemService, just call Android Service wrapper AndroidSystemServiceWrapper.getSystemService() to get the service object. The object will delegate all the API calls to either Android system service or to your mock service object depending on your setup. 

With the introducing of an indirect layer, we decouple the tight dependence between android App and android system service, hence facilitate use case testing.

## Components

Wrapper for some basic Android system service.

* AndroidSystemServiceWrapper
* LocationManagerWrapper
* TelephonyManagerWrapper
* WifiManagerWrapper
* PowerManagerWrapper
* More to be added...

Mock Service inheritants Service Wrapper and throws UnsupportedOperationException for all APIs by default.
Override the API you intent to test with your mock data for testing.

## Example

To get LocationManager from LocationService, do the following:

'''java
    import com.com.androidservicewrapper.AndroidServiceWrapper;
    import com.com.androidservicewrapper.LocationManagerWrapper;

    private LocationManagerWrapper mLocationManager;

    mLocationManager = (LocationManagerWrapper)AndroidServiceWrapper.getSystemService(
        mContext, Context.LOCATION_SERVICE);

    mLocationManager.requestLocationUpdate(...);

'''

To setup testing, create context for testing, and add your service mock to the context.
Once testing context is setup with mock service, all service API calls will be directed to your mock.

'''java

    mTestContext = new ServiceContext(getSystemContext());  
    mTestLocMan = new TestLocationManager(mTestContext);
    mTestTelMan = new TestTelephonyManager(mTestContext);
    mTestLocMan = new TestLocationManager(mTestContext);
    mTestWifiMan = new TestWifiManager(mTestContext);
    mTestPowMan = new TestPowerManager(mTestContext);
    mTestAlarmMan = new TestAlarmManager(mTestContext);
                                                 
    AndroidServiceWrapper.setMockSystemService(Context.TELEPHONY_SERVICE, mTestTelMan);
    AndroidServiceWrapper.setMockSystemService(Context.WIFI_SERVICE, mTestWifiMan);
    AndroidServiceWrapper.setMockSystemService(Context.ALARM_SERVICE, mTestAlarmMan);
    AndroidServiceWrapper.setMockSystemService(Context.LOCATION_SERVICE, mTestLocMan);

'''

Your test service object extends mock service wrapper and override the functions you want to mock.

'''java

    public class TestTelephonyManager extends MockTelephonyManager {
        @Override
        public int getDataState(){
            return TelephonyManager.DATA_CONNECTED;
        }
    }

'''

Package the wrapper layer into jar lib so both your appliation and your test app can share with it.
For more details, please refer to the test project.


## Extra

How to create an andriod lib project to package system service framework into a lib ?

    android create lib-project --name SystemService --target 14 --path . --package my-package

