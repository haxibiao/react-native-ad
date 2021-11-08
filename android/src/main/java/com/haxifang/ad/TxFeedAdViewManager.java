package com.haxifang.ad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.haxifang.ad.views.tencent.TxFeedAdView;
import java.util.Map;

public class TxFeedAdViewManager extends ViewGroupManager<TxFeedAdView> {
  public static final String TAG = "TxFeedAd";
  private ReactContext mContext;

  public TxFeedAdViewManager(ReactApplicationContext context) {
    mContext = context;
  }

  @NonNull
  @Override
  public String getName() {
    return TAG;
  }

  @NonNull
  @Override
  protected TxFeedAdView createViewInstance(
    @NonNull ThemedReactContext reactContext
  ) {
    return new TxFeedAdView(reactContext);
  }

  @Override
  public boolean needsCustomLayoutForChildren() {
    return true;
  }

  // 设置什么广告（头条，腾讯，百度），暂时不用
  //    @ReactProp(name = "provider")
  //    public void setProvider(FeedAdView view, @Nullable String provider) {
  //        view.setProvider(provider);
  //    }

  // 设置广告是否使用缓存，现在先不用
  //    @ReactProp(name = "useCache")
  //    public void setUseCache(FeedAdView view, @Nullable Boolean useCache) {
  //        view.setUseCache(useCache);
  //    }

  @ReactProp(name = "codeid")
  public void setCodeId(TxFeedAdView view, @Nullable String codeid) {
    view.setCodeId(codeid);
  }

  // 设置大小，暂时不用
  //    @ReactProp(name = "size")
  //    public void setSize(FeedAdView view, @Nullable String size) {
  //        view.setSize(size);
  //    }

  @ReactProp(name = "adWidth")
  public void setAdWidth(TxFeedAdView view, @Nullable int adWidth) {
    view.setWidth(adWidth);
  }

  @ReactProp(name = "adHeight")
  public void setAdHeight(TxFeedAdView view, @Nullable int adHeight) {
    view.setHeight(adHeight);
  }

  @Override
  public Map getExportedCustomBubblingEventTypeConstants() {
    return MapBuilder
      .builder()
      .put(
        "onAdClick",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onAdClick")
        )
      )
      .put(
        "onAdError",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onAdError")
        )
      )
      .put(
        "onAdClose",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onAdClose")
        )
      )
      .put(
        "onAdLayout",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onAdLayout")
        )
      )
      .build();
  }
}
