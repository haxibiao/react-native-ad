//package com.haxifang.ad.activities.tencent;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
//import com.bumptech.glide.request.RequestOptions;
//import com.haxifang.R;
//import com.qq.e.ads.splash.SplashAD;
//import com.qq.e.ads.splash.SplashADListener;
//import com.qq.e.comm.util.AdError;
//
//
//public class SplashActivity extends Activity {
//
//    protected static final String TAG = "TXSplashActivity";
//    Context mContext;
//
//    String appId = null;
//    String codeId = null;
//
//    public boolean canJump = false;
//
//    // 控制返回按钮点击事件
//    private boolean needFinish = false;
//
//    private SplashAD splashAD;
//
//    private LinearLayout mADViewRoot;
//    private TextView skipContainer;
//    private FrameLayout adContainer;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_tx);
//
//        mContext = this;
//
//        // init View
//        mADViewRoot = findViewById(R.id.tx_splash_view_root);
//        skipContainer = findViewById(R.id.tx_skip_view);
//        adContainer = findViewById(R.id.tx_splash_container);
//
//        try {
//            ActivityInfo appInfo = getPackageManager().getActivityInfo(this.getComponentName(),
//                    PackageManager.GET_META_DATA);
//
//            appId = getIntent().getStringExtra("appid");
//            codeId = getIntent().getStringExtra("codeid");
//             Log.i(TAG, "tx splash onCreate_appid: " + appId + " codeid:" + codeId);
//
//            RoundedCorners roundedCorners = new RoundedCorners(20);
//            // 通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
//            ImageView splashIcon = findViewById(R.id.tx_splash_icon);
//            Glide.with(this).load(appInfo.loadIcon(getPackageManager())).apply(options).into(splashIcon);
//            // 设置 appIcon
//
//            Bundle bundle = appInfo.metaData;
//
//            if(bundle != null ){
//
//                String splashTitle = bundle.getString("splash_title");
//                // 获取标题
//
//                int splashTitleColor = bundle.getInt("splash_title_color");
//                // 获取标题颜色
//
//                TextView splashName = findViewById(R.id.tx_splash_name);
//                if (splashTitle != null) {
//                    splashName.setText(splashTitle);
//                }
//                if (splashTitleColor != 0) {
//                    splashName.setTextColor(splashTitleColor);
//                }
//            }
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        // 初始化广告
//        splashAD = new SplashAD(this, skipContainer, appId, codeId, new SplashADListener() {
//            @Override
//            public void onADDismissed() {
//                // 广告关闭时调用，可能是用户关闭或者展示时间到。此时一般需要跳过开屏的 Activity，进入应用内容页面
//                goToMainActivity();
//            }
//
//            @Override
//            public void onNoAD(AdError adError) {
//                // 广告加载失败，error 对象包含了错误码和错误信息
//                if(adError.getErrorCode() == 5004) {
//                    // TODO:没有广告自动跳转填充头条广告
//                    goToMainActivity();
//                } else {
//                    goToMainActivity();
//                    Log.e(TAG,adError.getErrorMsg() + " 错误代码：" + adError.getErrorCode());
//                }
//
//            }
//
//            @Override
//            public void onADPresent() {
//                // 广告成功展示时调用，成功展示不等于有效展示（比如广告容器高度不够）
//            }
//
//            @Override
//            public void onADClicked() {
//                // 广告被点击时调用，不代表满足计费条件（如点击时网络异常）
//            }
//
//            /**
//             * 倒计时回调，返回广告还将被展示的剩余时间。
//             * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
//             *
//             * @param millisUntilFinished 剩余毫秒数
//             */
//            @Override
//            public void onADTick(long millisUntilFinished) {
//                // 打印广告倒计时
//                Log.i(TAG,millisUntilFinished + "");
//            }
//
//            @Override
//            public void onADExposure() {
//                // 广告曝光时调用，此处的曝光不等于有效曝光（如展示时长未满足）
//                Log.i(TAG,"腾讯广告开始曝光");
//            }
//
//        }, 0, null);
//        splashAD.preLoad();
//        splashAD.fetchAndShowIn(adContainer);
//
//    }
//
//
//    // 跳转到主页面
//    private void goToMainActivity() {
//        /**
//         * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
//         * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
//         */
//        if (canJump) {
//            mADViewRoot.removeAllViews();
//            // remove 广告 view 视图
//            this.overridePendingTransition(0, 0);
//            // 不要过渡动画
//            this.finish();
//            // 关闭开屏广告
//        } else {
//            canJump = true;
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        canJump = false;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (canJump) {
//            goToMainActivity();
//        }
//        canJump = true;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
//        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
//            if(needFinish){
//                finish();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//
//}
