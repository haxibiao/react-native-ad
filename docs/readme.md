# 穿山甲（头条）React Native 广告模块对接文档

> 广告平台地址：<https://partner.oceanengine.com/>

> 广告平台对接文档地址：<https://ad.oceanengine.com/union/media/union/download>

> 当前 Android 集成广告平台 SDK 版本：v3.1.0.0

> 当前 IOS 集成广告平台 SDK 版本：???

文档目录：

-   [穿山甲（头条）React Native 广告模块对接文档](#穿山甲头条react-native-广告模块对接文档) - [创建获取 appId 和广告位 codeId](#创建获取-appid-和广告位-codeid) - [获取穿山甲 appId](#获取穿山甲-appid) - [获取穿山甲 codeId](#获取穿山甲-codeid) - [安装配置 react-native-ad 模块](#安装配置-react-native-ad-模块) - [开屏（Splash）广告的对接及示例](#开屏splash广告的对接及示例) - [激励视频（RewardVideo）广告的对接及示例](#激励视频rewardvideo广告的对接及示例) - [激励视频回调对象](#激励视频回调对象) - [全屏视频（FullVideo）广告的对接及示例](#全屏视频fullvideo广告的对接及示例) - [信息流（Feed）广告的对接及示例](#信息流feed广告的对接及示例) - [视频信息流（DrawFeed）广告的对接及示例](#视频信息流drawfeed广告的对接及示例) - [横幅（Banner）广告的对接及示例](#横幅banner广告的对接及示例)

## 创建获取 appId 和广告位 codeId

> 穿山甲广告平台，首先是要注册企业账号，要对接广告的话首先得先到应用商店上架

#### 获取穿山甲 appId

![企业微信截图_fa891d04-2ef9-4ce4-b9cd-c407e590f975.png](http://cos.haxibiao.com/images/5f05cae38e8ea.png)

在广告平台我们可以创建相应的应用，Android 和 IOS 可以分开创建，命名方式建议（xxxx_android or xxxx_ios）创建后经过平台审核通过后才可以创建广告位 codeId

#### 获取穿山甲 codeId

在获取 codeId 前，我们先了解一下广告类型：信息流（Feed）广告，横幅（Banner）广告，全屏视频（FullVideo）广告，开屏（Splash）广告，激励视频（RewardVideo）广告，视频信息流（DrawFeed）广告

![屏幕快照 2020-07-08 下午9.39.36.png](http://cos.haxibiao.com/images/5f05cca0df84b.png)

不同的广告类型对应不同 codeId 可以通过广告平台后台创建。

## 安装配置 react-native-ad 模块

可以使用 yarn 或者 npm 进行安装

```
yarn add git+http://auto:hxb332211@code.haxibiao.cn/native/bytedad.git
```

或者

```
npm install -D git+http://auto:hxb332211@code.haxibiao.cn/native/bytedad.git
```

或者在项目 package.json 的 dependencies 下添加一行

```
"hxf-byted-ad":"git+http://auto:hxb332211@code.haxibiao.cn/native/bytedad.git"
```

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

| 监听事件  | 回调参数类型 |                  说明                   |
| --------- | :----------: | :-------------------------------------: |
| onAdShow  |    string    |   开屏广告开始展示将会回调，返回 true   |
| onError   |    string    | 开屏广告渲染失败将会回调，返回错误信息  |
| onTimeout |   boolean    | 开屏广告加载超时回调(仅安卓)，返回 true |
| onAdClick |   boolean    |      开屏广告被点击回调，返回 true      |
| onAdSkip  |   boolean    |     用户点击跳过广告回调，返回 true     |
| onAdClose |   boolean    |  开屏广告展示倒计时结束回调，返回 true  |

示例代码：

```
const splashAd = loadSplashAd(appid,codeid);

splashAd.subscribe('onAdShow', e => {
  console.log('广告开始显示监听', e);
});

splashAd.subscribe('onError', e => {
  console.log('广告加载出错监听', e);
});

splashAd.subscribe('onTimeout', event => {
  console.log('广告加载超时监听', event);
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

更多使用示例代码请查看：[SplashAd](../example/src/SplashAd.js)

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

| 监听事件         | 回调参数类型 |                               说明                               |
| ---------------- | :----------: | :--------------------------------------------------------------: |
| onAdLoaded       |  event: {}   |   广告加载成功回调，返回 [激励视频回调对象](#激励视频回调对象)   |
| onAdError        |  event: {}   |   广告加载失败回调，返回 [激励视频回调对象](#激励视频回调对象)   |
| onAdClose        |  event: {}   |    广告被关闭回调，返回 [激励视频回调对象](#激励视频回调对象)    |
| onAdClick        |  event: {}   | 广告点击查看详情回调，返回 [激励视频回调对象](#激励视频回调对象) |
| onAdVideoCached  |  event: {}   | 激励视频缓存成功回调，返回 [激励视频回调对象](#激励视频回调对象) |
| onVideoComplete  |  event: {}   | 激励视频播放失败回调，返回 [激励视频回调对象](#激励视频回调对象) |
| onDownloadActive |  event: {}   | 广告应用下载相关回调，返回 [激励视频回调对象](#激励视频回调对象) |

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

| code |     回调事件     |              说明              |
| ---- | :--------------: | :----------------------------: |
| 1001 |    onAdError     |      广告位 code id 为空       |
| 1002 |    onAdError     |        激励视频加载失败        |
| 1003 |    onAdError     |       激励视频还没加载好       |
| 200  |    onAdLoaded    |          广告加载完毕          |
| 201  | onAdVideoCached  |        激励视频缓存成功        |
| 202  |    onAdLoaded    |        开始展示激励视频        |
| 203  |    onAdClick     |        查看激励视频详情        |
| 204  |    onAdClose     |          关闭激励视频          |
| 205  | onVideoComplete  |        激励视频播放失败        |
| 300  | onDownloadActive |        开始下载广告应用        |
| 301  | onDownloadActive |   下载暂停，点击下载区域继续   |
| 302  | onDownloadActive | 下载完成，点击下载区域重新下载 |
| 303  | onDownloadActive |   安装完成，点击下载区域打开   |
| 304  | onDownloadActive | 下载失败，点击下载区域重新下载 |

更多使用示例代码请查看：[TTRewardVideoDemo](../example/src/TTRewardVideoDemo.js)

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

更多使用示例代码请查看：[FullScreenVideo](../example/src/FullScreenVideo.js)

## 信息流（Feed）广告的对接及示例

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

使用组件 <ad.FeedAd /> 显示广告，codeId（ 广告位 ID ）必传，adWidth（ 广告宽度 ）必传

```
<ad.FeedAd
  codeId="00000000" // 广告位 codeid （必传)
  visible={true}
  adWidth={300}
  onLoad={smg => {
    // 广告加载成功回调
    console.log('头条 Feed 广告加载成功！', smg);
  }}
  onError={err => {
    // 广告加载失败回调
    console.log('头条 Feed 广告加载失败！', err);
  }}
  onClick={val => {
    // 广告点击回调
    console.log('头条 Feed 广告被用户点击！', val);
  }}
/>
```

ad.FeedAd 参数参照表：

| 参数名  |  参数类型  |                说明                |
| ------- | :--------: | :--------------------------------: |
| codeId  |   string   |             广告位 ID              |
| adWidth |   number   |              广告宽度              |
| visible |  boolean   |     是否显示广告，控制广告显示     |
| onLoad  | () => void | 广告加载成功回调，返回相应回调信息 |
| onError | () => void | 广告加载失败回调，返回相应回调信息 |
| onClick | () => void |   广告点击回调，返回相应回调信息   |

更多使用示例代码请查看：[FeedAd](../example/src/FeedAd.js)

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

使用组件 <ad.DrawFeedAd /> 显示广告，codeId（ 广告位 ID ）必传

```
<ad.DrawFeedAd
  codeId="00000000" // 广告位 codeid （必传），注意区分 Android 和 IOS
  isExpress={false} // isExpress 用来区分是否用原生方式渲染（非必传），默认值：false
  onLoad={smg => {
    // 广告加载成功回调
    console.log('头条 Draw Feed 广告加载成功！', smg);
  }}
  onError={err => {
    // 广告加载失败回调
    console.log('头条 Draw Feed 广告加载失败！', err);
  }}
  onClick={val => {
    // 广告点击回调
    console.log('头条 Draw Feed 广告被用户点击！', val);
  }}
/>
```

ad.DrawFeedAd 参数参照表：

| 参数名    |  参数类型  |                             说明                              |
| --------- | :--------: | :-----------------------------------------------------------: |
| codeId    |   string   |                           广告位 ID                           |
| isExpress |  boolean   | isExpress 用来区分是否用原生方式渲染（非必传），默认值：false |
| onLoad    | () => void |              广告加载成功回调，返回相应回调信息               |
| onError   | () => void |              广告加载失败回调，返回相应回调信息               |
| onClick   | () => void |                广告点击回调，返回相应回调信息                 |

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

-   v1.0.0

    -   初始化项目
    -   对接 ad 开屏广告 android 模块

-   v1.0.1

    -   规范接口，代码结构
    -   对接 ad 信息流，视频信息流，Banner 广告模块
    -   补全 example 示例

-   v1.0.2
    -   升级 Android ad SDK 版本 v3.1.0.0
    -   规范完善 example 示例
    -   对接 ad 激励视频，全屏视频广告模块
    -   完善对接文档整理
```
