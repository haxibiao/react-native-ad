import { NativeModules } from 'react-native';

import { init, loadFeedAd } from './AdManager';
import startSplash from './Splash';
import startFullVideo from './FullScreenVideo';
import startRewardVideo from './RewardVideo';
import DrawFeed from './DrawFeed';
import Feed from './Feed';

import { initRY, trackRYEvent } from './AdModule';

export default {
    init,
    loadFeedAd,
    startSplash,
    startFullVideo,
    startRewardVideo,
    DrawFeed,
    Feed,
    initRY,
    trackRYEvent,
};
