//
//  DrawFeedAd.m
//  datizhuanqian
//
//  Created by ivan zhang on 2019/9/18.
//  Copyright © 2019 Facebook. All rights reserved.
//


#import "DrawFeedAd.h"
#import <BUAdSDK/BUNativeExpressAdManager.h>
#import "BUDPlayerController.h"
#import "BUDMacros.h"
#import "NSString+LocalizedString.h"
#include "AdBoss.h"


#define BUD_RGB(a,b,c) [UIColor colorWithRed:(a/255.0) green:(b/255.0) blue:(c/255.0) alpha:1]
#define GlobleHeight [UIScreen mainScreen].bounds.size.height
#define GlobleWidth [UIScreen mainScreen].bounds.size.width
#define inconWidth 45
#define inconEdge 15
#define bu_textEnde 5
#define bu_textColor BUD_RGB(0xf0, 0xf0, 0xf0)
#define bu_textFont 14


@interface DrawFeedAd() <BUNativeExpressAdViewDelegate>
@property(nonatomic, strong) NSString *_codeid;
@property (nonatomic, strong, nullable) UIImageView *likeImg;
@property (nonatomic, strong, nullable) UILabel *likeLable;
@property (nonatomic, strong, nullable) UIImageView *commentImg;
@property (nonatomic, strong, nullable) UILabel *commentLable;
@property (nonatomic, strong, nullable) UIImageView *forwardImg;
@property (nonatomic, strong, nullable) UILabel *forwardLable;

@property (nonatomic, strong) BUNativeExpressAdManager *adManager;
@property (nonatomic, copy) NSArray *dataSource;


@property (nonatomic, strong)BUDPlayerController *player;
@end


@implementation DrawFeedAd


- (void)setCodeId:(NSString *)codeid {
  BUD_Log(@"DrawVideo set codeid %@", codeid);
  if(!codeid.length) {
    BUD_Log(@"DrawVideo 无效 codeid %@", codeid);
    return;
  }
  self._codeid = codeid;
  [self buildupVideoView];
  [self buildupView];
  [self loadExpressAds];
}


- (void)buildupView {
    self.titleLabel = [UILabel new];
    self.titleLabel.frame = CGRectMake(10, GlobleHeight*0.72, GlobleWidth-70, 30);
    self.titleLabel.font = [UIFont boldSystemFontOfSize:17];
    self.titleLabel.numberOfLines = 0;
    self.titleLabel.textAlignment = NSTextAlignmentLeft;
    self.titleLabel.textColor = bu_textColor;
    [self addSubview:self.titleLabel];
    
    self.descriptionLabel = [UILabel new];
  self.descriptionLabel.frame = CGRectMake(10, GlobleHeight*0.72+32, GlobleWidth-70, 50);
    self.descriptionLabel.font = [UIFont systemFontOfSize:15];
    self.descriptionLabel.numberOfLines = 0;
    self.descriptionLabel.textColor = bu_textColor;
    [self addSubview:self.descriptionLabel];
    
    _headImg = [[UIImageView alloc] initWithFrame:CGRectMake(GlobleWidth-inconWidth-23, GlobleHeight*0.5, inconWidth+10, inconWidth+10)];
    _headImg.image = [UIImage imageNamed:@"head"];
    _headImg.clipsToBounds = YES;
    _headImg.layer.cornerRadius = (inconWidth+10)/2;
    _headImg.layer.borderColor = bu_textColor.CGColor;
    _headImg.layer.borderWidth = 1;
    [self addSubview:_headImg];
    
    _likeImg = [[UIImageView alloc] initWithFrame:CGRectMake(GlobleWidth-inconWidth-18, _headImg.frame.origin.y + inconWidth + inconEdge+13, inconWidth, inconWidth)];
    _likeImg.image = [UIImage imageNamed:@"like"];
    
    [self addSubview:_likeImg];
    
    _likeLable = [[UILabel alloc] initWithFrame:CGRectMake(GlobleWidth-inconWidth-18, _likeImg.frame.origin.y + inconWidth + bu_textEnde, inconWidth, bu_textFont)];
    _likeLable.font = [UIFont systemFontOfSize:bu_textFont];
    _likeLable.textAlignment = NSTextAlignmentCenter;
    _likeLable.textColor = bu_textColor;
    _likeLable.text = @"21.4w";
    [self addSubview:_likeLable];
    
    _commentImg = [[UIImageView alloc] initWithFrame:CGRectMake(GlobleWidth-inconWidth-18, _likeLable.frame.origin.y + bu_textFont + inconEdge+12, inconWidth, inconWidth-10)];
    _commentImg.image = [UIImage imageNamed:@"comment"];
    [_commentImg setContentMode:UIViewContentModeScaleAspectFill];
    [self addSubview:_commentImg];
    
    _commentLable = [[UILabel alloc] initWithFrame:CGRectMake(GlobleWidth-inconWidth-18, _commentImg.frame.origin.y + inconWidth + bu_textEnde, inconWidth, bu_textFont)];
    _commentLable.font = [UIFont systemFontOfSize:bu_textFont];
    _commentLable.textAlignment = NSTextAlignmentCenter;
    _commentLable.textColor = bu_textColor;
    _commentLable.text = @"3065";
    [self addSubview:_commentLable];
}

- (void)loadExpressAds {
  
  BUAdSlot *slot1 = [[BUAdSlot alloc] init];
  slot1.ID = self._codeid;
  slot1.AdType = BUAdSlotAdTypeDrawVideo; //required
  slot1.isOriginAd = YES; //required
  slot1.position = BUAdSlotPositionTop;
  slot1.imgSize = [BUSize sizeBy:BUProposalSize_DrawFullScreen];
  slot1.isSupportDeepLink = YES;
  
  
//  CGSizeMake(1080, 1920)

  if (!self.adManager) {
      self.adManager = [[BUNativeExpressAdManager alloc] initWithSlot:slot1 adSize:[AdBoss getWindow].bounds.size];
  }
//  self.adManager.adSize = self.bounds.size;
  self.adManager.delegate = self;
  [self.adManager loadAd:1];
  
}



#pragma mark - BUNativeExpressAdViewDelegate
- (void)nativeExpressAdSuccessToLoad:(BUNativeExpressAdManager *)nativeExpressAd views:(NSArray<__kindof BUNativeExpressAdView *> *)views {
  
  BUD_Log(@"DrawVideo nativeExpressAdSuccessToLoad isReady = %d",views[0].isReady ?1:0);
  
  if (views.count) {
        [views enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            BUNativeExpressAdView *expressAdView = (BUNativeExpressAdView *)obj;
            [expressAdView render];
        }];
  }
}

- (void)nativeExpressAdFailToLoad:(BUNativeExpressAdManager *)nativeExpressAd error:(NSError *)error {
    BUD_Log(@"%s",__func__);
    NSLog(@"error code : %ld , error message : %@",(long)error.code,error.description);
}

- (void)nativeExpressAdViewRenderSuccess:(BUNativeExpressAdView *)nativeExpressAdView {
    
  NSLog(@"====== %p draw 成功渲染 videoDuration = %ld",nativeExpressAdView,(long)nativeExpressAdView.videoDuration);
  
  dispatch_async(dispatch_get_main_queue(), ^{
    nativeExpressAdView.rootViewController = [AdBoss getWindow].rootViewController;
    [self addSubview: nativeExpressAdView];
  });

}

- (void)updateCurrentPlayedTime {
    for (id nativeExpressAdView in self.dataSource) {
        if ([nativeExpressAdView isKindOfClass:[BUNativeExpressAdView class]]) {
            BUNativeExpressAdView *adView = nativeExpressAdView;
            NSLog(@"====== %p currentPlayedTime = %f",nativeExpressAdView,adView.currentPlayedTime);
        }
    }
}

- (void)nativeExpressAdView:(BUNativeExpressAdView *)nativeExpressAdView stateDidChanged:(BUPlayerPlayState)playerState {
    NSLog(@"====== %p playerState = %ld",nativeExpressAdView,playerState);
}

- (void)nativeExpressAdViewRenderFail:(BUNativeExpressAdView *)nativeExpressAdView error:(NSError *)error {
    BUD_Log(@"%s",__func__);
}

- (void)nativeExpressAdViewWillShow:(BUNativeExpressAdView *)nativeExpressAdView {
    BUD_Log(@"%s",__func__);
}

- (void)nativeExpressAdViewDidClick:(BUNativeExpressAdView *)nativeExpressAdView {
    BUD_Log(@"%s",__func__);
  
  //TODO: 日后回调给RN 统计点击事件和贡献奖励
  
//  dispatch_async(dispatch_get_main_queue(), ^{
//     self.onAdClicked(@{
//         @"message":@"Draw视频广告 been clicked"
//       });
//  });
 
}

- (void)nativeExpressAdViewPlayerDidPlayFinish:(BUNativeExpressAdView *)nativeExpressAdView error:(NSError *)error {
    BUD_Log(@"%s",__func__);
}

- (void)nativeExpressAdView:(BUNativeExpressAdView *)nativeExpressAdView dislikeWithReason:(NSArray<BUDislikeWords *> *)filterWords {
    BUD_Log(@"%s",__func__);
}

- (void)nativeExpressAdViewDidClosed:(BUNativeExpressAdView *)nativeExpressAdView {
    BUD_Log(@"%s",__func__);
}

- (void)nativeExpressAdViewWillPresentScreen:(BUNativeExpressAdView *)nativeExpressAdView {
    BUD_Log(@"%s",__func__);
}

- (void)nativeExpressAdViewDidCloseOtherController:(BUNativeExpressAdView *)nativeExpressAdView interactionType:(BUInteractionType)interactionType {
    
  NSString *str = nil;
    if (interactionType == BUInteractionTypePage) {
        str = @"ladingpage";
    } else if (interactionType == BUInteractionTypeVideoAdDetail) {
        str = @"videoDetail";
    } else {
        str = @"appstoreInApp";
    }
  
    BUD_Log(@"%s __ %@",__func__,str);
}



-(void)autoPlay {
  self.player = [self reuseInlinePlayer];
  NSString *resourceName = [NSString stringWithFormat:@"drawLocal_0%u",arc4random()%8];
  NSString *loacUrl = [[NSBundle mainBundle] pathForResource:resourceName ofType:@"mp4"];
  _player.contentURL = [NSURL fileURLWithPath:loacUrl];
  [_player play];
}

- (void)pause {
  [_player pause];
}

- (void)setPlayer:(BUDPlayerController *)player {
  _player = player;
  if (_player) {
    _player.view.frame = CGRectMake(0, 0, GlobleWidth, GlobleHeight);
    [self insertSubview:_player.view atIndex:0];
  }
}

- (BOOL)willDealloc {
  return NO;
}

- (BUDPlayerController *)reuseInlinePlayer {
  static BUDPlayerController* player = nil;
  if (!player) {
    player = [[BUDPlayerController alloc] init];
  }else{
    player.contentURL = nil;
    [player.view removeFromSuperview];
    [player pause];
  }
  return player;
}

-(void)refreshUIAtIndex:(NSUInteger)index{
  self.titleLabel.text = [NSString localizedStringWithFormat:[NSString localizedStringForKey:DrawTitle],(unsigned long)index];
  self.descriptionLabel.text = [NSString localizedStringForKey:DrawDescription];
  self.backgroundColor = [UIColor blackColor];
}



- (void)buildupVideoView{
  self.nativeAdRelatedView = [[BUNativeAdRelatedView alloc] init];
  
  if (!self.nativeAdRelatedView.videoAdView.superview) {
    self.nativeAdRelatedView.videoAdView.frame = CGRectMake(0, 0, GlobleWidth, GlobleHeight);
    [self.nativeAdRelatedView.videoAdView playerPlayIncon:[UIImage imageNamed:@"adPlay.png"] playInconSize:CGSizeMake(60, 60)];
    //Whether to support click pause
    self.nativeAdRelatedView.videoAdView.drawVideoClickEnable = YES;
    [self insertSubview:self.nativeAdRelatedView.videoAdView atIndex:0];
  }
  
  if (!self.nativeAdRelatedView.adLabel.superview) {
    self.nativeAdRelatedView.adLabel.frame = CGRectMake(13, GlobleHeight*0.835, 30, 16);
    [self addSubview:self.nativeAdRelatedView.adLabel];
  }
  
  if (self.creativeButton && !self.creativeButton.superview) {
    self.creativeButton.frame = CGRectMake(10, GlobleHeight*0.83, GlobleWidth*0.6, 36);
    
    [self.creativeButton.layer setCornerRadius:3];
    [self addSubview:self.creativeButton];
  }
  [self addAccessibilityIdentifier];
}

- (UIButton *)creativeButton{
  if (!_creativeButton) {
    _creativeButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [_creativeButton setTitle:[NSString localizedStringForKey:Detail] forState:UIControlStateNormal];
    _creativeButton.backgroundColor = BUD_RGB(0x80,0xbb,0x41);
    _creativeButton.titleLabel.font = [UIFont systemFontOfSize:14.0f];
  }
  return _creativeButton;
}

-(void)refreshUIWithModel:(BUNativeAd *)model{
  self.titleLabel.text = model.data.AdTitle;
  self.descriptionLabel.text = model.data.AdDescription;
  [self.creativeButton setTitle:model.data.buttonText forState:UIControlStateNormal];
  [self.nativeAdRelatedView refreshData:model];
}

#pragma mark addAccessibilityIdentifier
- (void)addAccessibilityIdentifier {
  self.creativeButton.accessibilityIdentifier = @"button";
  self.nativeAdRelatedView.videoAdView.accessibilityIdentifier = @"draw_view";
  self.titleLabel.accessibilityIdentifier = @"draw_appname";
  self.descriptionLabel.accessibilityIdentifier = @"draw_appdetial";
}


@end


