package com.haxifang.ad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.haxifang.ad.views.DrawFeedView;

import java.util.Map;

public class DrawFeedViewManager extends ViewGroupManager<DrawFeedView> {

    public static final String TAG = "DrawFeedAd";
    private ReactContext mContext;

    public DrawFeedViewManager(ReactApplicationContext context) {
        mContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }

    @NonNull
    @Override
    protected DrawFeedView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new DrawFeedView(reactContext);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

    @ReactProp(name = "codeid")
    public void setCodeId(@NonNull DrawFeedView view, @NonNull String codeid) {
        view.setCodeId(codeid);
    }

    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put(
                        "onAdError",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onAdError")))
                .put(
                        "onAdShow",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onAdShow")))
                .put(
                        "onAdClick",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onAdClick")))
                .build();
    }
}
