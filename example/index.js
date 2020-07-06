/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';

import TTSplashDemo from './TTSplashDemo'; // 头条开屏广告对接示例
import TTDrawFeedDemo from './TTDrawFeedDemo'; // 头条 DrawFeed 广告对接示例

AppRegistry.registerComponent(appName, () => TTDrawFeedDemo);
