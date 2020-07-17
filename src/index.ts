import { NativeModules } from "react-native";

import loadSplashAd from "./SplashAd";
import loadFullVideoAd from "./FullVideo";
import loadRewardVideoAd from "./RewardVideo";
import DrawFeed from "./DrawFeed";
import FeedAd from "./FeedAd";

const { AdManager } = NativeModules;
export const init = (appid: string) => {
  AdManager.init(appid);
};

export default {
  init,
  loadSplashAd,
  loadFullVideoAd,
  loadRewardVideoAd,
  DrawFeed,
  FeedAd,
};
