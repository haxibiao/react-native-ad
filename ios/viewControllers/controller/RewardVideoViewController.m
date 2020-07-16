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

@interface RewardVideoViewController () <BUNativeExpressRewardedVideoAdDelegate>
@property (nonatomic, strong) BUNativeExpressRewardedVideoAd *rewardedVideoAd;
@end

@implementation RewardVideoViewController

- (void)viewDidLoad {

    self.rewardedVideoAd = [AdBoss getRewardAd];
  
  //rewardedVideoAd 无缓存？
  self.rewardedVideoAd.delegate = self;
  [self.rewardedVideoAd loadAdData];
  
//  [self.rewardedVideoAd showAdFromRootViewController:self];
//  self.navigationController]
}

#pragma mark BUNativeExpressRewardedVideoAdDelegate

- (void)nativeExpressRewardedVideoAdDidLoad:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
  NSLog(@"rewardedVideoAd data load success");
  [rewardedVideoAd showAdFromRootViewController:self];
}

- (void)nativeExpressRewardedVideoAd:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd didFailWithError:(NSError *_Nullable)error {
  NSLog(@"rewardedVideoAd video load success");
  
  UIViewController *rootVC = [AdBoss getWindow].rootViewController;
  [rewardedVideoAd showAdFromRootViewController:rootVC];
}


- (void)nativeExpressRewardedVideoAdCallback:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd withType:(BUNativeExpressRewardedVideoAdType)nativeExpressVideoType{
    BUD_Log(@"%s",__func__);
}

- (void)nativeExpressRewardedVideoAdDidDownLoadVideo:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    //TODO: 激励视频下载完成了
    BUD_Log(@"%s 激励视频下载完成了",__func__);
}

- (void)nativeExpressRewardedVideoAdViewRenderSuccess:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s 视频视图渲染成功",__func__);
  [self.rewardedVideoAd showAdFromRootViewController:self];
}

- (void)nativeExpressRewardedVideoAdViewRenderFail:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd error:(NSError *_Nullable)error {
    BUD_Log(@"%s  视频视图渲染出错了",__func__);
    //TODO: 视频视图渲染出错了
}

- (void)nativeExpressRewardedVideoAdWillVisible:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s",__func__);
}

- (void)nativeExpressRewardedVideoAdDidVisible:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s",__func__);
}

- (void)nativeExpressRewardedVideoAdWillClose:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s",__func__);
}

- (void)nativeExpressRewardedVideoAdDidClose:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s",__func__);
    
  //完成关闭
  UIViewController *rootVC = [AdBoss getWindow].rootViewController;
  [rootVC dismissViewControllerAnimated:true completion:^{
    //通知boss给RN回调...
        if([AdBoss getRewardVideoClicks] > 0)
        {
          [AdBoss resetClickRewardVideo];
          [AdBoss getResolve](@{
            @"video_play":@1,
            @"ad_click":@1,
            @"verify_status":@0
          });
        } else {
          [AdBoss getResolve](@{
            @"video_play":@1,
            @"ad_click":@0,
            @"verify_status":@0
          });
        }
  }];
  
}

- (void)nativeExpressRewardedVideoAdDidClick:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s",__func__);
  //点了激励视频
  [AdBoss clickRewardVideo];
}

- (void)nativeExpressRewardedVideoAdDidClickSkip:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s",__func__);
  //TODO: 点了跳过激励视频
  //  [AdBoss clickRewardVideo];
}

- (void)nativeExpressRewardedVideoAdDidPlayFinish:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd didFailWithError:(NSError *_Nullable)error {
    BUD_Log(@"%s",__func__);
}

- (void)nativeExpressRewardedVideoAdServerRewardDidSucceed:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd verify:(BOOL)verify {
    BUD_Log(@"%s",__func__);
}

- (void)nativeExpressRewardedVideoAdServerRewardDidFail:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    BUD_Log(@"%s",__func__);
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
    BUD_Log(@"%s __ %@",__func__,str);
}

-(BOOL)shouldAutorotate{
  return YES;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
  return UIInterfaceOrientationMaskAll;
}
@end
