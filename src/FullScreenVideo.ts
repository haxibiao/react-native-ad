import React from "react";
import {
	NativeModules,
	NativeEventEmitter,
	requireNativeComponent,
} from "react-native";

const listenerCache={};

export default (appid: string,codeid: string) => {
	const {FullScreenVideo}=NativeModules;
	const eventEmitter=new NativeEventEmitter(FullScreenVideo);
	let result=FullScreenVideo.startAd({appid,codeid});

	return {
		result,
		subscribe: (
			type: keyof EVENT_TYPE,
			callback: (event: any) => void,
		) => {
			if(listenerCache[type]) {
				listenerCache[type].remove();
			}
			return listenerCache[type]=eventEmitter.addListener("FullScreenVideo-"+type,(event: any) => {
				callback(event);
			});
		}
	};
};
