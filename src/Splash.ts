import React from 'react';
import { NativeModules, NativeEventEmitter, requireNativeComponent } from 'react-native';

export interface EVENT_TYPE {
    onAdError: string; // 广告加载失败监听
    onAdClick: string; // 广告被点击监听
    onAdClose: string; // 广告关闭
    onAdSkip: string; // 用户点击跳过广告监听
    onAdShow: string; // 开屏广告开始展示
}

export interface SPLASHAD_PROPS_TYPE {
    appid: string,
    codeid: string,
    provider: "头条" | "百度" | "腾讯",
    anim?: "default" | "none" | "catalyst" | "slide" | "fade",
};

const listenerCache = {};

export default ({ appid, codeid, provider = "头条", anim = "default" }: SPLASHAD_PROPS_TYPE) => {
    const { SplashAd } = NativeModules;
    const eventEmitter = new NativeEventEmitter(SplashAd);
    let result = SplashAd.loadSplashAd({ appid, codeid, provider, anim });

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
