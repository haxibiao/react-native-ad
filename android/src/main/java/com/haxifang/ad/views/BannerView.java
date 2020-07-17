package com.haxifang.ad.views;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.haxifang.R;
import com.haxifang.ad.TTAdManagerHolder;
import com.haxifang.ad.utils.DislikeDialog;
import com.haxifang.ad.utils.Utils;

import java.util.List;

public class BannerView extends LinearLayout {

    // TODO: Banner 广告还有一个巨大的坑，显示宽高有问题
    private final String TAG = "hxb-rn-TTBannerAd";

    private ReactContext reactContext;
    private Activity mContext;
    private AdSlot adSlot;
    private TTAdNative mTTAdNative;

    private LinearLayout _bannerView;
    private View adView = null;
    private String _codeId;
    private int screenWidth; // default, 通过屏幕宽度计算得出
    private int _expectedWidth = 0, _expectedHeight = 0; // 高度等于 0 自适应
    private long startTime = 0;
    private boolean mHasShowDownloadActive = false;


    public BannerView(ReactContext context) {
        super(context);
        reactContext = context;
        mContext = context.getCurrentActivity();
        mTTAdNative = TTAdManagerHolder.get().createAdNative(context);
        inflate(context, R.layout.tt_banner_view_hxb, this);
        _bannerView = findViewById(R.id.banner_container);

        // DisplayMetrics metric = new DisplayMetrics();
        // mContext.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // screenWidth = (int) (metric.widthPixels / metric.density);

        // 此方法用于修复设置 view 属性后 RN 不刷新组件
        Utils.setupLayoutHack(this);

    }

//    public void setSize(String size) {
//        Log.d(TAG, "setSize: " + size + ", screenWidth:" + screenWidth);
//        switch (size) {
//            case "large":
//                _expectedWidth = screenWidth * 4 / 5;
//                break;
//            default:
//                _expectedWidth = screenWidth / 2;
//                break;
//
//        }
//        // size 可选了
//    }

    public void setAdWidth(int width) {
        Log.d(TAG, "setAdWidth: " + width + ", screenWidth:" + screenWidth);
        _expectedWidth = width;
        loadAd();
    }

    public void setCodeId(String codeId) {
        _codeId = codeId;
        loadAd();
    }

    public void loadAd() {

        Log.d(TAG, "loadAd , _expectedWidth: " + _expectedWidth + " , _codeId: " + _codeId);
        if (_expectedWidth == 0 || _codeId.isEmpty()) {
            // 必传组件宽度和广告位 code id，否则跳出加载广告
            return;
        }

        // 创建广告请求参数 AdSlot , 具体参数含义参考文档 modules.add(new Interaction(reactContext));
        adSlot = new AdSlot.Builder()
                .setCodeId(_codeId)
                .setSupportDeepLink(true)
                .setAdCount(3) // 请求广告数量为 1 到 3 条
                .setExpressViewAcceptedSize(_expectedWidth, _expectedHeight) // 期望模版广告 view 的 size，单位 dp，高度输入 0 表示自适应
                .setImageAcceptedSize(640, 320)
                .build();

        BannerView _this = this;
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {

            @Override
            public void onError(int code, String message) {
                Log.d(TAG, "onError: " + message + ", code: " + code);
                _bannerView.removeAllViews();
                _this.onError(message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                Log.d(TAG, "onNativeExpressAdLoad loaded ok!!! " + ads.size());
                if (ads == null || ads.size() <= 0) {
                    _this.onError("加载成功但是没有广告！");
                    return;
                }

                TTNativeExpressAd mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30 * 1000); // 最低 30s 换一个 Banner，最高 120s

                bindAdListener(mTTAd, mContext, _bannerView);
                startTime = System.currentTimeMillis();
                mTTAd.render();

            }
        });

    }

    private void bindAdListener(TTNativeExpressAd ad, final Activity context, LinearLayout bannerView) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int i) {
                // 广告被点击
                onAdClick();
            }

            @Override
            public void onAdShow(View view, int i) {
                // 广告开始展示
            }

            @Override
            public void onRenderFail(View view, String s, int i) {
                // 广告渲染失败
                onError("广告渲染失败！");
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                // 广告渲染成功
                Log.e(TAG, "render suc:" + (System.currentTimeMillis() - startTime));
                adView = view;
                onLayoutChanged((int) width, (int) height);

                context.runOnUiThread(() -> {
                    if (!context.isFinishing()) {
                        Log.d(TAG, "activity not finished " + _bannerView.toString());
                        _bannerView.addView(view);
                    } else {
                        Log.d(TAG, "activity finished");
                    }
                });
            }
        });

        // dislike 设置，用户点击不喜欢广告设置
        bindDislike(ad, true, context);

        // 广告是下载类型，设置下载监听
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                // 点击开始下载
            }

            @Override
            public void onDownloadActive(long l, long l1, String s, String s1) {
                // 下载中点击暂停
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                }
            }

            @Override
            public void onDownloadPaused(long l, long l1, String s, String s1) {
                // 下载暂停，点击继续
            }

            @Override
            public void onDownloadFailed(long l, long l1, String s, String s1) {

                // 下载失败，点击重新下载
            }

            @Override
            public void onDownloadFinished(long l, String s, String s1) {
                // 安装完成，点击打开
            }

            @Override
            public void onInstalled(String s, String s1) {
                // 点击安装
            }
        });

    }

    private void bindDislike(TTNativeExpressAd ad, boolean customStyle, final Activity context) {
        if (customStyle) {
            // 使用自定义样式
            List<FilterWord> words = ad.getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(context, words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    // 屏蔽广告
                    // TToast.show(mContext, "点击 " + filterWord.getName());
                    // 用户选择不喜欢原因后，移除广告展示
                    _bannerView.removeAllViews();
                    onAdClosed(filterWord.getName());
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(context, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                // TToast.show(mContext, "点击 " + value);
                // 用户选择不喜欢原因后，移除广告展示
                _bannerView.removeAllViews();
                onAdClosed(value);
            }

            @Override
            public void onCancel() {
                // TToast.show(mContext, "点击取消 ");
            }

            @Override
            public void onRefuse() {

            }
        });
    }

    public void onError(String message) {
        WritableMap event = Arguments.createMap();
        event.putString("message", message);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdError", event);
    }

    public void onAdClick() {
        WritableMap event = Arguments.createMap();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdClicked", event);
    }

    public void onAdClosed(String reason) {
        WritableMap event = Arguments.createMap();
        event.putString("reason", reason);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdClosed", event);
    }

    public void onLayoutChanged(int width, int height) {
        Log.d(TAG, "onLayoutChanged: " + width + ", " + height);
        WritableMap event = Arguments.createMap();
        event.putInt("width", width);
        event.putInt("height", height);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onLayoutChanged", event);
    }


}
