package com.haxifang.ad.views;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.os.Handler;
import android.os.Looper;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTImage;
import com.facebook.react.bridge.ReactContext;
import com.haxifang.R;
import com.haxifang.ad.AdBoss;
import com.haxifang.ad.utils.Utils;

import java.util.LinkedList;
import java.util.List;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

public class StreamView extends RelativeLayout {

    public static final String TAG = "StreamView";
    private String _codeId = null;
    protected Context mContext;
    protected ReactContext reactContext;
    final protected FrameLayout mContainer;
    private AdSlot adSlot;
    private boolean adShowing = false;

    private StreamAdPlayer mStreamAdPlayer;
//    private ViewGroup mAdLayout;


    public StreamView(ReactContext context) {
        super(context);
        reactContext = context;
        mContext = context;

        // 初始化广告渲染组件
        inflate(mContext, R.layout.stream_view, this);
        mContainer = findViewById(R.id.tt_stream_container);

        // 这个函数很关键，不然不能触发再次渲染，让 view 在 RN 里渲染成功!!
        Utils.setupLayoutHack(this);
    }

    public void setCodeId(String codeId) {
        _codeId = codeId;
        runOnUiThread(() -> {
            tryShowAd();
        });
    }


    void tryShowAd() {
        if (AdBoss.TTAdSdk == null) {
            Log.d(TAG, "AdBoss 还没初始化完成 with appid " + AdBoss.tt_appid);
            return;
        }
        if (_codeId == null) {
            Log.d(TAG, "loadStreamAd: 属性还不完整 _codeId=" + _codeId);
            return;
        }

        // 开始渲染 Stream 广告，原生渲染
        Log.d(TAG, "原生渲染 loadStreamAd: loadAd");
        loadStreamAd();
    }


    private void loadStreamAd() {
        // step4:创建feed广告请求类型参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(_codeId)
                .setImageAcceptedSize(640, 320)
                .setAdCount(1) //请求广告数量为1到3条
                .build();
        // step5:请求广告，调用feed广告异步请求接口，加载到广告后，拿到广告素材自定义渲染
        AdBoss.TTAdSdk.loadStream(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.d(TAG, message);
//                TToast.show(StreamActivity.this, message);
            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> ads) {

                if (ads == null || ads.isEmpty()) {
//                    TToast.show(getApplicationContext(), "广告加载失败");
                    return;
                }
//                TToast.show(getApplicationContext(), "广告加载完成");

                if (mStreamAdPlayer != null) {
                    mStreamAdPlayer.clear();
                }
                // step6: StreamAdPlayer是我们提供的Demo示例，仅供参考
                mStreamAdPlayer = new StreamAdPlayer(ads, mContainer);

                mStreamAdPlayer.play();
            }
        });
    }




    class StreamAdPlayer {
        private TTHandler mHandler = new TTHandler(Looper.getMainLooper());

        private ViewGroup mParent;
        private List<TTFeedAd> mAdList;
        private int mNowPlay = 0;

        private Runnable mPlayNext = new Runnable() {
            @Override
            public void run() {
                mParent.removeAllViews();
                if (mAdList.size() <= mNowPlay) {
                    return;
                }
                TTFeedAd ttFeedAd = mAdList.get(mNowPlay++);
                OneAd oneAd = getAdView(ttFeedAd);
                Log.d(TAG, "开始播放 " + ttFeedAd.getDescription() + mNowPlay);
                mParent.addView(oneAd.adView);
                mHandler.sendPostDelayed(mPlayNext, (long) (oneAd.duration * 1000));
            }
        };

        public OneAd getAdView(TTFeedAd ttFeedAd) {
            OneAd oneAd = null;

            switch (ttFeedAd.getImageMode()) {
                case TTAdConstant.IMAGE_MODE_LARGE_IMG:
                case TTAdConstant.IMAGE_MODE_SMALL_IMG:
                case TTAdConstant.IMAGE_MODE_GROUP_IMG:
                case TTAdConstant.IMAGE_MODE_VERTICAL_IMG:
                    oneAd = getImageTypeView(ttFeedAd);
                    break;
                case TTAdConstant.IMAGE_MODE_VIDEO:
                case TTAdConstant.IMAGE_MODE_VIDEO_VERTICAL:
                    oneAd = getVideoTypeView(ttFeedAd);
                    break;
            }
            return oneAd;
        }

        public StreamAdPlayer(List<TTFeedAd> adList, ViewGroup parent) {
            this.mAdList = adList;
            this.mParent = parent;
        }

        public OneAd getImageTypeView(TTFeedAd ttFeedAd) {
            List<TTImage> ttImages = ttFeedAd.getImageList();
            if (ttImages.size() == 0) {
                return null;
            }
            TTImage ttImage = ttImages.get(0);
            if (!ttImage.isValid()) {
                return null;
            }
            ImageView imageView = new ImageView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000);
            imageView.setLayoutParams(layoutParams);
            Glide.with(mContext).load(ttImage.getImageUrl()).into(imageView);


            List<View> clickViews = new LinkedList<>();
            clickViews.add(imageView);
            List<View> creativeClickViews = new LinkedList<>();
            creativeClickViews.add(imageView);
            ttFeedAd.registerViewForInteraction(mContainer, clickViews, creativeClickViews, new TTNativeAd.AdInteractionListener() {
                @Override
                public void onAdClicked(View view, TTNativeAd ad) {
                    if (ad != null) {
//                        TToast.show(getApplicationContext(), "广告" + ad.getTitle() + "被点击");
                    }
                }

                @Override
                public void onAdCreativeClick(View view, TTNativeAd ad) {
                    if (ad != null) {
//                        TToast.show(getApplicationContext(), "广告" + ad.getTitle() + "被创意按钮被点击");
                    }
                }

                @Override
                public void onAdShow(TTNativeAd ad) {
                    if (ad != null) {
//                        TToast.show(getApplicationContext(), "广告" + ad.getTitle() + "展示");
                    }
                }
            });
            return new OneAd(imageView, ttImage.getDuration());
        }

        public OneAd getVideoTypeView(TTFeedAd ttFeedAd) {

            View videoView = ttFeedAd.getAdView();

            List<View> clickViews = new LinkedList<>();
            clickViews.add(ttFeedAd.getAdView());
            List<View> creativeClickViews = new LinkedList<>();
            creativeClickViews.add(videoView);
            ttFeedAd.registerViewForInteraction(mContainer, clickViews, creativeClickViews, new TTNativeAd.AdInteractionListener() {
                @Override
                public void onAdClicked(View view, TTNativeAd ad) {
                    if (ad != null) {
//                        TToast.show(getApplicationContext(), "广告" + ad.getTitle() + "被点击");
                    }
                }

                @Override
                public void onAdCreativeClick(View view, TTNativeAd ad) {
                    if (ad != null) {
//                        TToast.show(getApplicationContext(), "广告" + ad.getTitle() + "被创意按钮被点击");
                    }
                }

                @Override
                public void onAdShow(TTNativeAd ad) {
                    if (ad != null) {
//                        TToast.show(getApplicationContext(), "广告" + ad.getTitle() + "展示");
                    }

                }
            });
            return new OneAd(videoView, ttFeedAd.getVideoDuration());
        }

        public void play() {
            if (mParent == null) {
                return;
            }

            mParent.removeAllViews();

            mPlayNext.run();
        }

        public void clear() {
            mHandler.clearCallback();
        }

        public void pauseHandler() {
            mHandler.postDelayedPause();
        }

        public void resumeHandler() {
            mHandler.postDelayedResume();
        }

        class OneAd {
            View adView;
            double duration;

            public OneAd(View adView, double duration) {
                this.adView = adView;
                this.duration = duration;
            }
        }

    }



    /**
     * 可暂停Handler，在界面onPause，onResume切换时，能保证视频postDelay的时间不被后台时消耗
     */
    static class TTHandler extends Handler {

        private Runnable mRunnable;
        private long mOldAtTime;
        private long mRemainTime;

        TTHandler(Looper looper) {
            super(looper);
        }

        void postDelayedPause() {
            mRemainTime = mOldAtTime - System.currentTimeMillis();
            Log.d(TAG, "TTHandler pause remainTime: " + mRemainTime);
            removeCallbacks(mRunnable);
        }

        void postDelayedResume() {
            if (mRemainTime > 0) {
                Log.d(TAG, "TTHandler resume remainTime: " + mRemainTime);
                sendPostDelayed(mRunnable, mRemainTime);
            }
        }

        void sendPostDelayed(Runnable r, long delayMillis) {
            mRemainTime = 0;
            mRunnable = r;
            mOldAtTime = System.currentTimeMillis() + delayMillis;
            postDelayed(mRunnable, delayMillis);
        }

        void clearCallback() {
            mOldAtTime = 0;
            mRemainTime = 0;
            removeCallbacks(mRunnable);
        }

    }


}
