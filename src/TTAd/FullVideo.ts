import React from "react";
import {
  NativeModules,
  NativeEventEmitter,
  requireNativeComponent,
} from "react-native";

const { TTFullScreenVideo } = NativeModules;

export default (appid: string, codeid: string) => {
  return TTFullScreenVideo.startAd({ appid, codeid });
};
