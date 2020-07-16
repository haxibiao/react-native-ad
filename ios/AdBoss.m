//
//  AdBoss.m
//
//  Created by ivan zhang on 2019/9/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "AdBoss.h"

//穿山甲广告SDK
#import <BUAdSDK/BUAdSDKManager.h>
#import <BUAdSDK/BUNativeExpressRewardedVideoAd.h>
#import <BUAdSDK/BUNativeExpressFullscreenVideoAd.h>
#import <BUAdSDK/BURewardedVideoModel.h>

#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>


@implementation AdBoss

static NSString *_appid = @"";
static UIWindow *_window = nil;
static UIResponder *_app = nil;
static BUNativeExpressRewardedVideoAd *rewardAd = nil;
static BUNativeExpressFullscreenVideoAd *fullScreenAd = nil;
static int rewardClicks = 0;

//保存js回调
static RCTPromiseResolveBlock adResolve;
static RCTPromiseRejectBlock adReject;

+ (void)saveResolve:(RCTPromiseResolveBlock)resolve {
  adResolve = resolve;
}

+ (RCTPromiseResolveBlock)getResolve{
  return adResolve;
}

+ (void)saveReject:(RCTPromiseRejectBlock)reject {
  adReject = reject;
}

+ (RCTPromiseRejectBlock)getReject {
  return adReject;
}

+(void) init:(NSString*) appid {
  _appid = appid;
  
#if DEBUG
  //Whether to open log. default is none.
  [BUAdSDKManager setLoglevel:BUAdSDKLogLevelDebug];
#endif
  [BUAdSDKManager setAppID:_appid];
  [BUAdSDKManager setIsPaidApp:NO];

}

+ (void)hookWindow:(UIWindow *)window {
  _window = window;
}

+ (UIWindow *)getWindow {
  return _window;
}

+ (void)hookApp:(UIResponder *)app {
  _app = app;
}

+ (UIResponder *)getApp {
  return _app;
}

+ (void) loadRewardAd:(NSString *)codeid userid:(NSString *)uid{
  #warning Every time the data is requested, a new one BURewardedVideoAd needs to be initialized. Duplicate request data by the same full screen video ad is not allowed.
    BURewardedVideoModel *model = [[BURewardedVideoModel alloc] init];
    model.userId = uid;
    rewardAd = [[BUNativeExpressRewardedVideoAd alloc] initWithSlotID:codeid rewardedVideoModel:model];
//  rewardedVideoAd.delegate = self;
    [rewardAd loadAdData];
}

+ (BUNativeExpressRewardedVideoAd *)getRewardAd {
  return rewardAd;
}

+ (void)loadFullScreenAd:(NSString *)codeid {
  #warning----- Every time the data is requested, a new one BUFullscreenVideoAd needs to be initialized. Duplicate request data by the same full screen video ad is not allowed.
  fullScreenAd = [[BUNativeExpressFullscreenVideoAd alloc] initWithSlotID:codeid];
//  fullScreenAd.delegate = self;
  [fullScreenAd loadAdData];
}

+ (BUNativeExpressFullscreenVideoAd *)getFullScreenAd{
  return fullScreenAd;
}

//统计激励视频是否点击查看
+ (void)clickRewardVideo {
  rewardClicks = rewardClicks + 1;
}

+ (void)resetClickRewardVideo {
  rewardClicks = 0;
}

+ (int)getRewardVideoClicks {
  return rewardClicks;
}

@end
