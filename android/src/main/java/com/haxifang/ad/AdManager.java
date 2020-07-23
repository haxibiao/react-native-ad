package com.haxifang.ad;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

public class AdManager extends ReactContextBaseJavaModule {
    public static ReactApplicationContext reactAppContext;
    protected String TAG = "AdManager";


    public AdManager(ReactApplicationContext reactContext) {
        super(reactAppContext);
        reactAppContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void init(ReadableMap options) {
		String appid = options.hasKey("appid") ? options.getString("appid") : null;

        // 判断头条 SDK 是否初始化
        if (!TTAdManagerHolder.sInit) {
            TTAdManagerHolder.init(reactAppContext, appid);
        }
    }

}
