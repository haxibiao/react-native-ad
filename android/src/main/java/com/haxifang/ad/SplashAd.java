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
import com.haxifang.ad.activities.SplashActivity;

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
        String anim = options.hasKey("anim") ? options.getString("anim") : "default";

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

        // 设置开屏广告启动动画
        setAnim(anim);

        // 默认走穿山甲
//		AdBoss.init(mContext, appid);
        startSplash(codeid);
    }

    private void startSplash(String codeid) {
        Intent intent = new Intent(mContext, SplashActivity.class);
        try {
            intent.putExtra("codeid", codeid);
            final Activity context = getCurrentActivity();
            context.startActivity(intent);

            if (AdBoss.splashAd_anim_in != -1) {
                // 实现广告开启跳转 Activity 动画设置
                context.overridePendingTransition(AdBoss.splashAd_anim_in, AdBoss.splashAd_anim_out);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTxSplash(String codeid) {
//        Intent intent = new Intent(mContext, com.haxifang.ad.activities.tencent.SplashActivity.class);
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

    private void setAnim(String animStr) {
        switch (animStr) {
            case "catalyst":
                AdBoss.splashAd_anim_in = com.haxifang.R.anim.catalyst_slide_up;
                AdBoss.splashAd_anim_out = com.haxifang.R.anim.catalyst_slide_down;
                break;
            case "none":
                AdBoss.splashAd_anim_in = 0;
                AdBoss.splashAd_anim_out = 0;
                break;
            case "slide":
                AdBoss.splashAd_anim_in = android.R.anim.slide_in_left;
                AdBoss.splashAd_anim_out = android.R.anim.slide_out_right;
                break;
            case "fade":
                AdBoss.splashAd_anim_in = android.R.anim.fade_in;
                AdBoss.splashAd_anim_out = android.R.anim.fade_in;
                break;
            default:
                AdBoss.splashAd_anim_in = -1;
                AdBoss.splashAd_anim_out = -1;
                break;
        }
    }

}
