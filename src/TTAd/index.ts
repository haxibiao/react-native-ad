import { NativeModules } from "react-native";

import loadSplashAd from "./Splash";
import DrawFeedAd from "./DrawFeed";
import FeedAd from "./Feed";

const { TTAdManager } = NativeModules;
export const init = (appid: string) => {
  TTAdManager.init(appid);
};

export default { init, loadSplashAd, DrawFeedAd, FeedAd };
