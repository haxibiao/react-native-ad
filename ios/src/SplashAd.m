//
//  SplashAd.m
//
//  Created by ivan zhang on 2019/9/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "SplashAd.h"

@interface SplashAd () <BUSplashAdDelegate>

@end

@implementation SplashAd

RCT_EXPORT_MODULE();

static RCTEventEmitter* staticEventEmitter = nil;

+ (BOOL)requiresMainQueueSetup {
    return YES;
}


- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

-(id) init {
    self = [super init];
    if (self) {
        staticEventEmitter = self;
    }
    return self;
}

- (void)_sendEventWithName:(NSString *)eventName body:(id)body {
    if (staticEventEmitter == nil)
        return;
    [staticEventEmitter sendEventWithName:eventName body:body];
}

- (NSArray<NSString *> *)supportedEvents {
    return @[
        @"Splash-onAdTimeOver",
        @"Splash-onAdSkip",
        @"Splash-onError",
        @"Splash-onAdClicked",
        @"Splash-onAdShow"
    ];
}


RCT_EXPORT_METHOD(loadSplashAd:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
    
    NSString  *codeid = options[@"codeid"];
    if(codeid == nil) {
        return;
    }
    
    NSString  *appid = options[@"appid"];
    if(appid != nil) {
        [AdBoss init:appid];
    }
    
    NSLog(@"Bytedance splash 开屏ios 代码位id %@", codeid);
    
    //穿山甲开屏广告
    CGRect frame = [UIScreen mainScreen].bounds;
    BUSplashAdView *splashView = [[BUSplashAdView alloc] initWithSlotID:codeid frame:frame]; //答妹ios测试 开屏广告位
    
    splashView.tolerateTimeout = 10;
    splashView.delegate = self; //[AdBoss getApp];
    
    [splashView loadAdData];
    
//    UIViewController *rootVC = [AdBoss getWindow].rootViewController;
    UIViewController *rootVC = (UIViewController * )[UIApplication sharedApplication].delegate.window.rootViewController;
    
    [rootVC.view addSubview:splashView];
    splashView.rootViewController = rootVC;
    
    resolve(@"结果：Splash Ad 成功");
}


//穿山甲开屏广告 回调
- (void)splashAdDidClose:(BUSplashAdView *)splashAd {
  [splashAd removeFromSuperview];
}
- (void)splashAd:(BUSplashAdView *)splashAd didFailWithError:(NSError *)error {
  [splashAd removeFromSuperview];
}

@end


