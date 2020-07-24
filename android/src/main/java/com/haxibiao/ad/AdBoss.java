package com.haxibiao.ad;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;


import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContext;
import com.haxibiao.ad.TTAdManagerHolder;



public class AdBoss {

    public static String TAG = "AdBoss";

    public static String tt_appid;
    public static String tx_appid;
    public static String bd_appid;

    // AdManager
    public static TTAdManager ttAdManager;
    public static TTAdNative mTTAdNative;


    // 缓存加载的头条广告
    public static TTRewardVideoAd rewardAd;
    public static TTFullScreenVideoAd fullAd;
    public static TTNativeExpressAd feedAd;



    // 存激励视频，全屏视频的回调
    public static Promise rewardPromise;
    public static Activity rewardActivity;

    public static Boolean is_show = false, is_click = false, is_close = false, is_reward = false;
    public static Boolean is_download_idle = false;
    public static boolean is_download_active = false;
    public static boolean is_install = false;

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
    public static String feed_provider = "";
    public static String reward_provider = "";
    public static String splash_provider = "";

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

    public static Context mContext;
    public static ReactContext reactContext;
    public static ArrayBlockingQueue<String> myBlockingQueue = new ArrayBlockingQueue<String>(1);

    public static void initTT(Context context, String appId) {
        mContext = context;
        if (context.getClass().getName() == "ReactApplicationContext") {
            reactContext = (ReactContext) context;
        }

        tt_appid = appId;
        Log.d(TAG, "tt_appid:" + tt_appid);

        // step1: 初始化sdk appid
        TTAdManagerHolder.init(context, appId);

        // step2:创建TTAdNative对象，createAdNative(Context context)
        // feed广告context需要传入Activity对象
        ttAdManager = TTAdManagerHolder.get();
        mTTAdNative = ttAdManager.createAdNative(context);

        // step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        // 换到激励视频时才调用
        // ttAdManager.requestPermissionIfNecessary(mContext);

    }

    public static void initTx(Context context, String appId) {
        mContext = context;
        tx_appid = appId;
        Log.d(TAG, "tx_appid:" + tx_appid);

        // 初始化TX sdk appid 无需额外操作...
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

    /**
     * 提前加载广告，避免第一次和弹层时，加载的有些慢
     */
    public static void loadFeedAd(String codeId, float width) {
        if (feed_provider.equals("腾讯")) {
//            loadTxFeedAd(codeId, width);
            return;
        }
        if (feed_provider.equals("百度")) {
            //百度的是横幅banner，不需要预加载
            return;
        }
        loadTTFeedAd(codeId, width);
    }


    /**
     * 加载穿山甲的信息流广告
     * @param codeId
     * @param width
     */
    private static void loadTTFeedAd(String codeId, float width) {
        // step4:创建广告请求参数AdSlot,具体参数含义参考文档
        float expressViewWidth = width > 0 ? width : 280; // 默认宽度，兼容大部分弹层的宽度即可
        float expressViewHeight = 0; // 自动高度

        AdSlot adSlot = new AdSlot.Builder().setCodeId(codeId) // 广告位id
                .setSupportDeepLink(true).setAdCount(1) // 请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) // 期望模板广告view的size,单位dp,高度0自适应
                .setImageAcceptedSize(640, 320).setNativeAdType(AdSlot.TYPE_INTERACTION_AD) // 坑啊，不设置这个，feed广告native出不来，一直差量无效，文档太烂
                .build();

        // step5:请求广告，对请求回调的广告作渲染处理
        AdBoss.mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.d(TAG, message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                Log.d(TAG, "onNativeExpressAdLoad: FeedAd !!!");
                if (ads == null || ads.size() == 0) {
                    return;
                }
                // 缓存加载成功的信息流广告
                Activity ac = reactContext.getCurrentActivity();
                if (ac != null) {
                    ac.runOnUiThread(() -> {
                        feedAd = ads.get(0);
                    });
                }
            }
        });
    }

}
