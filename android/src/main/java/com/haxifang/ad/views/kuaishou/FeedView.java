package com.haxifang.ad.views.kuaishou;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.haxifang.R;
import com.haxifang.ad.AdBoss;
import com.haxifang.ad.utils.Utils;
import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsAdVideoPlayConfig;
import com.kwad.sdk.api.KsFeedAd;
import com.kwad.sdk.api.KsLoadManager;
import com.kwad.sdk.api.KsScene;
import java.util.LinkedList;
import java.util.List;

public class FeedView extends RelativeLayout {
  public static final String TAG = "KsFeedAdView";
  private long _codeId = 0;
  protected Context mContext;
  protected ReactContext reactContext;
  private boolean adShowing = false;
  private int _expectedWidth = 0;
  private int _expectedHeight = 0;
  protected final RelativeLayout mExpressContainer;
  //避免腾讯重复加载死循环
  private int txFeedLoadCount = 0;
  private boolean isPreloadVideo;

  public FeedView(ReactContext context) {
    super(context);
    reactContext = context;

    mContext = context;

    // 初始化广告渲染组件
    inflate(mContext, R.layout.feed_view, this);
    mExpressContainer = findViewById(R.id.feed_container);

    // 这个函数很关键，不然不能触发再次渲染，让 view 在 RN 里渲染成功!!
    Utils.setupLayoutHack(this);
  }

  public void setWidth(int width) {
    _expectedWidth = width;
    // showAd();
  }

  public void setCodeId(String codeId) {
    _codeId = Long.parseLong(codeId);
    runOnUiThread(
      () -> {
        showAd();
      }
    );
  }

  void showAd() {
    if (_codeId == 0) {
      Log.d(TAG, "loadStreamAd: 属性还不完整 _codeId=" + _codeId);
      return;
    }
    // 信息流广告原来不能提前预加载，很容易出现超时，必须当场加载
    // sdk里很容易出现 message send to dead thread ... 肯定有些资源线程依赖！
    runOnUiThread(
      () -> {
        loadAd();
      }
    );
  }

  private void loadAd() {
    KsScene scene = new KsScene.Builder(_codeId)
      .width(_expectedWidth)
      .adNum(1)
      .build();
    KsAdSDK
      .getLoadManager()
      .loadConfigFeedAd(
        scene,
        new KsLoadManager.FeedAdListener() {

          @Override
          public void onError(int code, String msg) {
            Log.d(TAG, "广告数据请求失败" + code + msg);
          }

          @Override
          public void onFeedAdLoad(@Nullable List<KsFeedAd> adList) {
            if (adList == null || adList.isEmpty()) {
              Log.d(TAG, "广告数据为空");
              return;
            }
            Log.d(TAG, "onADLoaded: " + adList.size());

            KsFeedAd feedAdView = adList.get(0);

            //主动加载成功的，要显示
            showAd(feedAdView);
          }
        }
      );
  }

  private void showAd(KsFeedAd feedAdView) {
    if (mExpressContainer.getChildCount() > 0) {
      mExpressContainer.removeAllViews();
    }

    feedAdView.setAdInteractionListener(
      new KsFeedAd.AdInteractionListener() {

        @Override
        public void onAdClicked() {
          Log.i(TAG, "广告点击回调");
          onAdClick();
        }

        @Override
        public void onAdShow() {
          Log.i(TAG, "广告曝光回调");
        }

        @Override
        public void onDislikeClicked() {
          Log.i(TAG, "广告不喜欢回调");
          mExpressContainer.removeAllViews();
          onAdClose();
        }

        @Override
        public void onDownloadTipsDialogShow() {
          Log.i(TAG, "广告展示下载合规弹窗");
        }

        @Override
        public void onDownloadTipsDialogDismiss() {
          Log.i(TAG, "广告关闭下载合规弹窗");
        }
      }
    );
    View videoView = feedAdView.getFeedView(mContext);
    if (videoView != null && videoView.getParent() == null) {
      Log.d(TAG, "onRenderSuccess");
      videoView.post(
        new Runnable() {

          @Override
          public void run() {
            //单位为px，传给RN需要转换单位使用
            if (videoView.getMeasuredHeight() > 0) {
              onLayoutChanged(
                _expectedWidth,
                px2dip(videoView.getMeasuredHeight())
              );
            } else {
              onLayoutChanged(_expectedWidth, 220);
            }
          }
        }
      );

      mExpressContainer.removeAllViews();
      mExpressContainer.addView(videoView);
    }
  }

  public void onLayoutChanged(int width, int height) {
    Log.d(TAG, "onLayoutChanged: " + width + ", " + height);
    // Log.d("height" + height);
    // WritableMap event = Arguments.createMap();
    // event.putInt("width", width);
    // event.putInt("height", height);
    // reactContext.getJSModule(RCTEventEmitter.class)
    //   .receiveEvent(getId(), "onLayoutChanged", event);
    onAdLayout((int) width, (int) height);
  }

  // 外部事件..
  public void onAdError(String message) {
    WritableMap event = Arguments.createMap();
    event.putString("message", message);
    reactContext
      .getJSModule(RCTEventEmitter.class)
      .receiveEvent(getId(), "onAdError", event);
  }

  public void onAdClick() {
    WritableMap event = Arguments.createMap();
    reactContext
      .getJSModule(RCTEventEmitter.class)
      .receiveEvent(getId(), "onAdClick", event);
  }

  public void onAdClose() {
    Log.d(TAG, "onAdClose: ");
    WritableMap event = Arguments.createMap();

    reactContext
      .getJSModule(RCTEventEmitter.class)
      .receiveEvent(getId(), "onAdClose", event);
  }

  public void onAdLayout(int width, int height) {
    Log.d(TAG, "onAdLayout: " + width + ", " + height);
    WritableMap event = Arguments.createMap();
    event.putInt("width", width);
    event.putInt("height", height);
    reactContext
      .getJSModule(RCTEventEmitter.class)
      .receiveEvent(getId(), "onAdLayout", event);
  }

  public int px2dip(float pxValue) {
    final float scale = reactContext.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }
}
