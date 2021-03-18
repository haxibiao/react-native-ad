import React from 'react';
import { NativeModules, NativeEventEmitter, requireNativeComponent } from 'react-native';

const listenerCache = {};

export interface AD_EVENT_TYPE {
    onAdError: string; // 广告加载失败监听
    onAdLoaded: string; // 广告加载成功监听
    onAdClick: string; // 广告被点击监听
    onAdClose: string; // 广告关闭监听
}

export default ({ appid, codeid }) => {
    const { FullScreenVideo } = NativeModules;
    const eventEmitter = new NativeEventEmitter(FullScreenVideo);
    let result = FullScreenVideo.startAd({ appid, codeid });

    return {
        result,
        subscribe: (type: keyof AD_EVENT_TYPE, callback: (event: any) => void) => {
            if (listenerCache[type]) {
                listenerCache[type].remove();
            }
            return (listenerCache[type] = eventEmitter.addListener('FullScreenVideo-' + type, (event: any) => {
                callback(event);
            }));
        },
    };
};
