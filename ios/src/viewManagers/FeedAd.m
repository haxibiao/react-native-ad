//
//  FeedAd.m
//  datizhuanqian
//
//  Created by ivan zhang on 2019/9/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "FeedAd.h"
#include "AdBoss.h"
#import <BUAdSDK/BUNativeExpressAdManager.h>
#import <BUAdSDK/BUNativeExpressAdView.h>

#import <BUAdSDK/BUAdSDK.h>

@interface FeedAd ()<BUNativeExpressAdViewDelegate>

@property (strong, nonatomic) NSMutableArray<__kindof BUNativeExpressAdView *> *expressAdViews;
@property (strong, nonatomic) BUNativeExpressAdManager *nativeExpressAdManager;

@property(nonatomic, strong) NSString *_codeid;
@end

@implementation FeedAd

- (void)setCodeId:(NSString *)codeid {
  self._codeid = !codeid.length ? @"916582757" : codeid;
  [self loadFeedAd];
}

- (void)loadFeedAd{
  
  if (!self.expressAdViews) {
    self.expressAdViews = [NSMutableArray arrayWithCapacity:20];
  }
  BUAdSlot *slot1 = [[BUAdSlot alloc] init];
  slot1.ID = self._codeid;
  slot1.AdType = BUAdSlotAdTypeFeed;
  BUSize *imgSize = [BUSize sizeBy:BUProposalSize_Feed228_150];
  slot1.imgSize = imgSize;
  slot1.position = BUAdSlotPositionFeed;
  slot1.isSupportDeepLink = YES;
  
  CGSize adSize = CGSizeMake(228, 150);
  
  // self.nativeExpressAdManager可以重用
  if (!self.nativeExpressAdManager) {
    self.nativeExpressAdManager = [[BUNativeExpressAdManager alloc] initWithSlot:slot1 adSize:adSize];
  }
  self.nativeExpressAdManager.adSize = adSize;
  self.nativeExpressAdManager.delegate = self;
  [self.nativeExpressAdManager loadAd:1];
  
}

#pragma mark - BUNativeExpressAdViewDelegate
- (void)nativeExpressAdSuccessToLoad:(BUNativeExpressAdManager *)nativeExpressAd views:(NSArray<__kindof BUNativeExpressAdView *> *)views {
  NSLog(@"feed ad success to load");
  [self.expressAdViews removeAllObjects];//【重要】不能保存太多view，需要在合适的时机手动释放不用的，否则内存会过大
  if (views.count) {
    [self.expressAdViews addObjectsFromArray:views];
    [views enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
      BUNativeExpressAdView *expressView = (BUNativeExpressAdView *)obj;
      
      dispatch_async(dispatch_get_main_queue(), ^{
        [expressView render];
      });
                     
                     
    }];
  }
  NSLog(@"【BytedanceUnion】feed ad 个性化模板拉取广告成功回调");
}

- (void)nativeExpressAdFailToLoad:(BUNativeExpressAdManager *)nativeExpressAd error:(NSError *)error {
  NSLog(@"feed ad faild to load");
//  self.onAdError(@{
//    @"message":[error localizedDescription],
//  });
}

- (void)nativeExpressAdViewRenderSuccess:(BUNativeExpressAdView *)nativeExpressAdView {
  NSLog(@"feed ad render success");
  [self addSubview:nativeExpressAdView];
}

- (void)nativeExpressAdViewRenderFail:(BUNativeExpressAdView *)nativeExpressAdView error:(NSError *)error {
  NSLog(@"feed ad render fail %@", error);
//  self.onAdError(@{
//    @"message":[error localizedDescription],
//  });
}

- (void)nativeExpressAdViewWillShow:(BUNativeExpressAdView *)nativeExpressAdView {
  CGFloat adWidth = nativeExpressAdView.bounds.size.width;
  NSLog(@"adWidth %f", adWidth);
  CGFloat adHeight = nativeExpressAdView.bounds.size.height;
  NSLog(@"adHeight %f", adHeight);
  self.onLayoutChanged(@{
    @"width":@(adWidth),
    @"height":@(adHeight)
  });
}

- (void)nativeExpressAdViewDidClick:(BUNativeExpressAdView *)nativeExpressAdView {
//  self.onAdClicked(@{
//    @"message":@"ad been clicked",
//  });
}

- (void)nativeExpressAdView:(BUNativeExpressAdView *)nativeExpressAdView dislikeWithReason:(NSArray<BUDislikeWords *> *)filterWords {//【重要】需要在点击叉以后 在这个回调中移除视图，否则，会出现用户点击叉无效的情况
  [self.expressAdViews removeObject:nativeExpressAdView];
  [self willRemoveSubview:nativeExpressAdView];
  self.onAdClosed(@{
    @"message":filterWords[0].name,
  });
}

- (void)nativeExpressAdViewDidClosed:(BUNativeExpressAdView *)nativeExpressAdView {
  self.onAdClosed(@{
    @"message": @"ad closed",
  });
}

- (void)nativeExpressAdViewWillPresentScreen:(BUNativeExpressAdView *)nativeExpressAdView {
  
}

@end
