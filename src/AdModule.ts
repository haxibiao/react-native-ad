import { NativeModules } from 'react-native';
const { AdModule } = NativeModules;

//热云key  "844fa6a32c83a5144b441d49c33aeddf"
export const initRY = (key: string) => {
    AdModule.initRY(key, (result: string) => {
        console.log('初始化热云sdk的回调' + result);
    });
};

//自定义事件跟踪
export const trackRYEvent = (eventName: string) => {
    //提前加载信息流FeedAd, 结果返回promise
    AdModule.trackRYEvent(eventName, (result: string) => {
        console.log('跟踪到热云自定义事件的回调 ' + result);
    });
};

export default {
    initRY,
    trackRYEvent,
};
