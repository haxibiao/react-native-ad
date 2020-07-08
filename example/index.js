/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './src/App';
import {name as appName} from './app.json';

import TTSplashDemo from './src/TTSplashDemo'; // 头条开屏广告对接示例
import TTFullScreenDemo from './src/TTFullScreenDemo'; // 头条全屏视频广告对接示例
import TTRewardVideoDemo from './src/TTRewardVideoDemo'; // 头条激励视频广告对接示例
import TTDrawFeedDemo from './src/TTDrawFeedDemo'; // 头条 DrawFeed 广告对接示例
import TTFeedDemo from './src/TTFeedDemo'; // 头条 Feed 广告对接示例
import TTBannerDemo from './src/TTBannerDemo'; // 头条 Banner 广告对接示例

AppRegistry.registerComponent(appName, () => TTRewardVideoDemo);