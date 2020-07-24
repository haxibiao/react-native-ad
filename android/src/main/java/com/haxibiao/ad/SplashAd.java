package com.haxibiao.ad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.haxibiao.ad.activities.SplashActivity;

public class SplashAd extends ReactContextBaseJavaModule {

    String TAG = "SplashAd";
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
    public void loadSplashAd(ReadableMap options) {
		String appid = options.hasKey("appid") ? options.getString("appid") : null;
		String codeid = options.hasKey("codeid") ? options.getString("codeid") : null;
		String provider = options.hasKey("provider") ? options.getString("provider") : "头条";

        Log.d(TAG, "provider:" + provider + ", codeid:" + codeid);

        if (provider.equals("腾讯")) {
			// SDK 初始化
        	AdBoss.initTx(mContext, appid);
			 startTxSplash(AdBoss.codeid_splash_tencent);
			 return;
		}

        if (provider.equals("百度")) {
			// SDK 初始化
        	AdBoss.initBd(mContext, appid);
			 startBdSplash(AdBoss.codeid_splash_baidu);
			 return;
        }

        // 默认走穿山甲
		AdBoss.initTT(mContext, appid);
		startSplash(codeid);
	}
	
	private void startSplash(String codeid) {
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

    private void startTxSplash(String codeid) {
//        Intent intent = new Intent(mContext, com.haxibiao.ad.activities.tencent.SplashActivity.class);
//        try {
//            String appid = AdBoss.tx_appid;
//            // String codeid = "8863364436303842593";
//            Log.d(TAG, "loadSplashAd: codeid=" + codeid + " appid=" + appid);
//            intent.putExtra("appid", appid);
//            intent.putExtra("codeid", codeid);
//            final Activity context = getCurrentActivity();
//            context.overridePendingTransition(0, 0); // 不要过渡动画
//            context.startActivityForResult(intent, 10000);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d(TAG, "loadSplashAd: " + e.getMessage());
//        }
    }

    private void startBdSplash(String codeid) {
        // Log.d(TAG, "百度开屏广告 codeid:" + codeid);
        // Intent intent = new Intent(mContext, BDSplashActivity.class);
        // try {
        //     intent.putExtra("codeid", codeid);
        //     final Activity context = getCurrentActivity();
        //     context.overridePendingTransition(0, 0); // 不要过渡动画
        //     context.startActivityForResult(intent, 10000);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }
}
