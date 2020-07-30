package com.haxifang.ad.views;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
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
import com.haxifang.ad.AdBoss;
import com.haxifang.ad.utils.DislikeDialog;
import com.haxifang.ad.utils.Utils;

import java.util.List;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

public class FeedAdView extends RelativeLayout {

    private static final String TAG = "FeedAd";

    private Activity mContext;
    private ReactContext reactContext;
    private String _codeid = "";
    private AdSlot adSlot;

    private int _expectedWidth = 0;
    private int _expectedHeight = 0; // 高度0 自适应
    final private long startTime = 0;
    private boolean mHasShowDownloadActive = false;

    final protected RelativeLayout mExpressContainer;

    public FeedAdView(ReactContext context) {
        super(context);
        mContext = context.getCurrentActivity();
        reactContext = context;
        inflate(context, R.layout.feed_view, this);
        mExpressContainer = findViewById(R.id.feed_container);

        // 这个函数很关键，不然不能触发再次渲染，让 view 在 RN 里渲染成功!!
        Utils.setupLayoutHack(this);
    }

    public void setWidth(int width) {
        Log.d(TAG, "setCodeId = " + _codeid + ", setWidth:" + width);
        _expectedWidth = width;
        showAd();
    }

    public void setCodeId(String codeId) {
        Log.d(TAG, "setCodeId: " + codeId + ", _expectedWidth:" + _expectedWidth);
        _codeid = codeId;
        showAd();
    }

    public void showAd() {
        Log.d(TAG, "showAd: width:" + _expectedWidth + " codeid:" + _codeid);
        // 显示广告
        if (_expectedWidth == 0 || _codeid.isEmpty()) {
            // 广告宽度未设置或 code id 未设置，停止显示广告
            return;
        }
        // 信息流广告原来不能提前预加载，很容易出现超时，必须当场加载
        // sdk里很容易出现 message send to dead thread ... 肯定有些资源线程依赖！
        runOnUiThread(() -> {
            loadTTFeedAd();
        });
    }

    // 显示头条的信息流广告
    public void loadTTFeedAd() {

        // 创建广告请求参数AdSlot,具体参数含义参考文档 modules.add(new Interaction(reactContext));
        adSlot = new AdSlot.Builder().setCodeId(_codeid) // 广告位id
                .setSupportDeepLink(true).setAdCount(1) // 请求广告数量为1到3条
                .setExpressViewAcceptedSize(_expectedWidth, _expectedHeight) // 期望模板广告view的size,单位dp,高度0自适应
                .setImageAcceptedSize(640, 320)
                // 新版本，不设置AdType 也没差量无效了...
                .setNativeAdType(AdSlot.TYPE_INTERACTION_AD).build();

        // 请求广告，对请求回调的广告作渲染处理
        final FeedAdView _this = this;
        AdBoss.TTAdSdk.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                message = "错误结果 loadNativeExpressAd onAdError: " + code + ", " + message;
                // TToast.show(getContext(), message);
                Log.d(TAG, message);
                mExpressContainer.removeAllViews();
                _this.onAdError(message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                Log.d(TAG, "onNativeExpressAdLoad: !!!");
                if (ads == null || ads.size() == 0) {
                    _this.onAdError("加载成功无广告内容");
                    return;
                }

                TTNativeExpressAd ad = ads.get(0);
                // 缓存加载成功的广告
                // AdBoss.feedAd = ad;
                _showTTAd(ad);
            }
        });
    }

    // 显示广告
    private void _showTTAd(final TTNativeExpressAd ad) {
        mContext.runOnUiThread(() -> {
            bindAdListener(ad);
            ad.render();
        });
    }

    // 绑定Feed express ================================
    final private void bindAdListener(TTNativeExpressAd ad) {
        final FeedAdView _this = this;
        final RelativeLayout mExpressContainer = findViewById(R.id.feed_container);
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.d(TAG, "feed ad clicked");
                // TToast.show(mContext, "广告被点击");
                onAdClick();
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.d(TAG, "render onAdShow:" + (System.currentTimeMillis() - startTime));
                // TToast.show(mContext, "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.d(TAG, "render fail:" + (System.currentTimeMillis() - startTime));
                _this.onAdError("加载成功 渲染失败 code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                // 渲染成功，清掉缓存
                // AdBoss.feedAd = null;

                // 返回view的宽高 单位 dp
                // TToast.show(mContext, "渲染成功");
                // 在渲染成功回调时展示广告，提升体验
                mExpressContainer.removeAllViews();
                mExpressContainer.addView(view);
                onAdLayout((int) width, (int) height);
            }
        });
        // dislike设置
        bindDislike(ad, true);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        // 可选，下载监听设置
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                // TToast.show(getContext(), "点击开始下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    // TToast.show(getContext(), "下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                // TToast.show(getContext(), "下载暂停，点击继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                // TToast.show(getContext(), "下载失败，点击重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                // TToast.show(getContext(), "安装完成，点击图片打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                // TToast.show(getContext(), "点击安装", Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 设置广告的不喜欢，开发者可自定义样式
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
        final RelativeLayout mExpressContainer = findViewById(R.id.feed_container);
        if (customStyle) {
            // 使用自定义样式
            List<FilterWord> words = ad.getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(getContext(), words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    // 屏蔽广告
                    // TToast.show(mContext, "点击=" + filterWord.getName());
                    // 用户选择不喜欢原因后，移除广告展示
                    mExpressContainer.removeAllViews();
                    onAdClose(filterWord.getName());
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        // 使用默认个性化模板中默认dislike弹出样式
        ad.setDislikeCallback(mContext, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                // TToast.show(mContext, "点击 " + value);
                // 用户选择不喜欢原因后，移除广告展示
                mExpressContainer.removeAllViews();
                onAdClose(value);
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


    // 外部事件..
    public void onAdError(String message) {
        WritableMap event = Arguments.createMap();
        event.putString("message", message);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdError", event);
    }

    public void onAdClick() {
        WritableMap event = Arguments.createMap();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdClick", event);
    }

    public void onAdClose(String reason) {
        Log.d(TAG, "onAdClose: " + reason);
        WritableMap event = Arguments.createMap();
        event.putString("reason", reason);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdClose", event);
    }

    public void onAdLayout(int width, int height) {
        Log.d(TAG, "onAdLayout: " + width + ", " + height);
        WritableMap event = Arguments.createMap();
        event.putInt("width", width);
        event.putInt("height", height);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdLayout", event);
    }

}
