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

### 更新模块

怎么查看当前版本？执行 ``` yarn list | grep "hxf-byted-ad" ```，将会输出你当前项目安装的 hxf-byted-ad 版本

然后可以使用 npm update 来更新模块
```
$ npm update hxf-byted-ad
```

或者使用 yarn upgrade 来更新
```
$ yarn upgrade hxf-byted-ad
```