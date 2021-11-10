package com.haxifang.ad.activities.kuaishou;

import static com.haxifang.ad.RewardVideo.sendEvent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.haxifang.R;
import com.haxifang.ad.AdBoss;
import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsLoadManager;
import com.kwad.sdk.api.KsRewardVideoAd;
import com.kwad.sdk.api.KsScene;
import com.kwad.sdk.api.KsVideoPlayConfig;
import com.kwad.sdk.api.SdkConfig;
import java.util.List;

public class RewardActivity extends Activity {
  private static final String TAG = "KsRewardVideo";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //先渲染个黑色背景的view
    setContentView(R.layout.video_view);

    //关联boss处理回调
    AdBoss.hookActivity(this);

    // 读取 codeId
    Bundle extras = getIntent().getExtras();
    String codeId = extras.getString("codeId");
    long mCodeId = Long.parseLong(codeId);
    // 开始加载广告
    loadAd(mCodeId);
  }

  public void loadAd(long codeId) {
    if (codeId == 0) {
      // 广告位 CodeId 未传, 抛出error
      AdBoss.getRewardResult();
      fireEvent("onAdError", 1001, "广告位 CodeId 未传");
      return;
    }

    AdBoss.ksRewardAd = null;

    // 创建广告请求参数 AdSlot, 具体参数含义参考文档
    KsScene.Builder builder = new KsScene.Builder(codeId)
      .setBackUrl("ksad://returnback")
      .screenOrientation(SdkConfig.SCREEN_ORIENTATION_UNKNOWN);

    KsScene scene = builder.build();

    // 请求的期望屏幕方向传递为1，表示期望为竖屏
    KsAdSDK
      .getLoadManager()
      .loadRewardVideoAd(
        scene,
        new KsLoadManager.RewardVideoAdListener() {

          @Override
          public void onError(int code, String msg) {
            Log.d("reward onError ", "激励视频广告请求失败" + code + msg);
            fireEvent("onAdError", 1002, msg);
            if (AdBoss.rewardActivity != null) {
              AdBoss.rewardActivity.finish();
            }
          }

          @Override
          public void onRequestResult(int adNumber) {
            Log.d("reward Cached ", "快手激励视频广告请求结果返回" + adNumber);
            fireEvent("onAdVideoCached", 201, "快手激励视频广告请求结果返回");
          }

          @Override
          public void onRewardVideoAdLoad(
            @Nullable List<KsRewardVideoAd> adList
          ) {
            if (adList != null && adList.size() > 0) {
              AdBoss.ksRewardAd = adList.get(0);
              Log.d(TAG, "reward AdLoad ");
              sendEvent("AdLoaded", null);
              fireEvent("onAdLoaded", 200, "视频广告的素材加载完毕");
              // showRewardVideoAd(null)
              // 缓存的更新为最新加载成功的广告
              showAd();
            }
          }
        }
      );
  }

  private void showAd() {
    if (AdBoss.ksRewardAd != null && AdBoss.ksRewardAd.isAdEnable()) {
      AdBoss.ksRewardAd.setRewardAdInteractionListener(
        new KsRewardVideoAd.RewardAdInteractionListener() {

          @Override
          public void onAdClicked() {
            AdBoss.is_click = true;
            String msg = "奖励视频查看成功,奖励即将发放";
            Log.d(TAG, msg);
            fireEvent("onAdClick", 203, msg);
          }

          @Override
          public void onPageDismiss() {
            AdBoss.is_close = true;
            AdBoss.getRewardResult();
          }

          @Override
          public void onVideoPlayError(int code, int extra) {
            fireEvent("onAdError", 1004, "激励视频播放出错了");
            if (AdBoss.rewardActivity != null) {
              AdBoss.rewardActivity.finish();
            }
          }

          @Override
          public void onVideoPlayEnd() {
            AdBoss.is_show = true;
            String msg = "头条奖励视频成功播放完成";
            // TToast.show(_this, msg);
            Log.d(TAG, msg);
            fireEvent("onVideoComplete", 205, msg);
          }

          @Override
          public void onVideoSkipToEnd(long playDuration) {
            AdBoss.is_close = true;
            AdBoss.is_show = false;
            fireEvent("onAdClose", 204, "关闭激励视频");
            // AdBoss.getRewardResult();
          }

          @Override
          public void onVideoPlayStart() {
            String msg = "开始展示奖励视频";
            Log.d(TAG, msg);
            fireEvent("onAdLoaded", 202, msg);
          }

          /**
           * 激励视频广告激励回调，只会回调一次
           */
          @Override
          public void onRewardVerify() {
            AdBoss.is_reward = true;
          }

          /**
           *  视频激励分阶段回调
           * @param taskType 当前激励视频所属任务类型
           *                 RewardTaskType.LOOK_VIDEO 观看视频类型             属于浅度奖励类型
           *                 RewardTaskType.LOOK_LANDING_PAGE 浏览落地页N秒类型  属于深度奖励类型
           *                 RewardTaskType.USE_APP 下载使用App N秒类型          属于深度奖励类型
           * @param currentTaskStatus  当前所完成任务类型，@RewardTaskType中之一
           */
          @Override
          public void onRewardStepVerify(int taskType, int currentTaskStatus) {}
        }
      );
      AdBoss.ksRewardAd.showRewardVideoAd(this, null);
    } else {
      String msg = "无可用激励视频广告，请等待缓存加载或者重新刷新";
      Log.d(TAG, msg);
      // TToast.show(this, msg);

      fireEvent("onAdError", 1003, msg);
      finish();
      return;
    }
  }

  // 二次封装发送到RN的事件函数
  public static void fireEvent(
    String eventName,
    int startCode,
    String message
  ) {
    WritableMap params = Arguments.createMap();
    params.putInt("code", startCode);
    params.putString("message", message);
    sendEvent(eventName, params);
  }
}
