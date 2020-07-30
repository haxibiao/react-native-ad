package com.haxifang.ad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
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

        TTAdManagerHolder.setPromise(promise);

        String appid = options.hasKey("appid") ? options.getString("appid") : null;
        String codeid = options.hasKey("codeid") ? options.getString("codeid") : null;

        // Log.d(TAG, "startAd:  appid: " + appid + ", codeid: " + codeid);


        // 判断头条 SDK 是否初始化
        if (!TTAdManagerHolder.sInit) {
            TTAdManagerHolder.init(mContext, appid);
        }

        Intent intent = new Intent(mContext, FullScreenActivity.class);
        try {
            intent.putExtra("codeid", codeid);
            // intent.putExtra("promise", promise)
            final Activity context = getCurrentActivity();
            context.overridePendingTransition(0, 0); // 不要过渡动画
            context.startActivityForResult(intent, 10000);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "startAd: ", e);
        }
    }

    // 发送事件到RN
    public static void sendEvent(String eventName, @Nullable WritableMap params) {
        mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TAG + "-" + eventName, params);
    }
}
