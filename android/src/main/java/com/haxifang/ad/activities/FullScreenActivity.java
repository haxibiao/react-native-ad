package com.haxifang.ad.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.haxifang.R;
import com.haxifang.ad.AdBoss;
import com.haxifang.ad.FullScreenVideo;
import com.haxifang.ad.TTAdManagerHolder;

import static com.haxifang.ad.FullScreenVideo.sendEvent;

public class FullScreenActivity extends Activity {

    final private String TAG = "FullScreenVideo";
    private TTFullScreenVideoAd fullAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);

        AdBoss.hookActivity(this);

        // 读取 codeId
        Bundle extras = getIntent().getExtras();
        String codeId = extras.getString("codeId");

        loadAdSlot(codeId);
    }

    private void loadAdSlot(String codeId) {
        // 创建广告请求参数 AdSlot ,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName(AdBoss.rewardName) // 奖励的名称
                .setRewardAmount(AdBoss.rewardAmount) // 奖励的数量
                .setUserID(AdBoss.userId)// 用户id,必传参数
                .setOrientation(TTAdConstant.VERTICAL) // 必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();

        // 请求广告
        AdBoss.TTAdSdk.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                //加载出错
                fireEvent("onAdError", message);
            }

            @Override
            public void onFullScreenVideoCached() {
                String msg = "成功缓存全屏广告的视频";
                fireEvent("onVideoCached", msg);
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                fullAd = ad;
                String msg = "成功加载的全屏广告";
                fireEvent("onAdShow", msg);
                showAd();
            }
        });
    }

    private void showAd() {
        final FullScreenActivity _this = this; // 全屏广告也必须全屏..

        if (fullAd == null) {
            // TToast.show(this, "广告加载错误");
            AdBoss.getRewardResult();
            return;
        }

        fullAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

            @Override
            public void onAdShow() {
                String msg = "展示全屏视频广告";
                fireEvent("onAdShow", msg);
                AdBoss.is_show = true;
            }

            @Override
            public void onAdVideoBarClick() {
                String msg = "查看详情成功,奖励即将发放";
                fireEvent("onAdClick", msg);
                AdBoss.is_click = true;
            }

            @Override
            public void onAdClose() {
                String msg = "全屏视频广告已关闭";
                fireEvent("onAdClose", msg);
                AdBoss.getRewardResult();
            }

            // 视频播放完成回调
            @Override
            public void onVideoComplete() {
                String msg = "全屏视频广告播放完成";
                fireEvent("onVideoComplete", msg);
                AdBoss.is_show = true;
            }

            @Override
            public void onSkippedVideo() {
                String msg = "跳过全屏视频广告播放";
                AdBoss.is_show = true; //主动跳过也算看完了
                fireEvent("onAdSkip", msg);
            }
        });

        // 开始显示广告
        fullAd.showFullScreenVideoAd(_this);
    }


    // 二次封装发送到RN的事件函数
    public void fireEvent(String eventName, String message) {
        WritableMap p = Arguments.createMap();
        p.putString("message", message);
        sendEvent(eventName, p);
    }

}
