/**
 * @format
 */

import {AppRegistry} from 'react-native';
import {name as appName} from './app.json';

import Splash from './src/Splash'; // 开屏广告对接示例
import FullScreenVideo from './src/FullScreenVideo'; // 全屏视频广告对接示例
import RewardVideo from './src/RewardVideo'; // 激励视频广告对接示例
import DrawFeed from './src/DrawFeed'; //  DrawFeed 广告对接示例
import Feed from './src/Feed'; //  Feed 广告对接示例

AppRegistry.registerComponent(appName, () => Splash);
