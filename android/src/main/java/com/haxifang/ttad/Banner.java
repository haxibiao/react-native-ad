package com.haxifang.ttad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.haxifang.ttad.views.BannerView;

import java.util.Map;

public class Banner extends ViewGroupManager<BannerView> {

    final public static String REACT_CLASS = "TTAdBanner";
    private ReactContext mContext;

    public Banner(ReactApplicationContext context) {
        mContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected BannerView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new BannerView(reactContext);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

    @ReactProp(name = "codeid")
    public void setCodeId(BannerView view, @Nullable String codeid) {
        view.setCodeId(codeid);
    }

    // @ReactProp(name = "size")
    // public void setSize(BannerView view, @Nullable String size) {
    //     view.setSize(size);
    // }

    @ReactProp(name = "adWidth")
    public void setAdWidth(BannerView view, @Nullable int adWidth) {
        view.setAdWidth(adWidth);
    }

    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put(
                        "onAdClick",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onAdClick")))
                .put(
                        "onAdError",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onAdError")))
                .put(
                        "onAdClosed",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onAdClosed")))
                .put(
                        "onLayoutChanged",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onLayoutChanged")))
                .build();
    }
}
