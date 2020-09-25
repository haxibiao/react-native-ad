import { NativeModules } from 'react-native';
const { AdManager } = NativeModules;

type appInfo = {
    appid: string;
    app: string | null; //app名称
    uid: string | null; //有些uid和穿山甲商务有合作的需要
    amount: number | null; //奖励数量
    reward: string | null; //奖励是啥
    codeid_reward_video: string | null; //需要提前预加载的激励视频广告位
    codeid_full_video: string | null; //需要提前预加载的全屏视频广告位
};

export const init = (appInfo: appInfo) => {
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

export default {
    init,
    loadFeedAd,
};
