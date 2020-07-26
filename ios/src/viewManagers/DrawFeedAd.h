//
//  DrawFeedAd.h
//  datizhuanqian
//
//  Created by ivan zhang on 2019/9/18.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>

#import <BUAdSDK/BUNativeAd.h>
#import <BUAdSDK/BUNativeAdRelatedView.h>


@interface DrawFeedAd : UIView

@property (nonatomic, copy) RCTBubblingEventBlock _Nullable onAdError;
@property (nonatomic, copy) RCTBubblingEventBlock _Nullable onAdClicked;

-(void) setCodeId:(NSString * _Nullable) codeid;

@property (nonatomic, strong, nullable) UILabel *titleLabel;
@property (nonatomic, strong, nullable) UILabel *descriptionLabel;
@property (nonatomic, strong, nullable) UIImageView *headImg;

@property (nonatomic, assign) NSInteger videoId;
//- (void)refreshUIAtIndex:(NSUInteger)index;
//- (void)autoPlay;
//- (void)pause;

//- (void)refreshUIWithModel:(BUNativeAd *_Nonnull)model;

@property (nonatomic, strong) UIButton * _Nullable creativeButton;
@property (nonatomic, strong) BUNativeAdRelatedView * _Nullable nativeAdRelatedView;


@end
