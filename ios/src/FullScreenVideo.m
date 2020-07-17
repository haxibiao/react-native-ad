//
//  FullScreenVideo.m
//
//  Created by ivan zhang on 2019/9/25.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>
#import "FullscreenViewController.h"
#import "BUDSlotViewModel.h"
#import "AdBoss.h"
#import <BUAdSDK/BUAdSDKManager.h>
#import <BUAdSDK/BURewardedVideoModel.h>


@interface FullScreenVideo : RCTEventEmitter <RCTBridgeModule>
{
  
}
@end

@implementation FullScreenVideo

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
           @"FullScreenVideo-adloaded",
           @"FullScreenVideo-videocached",
           @"FullScreenVideo-videoplayed",
           @"FullScreenVideo-adclick"
           ];
}


RCT_EXPORT_METHOD(loadAd:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  NSString *codeid = options[@"codeid"];
  NSLog(@"full screen load code id %@",codeid);
  if(codeid == nil) {
    return;
  }
  
  [AdBoss loadFullScreenAd:codeid];

  resolve(@"OK");
}

RCT_EXPORT_METHOD(startAd:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
  NSString *codeid = options[@"codeid"];
  if(codeid == nil) {
    return;
  }
  
  NSLog(@"full screen code id %@",codeid);
  [AdBoss loadFullScreenAd:codeid];
  
   FullscreenViewController *vc = [FullscreenViewController new];
   vc.slotID = codeid;
   vc.view.backgroundColor = [UIColor whiteColor];
   
  [[AdBoss getWindow].rootViewController presentViewController:vc animated:true completion:^{
       [AdBoss saveResolve:resolve];
       [AdBoss saveReject:reject];
  }];
}

@end


