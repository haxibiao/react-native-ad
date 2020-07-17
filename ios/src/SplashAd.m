//
//  SplashAd.m
//
//  Created by ivan zhang on 2019/9/19.
//  Copyright © 2019 Facebook. All rights reserved.
//


#import <Foundation/Foundation.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>

#import <BUAdSDK/BUAdSDKManager.h>
#import <BUAdSDK/BURewardedVideoModel.h>
#import "BUAdSDK/BUSplashAdView.h"
#include "AdBoss.h"


@interface Splash : RCTEventEmitter <RCTBridgeModule>
{
  
}
@end

@implementation Splash

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
           @"splash-skipped",
           @"splash-clicked"
           ];
}


RCT_EXPORT_METHOD(loadSplashAd:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  
  NSString  *codeid = options[@"codeid"];
  if(codeid == nil) {
    return;
  }
  
  NSLog(@"Bytedance splash 开屏ios 代码位id %@", codeid);
  
  //穿山甲开屏广告
    CGRect frame = [UIScreen mainScreen].bounds;
    BUSplashAdView *splashView = [[BUSplashAdView alloc] initWithSlotID:codeid frame:frame]; //答妹ios测试 开屏广告位
    
    splashView.tolerateTimeout = 10;
    splashView.delegate = [AdBoss getApp];
    
    [splashView loadAdData];
  
    UIViewController *rootViewContrller = [AdBoss getWindow].rootViewController;
    [rootViewContrller.view addSubview:splashView];
    splashView.rootViewController = rootViewContrller;
  
   resolve(@"结果：Splash Ad 成功");
}

@end


