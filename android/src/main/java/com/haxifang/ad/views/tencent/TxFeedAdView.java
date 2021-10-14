package com.haxifang.ad.views.tencent;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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
import com.qq.e.ads.cfg.DownAPPConfirmPolicy;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;
import java.util.LinkedList;
import java.util.List;

public class TxFeedAdView
  extends RelativeLayout
  implements NativeExpressAD.NativeExpressADListener {
  public static final String TAG = "TxFeedAdView";
  private String _codeId = null;
  protected Context mContext;
  protected ReactContext reactContext;
  private boolean adShowing = false;
  private int _expectedWidth = 0;
  protected final RelativeLayout mExpressContainer;
  //避免腾讯重复加载死循环
  private int txFeedLoadCount = 0;
  private boolean isPreloadVideo;

  public TxFeedAdView(ReactContext context) {
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

    showAd();
  }

  public void setCodeId(String codeId) {
    _codeId = codeId;
    runOnUiThread(
      () -> {
        showAd();
      }
    );
  }

  void showAd() {
    if (_codeId == null) {
      Log.d(TAG, "loadStreamAd: 属性还不完整 _codeId=" + _codeId);
      return;
    }
    // 信息流广告原来不能提前预加载，很容易出现超时，必须当场加载
    // sdk里很容易出现 message send to dead thread ... 肯定有些资源线程依赖！
    runOnUiThread(
      () -> {
        loadTxFeedAd();
      }
    );
  }

  // 显示腾讯的信息流广告
  NativeExpressAD tx_nativeExpressAD;

  private void loadTxFeedAd() {
    tx_nativeExpressAD =
      new NativeExpressAD(
        mContext,
        new ADSize(_expectedWidth, ADSize.AUTO_HEIGHT),
        _codeId,
        this
      );
    tx_nativeExpressAD.loadAD(1);
  }

  private NativeExpressADView txFeedAdView;

  private void _showTxFeedAd(NativeExpressADView txFeedAdView) {
    // 释放前一个展示的NativeExpressADView的资源
    if (txFeedAdView != null) {
      txFeedAdView.destroy();
    }

    if (mExpressContainer.getChildCount() > 0) {
      mExpressContainer.removeAllViews();
    }

    if (
      txFeedAdView.getBoundData().getAdPatternType() ==
      AdPatternType.NATIVE_VIDEO
    ) {
      Log.e(TAG, "Load视频素材:");
      txFeedAdView.setMediaListener(mediaListener);
      // 预加载视频素材，加载成功会回调mediaListener的onVideoCached方法，失败的话回调onVideoError方法errorCode为702。
      txFeedAdView.preloadVideo();
    } else {
      mExpressContainer.addView(txFeedAdView);
      Log.e(TAG, "素材渲染:");
      // 图文情况:广告可见才会产生曝光，否则将无法产生收益。
      if (txFeedLoadCount < 3) {
        try {
          txFeedAdView.render();
        } catch (Exception e) {
          Log.e(TAG, "腾讯的FeedAd居然异常:" + e.getMessage());
          //只好重新加载,别死循环..
          loadTxFeedAd();
        }
      }
    }
  }

  @Override
  public void onADClicked(NativeExpressADView nativeExpressADView) {
    // mExpressContainer.removeAllViews();
    onAdClick();
  }

  @Override
  public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {}

  @Override
  public void onADClosed(NativeExpressADView nativeExpressADView) {
    onAdClose();
    mExpressContainer.removeAllViews();
  }

  @Override
  public void onADExposure(NativeExpressADView nativeExpressADView) {}

  @Override
  public void onADLeftApplication(NativeExpressADView nativeExpressADView) {}

  // 腾讯信息流广告需要监听的...
  @Override
  public void onADLoaded(List<NativeExpressADView> adList) {
    Log.i(TAG, "onADLoaded: " + adList.size());

    txFeedAdView = adList.get(0);

    //AdBoss 主动 load时 会缓存腾讯FeedAd
    //        AdBoss.txFeedAdView = txFeedAdView;

    Log.i(TAG, "onADLoaded, video info: " + getAdInfo(txFeedAdView));

    //主动加载成功的，要显示
    _showTxFeedAd(txFeedAdView);
  }

  @Override
  public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {}

  @Override
  public void onRenderFail(NativeExpressADView nativeExpressADView) {
    Log.i(TAG, "onRenderFail");
    onAdError("onRenderFail");
  }

  @Override
  public void onRenderSuccess(NativeExpressADView txFeedAdView) {
    Log.i(TAG, "onRenderSuccess");
    // 返回view的宽高 单位 dp
    // TToast.show(mContext, "渲染成功");

    txFeedAdView.post(
      new Runnable() {

        @Override
        public void run() {
          //单位为px，传给RN需要转换单位使用
          if (txFeedAdView.getMeasuredHeight() > 0) {
            onLayoutChanged(
              _expectedWidth,
              px2dip(txFeedAdView.getMeasuredHeight())
            );
          } else {
            onLayoutChanged(_expectedWidth, 220);
          }
        }
      }
    );
  }

  /**
   * 腾讯的 获取广告数据
   *
   * @param txFeedAdView
   * @return
   */
  public static String getAdInfo(NativeExpressADView txFeedAdView) {
    AdData adData = txFeedAdView.getBoundData();
    if (adData != null) {
      StringBuilder infoBuilder = new StringBuilder();
      infoBuilder
        .append("title:")
        .append(adData.getTitle())
        .append(",")
        .append("desc:")
        .append(adData.getDesc())
        .append(",")
        .append("patternType:")
        .append(adData.getAdPatternType());
      if (adData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
        infoBuilder
          .append(", video info: ")
          .append(getVideoInfo(adData.getProperty(AdData.VideoPlayer.class)));
      }
      Log.d(
        TAG,
        "eCPM = " + adData.getECPM() + " , eCPMLevel = " + adData.getECPMLevel()
      );
      return infoBuilder.toString();
    }
    return null;
  }

  /**
   * 获取播放器实例
   *
   * @param videoPlayer
   * @return
   */
  public static String getVideoInfo(AdData.VideoPlayer videoPlayer) {
    if (videoPlayer != null) {
      StringBuilder videoBuilder = new StringBuilder();
      videoBuilder
        .append("{state:")
        .append(videoPlayer.getVideoState())
        .append(",")
        .append("duration:")
        .append(videoPlayer.getDuration())
        .append(",")
        .append("position:")
        .append(videoPlayer.getCurrentPosition())
        .append("}");
      return videoBuilder.toString();
    }
    return null;
  }

  // 腾讯 FeedAd 视频情况下的回调
  private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {

    @Override
    public void onVideoInit(NativeExpressADView txFeedAdView) {
      Log.i(
        TAG,
        "onVideoInit: " +
        getVideoInfo(
          txFeedAdView.getBoundData().getProperty(AdData.VideoPlayer.class)
        )
      );
    }

    @Override
    public void onVideoLoading(NativeExpressADView txFeedAdView) {
      Log.i(TAG, "onVideoLoading");
    }

    @Override
    public void onVideoCached(NativeExpressADView adView) {
      Log.i(TAG, "onVideoCached");
      // 视频素材加载完成，此时展示视频广告不会有进度条。
      // 广告可见才会产生曝光，否则将无法产生收益。

      if (mExpressContainer.getChildCount() > 0) {
        mExpressContainer.removeAllViews();
      }
      mExpressContainer.addView(txFeedAdView);
      txFeedAdView.render();
      // onLayoutChanged(adView.getMeasuredWidth(), adView.getMeasuredHeight());
    }

    @Override
    public void onVideoReady(NativeExpressADView txFeedAdView, long l) {
      Log.i(TAG, "onVideoReady");
    }

    @Override
    public void onVideoStart(NativeExpressADView txFeedAdView) {
      Log.i(
        TAG,
        "onVideoStart: " +
        getVideoInfo(
          txFeedAdView.getBoundData().getProperty(AdData.VideoPlayer.class)
        )
      );
    }

    @Override
    public void onVideoPause(NativeExpressADView txFeedAdView) {
      Log.i(
        TAG,
        "onVideoPause: " +
        getVideoInfo(
          txFeedAdView.getBoundData().getProperty(AdData.VideoPlayer.class)
        )
      );
    }

    @Override
    public void onVideoComplete(NativeExpressADView txFeedAdView) {
      Log.i(
        TAG,
        "onVideoComplete: " +
        getVideoInfo(
          txFeedAdView.getBoundData().getProperty(AdData.VideoPlayer.class)
        )
      );
    }

    @Override
    public void onVideoError(
      NativeExpressADView txFeedAdView,
      AdError adError
    ) {
      Log.i(TAG, "onVideoError");
    }

    @Override
    public void onVideoPageOpen(NativeExpressADView txFeedAdView) {
      Log.i(TAG, "onVideoPageOpen");
    }

    @Override
    public void onVideoPageClose(NativeExpressADView txFeedAdView) {
      Log.i(TAG, "onVideoPageClose");
    }
  };

  public void onLayoutChanged(int width, int height) {
    Log.d(TAG, "onLayoutChanged: " + width + ", " + height);
    // WritableMap event = Arguments.createMap();
    // event.putInt("width", width);
    // event.putInt("height", height);
    // reactContext.getJSModule(RCTEventEmitter.class)
    //   .receiveEvent(getId(), "onLayoutChanged", event);
    onAdLayout((int) width, (int) height);
  }

  @Override
  public void onNoAD(AdError adError) {
    Log.i(
      TAG,
      String.format(
        "onNoAD, error code: %d, error msg: %s",
        adError.getErrorCode(),
        adError.getErrorMsg()
      )
    );
    onAdError(adError.getErrorMsg());
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
