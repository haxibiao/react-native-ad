package com.haxibiao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;

import com.facebook.react.uimanager.ViewManager;
import com.haxibiao.ad.AdManager;
import com.haxibiao.ad.DrawFeedViewManager;
import com.haxibiao.ad.FeedAdViewManager;
import com.haxibiao.ad.FullScreenVideo;
import com.haxibiao.ad.RewardVideo;
import com.haxibiao.ad.SplashAd;

public class AdPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new AdManager(reactContext));
        modules.add(new SplashAd(reactContext));
        modules.add(new FullScreenVideo(reactContext));
        modules.add(new RewardVideo(reactContext));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
                new DrawFeedViewManager(reactContext),
                new FeedAdViewManager(reactContext)
        );
    }
}
