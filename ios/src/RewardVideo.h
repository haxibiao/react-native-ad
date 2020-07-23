//
//  RewardVideo.h
//  ReactNativeAd
//
//  Created by ivan zhang on 2020/7/22.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>

#import "RewardVideoViewController.h"
#import "BUDSlotViewModel.h"
#import "AdBoss.h"
#import <BUAdSDK/BUAdSDKManager.h>
#import <BUAdSDK/BURewardedVideoModel.h>

@interface RewardVideo : RCTEventEmitter <RCTBridgeModule>
{
}
@end
