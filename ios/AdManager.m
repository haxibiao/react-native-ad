//
//  AdManager.m
//
//  Created by ivan zhang on 2019/12/3.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>

#import <BUAdSDK/BUAdSDKManager.h>
#import <BUAdSDK/BURewardedVideoModel.h>
#import "BUAdSDK/BUSplashAdView.h"
#include "AdBoss.h"


@interface AdManager : RCTEventEmitter <RCTBridgeModule>
{
  
}
@end

@implementation AdManager

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
  return @[];
}

RCT_EXPORT_METHOD(init:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  
  NSString  *appid = options[@"appid"];
  if(appid == nil) {
    reject(@"", @"appid未提供", nil);
    return;
  }
  
  //穿山甲 init appid
  [AdBoss init: appid]; //RN可以修改appid
  
  
  NSLog(@"AdManager init appid %@",appid);

   resolve(@"OK");
}


RCT_EXPORT_METHOD(loadFeedAd:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  
  NSString  *codeid = options[@"codeid"];
  if(codeid == nil) {
    reject(@"", @"codeid未提供", nil);
    return;
  }
  
   resolve(@"TODO: 还未实现是否需要给ios提前预加载 Feed Ad，这里同步的安卓的RN方法 ...");
}

@end



