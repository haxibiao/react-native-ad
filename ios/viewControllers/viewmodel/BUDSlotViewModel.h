//
//  BUDSlotViewModel.h
//  datizhuanqian
//
//  Created by ivan zhang on 2019/5/6.
//  Copyright © 2019 Facebook. All rights reserved.
//

#ifndef BUDSlotViewModel_h
#define BUDSlotViewModel_h


#endif /* BUDSlotViewModel_h */


#import <Foundation/Foundation.h>
#import "BUDDefs.h"

//https://wiki.bytedance.net/pages/viewpage.action?pageId=146011735

@interface BUDSlotViewModel : NSObject<BUDViewModel>
@property (nonatomic, copy) NSString *slotID;
@property (nonatomic, copy) NSString *userId;
@end

