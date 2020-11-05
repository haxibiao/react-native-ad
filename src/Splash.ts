import React from 'react';
import { NativeModules, NativeEventEmitter, requireNativeComponent } from 'react-native';

interface EVENT_TYPE {
    onAdError: string; // 广告加载失败监听
    onAdClick: string; // 广告被点击监听
    onAdClose: string; // 广告关闭
    onAdSkip: string; // 用户点击跳过广告监听
    onAdShow: string; // 开屏广告开始展示
}

const listenerCache = {};

export default ({ appid, codeid, provider }) => {
    const { SplashAd } = NativeModules;
    const eventEmitter = new NativeEventEmitter(SplashAd);
    let result = SplashAd.loadSplashAd({ appid, codeid, provider });

    return {
        result,
        subscribe: (type: keyof EVENT_TYPE, callback: (event: any) => void) => {
            if (listenerCache[type]) {
                listenerCache[type].remove();
            }
            return (listenerCache[type] = eventEmitter.addListener('SplashAd-' + type, (event: any) => {
                console.log('SplashAd event type ', type);
                console.log('SplashAd event ', event);
                callback(event);
            }));
        },
    };
};
