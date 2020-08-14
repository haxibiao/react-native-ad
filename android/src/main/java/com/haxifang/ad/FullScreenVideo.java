package com.haxifang.ad;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.haxifang.ad.activities.FullScreenActivity;


public class FullScreenVideo extends ReactContextBaseJavaModule {

    final private static String TAG = "FullScreenVideo";
    protected static ReactApplicationContext mContext;

    public FullScreenVideo(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    public String getName() {
        return TAG;
    }
    
    @ReactMethod
    public void startAd(ReadableMap options, final Promise promise) {

        String appId = options.getString("appid");
        String codeId = options.getString("codeid");

        AdBoss.prepareReward(promise,mContext, appId);

        Intent intent = new Intent(mContext, FullScreenActivity.class);
        try {
            intent.putExtra("codeId", codeId);
            final Activity context = getCurrentActivity();
            // 不要过渡动画
            context.overridePendingTransition(0, 0);
            context.startActivityForResult(intent, 10000);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "start FullScreen Activity error: ", e);
        }
    }

    // 发送事件到RN
    public static void sendEvent(String eventName, @Nullable WritableMap params) {
        mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TAG + "-" + eventName, params);
    }
}
