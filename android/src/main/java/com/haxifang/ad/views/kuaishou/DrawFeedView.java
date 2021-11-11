package com.haxifang.ad.views.kuaishou;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.haxifang.R;
import com.haxifang.ad.AdBoss;
import com.haxifang.ad.utils.Utils;
import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsDrawAd;
import com.kwad.sdk.api.KsLoadManager;
import com.kwad.sdk.api.KsScene;
import java.util.ArrayList;
import java.util.List;

public class DrawFeedView extends RelativeLayout {
  private String TAG = "DrawFeed";
  private String _isExpress = "true"; //新的默认用模板渲染
  private long _codeId = 0;
  protected Context mContext;
  protected ReactContext reactContext;
  protected final FrameLayout mContainer;
  private boolean adShowing = false;

  public DrawFeedView(ReactContext context) {
    super(context);
    reactContext = context;
    mContext = context;

    // 初始化广告渲染组件
    inflate(mContext, R.layout.draw_video, this);
    mContainer = findViewById(R.id.tt_video_layout_hxb);

    // 这个函数很关键，不然不能触发再次渲染，让 view 在 RN 里渲染成功!!
    Utils.setupLayoutHack(this);
  }

  public void setCodeId(String codeId) {
    _codeId = Long.parseLong(codeId);
    runOnUiThread(
      () -> {
        tryShowAd();
      }
    );
  }

  void tryShowAd() {
    // if (AdBoss.ksAdSdk == null) {
    //   Log.d(TAG, "AdBoss 还没初始化完成 with appid " + AdBoss.tt_appid);
    //   return;
    // }
    if (_codeId == 0) {
      Log.d(TAG, "loadDrawFeedAd: 属性还不完整 _codeId=" + _codeId);
      return;
    }

    Log.d(TAG, "模版渲染 loadDrawFeedAd: loadExpressDrawNativeAd()");
    if (AdBoss.ksDrawAd != null) {
      //渲染已缓存的广告
      Log.d(TAG, "渲染已缓存的广告");
      showAd(AdBoss.ksDrawAd);
    }

    //加载新的广告并尝试渲染
    loadAd();
  }

  // 加载原生渲染方式的 Draw 广告
  private void loadAd() {
    // 创建广告请求参数AdSlot,具体参数含义参考文档
    KsScene scene = new KsScene.Builder(_codeId).adNum(1).build(); // 此为测试posId，请联系快手平台申请正式posId
    // 支持返回多条广告，默认1条，最多5条，参数范围1-5

    KsAdSDK
      .getLoadManager()
      .loadDrawAd(
        scene,
        new KsLoadManager.DrawAdListener() {

          @Override
          public void onError(int code, String msg) {
            Log.d(TAG, msg);
          }

          @Override
          public void onDrawAdLoad(@Nullable List<KsDrawAd> adList) {
            if (adList == null || adList.isEmpty()) {
              Log.d(TAG, "广告数据为空");
              return;
            }

            //成功加载到广告，开始渲染,我们每次只拉取1条
            KsDrawAd ad = adList.get(0);
            //尝试展示广告
            showAd(ad);
            //缓存给下次直接秒展示
            AdBoss.ksDrawAd = ad;
          }
        }
      );
  }

  private void showAd(KsDrawAd ad) {
    if (adShowing) {
      return;
    }
    adShowing = true; //当前实例已在渲染广告

    ad.setAdInteractionListener(
      new KsDrawAd.AdInteractionListener() {

        @Override
        public void onAdClicked() {
          Log.d(TAG, "广告点击回调");
          onAdClick();
        }

        @Override
        public void onAdShow() {
          Log.d(TAG, "express onAdShow");
          onExpressAdLoad();
        }

        @Override
        public void onVideoPlayStart() {
          Log.d(TAG, "广告视频开始播放");
        }

        @Override
        public void onVideoPlayPause() {
          Log.d(TAG, "广告视频暂停播放");
        }

        @Override
        public void onVideoPlayResume() {
          Log.d(TAG, "广告视频恢复播放");
        }

        @Override
        public void onVideoPlayEnd() {
          Log.d(TAG, "广告视频播放结束");
        }

        @Override
        public void onVideoPlayError() {
          Log.d(TAG, "广告视频播放出错");
        }
      }
    );
    View drawVideoView = ad.getDrawView(mContext);
    if (drawVideoView != null && drawVideoView.getParent() == null) {
      mContainer.removeAllViews();
      mContainer.addView(drawVideoView);
    }

    // ad.render();
    adShowing = false; //当前实例已完成渲染广告
  }

  // 广告点击回调响应事件
  public void onAdClick() {
    WritableMap event = Arguments.createMap();
    reactContext
      .getJSModule(RCTEventEmitter.class)
      .receiveEvent(getId(), "onAdClick", event);
  }

  // 新模板渲染广告加载成功回调
  public void onExpressAdLoad() {
    WritableMap event = Arguments.createMap();
    reactContext
      .getJSModule(RCTEventEmitter.class)
      .receiveEvent(getId(), "onAdShow", event);
  }
}
