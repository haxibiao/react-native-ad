package com.haxifang.ad.activities;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.haxifang.R;
import com.haxifang.ad.AdBoss;
import com.haxifang.ad.AdManager;
import com.haxifang.ad.WeakHandler;

public class SplashActivity extends AppCompatActivity implements WeakHandler.IHandler {

    // 开屏广告加载超时时间,建议大于1000,这里为了冷启动第一次加载到广告并且展示,示例设置了2000ms
    private static final int AD_TIME_OUT = 2000;
    private static final int MSG_GO_MAIN = 1;
    static String TAG = "SplashAd";
    // 开屏广告加载发生超时但是SDK没有及时回调结果的时候，做的一层保护。
    private final WeakHandler mHandler = new WeakHandler(this);
    private TTAdNative mTTAdNative;
    private FrameLayout mSplashContainer;
    // 是否强制跳转到主页面
    private boolean mForceGoMain;
    // 开屏广告是否已经加载
    private boolean mHasLoaded;

    private String code_id;

    // 注册监听方法
    private static void sendEvent(String eventName, WritableMap params) {
        AdManager.reactAppContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 读取 code id
        Bundle extras = getIntent().getExtras();
        code_id = extras.getString("codeid");

        // 初始化广告 SDK
        mTTAdNative = AdBoss.TTAdSdk;

        // 在合适的时机申请权限，如read_phone_state,防止获取不了 imei 时候，下载类广告没有填充的问题
        // 在开屏时候申请不太合适，因为该页面倒计时结束或者请求超时会跳转，在该页面申请权限，体验不好
        // TTAdManagerHolder.getInstance(this).requestPermissionIfNecessary(this);

        // 定时，AD_TIME_OUT时间到时执行，如果开屏广告没有加载则跳转到主页面
        mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT);

        // 初始化自定义广告 View
        initView();

        // 加载开屏广告
        loadSplashAd();
    }

    // 初始化开屏广告 View
    private void initView() {
        // 初始化广告渲染组件
        mSplashContainer = this.findViewById(R.id.splash_container);

        // 设置软件底部 icon，title
        try {
            ActivityInfo appInfo = getPackageManager().getActivityInfo(this.getComponentName(),
                    PackageManager.GET_META_DATA);

            RoundedCorners roundedCorners = new RoundedCorners(20);
            // 通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
            ImageView splashIcon = findViewById(R.id.splash_icon);
            Glide.with(this).load(appInfo.loadIcon(getPackageManager())).apply(options).into(splashIcon);
            // 设置 appIcon

            Bundle bundle = appInfo.metaData;

            if (bundle != null) {

                String splashTitle = bundle.getString("splash_title");
                // 获取标题

                int splashTitleColor = bundle.getInt("splash_title_color");
                // 获取标题颜色

                TextView splashName = findViewById(R.id.splash_name);
                if (splashTitle != null) {
                    splashName.setText(splashTitle);
                }
                if (splashTitleColor != 0) {
                    splashName.setTextColor(splashTitleColor);
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 加载开屏广告方法
    private void loadSplashAd() {

        // 创建开屏广告请求参数 AdSlot ,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(code_id)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();

        // 请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {

            @Override
            @MainThread
            public void onError(int code, String message) {
                // 广告渲染失败
                Log.d(TAG, message);
                mHasLoaded = true;
//                showToast(message + " - " + code_id);

                // 回调监听方法
                WritableMap params = Arguments.createMap();
                params.putString("onAdError", "广告渲染失败:" + message);
                sendEvent(TAG + "-onAdError", params);

                // 关闭开屏广告
                goToMainActivity();
            }

            @Override
            @MainThread
            public void onTimeout() {
                // 开屏广告渲染超时
                mHasLoaded = true;
//                showToast("加载超时");
                // 回调监听方法
                WritableMap params = Arguments.createMap();
                params.putString("onAdError", "加载超时");
                sendEvent(TAG + "-onAdError", params);

                // 关闭开屏广告
                goToMainActivity();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                mHasLoaded = true;
                mHandler.removeCallbacksAndMessages(null);
                if (ad == null) {
                    // 回调监听方法
                    WritableMap params = Arguments.createMap();
                    params.putString("onAdError", "未拉取到开屏广告");
                    sendEvent(TAG + "-onAdError", params);

                    // 未知错误获取到的广告对象为空，关闭广告
                    goToMainActivity();
                    return;
                }

                // 获取SplashView
                View view = ad.getSplashView();
                mSplashContainer.removeAllViews();

                // 把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                mSplashContainer.addView(view);

                // 设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                // ad.setNotAllowSdkCountdown();

                // 设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {

                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d(TAG, "onAdClick");
                        WritableMap params = Arguments.createMap();
                        params.putBoolean("onAdClick", true);
                        sendEvent(TAG + "-onAdClick", params);

                        // showToast("开屏广告点击");
                        goToMainActivity();
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d(TAG, "onAdShow");
                        WritableMap params = Arguments.createMap();
                        params.putBoolean("onAdShow", true);
                        sendEvent(TAG + "-onAdShow", params);
                        // showToast("开屏广告展示");
                    }

                    @Override
                    public void onAdSkip() {
                        Log.d(TAG, "onAdSkip");
                        WritableMap params = Arguments.createMap();
                        params.putBoolean("onAdSkip", true);
                        sendEvent(TAG + "-onAdSkip", params);

                        // showToast("开屏广告跳过");
                        goToMainActivity();

                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.d(TAG, "onAdTimeOver");
                        // showToast("开屏广告倒计时结束");
                        WritableMap params = Arguments.createMap();
                        params.putBoolean("onAdClose", true);
                        sendEvent(TAG + "-onAdClose", params);
                        goToMainActivity();
                    }
                });
            }

        }, AD_TIME_OUT);


    }

    // 关闭开屏广告方法
    private void goToMainActivity() {
        if (mSplashContainer != null) {
            mSplashContainer.removeAllViews();
        }
        this.overridePendingTransition(0, 0); // 不要过渡动画
        this.finish();
    }

    private void showToast(String msg) {
        // TToast.show(this, "splash:" + msg);
    }

    @Override
    public void handleMsg(Message msg) {
        if (msg.what == MSG_GO_MAIN) {
            if (!mHasLoaded) {
                showToast("加载超时");
                goToMainActivity();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (AdBoss.splashAd_anim_in != -1) {
            // 实现广告关闭跳转 Activity 动画设置
            overridePendingTransition(AdBoss.splashAd_anim_in, AdBoss.splashAd_anim_out);
        }
    }
}

