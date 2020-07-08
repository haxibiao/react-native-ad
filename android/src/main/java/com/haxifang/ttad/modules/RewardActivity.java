package com.haxifang.ttad.modules;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.haxifang.R;
import com.haxifang.ttad.RewardVideo;
import com.haxifang.ttad.TTAdManagerHolder;

import static com.haxifang.ttad.RewardVideo.sendEvent;

public class RewardActivity extends Activity {

    final private static String TAG = "hxb-rn-RewardVideo";
    private Promise rewardPromise;
    private Activity rewardActivity;
    private TTAdNative mTTAdNative;

    private boolean is_show = false,
            is_click = false,
            is_install = false,
            is_reward = false,
            is_close = false,
            is_download_idle = false,
            is_download_active = false;

    // 启动激励视频页面
    public static void startActivity(Activity context, String codeId) {
        if (context != null) {
            context.runOnUiThread(() -> {
                Intent intent = new Intent(context, RewardActivity.class);
                try {
                    intent.putExtra("codeId", codeId);
                    context.overridePendingTransition(0, 0); // 不要过渡动画
                    context.startActivityForResult(intent, 10000);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "startActivity: ", e);
                }
            });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tt_view_video_hxb);
        rewardActivity = this;
        rewardPromise = TTAdManagerHolder.mPromise;

        // 读取 code id
        Bundle extras = getIntent().getExtras();
        String codeId = extras.getString("codeId");

        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);

        // 开始加载广告
        loadAd(codeId);

    }

    private void loadAd(String codeId) {
        if (codeId.isEmpty()) {
            // 广告位 CodeId 未传, 抛出异常
            getRewardResult();

            RNCallBack("onAdError", 1001, "广告位 CodeId 未传");

            if (RewardVideo.promise != null)
                RewardVideo.promise.resolve(false);
            return;
        }

        // 创建广告请求参数 AdSlot, 具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder().setCodeId(codeId).setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") // 奖励的名称 //暂时不都显示
                .setRewardAmount(1) // 奖励的数量 //暂时不都显示
                .setUserID("1")// 用户id,必传参数 //暂时不都显示
                .setMediaExtra("media_extra") // 附加参数，可选
                .setOrientation(TTAdConstant.VERTICAL) // 必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();

        // 请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.d("reward onError ", message);

                RNCallBack("onAdError", 1002, message);

                if (RewardVideo.promise != null)
                    RewardVideo.promise.resolve(false);
            }

            // 视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                Log.d("reward Cached ", "穿山甲激励视频缓存成功");

                RNCallBack("onAdVideoCached", 201, "穿山甲激励视频缓存成功");

            }

            // 视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                Log.d("reward AdLoad ", ad.toString());
                sendEvent("AdLoaded", null);

                RNCallBack("onAdLoaded", 200, "视频广告的素材加载完毕");

                // 展示加载成功的广告
                showAd(ad);

                if (RewardVideo.promise != null) {
                    RewardVideo.promise.resolve(true);
                }
            }
        });
    }

    private void showAd(TTRewardVideoAd ad) {
        // 激励视频必须全屏 activity 无法加底部...
        final RewardActivity _this = this;

        if (ad == null) {
            String msg = "头条奖励视频还没加载好,请先加载...";
            Log.d(TAG, msg);
            // TToast.show(this, msg);

            RNCallBack("onAdError", 1003, msg);


            finish();
            return;
        }

        // ad.setShowDownLoadBar(false);
        ad.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

            @Override
            public void onAdShow() {
                String msg = "开始展示奖励视频";
                // TToast.show(_this, msg);
                Log.d(TAG, msg);

                RNCallBack("onAdLoaded", 202, msg);

                is_show = true;
            }

            @Override
            public void onAdVideoBarClick() {
                is_click = true;
                String msg = "头条奖励视频查看成功,奖励即将发放";
                // TToast.show(_this, msg);
                Log.d(TAG, msg);

                RNCallBack("onAdClicked", 203, msg);


            }

            @Override
            public void onAdClose() {
                is_close = true;

                RNCallBack("onAdClose", 204, "关闭激励视频");

                getRewardResult();
            }

            // 视频播放完成回调
            @Override
            public void onVideoComplete() {
                is_show = true;
                String msg = "头条奖励视频成功播放完成";
                // TToast.show(_this, msg);
                Log.d(TAG, msg);

                RNCallBack("onVideoComplete", 205, msg);

            }

            @Override
            public void onVideoError() {
                // TToast.show(_this, "奖励视频出错了...");
                RNCallBack("onAdError", 1004, "激励视频播放出错了");
            }

            // 视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励数量，rewardName：奖励名称
            @Override
            public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                if (rewardVerify) {
                    // TToast.show(_this, "验证:成功  数量:" + rewardAmount + " 奖励:" + rewardName, Toast.LENGTH_LONG);
                } else {
                    // TToast.show(_this, "头条激励视频验证:" + "失败 ...", Toast.LENGTH_SHORT);
                }
                is_reward = true;
            }

            @Override
            public void onSkippedVideo() {
                // TToast.show(_this, "rewardVideoAd has onSkippedVideo");
                is_show = false; //激励视频不允许跳过...
            }
        });

        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                is_download_idle = true;
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!is_download_active) {
                    is_download_active = true;
                    // TToast.show(_this, "下载中，点击下载区域暂停", Toast.LENGTH_LONG);
                    RNCallBack("onDownloadActive", 300, "下载中，点击下载区域暂停");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                // TToast.show(_this, "下载暂停，点击下载区域继续", Toast.LENGTH_LONG);
                RNCallBack("onDownloadActive", 301, "下载暂停，点击下载区域继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                // TToast.show(_this, "下载失败，点击下载区域重新下载", Toast.LENGTH_LONG);
                RNCallBack("onDownloadActive", 304, "下载失败，点击下载区域重新下载");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                // TToast.show(_this, "下载完成，点击下载区域重新下载", Toast.LENGTH_LONG);
                RNCallBack("onDownloadActive", 302, "下载完成，点击下载区域重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                String msg = "安装完成，点击下载区域打开";
                // TToast.show(_this, msg);
                Log.d(TAG, "onInstalled: " + msg);

                RNCallBack("onDownloadActive", 303, msg);

                is_install = true;
            }
        });

        // 开始显示广告,会铺满全屏...
        ad.showRewardVideoAd(_this);
    }

    public String getRewardResult() {
        String json = "{\"video_play\":" + is_show + ",\"ad_click\":" + is_click + ",\"apk_install\":" + is_install + ",\"verify_status\":" + is_reward + "}";
        if (rewardPromise != null)
            rewardPromise.resolve(json); //返回当前窗口加载的
        if (rewardActivity != null) {
            rewardActivity.finish();
        }
        Log.d(TAG, "getRewardResult: " + json);
        return json;
    }

    // 二次封装回调函数
    public void RNCallBack(String eventName, int startCode, String message) {
        WritableMap p = Arguments.createMap();
        p.putInt("code", startCode);
        p.putString("message", message);
        sendEvent(eventName, p);
    }

}
