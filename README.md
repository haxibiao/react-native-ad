# react-native-ad

#### 安装方法 🔨

```
yarn add git+http://auto:hxb332211@code.haxibiao.cn/packages/react-native-ad.git
```

或者

```
npm install -D git+http://auto:hxb332211@code.haxibiao.cn/packages/react-native-ad.git
```

或者在项目 package.json 的 dependencies 下添加一行

```
"react-native-ad":"git+http://auto:hxb332211@code.haxibiao.cn/packages/react-native-ad.git"
```

## 接口说明：🍎

#### 开屏广告 🍖🍔🍟

导入`loadSplashAd`模块

**import { loadSplashAd } from 'react-native-ad';**

需要穿山甲广告平台的 appid 和 codeid

```
...
const appid = "";
const codeid = "";
loadSplashAd(appid, codeid);
...
```

开屏广告监听回调，调用 `loadSplashAd` 将返回一个对象中包含回调监听方法 `subscribe` 该方法接收两个参数监听事件和监听回调方法包含一个回调参数

| 监听事件     | 回调参数类型 |                  说明                  |
| ------------ | :----------: | :------------------------------------: |
| onError      |    string    | 开屏广告渲染失败将会回调，返回错误信息 |
| onTimeout    |   boolean    |    开屏广告加载超时回调，返回 true     |
| onAdClicked  |   boolean    |     开屏广告被点击回调，返回 true      |
| onAdSkip     |   boolean    |    用户点击跳过广告回调，返回 true     |
| onAdTimeOver |   boolean    | 开屏广告展示倒计时结束回调，返回 true  |

示例代码：

```
const splashAd = loadSplashAd(appid,codeid);

splashAd.subscribe('onError', e => {
  console.log('广告加载出错监听', e);
});

splashAd.subscribe('onTimeout', event => {
  console.log('广告加载超时监听', event);
});

splashAd.subscribe('onAdTimeOver', event => {
  console.log('广告时间结束监听', event);
});

splashAd.subscribe('onAdSkip', i => {
  console.log('用户点击跳过监听', i);
});

splashAd.subscribe('onAdClicked', i => {
  console.log('广告被点击监听', i);
});

```

### 更新模块

怎么查看当前版本？执行 `yarn list | grep "react-native-ad"`，将会输出你当前项目安装的 react-native-ad 版本

然后可以使用 npm update 来更新模块

```
$ npm update react-native-ad
```

或者使用 yarn upgrade 来更新

```
$ yarn upgrade react-native-ad
```
