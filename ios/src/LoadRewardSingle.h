//
//  LoadRewardSingle.h
//  react-native-ad
//
//  Created by 袁超 on 2020/9/4.
//

#ifndef LoadRewardSingle_h
#define LoadRewardSingle_h

@interface LoadRewardSingle: NSObject

/**
 * 获取单例类
 *
 * @return 单例类对象
 */
+(instancetype) sharedInstance;

@property NSMutableDictionary *rewardAdMap;

@property NSString *codeId;

-(NSMutableDictionary *) rewardAdMap;

@end

#endif /* LoadRewardSingle_h */
