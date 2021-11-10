package com.haxifang.ad;

import android.content.Context;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;

/**
 * 单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
public class TTAdManagerHolder {
  private static String TAG = "TTAdManagerHolder";
  public static boolean sInit;
  public static Context mContext;

  public static TTAdManager get() {
    if (!sInit) {
      throw new RuntimeException("TTAdSdk is not init, please check.");
    }
    return TTAdSdk.getAdManager();
  }

  public static void init(Context context, String appid, Boolean debug) {
    mContext = context;
    doInit(context, appid, debug);
  }

  // 接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
  private static void doInit(
    final Context context,
    final String appid,
    final Boolean debug
  ) {
    if (!sInit) {
      TTAdSdk.init(context, buildConfig(context, appid, debug));
      sInit = true;
    }
  }

  private static TTAdConfig buildConfig(
    Context context,
    String appid,
    Boolean debug
  ) {
    return new TTAdConfig.Builder()
      .appId(appid)
      .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
      .appName(AdBoss.appName)
      .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
      .allowShowNotify(true) //是否允许sdk展示通知栏提示
      .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
      .debug(debug) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
      .directDownloadNetworkType() //允许直接下载的网络状态集合
      .supportMultiProcess(false)
      .build();
  }
}
