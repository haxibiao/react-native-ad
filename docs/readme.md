# 穿山甲（头条）React Native 广告模块对接文档

> 广告平台地址：<https://partner.oceanengine.com/>

> 广告平台对接文档地址：<https://ad.oceanengine.com/union/media/union/download>

> 当前 Android 集成广告平台 SDK 版本：v3.1.0.0

> 当前 IOS 集成广告平台 SDK 版本：v3.1.0.5

文档目录：

- [穿山甲（头条）React Native 广告模块对接文档](#穿山甲头条react-native-广告模块对接文档)
- [创建获取 appId 和广告位 codeId](#创建获取-appid-和广告位-codeid)
- [获取穿山甲 appId](#获取穿山甲-appid)
- [获取穿山甲 codeId](#获取穿山甲-codeid)
- [安装配置 react-native-ad 模块](#安装配置-react-native-ad-模块)
- [初始化 react-native-ad SDK](#初始化-SDK)
- [开屏（Splash）广告的对接及示例](#开屏splash广告的对接及示例)
- [激励视频（RewardVideo）广告的对接及示例](#激励视频rewardvideo广告的对接及示例)
- [激励视频回调对象](#激励视频回调对象)
- [全屏视频（FullVideo）广告的对接及示例](#全屏视频fullvideo广告的对接及示例)
- [信息流（Feed）广告的对接及示例](#信息流feed广告的对接及示例)
- [视频信息流（DrawFeed）广告的对接及示例](#视频信息流drawfeed广告的对接及示例)
- [横幅（Banner）广告的对接及示例](#横幅banner广告的对接及示例)

## 创建获取 appId 和广告位 codeId

> 穿山甲广告平台，首先是要注册企业账号，要对接广告的话首先得先到应用商店上架

#### 获取穿山甲 appId

![c407e590f975.png](http://cos.haxibiao.com/images/5f05cae38e8ea.png)

在广告平台我们可以创建相应的应用，Android 和 IOS 可以分开创建，命名方式建议（xxxx_android or xxxx_ios）创建后经过平台审核通过后才可以创建广告位 codeId

#### 获取穿山甲 codeId

在获取 codeId 前，我们先了解一下广告类型：信息流（Feed）广告，横幅（Banner）广告，全屏视频（FullVideo）广告，开屏（Splash）广告，激励视频（RewardVideo）广告，视频信息流（DrawFeed）广告

![ds5vsa6vea1212.png](http://cos.haxibiao.com/images/5f05cca0df84b.png)

不同的广告类型对应不同 codeId 可以通过广告平台后台创建。

## 安装配置 react-native-ad 模块

[![npm version](https://badge.fury.io/js/react-native-ad.svg)](https://badge.fury.io/js/react-native-ad) 


可以使用 yarn 或者 npm 进行安装

```
yarn add react-native-ad
```

或者

```
npm install -D react-native-ad
```

或者在项目 package.json 的 dependencies 下添加一行

```
"react-native-ad":"^1.1.6"
```

**注意：** 对接 react-native-ad v1.1.5 及以上版本或 React Native v0.64.0 以上版本对接之后导致闪退的需参考：[React Native v0.64.0 Android 对接 sdk 过渡方案](./excessive_v1.16.md)

## 初始化 SDK

> 为了实现部分广告的预加载，新增加 ad.init() 方法，在调用任何广告之前需要先调用 ad.init 方法初始化 SDK（已经初始化过的可以忽略，初始化一次后其他地方可以不需要再初始化了），需要传入穿山甲广告平台的 appid

```
import React, { useEffect } from 'react';
import {ad} from 'react-native-ad';

function App() {
  useEffect(() => {

    const appid = "";

    // 启动前，初始化Ad
    ad.init({
        appid,
    });

  }, []);
}

```

ad.init 参数参照表：

| 参数名              | 是否必传 | 参数类型 |               说明                |
| ------------------- | :------: | :------: | :-------------------------------: |
| appid               |   必传   |  string  |            广告 APP ID            |
| app                 |   可选   |  string  |             APP 名称              |
| uid                 |   可选   |  string  | 有些 uid 和穿山甲商务有合作的需要 |
| amount              |   可选   |  number  |             奖励数量              |
| reward              |   可选   |  string  |             奖励名称              |
| codeid_reward_video |   可选   |  string  |  需要提前预加载的激励视频广告位   |
| codeid_full_video   |   可选   |  string  |  需要提前预加载的全屏视频广告位   |

## 开屏（Splash）广告的对接及示例

导入 `ad` 模块

**import {ad} from 'react-native-ad';**

调用 ad.loadSplashAd 方法，需要传入穿山甲广告平台的 appid 和 codeid

```
...
const appid = "";
const codeid = "";
ad.loadSplashAd({appid, codeid});
...
```

开屏广告监听回调，调用 `ad.loadSplashAd` 将返回一个对象中包含回调监听方法 `subscribe` 该方法接收两个参数监听事件和监听回调方法包含一个回调参数

| 监听事件  | 回调参数类型 |                  说明                  |
| --------- | :----------: | :------------------------------------: |
| onAdShow  |    string    |  开屏广告开始展示将会回调，返回 true   |
| onAdError |    string    | 开屏广告渲染失败将会回调，返回错误信息 |
| onAdClick |   boolean    |     开屏广告被点击回调，返回 true      |
| onAdSkip  |   boolean    |    用户点击跳过广告回调，返回 true     |
| onAdClose |   boolean    | 开屏广告展示倒计时结束回调，返回 true  |

示例代码：

```
const splashAd = loadSplashAd(appid,codeid);

splashAd.subscribe('onAdShow', e => {
  console.log('广告开始显示监听', e);
});

splashAd.subscribe('onAdError', e => {
  console.log('广告加载出错监听', e);
});

splashAd.subscribe('onAdClose', event => {
  console.log('广告时间结束监听', event);
});

splashAd.subscribe('onAdSkip', i => {
  console.log('用户点击跳过监听', i);
});

splashAd.subscribe('onAdClick', i => {
  console.log('广告被点击监听', i);
});

```

> **可选参数：**
>
> `ad.loadSplashAd({appid, codeid, anim: 'default'});`（开屏 Splash 广告启动动画效果）详细文档请查看：[SplashAd Anim 文档](./splash_anim.md)

更多使用示例代码请查看：[SplashAd](../example/src/Splash.js)

## 激励视频（RewardVideo）广告的对接及示例

导入 `ad` 模块

**import {ad} from 'react-native-ad';**

调用 ad.startRewardVideo 方法，两个必传参数需要传入穿山甲广告平台的 appid 和 codeid

```
...
const appid = "";
const codeid = "";
ad.startRewardVideo(appid, codeid);
...
```

激励视频监听回调，调用 `ad.startRewardVideo` 将返回一个对象中包含回调监听方法 `subscribe` 该方法接收两个参数监听事件和监听回调方法包含一个回调参数

| 监听事件   | 回调参数类型 |                               说明                               |
| ---------- | :----------: | :--------------------------------------------------------------: |
| onAdLoaded |  event: {}   |   广告加载成功回调，返回 [激励视频回调对象](#激励视频回调对象)   |
| onAdError  |  event: {}   |   广告加载失败回调，返回 [激励视频回调对象](#激励视频回调对象)   |
| onAdClose  |  event: {}   |    广告被关闭回调，返回 [激励视频回调对象](#激励视频回调对象)    |
| onAdClick  |  event: {}   | 广告点击查看详情回调，返回 [激励视频回调对象](#激励视频回调对象) |

示例代码：

```
const rewardVideoAd = startRewardVideo(appid,codeid);

rewardVideo.subscribe('onAdLoaded', event => {
console.log('广告加载成功监听', event);
});

rewardVideo.subscribe('onAdError', event => {
console.log('广告加载失败监听', event);
});

rewardVideo.subscribe('onAdClose', event => {
console.log('广告被关闭监听', event);
});

rewardVideo.subscribe('onAdClick', event => {
console.log('广告点击查看详情监听', event);
});

rewardVideo.subscribe('onAdVideoCached', event => {
console.log('激励视频缓存成功监听', event);
});

rewardVideo.subscribe('onVideoComplete', event => {
console.log('激励视频播放失败监听', event);
});

rewardVideo.subscribe('onDownloadActive', event => {
console.log('广告应用下载相关监听', event);
});
```

激励视频除了通过监听事件取得回调还有一个更加稳妥的回调操作获取方式通过 Promise 异步获取用户对激励视频的关键操作，将获取一个 json 数据对应相关的 boolean 值：video_play（视频是否播放完），ad_click（用户是否点击广告），apk_install（用户是否安装广告应用），verify_status（认证激励状态）

示例代码：

```
const rewardVideoAd = startRewardVideo(appid,codeid);

rewardVideo.then(val => {
    console.log('FullVideoAd', val);
});

```

返回数据示例（更多操作可以看具体代码示例）：

```
{
    "video_play":"false",
    "ad_click":"false",
    "apk_install":"false",
    "verify_status":"false"
}
```

#### 激励视频回调对象

> 由于激励视频回调事件情况比较多，这边统一整理了回调对象，回调对象有 2 个值 code 和 message ，数据格式如下所示

```
{
    code: 200,
    message: "激励视频展示成功",
}
```

| code |  回调事件  |        说明         |
| ---- | :--------: | :-----------------: |
| 1001 | onAdError  | 广告位 code id 为空 |
| 1002 | onAdError  |  激励视频加载失败   |
| 1003 | onAdError  | 激励视频还没加载好  |
| 200  | onAdLoaded |    广告加载完毕     |
| 202  | onAdLoaded |  开始展示激励视频   |
| 203  | onAdClick  |  查看激励视频详情   |
| 204  | onAdClose  |    关闭激励视频     |

更多使用示例代码请查看：[RewardVideo](../example/src/RewardVideo.js)

## 全屏视频（FullVideo）广告的对接及示例

导入 `ad` 模块

**import {ad} from 'react-native-ad';**

调用 ad.startFullVideo 方法，需要传入穿山甲广告平台的 appid 和 codeid

```
...
const appid = "";
const codeid = "";
ad.startFullVideo(appid, codeid);
...
```

全屏视频广告回调操作获取方式通过 Promise 异步获取用户对全屏视频的关键操作，将获取一个 json 数据对应相关的 boolean 值：video_play（视频是否播放完），ad_click（用户是否点击广告），apk_install（用户是否安装广告应用），verify_status（认证激励状态）

示例代码：

```
const fullVideoAd = startFullVideo(appid,codeid);

fullVideoAd.then(val => {
    console.log('FullVideoAd', val);
});

```

返回数据示例（更多操作可以看具体代码示例）：

```
{
    "video_play":"false",
    "ad_click":"false",
    "apk_install":"false",
    "verify_status":"false"
}
```

全屏视频监听回调，调用 `ad.startFullVideo` 将返回一个对象中包含回调监听方法 `subscribe` 该方法接收两个参数监听事件和监听回调方法包含一个回调参数

| 监听事件   | 回调参数类型 |         说明         |
| ---------- | :----------: | :------------------: |
| onAdLoaded |  event: {}   |   广告加载成功回调   |
| onAdError  |  event: {}   |   广告加载失败回调   |
| onAdClose  |  event: {}   |    广告被关闭回调    |
| onAdClick  |  event: {}   | 广告点击查看详情回调 |

示例代码：

```
let fullVideo = ad.startFullVideo({appid,codeid});

fullVideo.subscribe('onAdLoaded', (event) => {
  console.log('广告加载成功监听', event);
});

fullVideo.subscribe('onAdError', (event) => {
  console.log('广告加载失败监听', event);
});

fullVideo.subscribe('onAdClose', (event) => {
  console.log('广告被关闭监听', event);
});

fullVideo.subscribe('onAdClick', (event) => {
  console.log('广告点击查看详情监听', event);
});
```

更多使用示例代码请查看：[FullScreenVideo](../example/src/FullScreenVideo.js)

## 信息流（Feed）广告的对接及示例

导入 `ad` 模块

**import {ad} from 'react-native-ad';**

调用 ad.init 方法初始化 SDK（已经初始化过的可以忽略，初始化一次后其他地方可以不需要再初始化了），需要传入穿山甲广告平台的 appid。
需要预加载 Feed AD 有两种方式，通过 `ad.init` 方法使用方法请参照：[初始化 react-native-ad SDK](#初始化-SDK) ，通过提前调用 `ad.loadFeedAd` 方法，该方法参数为广告配置对象 `{ appid, codeid, width }` ，使用示例代码如下：

```
...

React.useEffect(() => {

  const appid = "";
  const codeid = "";
  const width = ""; // feed ad 宽度
  ad.init(appid);

  // 预加载 feed ad
  ad.loadFeedAd({
    appid,
    codeid,
    width,
  }).then(
    (result) => {
      console.log('load feed ad result ', result);
    },
    (reason) => {
      console.log('加载 feed ad 失败理由 ', reason);
    },
  );

}, []);

...
```

> **提示：** 并非必须预加载广告，根据自己需求所定。预加载广告可能提升部分用户体验

使用组件 <ad.Feed /> 显示广告，codeId（ 广告位 ID ）必传，adWidth（ 广告宽度 ）必传

```
<ad.Feed
  codeId="00000000" //必传 广告位 codeid 注意区分 Android 和 IOS
  adWidth={300}
  visible={true}
  onAdLayout={(data) => {
    console.log('Feed 广告加载成功！', data);
  }}
  onAdClose={(data) => {
    console.log('Feed 广告关闭！', data);
  }}
  onAdError={(err) => {
    console.log('Feed 广告加载失败！', err);
  }}
  onAdClick={(val) => {
    console.log('Feed 广告被用户点击！', val);
  }}
/>
```

ad.Feed 参数参照表：

| 参数名     |      参数类型       |                说明                 |
| ---------- | :-----------------: | :---------------------------------: |
| codeId     |       string        |              广告位 ID              |
| adWidth    |       number        |              广告宽度               |
| visible    |       boolean       |     是否显示广告，控制广告显示      |
| onAdLayout | (event: {}) => void | 广告加载成功回调，返回相应回调信息  |
| onAdClose  | (event: {}) => void | Feed 广告关闭回调，返回相应回调信息 |
| onAdError  | (event: {}) => void | 广告加载失败回调，返回相应回调信息  |
| onAdClick  | (event: {}) => void |   广告点击回调，返回相应回调信息    |

更多使用示例代码请查看：[FeedADExample](../example/src/Feed.js)

## 视频信息流（DrawFeed）广告的对接及示例

导入 `ad` 模块

**import {ad} from 'react-native-ad';**

调用 ad.init 方法初始化 SDK（已经初始化过的可以忽略，初始化一次后其他地方可以不需要再初始化了），需要传入穿山甲广告平台的 appid

```
...

React.useEffect(() => {

  const appid = "";

  ad.init(appid);

}, []);

...
```

使用组件 <ad.DrawFeed /> 显示广告，codeId（ 广告位 ID ）必传

```
<ad.DrawFeed
  codeId="00000000" // 广告位 codeid （必传），注意区分 Android 和 IOS
  visible={false} // 是否显示广告，默认值：true
  onAdShow={(msg) => {
    // 广告加载成功回调
    console.log('Draw Feed 广告加载成功！', msg);
  }}
  onAdError={(err) => {
    // 广告加载失败回调
    console.log('Draw Feed 广告加载失败！', err);
  }}
  onAdClick={(val) => {
    // 广告点击回调
    console.log('Draw Feed 广告被用户点击！', val);
  }}
/>
```

ad.DrawFeed 参数参照表：

| 参数名    |  参数类型  |                   说明                   |
| --------- | :--------: | :--------------------------------------: |
| codeId    |   string   |                广告位 ID                 |
| visible   |  boolean   | 是否显示广告，控制广告显示，默认值：true |
| onAdShow  | () => void |    广告开始展示回调，返回相应回调信息    |
| onAdError | () => void |    广告加载失败回调，返回相应回调信息    |
| onAdClick | () => void |      广告点击回调，返回相应回调信息      |

更多使用示例代码请查看：[DrawFeed](../example/src/DrawFeed.js)

## 横幅（Banner）广告的对接及示例

导入 `ad` 模块

**import {ad} from 'react-native-ad';**

调用 ad.init 方法初始化 SDK（已经初始化过的可以忽略，初始化一次后其他地方可以不需要再初始化了），需要传入穿山甲广告平台的 appid

```
...

React.useEffect(() => {

  const appid = "";

  ad.init(appid);

}, []);

...
```

> **Banner 广告待接入，如有需要请催更…**

## 常见问题及模块更新

怎么查看当前版本？执行 `yarn list | grep "react-native-ad"`，将会输出你当前项目安装的 react-native-ad 版本

然后可以使用 npm update 来更新模块

```

\$ npm update react-native-ad

```

或者使用 yarn upgrade 来更新

```

\$ yarn upgrade react-native-ad

```

## 更新日志

- v1.0.0

  - 初始化项目
  - 对接 ad 开屏广告 android 模块

- v1.0.1

  - 规范接口，代码结构
  - 对接 ad 信息流，视频信息流，Banner 广告模块
  - 补全 example 示例

- v1.0.2

  - 升级 Android ad SDK 版本 v3.1.0.0
  - 规范完善 example 示例
  - 对接 ad 激励视频，全屏视频广告模块
  - 完善对接文档整理

- v1.0.4

  - 重构 ios 代码，修复全屏视频唤起
  - 修复完成全部 ios 广告位渲染问题
  - 安卓先重构 layouts 命名

- v1.1.0

  - 同步最新代码
  - 修复全屏视频安卓错误

- v1.1.1

  - 修复重复出现 com.haxibiao.BuildConfig 不兼容问题
  - 暴露 loadFeedAd 方法预加载 Feed 缓存
  - 统一 Feed, DrawFeed 事件名
  - 升级 ios Bytedance-UnionAD pod 到 v3.1.0.5

- v1.1.3

  - 重构激励视频，全屏视频安卓代码的回调
  - 启动激励视频支持几个头条穿山甲的参数 appName, userId, reward, amount 等
  - 优化实例代码，加载 feed ad 返回成功失败回调和理由
  - ios feed 广告支持 adWidth 属性

- v1.1.4
  - 修复开屏广告 onAdShow 回调方法
  - 添加 Android 开屏广告可选动画效果
  - 更新对接文档
