import { NativeModules } from "react-native";

import startSplash from "./Splash";
import startFullVideo from "./FullScreenVideo";
import startRewardVideo from "./RewardVideo";
import DrawFeed from "./DrawFeed";
import Feed from "./Feed";

const { AdManager } = NativeModules;

export const init = ({ appid }) => {
	//FIXME: init 传入一些codeid可以提前加载广告，比如视频类
	AdManager.init({ appid });
};

export default {
	init,
	startSplash,
	startFullVideo,
	startRewardVideo,
	DrawFeed,
	Feed,
};
