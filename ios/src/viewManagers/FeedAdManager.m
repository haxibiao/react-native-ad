//
//  FeedAdManager.m
//  datizhuanqian
//
//  Created by ivan zhang on 2019/9/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <React/RCTViewManager.h>
#import "FeedAd.h"

@interface FeedAdManager : RCTViewManager

@end


@implementation FeedAdManager

RCT_EXPORT_MODULE(FeedAd)

RCT_EXPORT_VIEW_PROPERTY(onAdLayout, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdError, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdClick, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdClose, RCTBubblingEventBlock)

- (FeedAd *)view
{
  return [[FeedAd alloc] init];
}

RCT_CUSTOM_VIEW_PROPERTY(codeid, NSString, FeedAd)
{
  if (json) {
    [view  setCodeId:json];
  }
}

RCT_CUSTOM_VIEW_PROPERTY(width, NSString, FeedAd)
{
  if (json) {
    [view  setAdWidth:json];
  }
}

@end
