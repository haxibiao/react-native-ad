//
//  FullScreenVideo.m
//
//  Created by ivan zhang on 2019/9/25.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "FullScreenVideo.h"


@implementation FullScreenVideo

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
        @"FullScreenVideo-onAdLoaded",
        @"FullScreenVideo-onAdError",
        @"FullScreenVideo-onAdClose",
        @"FullScreenVideo-onAdClick"
    ];
}

-(void) startObserving {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(emitEventInternal:) name:@"emit-event" object:nil];
}

-(void) stopObserving {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

-(void) emitEventInternal:(NSNotification *)notification{
    if([@"onAdLoaded" compare:[notification.userInfo objectForKey:@"type"]] == NSOrderedSame ){
        [self sendEventWithName:@"FullScreenVideo-onAdLoaded" body:@{@"message":[notification.userInfo objectForKey:@"message"]}];   
    }else if( [@"onAdError" compare:[notification.userInfo objectForKey:@"type"]] == NSOrderedSame ){
        [self sendEventWithName:@"FullScreenVideo-onAdError" body:@{@"message":[notification.userInfo objectForKey:@"message"]}];   
    }else if( [@"onAdClose" compare: [notification.userInfo objectForKey:@"type"]] == NSOrderedSame ){
        [self sendEventWithName:@"FullScreenVideo-onAdClose" body:@{@"message":[notification.userInfo objectForKey:@"message"]}];   
    }else if( [@"onAdClick" compare: [notification.userInfo objectForKey:@"type"]] == NSOrderedSame ){
        [self sendEventWithName:@"FullScreenVideo-onAdClick" body:@{@"message":[notification.userInfo objectForKey:@"message"]}];   
    }
}

+(void) emitEvent: (NSDictionary *)payload
{
    // {type: @"", message: @""}
    [[NSNotificationCenter defaultCenter] postNotificationName:@"emit-event" object:nil userInfo:payload];
}

RCT_EXPORT_METHOD(loadAd:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
    NSString *codeid = options[@"codeid"];
    NSLog(@"full screen load codeid %@",codeid);
    if(codeid == nil) {
        return;
    }
    
    [AdBoss initFullScreenAd:codeid];
    resolve(@"OK");
}

RCT_EXPORT_METHOD(startAd:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
    NSString *codeid = options[@"codeid"];
    if(codeid == nil) {
        return;
    }
    
    if([AdBoss getFullScreenAd].adValid) {
        NSLog(@"有提前初始化的 full screen ad from adManager");
    }
    
    //加载新的广告位数据
    NSLog(@"full screen start codeid %@",codeid);
    [AdBoss initFullScreenAd:codeid];
    
    FullscreenViewController *vc = [FullscreenViewController new];
    vc.slotID = codeid;
    vc.view.backgroundColor = [UIColor whiteColor];
    
    [[AdBoss getRootVC] presentViewController:vc animated:true completion:^{
        [AdBoss saveResolve:resolve];
        [AdBoss saveReject:reject];
    }];
}

@end


