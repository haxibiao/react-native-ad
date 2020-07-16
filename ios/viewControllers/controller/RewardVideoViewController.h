//
//  RewardVideoViewController.h
//
//  Created by ivan zhang on 2019/5/6.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BUDDefs.h"

@interface RewardVideoViewController : UIViewController<BUDViewControl>
@property (nonatomic, strong) id<BUDViewModel> viewModel;
@end
