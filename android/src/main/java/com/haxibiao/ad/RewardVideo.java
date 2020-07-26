package com.haxibiao.ad;

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
import com.haxibiao.ad.activities.RewardActivity;
import com.haxibiao.ad.utils.TToast;

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

        if(mContext == null) {
            // 上下文未初始化，跳出加载广告方法，避免闪退问题
            return;
        }
        String appid = options.hasKey("appid") ? options.getString("appid") : null;
        String codeid = options.hasKey("codeid") ? options.getString("codeid") : null;
        // Log.d(TAG, "startAd:  appid: " + appid + ", codeid: " + codeid);

        // 判断头条 SDK 是否初始化
        if (!TTAdManagerHolder.sInit) {
            TTAdManagerHolder.init(mContext, appid);
		}
		
		AdBoss.rewardPromise = promise;

        // 启动激励视频广告页面
        RewardActivity.startActivity(mContext.getCurrentActivity(), codeid);

	}


    /**
     * 启动穿山甲激励视频
     * @param codeid
     */
    public static void startTT(String codeid) {
        Activity ac = mContext.getCurrentActivity();
        if (ac != null) {
            ac.runOnUiThread(() -> {
                Intent intent = new Intent(mContext, RewardActivity.class);
                ac.startActivityForResult(intent, 10000);
            });
        }
    }

    /**
     * 启动优量汇激励视频
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
                Intent intent = new Intent(mContext, com.haxibiao.ad.activities.RewardActivity.class);
                intent.putExtra("appid", appId);
                intent.putExtra("codeid", codeId);
                ac.startActivity(intent);
            });
        }
    }

    // 发送事件到RN
    public static void sendEvent(String eventName, @Nullable WritableMap params) {
        mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TAG + "-" + eventName, params);
    }

}
