package com.haxifang;

import android.app.Application;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.reyun.tracking.sdk.Tracking;

public class AdModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public AdModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "AdModule";
    }

    @ReactMethod
    public void initRY(String key, Callback callback) {
//        Tracking.setDebugMode(true);
//        String key = "844fa6a32c83a5144b441d49c33aeddf";
        Tracking.initWithKeyAndChannelId((Application)reactContext.getApplicationContext(),key,"_default_");
        callback.invoke("Tracking done init 热云 sdk ... " );
    }

    @ReactMethod
    public void trackRYEvent(String eventName, Callback callback) {
        Log.d("Tracking","Tracking Received 热云 track eventName: " + eventName);
        Tracking.setEvent(eventName);
        callback.invoke("Tracking Received 热云 track eventName: " + eventName );
    }

}
