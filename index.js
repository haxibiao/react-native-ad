import { NativeModules } from "react-native";

const { BytedADSplash } = NativeModules;

export const loadSplashAd = (appid, codeid) => {
  BytedADSplash.loadSplashAd(appid, codeid);
};

export default { loadSplashAd };
