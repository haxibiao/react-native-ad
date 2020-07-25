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

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

- (NSArray<NSString *> *)supportedEvents {
    return @[
        @"SplashAd-onAdClose",
        @"SplashAd-onAdSkip",
        @"SplashAd-onAdError",
        @"SplashAd-onAdClick",
        @"SplashAd-onAdShow"
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

    [[AdBoss getRootVC].view addSubview:splashView];
    splashView.rootViewController = [AdBoss getRootVC];
    
    resolve(@"结果：Splash Ad 成功");
}

//穿山甲开屏广告 回调
- (void)splashAdDidLoad:(BUSplashAdView *)splashAd {
	NSLog(@"SplashAd-onAdShow ...");
    [self sendEventWithName:@"SplashAd-onAdShow" body:@""];
}

- (void)splashAdDidClick:(BUSplashAdView *)splashAd {
	NSLog(@"SplashAd-onAdClick ...");
    [self sendEventWithName:@"SplashAd-onAdClick" body:@"..."];
}

- (void)splashAdDidClickSkip:(BUSplashAdView *)splashAd {
	NSLog(@"SplashAd-onAdSkip ...");
    [self sendEventWithName:@"SplashAd-onAdSkip" body:@""];
}

- (void)splashAdDidClose:(BUSplashAdView *)splashAd {
	NSLog(@"SplashAd-onAdClose ...");
    [splashAd removeFromSuperview];
    [self sendEventWithName:@"SplashAd-onAdClose" body:@""];
}

- (void)splashAd:(BUSplashAdView *)splashAd didFailWithError:(NSError *)error {
	NSLog(@"SplashAd-onAdError ...");
    [splashAd removeFromSuperview];
    [self sendEventWithName:@"SplashAd-onAdError" body:@""];
}

@end


