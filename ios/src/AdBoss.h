//
//  AdBoss.h
//
//  Created by ivan zhang on 2019/9/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <BUAdSDK/BUNativeExpressRewardedVideoAd.h>
#import <BUAdSDK/BUNativeExpressFullscreenVideoAd.h>

#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>


#define BUD_Log(frmt, ...)   \
do {                                                      \
NSLog(@"【BUAd】%@", [NSString stringWithFormat:frmt,##__VA_ARGS__]);  \
} while(0)


@interface AdBoss: NSObject

+(void) init: (NSString*) appid;

+(UIViewController*) getRootVC;

+(void) loadRewardAd: (NSString*) codeid userid:(NSString *)uid;
+(BUNativeExpressRewardedVideoAd *) getRewardAd;

+(void) loadFullScreenAd: (NSString*) codeid;
+(BUNativeExpressFullscreenVideoAd *) getFullScreenAd;

//统计激励视频是否点击查看
+(void) clickRewardVideo;
+(void) resetClickRewardVideo;
+(int) getRewardVideoClicks;


//保存js回调
+(void) saveResolve:(RCTPromiseResolveBlock) resolve;
+(RCTPromiseResolveBlock) getResolve;
+(void) saveReject:(RCTPromiseRejectBlock) reject;
+(RCTPromiseRejectBlock) getReject;

@end
