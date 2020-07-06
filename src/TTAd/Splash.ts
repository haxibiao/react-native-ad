import React from "react";
import {
  NativeModules,
  NativeEventEmitter,
  requireNativeComponent,
} from "react-native";

const { TTAdSplash } = NativeModules;

// 默认很多是用旧的native方式申请的drawfeed 代码位...
// const isExpress = DrawFeedUseExpress ? DrawFeedUseExpress : "false";
const isExpress = "false";

interface EVENT_TYPE {
  onError: string; // 广告加载失败监听
  onTimeout: string; // 广告加载超时监听
  onAdClicked: string; // 广告被点击监听
  onAdTimeOver: string; // 广告倒计时结束监听
  onAdSkip: string; // 用户点击跳过广告监听
}

export default (appid: string, codeid: string) => {
  TTAdSplash.loadSplashAd(appid, codeid);
  return {
    subscrib: (type: keyof EVENT_TYPE, callback: (event: any) => void) => {
      const eventEmitter = new NativeEventEmitter(TTAdSplash);
      return eventEmitter.addListener("TTSplashAdListener", (event: any) => {
        if (event[type]) {
          callback(event[type]);
        }
      });
    },
  };
};
