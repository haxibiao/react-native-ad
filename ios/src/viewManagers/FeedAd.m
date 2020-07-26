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
    self._codeid = codeid;
    NSLog(@"开始 加载Feed广告 codeid: %@", self._codeid);
    [self loadFeedAd];
}

/**
 加载Feed广告
 */
- (void)loadFeedAd{
    
    if(self._codeid == nil) {
        return;
    }
    
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
    if (!self.nativeExpressAdManager) {
        self.nativeExpressAdManager = [[BUNativeExpressAdManager alloc] initWithSlot:slot1 adSize:adSize];
    }
    self.nativeExpressAdManager.adSize = adSize;
    self.nativeExpressAdManager.delegate = self;
    //一次加载一个feed广告数据
    [self.nativeExpressAdManager loadAd:1];
}

#pragma mark - BUNativeExpressAdViewDelegate
- (void)nativeExpressAdSuccessToLoad:(BUNativeExpressAdManager *)nativeExpressAd views:(NSArray<__kindof BUNativeExpressAdView *> *)views {
    BUD_Log(@"feed ad success to load");
    //清空准备显示
    [self.expressAdViews removeAllObjects];
    
    //【重要】不能保存太多view，需要在合适的时机手动释放不用的，否则内存会过大
    if (views.count) {
        [self.expressAdViews addObjectsFromArray:views];
        [views enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            BUNativeExpressAdView *expressView = (BUNativeExpressAdView *)obj;
            dispatch_async(dispatch_get_main_queue(), ^{
                [expressView render];
            });
        }];
    }
    BUD_Log(@"feed ad 个性化模板拉取广告成功回调");
}

- (void)nativeExpressAdFailToLoad:(BUNativeExpressAdManager *)nativeExpressAd error:(NSError *)error {
    BUD_Log(@"feed ad faild to load");
      self.onAdError(@{
        @"message":[error localizedDescription],
      });
}

- (void)nativeExpressAdViewRenderSuccess:(BUNativeExpressAdView *)nativeExpressAdView {
    BUD_Log(@"feed ad render success");
    [self addSubview:nativeExpressAdView];
}

- (void)nativeExpressAdViewRenderFail:(BUNativeExpressAdView *)nativeExpressAdView error:(NSError *)error {
    BUD_Log(@"feed ad render fail %@", error);
      self.onAdError(@{
        @"message":[error localizedDescription],
      });
}

- (void)nativeExpressAdViewWillShow:(BUNativeExpressAdView *)nativeExpressAdView {
    CGFloat adWidth = nativeExpressAdView.bounds.size.width;
    BUD_Log(@"adWidth %f", adWidth);
    CGFloat adHeight = nativeExpressAdView.bounds.size.height;
    BUD_Log(@"adHeight %f", adHeight);
    self.onAdLayout(@{
        @"width":@(adWidth),
        @"height":@(adHeight)
    });
}

- (void)nativeExpressAdViewDidClick:(BUNativeExpressAdView *)nativeExpressAdView {
    //FIXME: 模拟器里点击详情按钮没反应
    BUD_Log(@"feed ad clicked");
    self.onAdClick(@{
        @"message": @"ad been clicked",
      });
}

- (void)nativeExpressAdView:(BUNativeExpressAdView *)nativeExpressAdView dislikeWithReason:(NSArray<BUDislikeWords *> *)filterWords {
    //【重要】需要在点击叉以后 在这个回调中移除视图，否则，会出现用户点击叉无效的情况
    [self.expressAdViews removeObject:nativeExpressAdView];
    [self willRemoveSubview:nativeExpressAdView];
//    self.onAdDislike(@{
//        @"message":filterWords[0].name,
//    });
}

- (void)nativeExpressAdViewDidClosed:(BUNativeExpressAdView *)nativeExpressAdView {
    //FIXME: 模拟器里点击关闭按钮没反应
    self.onAdClose(@{
        @"message": @"ad closed",
    });
}

- (void)nativeExpressAdViewWillPresentScreen:(BUNativeExpressAdView *)nativeExpressAdView {
    
}

@end
