# hxf-byted-ad

#### 安装方法🔨

```
yarn add git+http://auto:hxb332211@code.haxibiao.cn/native/bytedad.git
```

或者

```
npm install -D git+http://auto:hxb332211@code.haxibiao.cn/native/bytedad.git
```

或者在项目package.json的 dependencies下添加一行

```
"hxf-byted-ad":"git+http://auto:hxb332211@code.haxibiao.cn/native/bytedad.git"
```


## 接口说明：🍎

#### 开屏广告🍖🍔🍟

导入`loadSplashAd`模块

**import { loadSplashAd } from 'hxf-byted-ad';**

需要穿山甲广告平台的 appid 和 codeid

```
...
const appid = "";
const codeid = "";
loadSplashAd(appid, codeid);
...
```

开屏广告监听回调，调用 `loadSplashAd` 将返回一个对象中包含回调监听方法 `subscrib` 该方法接收两个参数监听事件和监听回调方法包含一个回调参数
| 监听事件 | 回调参数类型 | 说明 |
|---|---|---|
| onError | string | 开屏广告渲染失败将会回调，返回错误信息 |
| onTimeout | boolean | 开屏广告加载超时回调，返回 true |
| onAdClicked | boolean | 开屏广告被点击回调，返回 true |
| onAdSkip | boolean | 用户点击跳过广告回调，返回 true |
| onAdTimeOver | boolean | 开屏广告展示倒计时结束回调，返回 true |

事例代码：
```
const splashAd = loadSplashAd(appid,codeid);

splashAd.subscrib('onError', e => {
  console.log('广告加载出错监听', e);
});

splashAd.subscrib('onTimeout', event => {
  console.log('广告加载超时监听', event);
});

splashAd.subscrib('onAdTimeOver', event => {
  console.log('广告时间结束监听', event);
});

splashAd.subscrib('onAdSkip', i => {
  console.log('用户点击跳过监听', i);
});

splashAd.subscrib('onAdClicked', i => {
  console.log('广告被点击监听', i);
});

```


### 更新模块

怎么查看当前版本？执行 ` yarn list | grep "hxf-byted-ad" `，将会输出你当前项目安装的 hxf-byted-ad 版本

然后可以使用 npm update 来更新模块
```
$ npm update hxf-byted-ad
```

或者使用 yarn upgrade 来更新
```
$ yarn upgrade hxf-byted-ad
```