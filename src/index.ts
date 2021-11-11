import { NativeModules } from 'react-native';

import { init, loadFeedAd, loadDrawFeedAd } from './AdManager';

//activity
import startSplash from './Splash';
import startFullVideo from './FullScreenVideo';
import startRewardVideo from './RewardVideo';

//component

import DrawFeed from './toutiao/DrawFeed';
import Feed from './toutiao/Feed';
import Stream from './toutiao/Stream';

import TxFeed from './tencent/TxFeed';

import KsDrawFeed from './kuaishou/KsDrawFeed';
import KsFeed from './kuaishou/KsFeed';

export default {
    init,
    loadFeedAd,
    loadDrawFeedAd,
    startSplash,
    startFullVideo,
    startRewardVideo,
    DrawFeed,
    Feed,
    Stream,
    TxFeed,
    KsDrawFeed,
    KsFeed,
};
