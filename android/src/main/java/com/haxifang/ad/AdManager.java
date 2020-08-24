package com.haxifang.ad;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.List;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

public class AdManager extends ReactContextBaseJavaModule {
    public static ReactApplicationContext reactAppContext;
    final public static String TAG = "AdManager";


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
        //默认头条穿山甲
        AdBoss.tt_appid = options.hasKey("appid") ? options.getString("appid") : AdBoss.tt_appid;
        AdBoss.init(reactAppContext, AdBoss.tt_appid);

        //支持传参头条需要的userId和appName ...
        if (options.hasKey("uid")) {
            AdBoss.userId = options.getString("uid");
        }
        if (options.hasKey("app")) {
            AdBoss.appName = options.getString("app");
        }
        if (options.hasKey("amount")) {
            AdBoss.rewardAmount = options.getInt("amount");
        }
        if (options.hasKey("reward")) {
            AdBoss.rewardName = options.getString("reward");
        }

        // 支持一口气init所有需要的adConfig
        AdBoss.tx_appid = options.hasKey("tx_appid") ? options.getString("tx_appid") : AdBoss.tx_appid;
        AdBoss.initTx(reactAppContext, AdBoss.tx_appid);
        AdBoss.bd_appid = options.hasKey("bd_appid") ? options.getString("bd_appid") : AdBoss.bd_appid;
        AdBoss.initBd(reactAppContext, AdBoss.bd_appid);

        // providers
        AdBoss.splash_provider = options.hasKey("splash_provider") ? options.getString("splash_provider") : "头条";
        AdBoss.feed_provider = options.hasKey("feed_provider") ? options.getString("feed_provider") : "头条";
        AdBoss.reward_provider = options.hasKey("reward_provider") ? options.getString("reward_provider") : "头条";

        // codeids
        AdBoss.codeid_splash = options.hasKey("codeid_splash") ? options.getString("codeid_splash") : AdBoss.codeid_splash;
        AdBoss.codeid_splash_tencent = options.hasKey("codeid_splash_tencent") ? options.getString("codeid_splash_tencent") : AdBoss.codeid_splash_tencent;
        AdBoss.codeid_splash_baidu = options.hasKey("codeid_splash_baidu") ? options.getString("codeid_splash_baidu") : AdBoss.codeid_splash_baidu;
        AdBoss.codeid_feed = options.hasKey("codeid_feed") ? options.getString("codeid_feed") : AdBoss.codeid_feed;
        AdBoss.codeid_feed_tencent = options.hasKey("codeid_feed_tencent") ? options.getString("codeid_feed_tencent") : AdBoss.codeid_feed_tencent;
        AdBoss.codeid_feed_baidu = options.hasKey("codeid_feed_baidu") ? options.getString("codeid_feed_baidu") : AdBoss.codeid_feed_baidu;
        AdBoss.codeid_draw_video = options.hasKey("codeid_draw_video") ? options.getString("codeid_draw_video") : AdBoss.codeid_draw_video;
        AdBoss.codeid_full_video = options.hasKey("codeid_full_video") ? options.getString("codeid_full_video") : AdBoss.codeid_full_video;
        AdBoss.codeid_reward_video = options.hasKey("codeid_reward_video") ? options.getString("codeid_reward_video") : AdBoss.codeid_reward_video;
        AdBoss.codeid_reward_video_tencent = options.hasKey("codeid_reward_video_tencent") ? options.getString("codeid_reward_video_tencent") : AdBoss.codeid_reward_video_tencent;
    }

    /**
     * 方便从RN主动预加载第一个广告，避免用户第一个签到的信息流广告加载+图片显示感觉很慢
     * （需要注意在展示弹层前才预加载）
     */
    @ReactMethod
    public void loadFeedAd(ReadableMap options, final Promise promise) {
        String codeId = options.getString("codeId");
        int width = options.getInt("width");
        AdBoss.feedPromise = promise;
        if (AdBoss.feed_provider.equals("腾讯")) {
            //FIXME ...
            return;
        }
        if (AdBoss.feed_provider.equals("百度")) {
            //百度的是横幅banner，不需要预加载
            return;
        }
        loadTTFeedAd(codeId, width);
    }

    /**
     * 加载穿山甲的信息流广告
     *
     * @param codeId
     * @param width
     */
    private static void loadTTFeedAd(String codeId, float width) {
        // step4:创建广告请求参数AdSlot,具体参数含义参考文档
        // 默认宽度，兼容大部分弹层的宽度即可
        float expressViewWidth = width > 0 ? width : 280;
        float expressViewHeight = 0; // 自动高度

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) // 广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) // 请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) // 期望模板广告view的size,单位dp,高度0自适应
                .setImageAcceptedSize(640, 320).setNativeAdType(AdSlot.TYPE_INTERACTION_AD) // 坑啊，不设置这个，feed广告native出不来，一直差量无效，文档太烂
                .build();

        // step5:请求广告，对请求回调的广告作渲染处理
        AdBoss.TTAdSdk.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.d(TAG, message);
                AdBoss.feedPromise.reject("101","feed ad error" + message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                Log.d(TAG, "onNativeExpressAdLoad: FeedAd !!!");
                if (ads == null || ads.size() == 0) {
                    return;
                }
                // 缓存加载成功的信息流广告
                AdBoss.feedAd = ads.get(0);
                AdBoss.feedPromise.resolve(true);
            }
        });
    }

    /**
     * 主动看激励视频时，才检查这个权限
     */
    @ReactMethod
    public void requestPermission() {
        // step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        AdBoss.ttAdManager.requestPermissionIfNecessary(reactAppContext);
    }


    /**
     *  预加载一个激励视频广告
     */
    @ReactMethod
    public void loadRewardAd(ReadableMap options, final Promise promise) {
        String codeId = options.getString("codeId");
        AdBoss.rewardAdPromise = promise;

        loadTTRewardAd(codeId);
    }

    private static void loadTTRewardAd(String codeId) {

        // 创建广告请求参数 AdSlot, 具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName(AdBoss.rewardName) // 奖励的名称
                .setRewardAmount(AdBoss.rewardAmount) // 奖励的数量
                .setUserID(AdBoss.userId)// 用户id,必传参数
                .setMediaExtra("media_extra") // 附加参数，可选
                .setOrientation(TTAdConstant.VERTICAL) // 必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();

        //FIXME:  穿山甲需要全面替换 express 模式
        // 请求广告
        AdBoss.TTAdSdk.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.d("reward onError ", message);
                AdBoss.rewardAdPromise.reject("1002", "loadRewardVideoAd ad error" + message);
            }

            // 视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                Log.d("reward Cached ", "穿山甲激励视频缓存成功");
            }

            // 视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                Log.d("reward AdLoad ", ad.toString());
                sendEvent("AdLoaded", null);
                AdBoss.rewardAd = ad;
                AdBoss.rewardAdPromise.resolve(true);
            }
        });
    }

}
