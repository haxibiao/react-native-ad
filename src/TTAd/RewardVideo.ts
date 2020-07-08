import React from "react";
import {
  NativeModules,
  NativeEventEmitter,
  requireNativeComponent,
} from "react-native";

const { TTRewardVideo } = NativeModules;

const listenerCache = {};

interface EVENT_TYPE {
  onAdError: string; // 广告加载失败监听
  onAdLoaded: string; // 广告加载成功监听
  onAdVideoCached: string; // 穿山甲激励视频缓存成功
  onAdClicked: string; // 广告被点击监听
  onAdClose: string; // 广告关闭监听
  onVideoComplete: string; // 激励视频播放失败
  onDownloadActive: string; // 广告应用下载相应监听
}

export default function(appid: string, codeid: string) {
  const RewardVideo = TTRewardVideo.startAd({ appid, codeid });
  const eventEmitter = new NativeEventEmitter(TTRewardVideo);

  RewardVideo.subscrib = (
    type: keyof EVENT_TYPE,
    callback: (event: any) => void,
  ) => {
    if(listenerCache[type]) {
      listenerCache[type].remove()
    }
    return listenerCache[type] = eventEmitter.addListener("TTRewardVideo-" + type, (event: any) => {
      callback(event);
    });
  };

  return RewardVideo;
};





