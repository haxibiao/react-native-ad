//
//  FullscreenViewController.m
//  damei
//
//  Created by ivan zhang on 2019/9/26.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "FullscreenViewController.h"

@interface FullscreenViewController () <BUNativeExpressFullscreenVideoAdDelegate>
@property BOOL isAdShowing;
@end

@implementation FullscreenViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    BUNativeExpressFullscreenVideoAd * ad = [AdBoss getFullScreenAd];
    if(ad != nil) {
        //关联回调
        ad.delegate = self;
        [ad loadAdData]; //加载广告
    }
}

//展示广告
- (void) showAd :(BUNativeExpressFullscreenVideoAd *) ad {
    if(!self.isAdShowing) {
        self.isAdShowing = true;
        NSLog(@"展示 提前缓存的ad adValid = %s", ad.adValid ? "Yes":"NO");
        if(ad.adValid) {
            [ad showAdFromRootViewController:self];
        }
    }
    else {
        NSLog(@"已展示提前缓存的ad !!!");
    }
}

#pragma mark BUFullscreenVideoAdDelegate


- (void)nativeExpressFullscreenVideoAdDidLoad:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd {
    BUD_Log(@"%s 全屏视频",__func__);
    [FullScreenVideo emitEvent: @{@"type": @"onAdLoaded", @"message": @"onAdLoaded"}];
    [AdBoss setFullScreenAdCache: fullscreenVideoAd];
    [self showAd: fullscreenVideoAd];
}

- (void)nativeExpressFullscreenVideoAdViewRenderSuccess:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd {
    BUD_Log(@"%s 全屏视频 渲染成功....",__func__);
    [AdBoss setFullScreenAdCache: fullscreenVideoAd];
    [self showAd: fullscreenVideoAd];
}

- (void)nativeExpressFullscreenVideoAd:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd didFailWithError:(NSError *_Nullable)error {
    BUD_Log(@"%s 全屏视频",__func__);
    NSLog(@"error code : %ld , error message : %@",(long)error.code, error.description);
    [FullScreenVideo emitEvent: @{@"type": @"onAdError", @"message": @""}];
}

- (void)nativeExpressFullscreenVideoAdViewRenderFail:(BUNativeExpressFullscreenVideoAd *)rewardedVideoAd error:(NSError *_Nullable)error {
    BUD_Log(@"%s 全屏视频",__func__);
}

- (void)nativeExpressFullscreenVideoAdDidDownLoadVideo:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd {
    BUD_Log(@"%s 全屏视频",__func__);
}

- (void)nativeExpressFullscreenVideoAdWillVisible:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd {
    BUD_Log(@"%s 全屏视频",__func__);
}

- (void)nativeExpressFullscreenVideoAdDidVisible:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd {
    BUD_Log(@"%s 全屏视频",__func__);
}

- (void)nativeExpressFullscreenVideoAdDidClick:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd {
    BUD_Log(@"%s 全屏视频",__func__);
    [FullScreenVideo emitEvent: @{@"type": @"onAdClick", @"message": @""}];
}

- (void)nativeExpressFullscreenVideoAdDidClickSkip:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd {
    BUD_Log(@"%s 全屏视频",__func__);
}

- (void)nativeExpressFullscreenVideoAdWillClose:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd {
    BUD_Log(@"%s 全屏视频",__func__);
}

- (void)nativeExpressFullscreenVideoAdDidClose:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd {
    BUD_Log(@"%s 全屏视频",__func__);
    [FullScreenVideo emitEvent: @{@"type": @"onAdClose", @"message": @""}];
    
    //完成播放 关闭广告 拿回promise结果
    [[AdBoss getRootVC] dismissViewControllerAnimated:true completion:^{
        if([AdBoss getRewardVideoClicks] > 0)
        {
            //每次返回rn后清空激励视频点击次数
            [AdBoss resetClickRewardVideo];
            
            [AdBoss getResolve](@{
                @"video_play":@1,
                @"ad_click":@1, //点击
                @"ad_skip":@1
            });
        }else{
            [AdBoss getResolve](@{
                @"video_play":@1,
                @"ad_click":@0, //没有点击
                @"ad_skip":@1
            });
        }
    }];
}

- (void)nativeExpressFullscreenVideoAdDidPlayFinish:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd didFailWithError:(NSError *_Nullable)error {
    BUD_Log(@"%s 全屏视频",__func__);
}

- (void)nativeExpressFullscreenVideoAdCallback:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd withType:(BUNativeExpressFullScreenAdType) nativeExpressVideoAdType{
    BUD_Log(@"%s 全屏视频",__func__);
}

- (void)nativeExpressFullscreenVideoAdDidCloseOtherController:(BUNativeExpressFullscreenVideoAd *)fullscreenVideoAd interactionType:(BUInteractionType)interactionType {
    NSString *str = nil;
    if (interactionType == BUInteractionTypePage) {
        str = @"ladingpage";
    } else if (interactionType == BUInteractionTypeVideoAdDetail) {
        str = @"videoDetail";
    } else {
        str = @"appstoreInApp";
    }
    BUD_Log(@"%s _全屏视频关闭操作_ %@",__func__,str);
}



@end
