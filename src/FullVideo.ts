import React from "react";
import {
  NativeModules,
  NativeEventEmitter,
  requireNativeComponent,
} from "react-native";

const { FullScreenVideo } = NativeModules;

export default (appid: string, codeid: string) => {
  return FullScreenVideo.startAd({ appid, codeid });
};
