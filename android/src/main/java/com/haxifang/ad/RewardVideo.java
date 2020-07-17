package com.haxifang.ad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.haxifang.ad.modules.RewardActivity;

public class RewardVideo extends ReactContextBaseJavaModule {

    final private static String REACT_CLASS = "RewardVideo";

    private static ReactApplicationContext mContext;
    public static Promise promise;

    public RewardVideo(ReactApplicationContext context) {
        super(context);
        mContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void startAd(ReadableMap options, final Promise promise) {

        if(mContext == null) {
            // 上下文未初始化，跳出加载广告方法，避免闪退问题
            return;
        }

        TTAdManagerHolder.setPromise(promise);

        String appid = options.hasKey("appid") ? options.getString("appid") : null;
        String codeid = options.hasKey("codeid") ? options.getString("codeid") : null;
        // Log.d(TAG, "startAd:  appid: " + appid + ", codeid: " + codeid);


        // 判断头条 SDK 是否初始化
        if (!TTAdManagerHolder.sInit) {
            TTAdManagerHolder.init(mContext, appid);
        }

        // 启动激励视频广告页面
        RewardActivity.startActivity(mContext.getCurrentActivity(), codeid);

    }

    // 回调通知
    public static void sendEvent(String eventName, @Nullable WritableMap params) {
        mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(REACT_CLASS + "-" + eventName, params);
    }

}
