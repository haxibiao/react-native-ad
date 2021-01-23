package com.haxifang.ad;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;

/**
 * 管理广告模块的核心对象
 */
public class AdBoss {

    public static String TAG = "AdBoss";

    public static String tt_appid;
    public static String tx_appid; //腾讯
    public static String bd_appid; //百度

    //头条广告init需要传的参数
    public static String userId = "";
    public static String appName = "穿山甲媒体APP";
    public static int rewardAmount = 1;
    public static String rewardName = "金币";

    // 头条广告sdk
    public static TTAdManager ttAdManager;
    public static TTAdNative TTAdSdk;

    // 缓存加载的头条广告数据
    public static TTRewardVideoAd rewardAd;
    public static TTFullScreenVideoAd fullAd;
    public static TTNativeExpressAd feedAd;

    // 存激励视频，全屏视频的回调
    public static Promise rewardPromise;
    public static Activity rewardActivity;

    //信息流广告回调
    public static Promise feedPromise;

    //激励视频类的状态
    public static boolean is_show = false;
    public static boolean is_click = false;
    public static boolean is_close = false;
    public static boolean is_reward = false;
    public static boolean is_download_idle = false;
    public static boolean is_download_active = false;
    public static boolean is_install = false;

    // SplashAd config
    public static int splashAd_anim_in = 0;
    public static int splashAd_anim_out = 0;

    /**
     * 准备新的激励(全屏)视频回调
     *
     * @param promise
     */
    public static void prepareReward(Promise promise, Context context, String appId) {
        rewardPromise = promise;
        resetRewardResult();
        init(context, appId);
    }

    /**
     * 关联页面，返回页面用
     *
     * @param activity
     */
    public static void hookActivity(Activity activity) {
        rewardActivity = activity;
    }


    public static void resetRewardResult() {
        is_show = false;
        is_click = false;
        is_close = false;
        is_reward = false;
        is_download_idle = false;
        is_download_active = false;
        is_install = false;
    }

    public static String getRewardResult() {
        String json = "{\"video_play\":" + is_show + ",\"ad_click\":" + is_click + ",\"apk_install\":" + is_install + ",\"verify_status\":" + is_reward + "}";
        if (rewardPromise != null)
            rewardPromise.resolve(json); //返回当前窗口加载的
        if (rewardActivity != null) {
            rewardActivity.finish();
        }
        Log.d(TAG, "getRewardResult: " + json);
        return json;
    }

    //providers
    public static String feed_provider = "头条";
    public static String reward_provider = "头条";
    public static String splash_provider = "头条";

    //codeids
    public static String codeid_splash;
    public static String codeid_splash_tencent;
    public static String codeid_splash_baidu;
    public static String codeid_feed;
    public static String codeid_feed_tencent;
    public static String codeid_feed_baidu;
    public static String codeid_draw_video;
    public static String codeid_full_video;
    public static String codeid_reward_video;
    public static String codeid_reward_video_tencent;

    public static ReactContext reactContext;
    public static ArrayBlockingQueue<String> myBlockingQueue = new ArrayBlockingQueue<String>(1);

    public static void init(Context context, String appId) {

        if (TTAdSdk != null && tt_appid == appId) {
            //已初始化
            Log.d(TAG, "已初始化 TTAdSdk tt_appid " + tt_appid);
            return;
        }

        Log.d(TAG, "init feed tt_appid:" + tt_appid);
        if (appId == null || appId.isEmpty()) {
            return;
        }

        tt_appid = appId;
        if (context.getClass().getName() == "ReactApplicationContext") {
            reactContext = (ReactContext) context;
        }

        //初始化回调结果
        resetRewardResult();

//        runOnUiThread(() -> {

        // step1: 初始化sdk appid
        TTAdManagerHolder.init(context, appId);

        // step2:创建TTAdNative对象，createAdNative(Context context)
        // feed广告context需要传入Activity对象
        ttAdManager = TTAdManagerHolder.get();
        TTAdSdk = ttAdManager.createAdNative(context);
        Log.d(TAG, "TTAdSdk init: " + TTAdSdk);

        // step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        // 换到激励视频时才调用
        // ttAdManager.requestPermissionIfNecessary(mContext);

//        });

    }


    public static void initTx(Context context, String appId) {
        tx_appid = appId;
        Log.d(TAG, "tx_appid:" + tx_appid);

        // 初始化TX sdk   无需额外操作...
    }

    public static void initBd(Context context, String appId) {
//        mContext = context;
//        bd_appid = appId;
//        Log.d(TAG, "bd_appid:" + bd_appid);
//
//        // 初始化baidu sdk
//        BaiduManager.init(mContext);
//        AdView.setAppSid(context, bd_appid);
        // 注意：AdView.setAppsId还有用,被垃圾百度文档搞晕...
    }

}
