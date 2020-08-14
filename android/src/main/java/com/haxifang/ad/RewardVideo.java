package com.haxifang.ad;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.haxifang.ad.activities.RewardActivity;
import com.haxifang.ad.utils.TToast;

public class RewardVideo extends ReactContextBaseJavaModule {

    final private static String TAG = "RewardVideo";
    private static ReactApplicationContext mContext;
    public static Promise promise;

    public RewardVideo(ReactApplicationContext context) {
        super(context);
        mContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void startAd(ReadableMap options, final Promise promise) {

        //拿到参数
        String appId = options.getString("appid"); //可空
        String codeId = options.getString("codeid");
        Log.d(TAG, "startAd:  appId: " + appId + ", codeId: " + codeId);

        //准备激励回调
        AdBoss.prepareReward(promise, mContext, appId);

        // 启动激励视频页面
        startTT(codeId);
    }


    /**
     * 启动穿山甲激励视频
     *
     * @param codeId
     */
    public static void startTT(String codeId) {
        Activity context = mContext.getCurrentActivity();
        try {
            Intent intent = new Intent(mContext, RewardActivity.class);
            intent.putExtra("codeId", codeId);
            // 不要过渡动画
            context.overridePendingTransition(0, 0);
            context.startActivityForResult(intent, 10000);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "start reward Activity error: ", e);
        }
    }

    /**
     * 启动优量汇激励视频
     *
     * @param codeId
     */
    public static void startTx(String codeId) {
        String appId = AdBoss.tx_appid;
        final String message = "启动腾讯激励视频";
        Log.d(TAG, message + "AppID：" + appId + "  codeID: " + codeId);
        Activity ac = mContext.getCurrentActivity();
        if (ac != null) {
            ac.runOnUiThread(() -> {
                TToast.show(mContext, message);
                Intent intent = new Intent(mContext, com.haxifang.ad.activities.RewardActivity.class);
                intent.putExtra("appid", appId);
                intent.putExtra("codeid", codeId);
                ac.startActivity(intent);
            });
        }
    }

    // 发送事件到RN
    public static void sendEvent(String eventName, @Nullable WritableMap params) {
        mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(TAG + "-" + eventName, params);
    }

}
