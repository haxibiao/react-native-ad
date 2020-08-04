package com.haxifang.ad.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.haxifang.R;
import com.haxifang.ad.AdBoss;

import java.util.ArrayList;
import java.util.List;

public class DrawFeedView extends RelativeLayout {

    private String TAG = "DrawFeed";
	private String _isExpress = "true";
    private String _codeid = null;
    protected Context mContext;
    protected ReactContext reactContext;
    final protected FrameLayout mContainer;
    private AdSlot adSlot;


    public DrawFeedView(ReactContext context) {
        super(context);
        reactContext = context;
        mContext = context;

        // 初始化广告渲染组件
        inflate(mContext, R.layout.draw_video, this);
        mContainer = findViewById(R.id.tt_video_layout_hxb);

        // 修复 add view 不显示问题
        setupLayoutHack();
    }
	
    public void setCodeId(String codeId) {
        _codeid = codeId;
        loadDrawFeedAd();
    }

    void loadDrawFeedAd() {
        if (_codeid == null) {
            Log.d(TAG, "loadDrawFeedAd: 属性还不完整 _codeid=" + _codeid);
            return;
		}

        if (_isExpress.equals("true")) {
            // 开始渲染 Draw 广告，原生模版
            Log.d(TAG, "loadDrawFeedAd: loadExpressDrawNativeAd()");
            loadExpressDrawNativeAd();
        } else {
            // 开始渲染 Draw 广告，自定义模版
            Log.d(TAG, "loadDrawFeedAd: loadAd");
            loadAd();
        }

    }

    // for fix addView not showing
    void setupLayoutHack() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                manuallyLayoutChildren();
                getViewTreeObserver().dispatchOnGlobalLayout();
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    void manuallyLayoutChildren() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        }
    }


    // 加载原生渲染方式的 Draw 广告
    private void loadExpressDrawNativeAd() {
        // 创建广告请求参数AdSlot,具体参数含义参考文档
        float expressViewWidth = 1080;
        float expressViewHeight = 1920;
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(_codeid)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) // 期望模板广告view的size,单位dp
                .setAdCount(1) // 请求广告数量为1到3条
                .build();

        // 请求广告,对请求回调的广告作渲染处理
        AdBoss.TTAdSdk.loadExpressDrawFeedAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.d(TAG, message);
                // showToast(message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.isEmpty()) {
                    // TToast.show(mContext, " ad is null!");
                    return;
                }
                for (final TTNativeExpressAd ad : ads) {
                    // 点击监听器必须在getAdView之前调
                    ad.setVideoAdListener(new TTNativeExpressAd.ExpressVideoAdListener() {
                        @Override
                        public void onVideoLoad() {

                        }

                        @Override
                        public void onVideoError(int errorCode, int extraCode) {

                        }

                        @Override
                        public void onVideoAdStartPlay() {

                        }

                        @Override
                        public void onVideoAdPaused() {

                        }

                        @Override
                        public void onVideoAdContinuePlay() {

                        }

                        @Override
                        public void onProgressUpdate(long current, long duration) {

                        }

                        @Override
                        public void onVideoAdComplete() {

                        }

                        @Override
                        public void onClickRetry() {
                            // TToast.show(mContext, " onClickRetry !");
                            Log.d("drawss", "onClickRetry!");
                        }
                    });
                    ad.setCanInterruptVideoPlay(true);
                    ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, int type) {

                        }

                        @Override
                        public void onAdShow(View view, int type) {

                        }

                        @Override
                        public void onRenderFail(View view, String msg, int code) {

                        }

                        @Override
                        public void onRenderSuccess(View view, float width, float height) {
                            // TToast.show(mContext, "Draw渲染成功");
                            mContainer.addView(ad.getExpressAdView());
                        }
                    });
                    ad.render();
                }


            }
        });
    }

    // 开始加载自定义方式的 Draw 广告
    private void loadAd() {
        // TODO: 这里动态获取权限待实现
        // code...

        // 创建广告请求参数
        adSlot = new AdSlot.Builder()
                .setCodeId(_codeid) // 设置广告位 ID
                .setSupportDeepLink(true)
                .setAdCount(1) // 设置请求广告条数为 1 到 3 条
                .setImageAcceptedSize(1080, 1920)
                .build();

        // 请求广告，对请求回调的广告做渲染处理
        final DrawFeedView _this = this;
        AdBoss.TTAdSdk.loadDrawFeedAd(adSlot, new TTAdNative.DrawFeedAdListener() {
            @Override
            public void onError(int code, String message) {
                message = "错误结果 loadDrawFeedAd onError: " + code + ", " + message;
                Log.d(TAG, message);
                _this.onError(message);
            }

            @Override
            public void onDrawFeedAdLoad(List<TTDrawFeedAd> ads) {
                // Draw 广告加载成功，开始执行渲染

                Log.d(TAG, "onDrawFeedAdLoad: load ad ok!!!!");
                if (ads == null || ads.size() <= 0) {
                    // 没有广告内容，跳出渲染并返回 error
                    _this.onError("Draw 广告加载成功，但是没有广告内容");
                    return;
                }

                // 遍历 Draw 广告组
                for (TTDrawFeedAd ad : ads) {

                    // 避免rn获取activity问题引起广告展示都闪退
                    Activity ac = reactContext.getCurrentActivity();
                    if (ac != null) {
                        ad.setActivityForDownloadApp(ac);
                    }

                    // 视频的点击监听器必须在getAdView之前调
                    ad.setDrawVideoListener(new TTDrawFeedAd.DrawVideoListener() {
                        @Override
                        public void onClickRetry() {
                            // 用户点击了重新播放广告
                            Log.d(TAG, "onClickRetry!");
                        }

                        @Override
                        public void onClick() {
                            // 用户点击了广告详情
                            Log.d(TAG, "onClick download or view detail page ! !");
                            onAdClick();
                        }
                    });

                    // 设置广告视频是否自动播放
                    ad.setCanInterruptVideoPlay(true);

                    // 设置广告视频暂停按钮图标
                    ad.setPauseIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_video_uot), 90);

                    // VideoView
                    View view = ad.getAdView();
                    mContainer.addView(view);

                    // 广告替换用户头像
                    String headicon = "";
                    if (ad.getIcon() != null && ad.getIcon().getImageUrl() != null) {
                        headicon = ad.getIcon().getImageUrl();
                    }
                    onAdLoad(headicon);

                    // 点击标题，下载按钮+事件
                    initAdViewAndAction(ad, mContainer);

                }


            }
        });

    }

    // 绑定 View 点击和下载事件
    private void initAdViewAndAction(TTDrawFeedAd ad, ViewGroup container) {
        TextView tvAdAppName = findViewById(R.id.tvAdAppName);
        tvAdAppName.setText("@ " + ad.getTitle());
        TextView tvDescription = findViewById(R.id.tvDescription);
        tvDescription.setText(ad.getDescription());
        // Button btnAction = findViewById(R.id.btnAction);
        // btnAction.setText(ad.getButtonText());

        if (_isExpress.equals("false")) {
            tvAdAppName.setVisibility(VISIBLE);
            tvDescription.setVisibility(VISIBLE);
            tvDescription.setVisibility(VISIBLE);
        }

        ImageView usAvatar = findViewById(R.id.usAvatar);
        String mUsAvatar = ad.getIcon().getImageUrl();
        Glide.with(mContext).load(mUsAvatar).apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(usAvatar);
        // 设置广告用户头像

        final ImageButton btnLikeVideo = findViewById(R.id.btn_likevideo);
        // 初始化点赞按钮

        btnLikeVideo.setOnClickListener(new OnClickListener() {
            boolean is_like = true;

            @Override
            public void onClick(View v) {

                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.6f, 1, 0.6f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                // 初始化点赞动画对象

                scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // 点赞动画执行开始方法
                        // Log.d(TAG, "onAnimationStart: 测试");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // 点赞动画执行结束方法
                        if (is_like) {
                            Glide.with(mContext).load(R.drawable.ic_video_liked).into(btnLikeVideo);
                        } else {
                            Glide.with(mContext).load(R.drawable.ic_video_like).into(btnLikeVideo);
                        }
                        is_like = !is_like;
                        // 处理点赞状态
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // 点赞动画执行中方法
                    }
                });
                // 设置监听点赞动画对象

                scaleAnimation.setDuration(100);
                // 设置点赞动画执行时间

                v.startAnimation(scaleAnimation);
                // 执行点赞动画

            }
        });

//        new Thread(() -> {
//            try {
//                Thread.sleep(6500);
//                 btnAction.setVisibility(View.VISIBLE);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();

        List<View> clickViews = new ArrayList<>();
        clickViews.add(mContainer);
        List<View> creativeViews = new ArrayList<>();
        // creativeViews.add(btnAction); // 添加广告点击按钮可触发广告点击
        creativeViews.add(usAvatar); // 添加点击广告用户可触发广告点击

        ad.registerViewForInteraction(container, clickViews, creativeViews, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ad) {
                onAdClick();
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {
                onAdClick();
            }

            @Override
            public void onAdShow(TTNativeAd ad) {
                String headicon = "";
                if (ad.getIcon() != null && ad.getIcon().getImageUrl() != null) {
                    headicon = ad.getIcon().getImageUrl();
                }
//                 onAdDidShow(headicon);
            }
        });

    }


    // 组件 Error 事件返回
    public void onError(String message) {
        WritableMap event = Arguments.createMap();
        event.putString("message", message);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdError", event);
    }

    // 广告点击回调响应事件
    public void onAdClick() {
        WritableMap event = Arguments.createMap();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdClick", event);
    }

    // 广告加载回调
    public void onAdLoad(String headIconUrl) {
        WritableMap event = Arguments.createMap();
        event.putString("headicon", headIconUrl);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdShow", event);
    }

}
