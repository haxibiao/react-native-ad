import { NativeModules } from 'react-native';

import startSplash from './Splash';
import startFullVideo from './FullScreenVideo';
import startRewardVideo from './RewardVideo';
import DrawFeed from './DrawFeed';
import Feed from './Feed';

const { AdManager } = NativeModules;

type appInfo = {
    appid: string;
    app: string;
    uid: string;
    amount: number;
    reward: string;
};

export const init = (appInfo) => {
    //FIXME: init 传入一些codeid可以提前加载广告，比如视频类
    AdManager.init(appInfo);
};

type feedInfo = {
    appid: string;
    codeid: string;
};

export const loadFeedAd = (info: feedInfo) => {
    //提前加载信息流FeedAd, 结果返回promise
    return AdManager.loadFeedAd(info);
};

type adInfo = {
    appId: string;
    codeId: string;
    extra: string;
    userId: string;
};
export const loadRewardAd = (info: adInfo) => {
    //提前加载激励视频, 结果返回promise
    return AdManager.loadRewardAd(info);
};

export default {
    init,
    loadFeedAd,
    startSplash,
    startFullVideo,
    startRewardVideo,
    DrawFeed,
    Feed,
    loadRewardAd
};
