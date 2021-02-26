import { NativeModules } from 'react-native';

import { init, loadFeedAd, loadDrawFeedAd } from './AdManager';
import startSplash from './Splash';
import startFullVideo from './FullScreenVideo';
import startRewardVideo from './RewardVideo';
import DrawFeed from './DrawFeed';
import Feed from './Feed';
import Stream from './Stream';

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
};
