import React from "react";
import {
	NativeModules,
	NativeEventEmitter,
	requireNativeComponent,
} from "react-native";


// 默认很多是用旧的native方式申请的drawfeed 代码位...
// const isExpress = DrawFeedUseExpress ? DrawFeedUseExpress : "false";
const isExpress = "false";

interface EVENT_TYPE {
	onError: string; // 广告加载失败监听
	onTimeout: string; // 广告加载超时监听
	onAdClicked: string; // 广告被点击监听
	onAdTimeOver: string; // 广告倒计时结束监听
	onAdSkip: string; // 用户点击跳过广告监听
	onAdShow: string; // 开屏广告开始展示
}

const listenerCache = {};

export default ({ appid, codeid }) => {
	const { SplashAd } = NativeModules;
	const eventEmitter = new NativeEventEmitter(SplashAd);
	SplashAd.loadSplashAd({ appid, codeid });

	return {
		subscribe: (type: keyof EVENT_TYPE, callback: (event: any) => void) => {
			if (listenerCache[type]) {
				listenerCache[type].remove();
			}
			return listenerCache[type] = eventEmitter.addListener("Splash-" + type, (event: any) => {
				if (event[type]) {
					callback(event[type]);
				}
			});
		}
	};
};
