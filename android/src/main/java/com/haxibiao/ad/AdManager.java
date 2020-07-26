package com.haxibiao.ad;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

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
		//默认头条穿山甲
		AdBoss.tt_appid = options.hasKey("appid") ? options.getString("appid") : AdBoss.tt_appid;
		AdBoss.init(reactAppContext, AdBoss.tt_appid);
		
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
     * 方便从RN主动预加载第一个广告，避免用户第一个签到的信息流广告加载+图片显示感觉很慢 （需要注意在展示弹层前才预加载）
     */
    @ReactMethod
    public void loadFeedAd(ReadableMap options) {
        String codeid = options.hasKey("codeid") ? options.getString("codeid") : null;
        int width = options.hasKey("width") ? options.getInt("width") : 0;
        Log.d(TAG, "loadFeedAd codeid " + codeid + " width:" + width);
        AdBoss.loadFeedAd(codeid, width);
    }

    /**
     * 主动看激励视频时，才检查这个权限
     */
    @ReactMethod
    public void requestPermission() {
        // step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        AdBoss.ttAdManager.requestPermissionIfNecessary(reactAppContext);
    }

}
