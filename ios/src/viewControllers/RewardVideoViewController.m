//
//  RewardVideoViewController.m
//
//  Created by ivan zhang on 2019/5/6.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "RewardVideoViewController.h"
#import <BUAdSDK/BUNativeExpressRewardedVideoAd.h>
#import <BUAdSDK/BURewardedVideoModel.h>
#import "AdBoss.h"
#import "RewardVideo.h"
#import "LoadRewardSingle.h"

#define BUD_RGB(a,b,c) [UIColor colorWithRed:(a/255.0) green:(b/255.0) blue:(c/255.0) alpha:1]
#define GlobleHeight [UIScreen mainScreen].bounds.size.height
#define GlobleWidth [UIScreen mainScreen].bounds.size.width
#define inconWidth 45
#define inconEdge 15
#define bu_textEnde 5
#define bu_textColor BUD_RGB(0xf0, 0xf0, 0xf0)
#define bu_textFont 14

@interface RewardVideoViewController () <BUNativeExpressRewardedVideoAdDelegate>

@property (nonatomic, strong, nullable) UILabel *titleLabel;

@end



@implementation RewardVideoViewController

BUNativeExpressRewardedVideoAd *ad = nil;

- (void)viewDidLoad {

    LoadRewardSingle *rewardSingle = [LoadRewardSingle sharedInstance];
    NSLog(@"Enda viewDidLoad CodeId %@",rewardSingle.codeId );
    [rewardSingle.rewardAdMap enumerateKeysAndObjectsUsingBlock:^(id  _Nonnull key, id  _Nonnull obj, BOOL * _Nonnull stop) {
        NSLog(@"Enda viewDidLoad for key:%@",key);
    }];
    
    // 根据代码位保存预加载视频
    ad = [rewardSingle.rewardAdMap objectForKey:rewardSingle.codeId];
    
    NSLog(@"Enda viewDidLoad %@",ad);
    ad.delegate = self;
    [ad loadAdData];
}


#pragma mark BUNativeExpressRewardedVideoAdDelegate

- (void)nativeExpressRewardedVideoAdDidLoad:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"rewardedVideoAd 激励视频 %s",__func__);
    [RewardVideo emitEvent: @{@"type": @"onAdLoaded", @"message": @"success"}];
    //express 只有渲染成功时才可以show ? 0.63 又必须这个时候show了
    [ad showAdFromRootViewController:self];
}

- (void)nativeExpressRewardedVideoAdViewRenderSuccess:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s rewardVideoAd 激励视频 渲染成功",__func__);
    [ad showAdFromRootViewController:self];
}

- (void)nativeExpressRewardedVideoAd:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd didFailWithError:(NSError *_Nullable)error {
    BUD_Log(@"rewardVideoAd 激励视频 didFailWithError: %@", error);
    [RewardVideo emitEvent: @{@"type": @"onAdError", @"message": @""}];
}


- (void)nativeExpressRewardedVideoAdCallback:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd withType:(BUNativeExpressRewardedVideoAdType)nativeExpressVideoType{
    BUD_Log(@"%s 激励视频 %lu",__func__, nativeExpressVideoType);
}

- (void)nativeExpressRewardedVideoAdDidDownLoadVideo:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s rewardVideoAd 激励视频 下载完成了",__func__);
}

- (void)nativeExpressRewardedVideoAdViewRenderFail:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd error:(NSError *_Nullable)error {
    BUD_Log(@"%s rewardVideoAd 激励视频 渲染出错了",__func__);
    //TODO: 视频视图渲染出错了
}

- (void)nativeExpressRewardedVideoAdWillVisible:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s 激励视频",__func__);
}

- (void)nativeExpressRewardedVideoAdDidVisible:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s 激励视频",__func__);
}

- (void)nativeExpressRewardedVideoAdWillClose:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s 激励视频",__func__);
}

- (void)nativeExpressRewardedVideoAdDidClose:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s 激励视频",__func__);
    [RewardVideo emitEvent: @{@"type": @"onAdClose", @"message": @""}];
    
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

- (void)nativeExpressRewardedVideoAdDidClick:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s 激励视频",__func__);
    //点了激励视频
    [AdBoss clickRewardVideo];
    [RewardVideo emitEvent: @{@"type": @"onAdClick", @"message": @""}];
}

- (void)nativeExpressRewardedVideoAdDidClickSkip:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s 激励视频",__func__);
    //TODO: 点了跳过激励视频
}

- (void)nativeExpressRewardedVideoAdDidPlayFinish:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd didFailWithError:(NSError *_Nullable)error {
    BUD_Log(@"%s 激励视频",__func__);
}

- (void)nativeExpressRewardedVideoAdServerRewardDidSucceed:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd verify:(BOOL)verify {
    BUD_Log(@"%s 激励视频",__func__);
}

- (void)nativeExpressRewardedVideoAdServerRewardDidFail:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s 激励视频",__func__);
}

- (void)nativeExpressRewardedVideoAdDidCloseOtherController:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd interactionType:(BUInteractionType)interactionType {
    NSString *str = nil;
    if (interactionType == BUInteractionTypePage) {
        str = @"ladingpage";
    } else if (interactionType == BUInteractionTypeVideoAdDetail) {
        str = @"videoDetail";
    } else {
        str = @"appstoreInApp";
    }
    BUD_Log(@"%s _激励视频其他关闭操作_ %@",__func__,str);
}

-(BOOL)shouldAutorotate{
    return YES;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return UIInterfaceOrientationMaskAll;
}
@end
