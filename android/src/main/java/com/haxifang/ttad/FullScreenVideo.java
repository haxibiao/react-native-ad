package com.haxifang.ttad;


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
import com.haxifang.ttad.modules.FullScreenActivity;
import com.haxifang.ttad.modules.SplashActivity;


public class FullScreenVideo extends ReactContextBaseJavaModule {

    final private String TAG = "hxb-rn-FullScreen";
    protected ReactApplicationContext mContext;

    public FullScreenVideo(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    public String getName() {
        return "TTFullScreenVideo";
    }

//    @ReactMethod
//    public void loadAd(ReadableMap options, final Promise promise) {
//        mContext.getCurrentActivity().runOnUiThread(() -> {
//            // 加载广告
//            String codeid = options.hasKey("codeid") ? options.getString("codeid") : null;
//            if(!codeid.isEmpty()) {
//                loadAdSlot(codeid, TTAdConstant.VERTICAL, promise);
//            }
//        });
//    }




    @ReactMethod
    public void startAd(ReadableMap options, final Promise promise) {

        TTAdManagerHolder.setPromise(promise);

        String appid = options.hasKey("appid") ? options.getString("appid") : null;
        String codeid = options.hasKey("codeid") ? options.getString("codeid") : null;

        // Log.d(TAG, "startAd:  appid: " + appid + ", codeid: " + codeid);


        // 判断头条 SDK 是否初始化
        if(!TTAdManagerHolder.sInit) {
            TTAdManagerHolder.init(mContext,appid);
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



}
