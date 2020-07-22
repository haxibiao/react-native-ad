//
//  FullscreenViewController.h
//
//  Created by ivan zhang on 2019/9/26.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <BUAdSDK/BUNativeExpressFullscreenVideoAd.h>
#import "BUDMacros.h"
#import "AdBoss.h"
#import "FullScreenVideo.h" 
#import "NSString+LocalizedString.h"


@interface FullscreenViewController : UIViewController
@property (nonatomic, copy) NSString *slotID;
@end
