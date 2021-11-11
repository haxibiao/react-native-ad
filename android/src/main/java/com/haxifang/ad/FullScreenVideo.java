package com.haxifang.ad;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.haxifang.ad.activities.FullScreenActivity;

public class FullScreenVideo extends ReactContextBaseJavaModule {
  private static final String TAG = "FullScreenVideo";
  protected static ReactApplicationContext mContext;

  public FullScreenVideo(ReactApplicationContext reactContext) {
    super(reactContext);
    mContext = reactContext;
  }

  @Override
  public String getName() {
    return TAG;
  }

  @ReactMethod
  public void startAd(ReadableMap options, final Promise promise) {
    String codeId = options.getString("codeid");
    String orientation = options.getString("orientation");
    String provider = options.getString("provider");

    AdBoss.prepareReward(promise, mContext);

    // 启动激励视频页面
    if (provider.equals("腾讯")) {
      startTx(codeId);
    } else {
      startTT(codeId, orientation);
    }
  }

  /**
   * 启动穿山甲激励视频
   *
   * @param codeId
   */
  public static void startTT(String codeId, String orientation) {
    Intent intent = new Intent(mContext, FullScreenActivity.class);
    try {
      intent.putExtra("codeId", codeId);
      intent.putExtra("orientation", orientation);
      Activity context = mContext.getCurrentActivity();
      // 不要过渡动画
      context.overridePendingTransition(0, 0);
      context.startActivityForResult(intent, 10000);
    } catch (Exception e) {
      e.printStackTrace();
      Log.e(TAG, "start FullScreen Activity error: ", e);
    }
  }

  /**
   * 启动优量汇插屏视频
   *
   * @param codeId
   */
  public static void startTx(String codeId) {
    final String message = "启动腾讯插屏视频";
    Log.d(TAG, message + "  codeID: " + codeId);
    Activity ac = mContext.getCurrentActivity();
    if (ac != null) {
      ac.runOnUiThread(
        () -> {
          // TToast.show(mContext, message);
          Intent intent = new Intent(
            mContext,
            com.haxifang.ad.activities.tencent.FullScreenActivity.class
          );
          intent.putExtra("codeId", codeId);
          ac.startActivity(intent);
        }
      );
    }
  }

  // 发送事件到RN
  public static void sendEvent(String eventName, @Nullable WritableMap params) {
    mContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(TAG + "-" + eventName, params);
  }
}
