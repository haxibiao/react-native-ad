import React from "react";
import {
  NativeModules,
  NativeEventEmitter,
  requireNativeComponent,
  Platform,
  StyleSheet,
} from "react-native";

const { TTAdManager, TTAdSplash } = NativeModules;
const NativeDrawFeedAd = requireNativeComponent("TTAdDrawFeed");

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

export const init = (appid: string) => {
  TTAdManager.init(appid);
};

export const loadSplashAd = (appid: string, codeid: string) => {
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

interface Props {
  appid: string;
  codeid: string;
  onError?: Function;
  onLoad?: Function;
  onAdClick?: Function;
}

export const DrawFeedAd = (props: Props) => {
  let codeid_draw_video = "";
  const draw_video_provider = "";

  const { appid, codeid, onError, onLoad, onAdClick } = props;
  const [visible, setVisible] = React.useState(true);
  
  if (codeid_draw_video == "") {
    codeid_draw_video = codeid;
    // Platform.OS === "ios" ? CodeIdDrawFeedIOS : CodeIdDrawFeed;
  }

  return (
    <NativeDrawFeedAd
      provider={draw_video_provider}
      is_express={isExpress}
      codeid={codeid_draw_video}
      style={{ ...styles.container }}
      onAdError={(e: any) => {
        console.log("onError feed", e.nativeEvent);
        setVisible(false);
        onError && onError(e.nativeEvent);
      }}
      onAdClick={onAdClick}
      onAdShow={(e: any) => {
        console.log("onAdShow", e.nativeEvent);
      }}
    />
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: "100%",
  },
});

export default { loadSplashAd, DrawFeedAd, init };
