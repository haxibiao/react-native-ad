//
//  DrawFeedAd.m
//  datizhuanqian
//
//  Created by ivan zhang on 2019/9/18.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <React/RCTViewManager.h>
#import <React/RCTViewManager.h>

@interface DrawFeedAdManager : RCTViewManager

@end

#import "DrawFeedAd.h"


@implementation DrawFeedAdManager

RCT_EXPORT_MODULE(DrawFeedAd)
RCT_EXPORT_VIEW_PROPERTY(onAdError, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdClicked, RCTBubblingEventBlock)


- (DrawFeedAd *)view
{
  return [[DrawFeedAd alloc] init];
}

RCT_CUSTOM_VIEW_PROPERTY(appid, NSString, DrawFeedAd)
{
  if (json) {
    [view  setAppId:json]; 
  }
}


RCT_CUSTOM_VIEW_PROPERTY(codeid, NSString, DrawFeedAd)
{
  if (json) {
    [view  setCodeId:json];
  }
}

@end
