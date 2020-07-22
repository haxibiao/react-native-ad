/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './src/App';
import {name as appName} from './app.json';

import SplashAd from './src/SplashAd'; // 头条开屏广告对接示例
import FullScreenVideo from './src/FullScreenVideo'; // 头条全屏视频广告对接示例
import RewardVideo from './src/RewardVideo'; // 头条激励视频广告对接示例
import DrawFeed from './src/DrawFeed'; // 头条 DrawFeed 广告对接示例
import FeedAd from './src/FeedAd'; // 头条 Feed 广告对接示例

AppRegistry.registerComponent(appName, () => FullScreenVideo);
