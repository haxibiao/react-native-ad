package com.haxifang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.bytedance.sdk.openadsdk.TTAdManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.bridge.JavaScriptModule;
import com.haxifang.ad.AdManager;
import com.haxifang.ad.DrawFeed;
import com.haxifang.ad.Feed;
import com.haxifang.ad.FullScreenVideo;
import com.haxifang.ad.RewardVideo;
import com.haxifang.ad.SplashAd;
import com.haxifang.ad.TTAdManagerHolder;

public class ReactNativeAdPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new SplashAd(reactContext));
        modules.add(new AdManager(reactContext));
        modules.add(new FullScreenVideo(reactContext));
        modules.add(new RewardVideo(reactContext));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
                new DrawFeed(reactContext),
                new FeedAd(reactContext)
        );
    }
}
