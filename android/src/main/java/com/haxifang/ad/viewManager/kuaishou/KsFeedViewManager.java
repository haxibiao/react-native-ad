package com.haxifang.ad.viewManager.kuaishou;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.haxifang.ad.views.kuaishou.FeedView;
import java.util.Map;

public class KsFeedViewManager extends ViewGroupManager<FeedView> {
  public static final String TAG = "KsFeedAd";
  private ReactContext mContext;

  public KsFeedViewManager(ReactApplicationContext context) {
    mContext = context;
  }

  @NonNull
  @Override
  public String getName() {
    return TAG;
  }

  @NonNull
  @Override
  protected FeedView createViewInstance(
    @NonNull ThemedReactContext reactContext
  ) {
    return new FeedView(reactContext);
  }

  @Override
  public boolean needsCustomLayoutForChildren() {
    return true;
  }

  // 设置什么广告（头条，腾讯，百度），暂时不用
  //    @ReactProp(name = "provider")
  //    public void setProvider(FeedView view, @Nullable String provider) {
  //        view.setProvider(provider);
  //    }

  // 设置广告是否使用缓存，现在先不用
  //    @ReactProp(name = "useCache")
  //    public void setUseCache(FeedView view, @Nullable Boolean useCache) {
  //        view.setUseCache(useCache);
  //    }

  @ReactProp(name = "codeid")
  public void setCodeId(FeedView view, @Nullable String codeid) {
    view.setCodeId(codeid);
  }

  // 设置大小，暂时不用
  //    @ReactProp(name = "size")
  //    public void setSize(FeedView view, @Nullable String size) {
  //        view.setSize(size);
  //    }

  @ReactProp(name = "adWidth")
  public void setAdWidth(FeedView view, @Nullable int adWidth) {
    view.setWidth(adWidth);
  }

  // @ReactProp(name = "adHeight")
  // public void setAdHeight(FeedView view, @Nullable int adHeight) {
  //   view.setHeight(adHeight);
  // }

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
