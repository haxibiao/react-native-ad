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

public class DrawFeed extends ViewGroupManager<DrawFeedView> {

    public static final String REACT_CLASS = "DrawFeed";
    private ReactApplicationContext reactContext;

    public DrawFeed(ReactApplicationContext context) {
        reactContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected DrawFeedView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new DrawFeedView(reactContext);
    }

    @ReactProp(name = "is_express")
    public void setIsExpress(@NonNull DrawFeedView view, @NonNull String is_express) {
        view.setIsExpress(is_express);
    }

    @ReactProp(name = "codeid")
    public void setCodeId(@NonNull DrawFeedView view, @NonNull String codeid) {
        view.setCodeId(codeid);
    }

    @Nullable
    @Override
    public Map getExportedCustomDirectEventTypeConstants() {
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
