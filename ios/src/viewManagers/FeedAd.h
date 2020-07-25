//
//  FeedAd.h
//  datizhuanqian
//
//  Created by ivan zhang on 2019/9/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>

@interface FeedAd : UIView
-(void) setAppId:(NSString *) appid;
-(void) setCodeId:(NSString *) codeid;
-(void) loadFeedAd; 
@property (nonatomic, copy) RCTBubblingEventBlock onAdLayout;
@property (nonatomic, copy) RCTBubblingEventBlock onAdError;
@property (nonatomic, copy) RCTBubblingEventBlock onAdClick;
@property (nonatomic, copy) RCTBubblingEventBlock onAdClose;
@end
