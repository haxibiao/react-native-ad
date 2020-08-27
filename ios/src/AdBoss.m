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

static NSString *_appid = nil;

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

+(void)init:(NSString*) appid {
    _appid = appid;
    
#if DEBUG
    //Whether to open log. default is none.
    [BUAdSDKManager setLoglevel:BUAdSDKLogLevelDebug];
#endif
    [BUAdSDKManager setAppID:_appid];
    [BUAdSDKManager setIsPaidApp:NO];
    
}

+ (UIViewController *) getRootVC {
    return (UIViewController * )[UIApplication sharedApplication].delegate.window.rootViewController;
}


+ (void) loadRewardAd:(NSString *)codeid userid:(NSString *)uid extra:(NSString *)extra{
    //    # 避免重复请求数据，每次加载会返回新的广告数据的
    BURewardedVideoModel *model = [[BURewardedVideoModel alloc] init];
    model.userId = uid;
    model.extra = extra;
    NSLog(@"Enda %@ %@",uid,extra);
    rewardAd = [[BUNativeExpressRewardedVideoAd alloc] initWithSlotID:codeid rewardedVideoModel:model];
    [rewardAd loadAdData];
}

+ (BUNativeExpressRewardedVideoAd *)getRewardAd {
    return rewardAd;
}

+ (void)loadFullScreenAd:(NSString *)codeid {
    //  # 避免重复请求数据，每次加载会返回新的广告数据的
    fullScreenAd = [[BUNativeExpressFullscreenVideoAd alloc] initWithSlotID:codeid];
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
