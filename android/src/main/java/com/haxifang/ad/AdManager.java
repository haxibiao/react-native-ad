package com.haxifang.ad;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class AdManager extends ReactContextBaseJavaModule {

    private ReactApplicationContext reactContext;
    protected String REACT_CLASS = "AdManager";
    protected String TAG = REACT_CLASS;

    public AdManager(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void init(String appid) {
        // 判断头条 SDK 是否初始化
        if (!TTAdManagerHolder.sInit) {
            TTAdManagerHolder.init(reactContext, appid);
        }
    }


}
