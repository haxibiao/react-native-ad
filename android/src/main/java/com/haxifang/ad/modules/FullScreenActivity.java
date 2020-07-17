package com.haxifang.ad.modules;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.facebook.react.bridge.Promise;
import com.haxifang.R;
import com.haxifang.ad.TTAdManagerHolder;

public class FullScreenActivity extends Activity {

    final private String TAG = "hxb-rn-FullScreen";

    private Promise rewardPromise;
    private Activity rewardActivity;
    private TTAdNative mTTAdNative;
    private TTFullScreenVideoAd fullAd;
    private boolean is_show = false, is_click = false, is_install = false, is_reward = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tt_view_video_hxb);
        rewardActivity = this;
        rewardPromise = TTAdManagerHolder.mPromise;

        // 读取 code id
        Bundle extras = getIntent().getExtras();
        String codeid = extras.getString("codeid");

        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        loadAdSlot(codeid, TTAdConstant.VERTICAL, rewardPromise);
    }

    private void loadAdSlot(String codeid, int orientation, final Promise promise) {
        // 创建广告请求参数 AdSlot ,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeid)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(orientation) // 必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        // 请求广告
        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                // TToast.show(mContext, message);
            }

            @Override
            public void onFullScreenVideoCached() {
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                // 缓存加载的全屏广告
                fullAd = ad;
//                if (promise != null) {
//                    promise.resolve(true);
//                }
                showAd();
            }
        });
    }

    private void showAd() {
        final FullScreenActivity _this = this; // 全屏广告也必须全屏..

        if (fullAd == null) {
            // TToast.show(this, "广告加载错误");
            getRewardResult();
            return;
        }

        fullAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

            @Override
            public void onAdShow() {
                // TToast.show(_this, "展示全屏视频广告");
                is_show = true;
            }

            @Override
            public void onAdVideoBarClick() {
                // TToast.show(_this, "查看详情成功,奖励即将发放");
                is_click = true;
            }

            @Override
            public void onAdClose() {
                // TToast.show(_this, "全屏视频广告已关闭");
                getRewardResult();
            }

            // 视频播放完成回调
            @Override
            public void onVideoComplete() {
                // TToast.show(_this, "全屏视频广告播放完成");
                is_show = true;
            }

            @Override
            public void onSkippedVideo() {
                // TToast.show(_this, "跳过全屏视频广告播放");
                is_show = true; //主动跳过也算看完了
            }
        });

        // 开始显示广告
        fullAd.showFullScreenVideoAd(_this);
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

}
