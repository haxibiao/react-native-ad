import { NativeModules, NativeEventEmitter } from "react-native";

const { BytedADSplash } = NativeModules;

interface EVENT_TYPE {
  onError: string; // 广告加载失败监听
  onTimeout: string; // 广告加载超时监听
  onAdClicked: string; // 广告被点击监听
  onAdTimeOver: string; // 广告倒计时结束监听 
  onAdSkip: string; // 用户点击跳过广告监听
}

export const loadSplashAd = (appid: string, codeid: string) => {
  BytedADSplash.loadSplashAd(appid, codeid);
  return {
    subscrib: (type: keyof EVENT_TYPE, callback: (event: any) => void) => {
      const eventEmitter = new NativeEventEmitter(BytedADSplash);
      return eventEmitter.addListener("TTSplashAdListener", (event: any) => {
        if (event[type]) {
          callback(event[type]);
        }
      });
    }
  };
};

export default { loadSplashAd };
