import React from 'react';
import { NativeModules, NativeEventEmitter } from 'react-native';

const listenerCache = {};
export interface AD_EVENT_TYPE {
    onAdError: string; // 广告加载失败监听
    onAdLoaded: string; // 广告加载成功监听
    onAdClick: string; // 广告被点击监听
    onAdClose: string; // 广告关闭监听
}

type rewardInfo = {
    appid: string;
    codeid: string;
    app?: string; //头条穿山甲参数appName (目前安卓可用，可选)
    uid?: string; //头条穿山甲参数userId (目前安卓可用，可选);
    reward?: string; //头条穿山甲参数rewardName (目前安卓可用，可选)
    amount?: number; //头条穿山甲参数rewardAmount (目前安卓可用，可选)
};

export default function (info: rewardInfo) {
    const { RewardVideo } = NativeModules;
    const eventEmitter = new NativeEventEmitter(RewardVideo);
    let result = RewardVideo.startAd(info);

    return {
        result,
        subscribe: (type: keyof AD_EVENT_TYPE, callback: (event: any) => void) => {
            if (listenerCache[type]) {
                listenerCache[type].remove();
            }
            return (listenerCache[type] = eventEmitter.addListener('RewardVideo-' + type, (event: any) => {
                callback(event);
            }));
        },
    };
}
