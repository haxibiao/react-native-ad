//
//  LoadRewardSingle.m
//  react-native-ad
//
//  Created by 袁超 on 2020/9/4.
//

#import "LoadRewardSingle.h"

@implementation LoadRewardSingle

static id _instance;

static NSMutableDictionary *rewardAdMap = nil;

- (NSMutableDictionary *)rewardAdMap{
    NSLog(@"Enda rewardAdMap is init");
    if (!rewardAdMap) {
        rewardAdMap = [NSMutableDictionary dictionary];
    }
    return rewardAdMap;
}

+ (instancetype)sharedInstance
{
     static dispatch_once_t onceToken;
     dispatch_once(&onceToken, ^{
         _instance = [[self alloc] init];
     });
     return _instance;
}
 
+ (id)allocWithZone:(struct _NSZone *)zone
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _instance = [super allocWithZone:zone];
    });
    return _instance;
}
 
- (id)copyWithZone:(NSZone *)zone
{
     return _instance;
}

@end

