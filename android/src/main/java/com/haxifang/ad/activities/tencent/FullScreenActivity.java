package com.haxifang.ad.activities.tencent;

import static com.haxifang.ad.FullScreenVideo.sendEvent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.haxifang.R;
import com.haxifang.ad.AdBoss;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.ads.interstitial2.UnifiedInterstitialMediaListener;
import com.qq.e.comm.constants.BiddingLossReason;
import com.qq.e.comm.managers.GDTAdSdk;
import com.qq.e.comm.util.AdError;

public class FullScreenActivity extends Activity {
  private final String TAG = "FullScreenVideo";
  private boolean adShowing = false;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //先渲染个白屏
    setContentView(R.layout.full_video_view);

    AdBoss.hookActivity(this);

    // 读取 codeId
    Bundle extras = getIntent().getExtras();
    String codeId = extras.getString("codeId");
    String orientation = extras.getString("orientation");

    // 有缓存的广告直接展示
    if (AdBoss.txfullScreenAd != null) {
      // Log.d(TAG, "直接展示提前加载的广告");
      showAd();
      AdBoss.rewardActivity.finish(); // 关闭全屏视频广告 Activity
    } else {
      // 否则直接加载视频广告
      loadAd(codeId, orientation);
    }
  }

  public void loadAd(String codeId, String orientation) {
    AdBoss.txfullScreenAd =
      new UnifiedInterstitialAD(
        this,
        codeId,
        new UnifiedInterstitialADListener() {

          @Override
          public void onNoAD(AdError error) {
            // mExpressContainer.removeAllViews();
            if (AdBoss.rewardActivity != null) {
              AdBoss.rewardActivity.finish();
            }
          }

          @Override
          public void onADReceive() {
            showAd();
          }

          @Override
          public void onADClicked() {
            // mExpressContainer.removeAllViews();
            String msg = "查看详情成功,奖励即将发放";
            fireEvent("onAdClick", msg);
            AdBoss.is_click = true;
          }

          @Override
          public void onADClosed() {
            AdBoss.is_close = true;
            Log.d(TAG, "onADClose: ");
            AdBoss.txfullScreenAd = null; // 显示广告之后将之前加载的广告清空
            AdBoss.getRewardResult();
            if (AdBoss.rewardActivity != null) {
              AdBoss.rewardActivity.finish();
            }
          }

          @Override
          public void onADExposure() {}

          @Override
          public void onVideoCached() {}

          @Override
          public void onADLeftApplication() {}

          @Override
          public void onRenderFail() {
            Log.i(TAG, "onRenderFail");
            if (AdBoss.rewardActivity != null) {
              AdBoss.rewardActivity.finish();
            }
          }

          @Override
          public void onRenderSuccess() {
            Log.i(TAG, "onRenderSuccess");
          }

          @Override
          public void onADOpened() {}
        }
      );

    AdBoss.txfullScreenAd.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO);
    AdBoss.txfullScreenAd.loadFullScreenAD();
  }

  /**
   * 展示全屏视频广告
   *
   * @param ad
   */
  private void showAd() {
    if (adShowing) {
      return;
    }
    adShowing = true;

    if (AdBoss.txfullScreenAd == null && !AdBoss.txfullScreenAd.isValid()) {
      Log.i(TAG, "广告加载错误");
      AdBoss.getRewardResult();
      if (AdBoss.rewardActivity != null) {
        AdBoss.rewardActivity.finish();
      }
      return;
    }

    AdBoss.txfullScreenAd.setMediaListener(mediaListener);

    // 开始显示广告
    AdBoss.txfullScreenAd.showFullScreenAD(this);
  }

  private UnifiedInterstitialMediaListener mediaListener = new UnifiedInterstitialMediaListener() {

    @Override
    public void onVideoInit() {}

    @Override
    public void onVideoLoading() {
      Log.i(TAG, "onVideoLoading");
    }

    @Override
    public void onVideoStart() {
      Log.i(TAG, "onVideoStart");
    }

    @Override
    public void onVideoPause() {
      Log.i(TAG, "onVideoPause: ");
    }

    @Override
    public void onVideoComplete() {
      Log.i(TAG, "onVideoComplete: ");
      AdBoss.is_show = true;
      String msg = "腾讯激励视频播放完毕";
      Log.d(TAG, msg);
    }

    @Override
    public void onVideoError(AdError adError) {
      Log.i(TAG, "onVideoError");
    }

    @Override
    public void onVideoPageClose() {
      Log.i(TAG, "onVideoPageClose");
    }

    @Override
    public void onVideoPageOpen() {
      Log.i(TAG, "onVideoPageOpen");
    }

    @Override
    public void onVideoReady(long videoDuration) {
      Log.i(TAG, "onVideoReady, duration = " + videoDuration);
    }
  };

  // 二次封装发送到RN的事件函数
  public static void fireEvent(String eventName, String message) {
    WritableMap params = Arguments.createMap();
    params.putString("message", message);
    sendEvent(eventName, params);
  }
}
