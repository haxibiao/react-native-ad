package com.haxifang.ad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.haxifang.ad.modules.SplashActivity;

public class SplashAd extends ReactContextBaseJavaModule {

    String TAG = "天真的调试";
    ReactApplicationContext mContext;

    public SplashAd(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "SplashAd";
    }

    @ReactMethod
    public void loadSplashAd(String appid, String codeid) {

        // 判断头条 SDK 是否初始化
        if(!TTAdManagerHolder.sInit) {
            TTAdManagerHolder.init(mContext,appid);
        }

        Intent intent = new Intent(mContext, SplashActivity.class);
        try {
            intent.putExtra("codeid", codeid);
            final Activity context = getCurrentActivity();
            context.overridePendingTransition(0, 0); // 不要过渡动画
            context.startActivityForResult(intent, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
