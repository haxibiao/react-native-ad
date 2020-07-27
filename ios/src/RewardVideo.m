//
//  RewardVideo.m
//
//  Created by ivan zhang on 2019/5/6.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "RewardVideo.h"
#import "AdBoss.h"

@implementation RewardVideo 

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
        @"RewardVideo-onAdError",
        @"RewardVideo-onAdLoaded",
        @"RewardVideo-onAdVideoCached",
        @"RewardVideo-onAdClick",
        @"RewardVideo-onAdClose",
        @"RewardVideo-onVideoComplete",
        @"RewardVideo-onDownloadActive"
    ];
}

//默认监听
-(void) startObserving {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(emitEventInternal:) name:@"emit-event" object:nil];
}

-(void) stopObserving {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

//最终发送事件到rn
-(void) emitEventInternal:(NSNotification *)notification{
    if([@"onAdLoaded" compare:[notification.userInfo objectForKey:@"type"]] == NSOrderedSame ){
        [self sendEventWithName:@"RewardVideo-onAdLoaded" body:@{@"message":[notification.userInfo objectForKey:@"message"]}];   
    }else if( [@"onAdError" compare:[notification.userInfo objectForKey:@"type"]] == NSOrderedSame ){
        [self sendEventWithName:@"RewardVideo-onAdError" body:@{@"message":[notification.userInfo objectForKey:@"message"]}];   
    }else if( [@"onAdClose" compare: [notification.userInfo objectForKey:@"type"]] == NSOrderedSame ){
        [self sendEventWithName:@"RewardVideo-onAdClose" body:@{@"message":[notification.userInfo objectForKey:@"message"]}];   
    }else if( [@"onAdClick" compare: [notification.userInfo objectForKey:@"type"]] == NSOrderedSame ){
        [self sendEventWithName:@"RewardVideo-onAdClick" body:@{@"message":[notification.userInfo objectForKey:@"message"]}];   
    }
}

//发送事件
+(void) emitEvent: (NSDictionary *)payload
{
    // {type: @"", message: @""}
    [[NSNotificationCenter defaultCenter] postNotificationName:@"emit-event" object:nil userInfo:payload];
}


RCT_EXPORT_METHOD(loadAd:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
    NSString *codeid = options[@"codeid"];
    if(codeid == nil) {
        return;
    }
    
    [AdBoss loadRewardAd:codeid userid:options[@"uid"]];
    resolve(@"OK");
}

RCT_EXPORT_METHOD(startAd:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
    NSString *codeid = options[@"codeid"];
    if(codeid == nil) {
        return;
    }
    
    NSString *appid = options[@"appid"];
    if(appid != nil) {
        [AdBoss init:appid];
    }
    
    [AdBoss loadRewardAd:codeid userid:options[@"uid"]];
    
    RewardVideoViewController *vc = [RewardVideoViewController new];
    vc.view.backgroundColor = [UIColor whiteColor];
    
    [[AdBoss getRootVC] presentViewController:vc animated:true completion:^{
        [AdBoss saveResolve:resolve];
        [AdBoss saveReject:reject];
    }];
    
}

@end

