# [RxTool](https://tamsiree.com/RxTool/)

[![tamsiree.com](https://img.shields.io/badge/%E6%8A%80%E6%9C%AF%E5%8D%9A%E5%AE%A2-Tamsiree-brightgreen.svg)](https://tamsiree.com/)  [![Stars](https://badgen.net/github/stars/tamsiree/RxTool)](https://ghbtns.com/github-btn.html?user=tamsiree&repo=rxtool&type=star)  [![RxTool](https://jitpack.io/v/tamsiree/RxTool.svg)](https://jitpack.io/#tamsiree/RxTool)  

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)  [![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)  [![](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html)  [![API](https://img.shields.io/badge/API-21%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=21)  [![Gradle-5.6.4](https://img.shields.io/badge/Gradle-5.6.4-brightgreen.svg)](https://img.shields.io/badge/Gradle-5.6.4-brightgreen.svg)
  
![image](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/DeskTop/miku.png)

>	所谓：工欲善其事必先利其器！
>
>	`RxTool` 是 `Android` 开发过程经常需要用到各式各样的工具类集合，虽然大部分只需谷歌/百度一下就能找到。
>
>	但是有时候急需使用却苦苦搜寻不到，于是整理了自己平常用到的工具类，以便以后的使用。


---

# 我的运行环境

> Android Studio 4.2.1
> 
> Build #AI-202.7660.26.42.7351085, built on May 11, 2021
> 
> Runtime version: 11.0.8+10-b944.6842174 amd64
> 
> VM: OpenJDK 64-Bit Server VM by N/A
> 
> Windows 10 10.0
> 
> GC: G1 Young Generation, G1 Old Generation
>  
> Memory: 1280M
> 
> Cores: 20
> 
> Registry: external.system.auto.import.disabled=true
> 
> Non-Bundled Plugins: com.intellij.marketplace, com.thoughtworks.gauge, dev.polek.adbwifi, net.vektah.codeglance2, org.jetbrains.kotlin, org.intellij.plugins.markdown
>
> targetSdkVersion 29
>
> [gradle-wrapper.properties文件内] distributionUrl 6.7.1
>
> [build.gradle文件内] gradle 4.2.1


---

# 如何使用它


## Step 1.先在 build.gradle(Project:XXXX) 的 repositories 添加:

```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

## Step 2.然后在 build.gradle(Module:app) 的 dependencies 添加:

```gradle
dependencies {
  //基础工具库
  implementation 'com.github.tamsiree.RxTool:RxKit:2.6.3'
  //UI库
  implementation 'com.github.tamsiree.RxTool:RxUI:2.6.3'
  //相机库
  implementation 'com.github.tamsiree.RxTool:RxCamera:2.6.3'
  //功能库（Zxing扫描与生成二维码条形码）
  implementation 'com.github.tamsiree.RxTool:RxFeature:2.6.3'
  //ArcGis For Android工具库（API：100.1以上版本）
  implementation 'com.github.tamsiree.RxTool:RxArcGisKit:2.6.3'
  //支付模块(支付宝 微信)[暂为待优化模块，谨慎]
  implementation 'com.github.tamsiree.RxTool:RxPay:2.6.3'
}
```

## Step 3.在Application中初始化


> (注：2.0.0 以后版本是分多模块的版本)
>
> (注：2.4.0 版本全面升级到 AndroidX)
>
> (注：2.4.2 版本更改了库的包名)

```java
RxTool.init(this);
```

# API使用文档

## 可以参考文档来调用相对应的API，欢迎指教
- [**[点我看文档]**](https://www.tamsiree.com/%E7%BC%96%E7%A8%8B%E8%AF%AD%E8%A8%80/Android/RxTool/RxTool/)
- [**[点我看文档]**](https://tamsiree.github.io/%E7%BC%96%E7%A8%8B%E8%AF%AD%E8%A8%80/Android/RxTool/RxTool/)
- 备选 [点我看文档](https://github.com/tamsiree/RxTool/wiki/RxTool-Wiki)

# 更新日志
> 因为自己用的关系，更新的频率可能有点快

|  VERSION  |  Description  |
| :-------: | ------------- |
|   2.6.3   | 修复自定义View的引用异常 |
|   2.6.2   | 更新 RxUI、RxKit、RxPay 模块 |
|   2.6.1   | 更新 RxUI、RxKit 模块，新增 测试数据生成工具 |
|   **2.6.0**   | 整体转换为kotlin |
|   2.5.9   | 更新 RxUI 模块，新增 TBlurVIew，更新了 RxActivityTool、RxDialogChooseImage |
|   2.5.8   | 更新 RxUI 模块，更新了 RxDialogChooseImage |
|   2.5.7   | 模块转换为kotlin |
|   2.5.6   | 更新项目配置 |
|   2.5.5   | 更新 RxUI 模块，更新了 RxActivityTool 工具类 |
|   2.5.4   | 更新 RxUI 模块，更新了 RxTitle、RxDialogDate |
|   2.5.3   | 更新 RxUI 模块，新增TStepperIndicator步骤指示器，删除 CardView 依赖 |
|   2.5.2   | 更新 RxUI 模块，新增TIndicator指示器 |
|   2.5.1   | 更新 RxUI 模块，新增若干自定义View |
|   2.5.0   | 优化所有自定义View，增加可预览功能 |
|   2.4.9   | 更新项目配置文件，模块添加kotlin支持 |
|   2.4.8   | 更新 RxKit 模块，更新了 RxCrashTool 工具类 |
|   2.4.7   | 更新 RxUI 模块，更新了 Fragment 工具类 |
|   2.4.6   | 更新 RxKit 模块，更新了 RxCrashTool 工具类，自定义崩溃模块 |
|   2.4.5   | 更新 RxUI 模块，新增 TabLayout、TUnReadView 自定义View |
|   2.4.4   | 更新 RxUI 模块，RxDialogWheelYearMonthDay 更名为 RxDialogDate  |
|   2.4.3   | 优化 RxTitle 控件 |
|   **2.4.2**   | 更改库的包名，完善若干已知BUG  |
|   2.4.1   | 完善 RxArcGisKit 模块  |
|   **2.4.0**   | **`全面升级到 Android X`**:<br>修复 RxToast 在 Android 9 上,连点只弹出一次的问题<br>修复 二维码扫描框的焦点偏离 问题<br>添加 生成二维码LOGO 功能<br>添加 RxQRCode 的空白边界设置方法<br>更新若干工具类  |
|   2.3.9   | 完善 RxFeature 模块  |
|   2.3.8   | 优化 RxFeature 模块  |
|   2.3.7   | 更新 RxFeature 模块  |
|   2.3.6   | 更新 RxFeature 模块，优化 RxUI 模块  |
|   2.3.5   | 优化 RxDataTool 模块  |
|   2.3.4   | 完善 RxKit 模块  |
|   2.3.3   | 更新 RxDataTool 模块  |
|   2.3.2   | 优化 RxKit 模块 |
|   2.3.1   | 更新 RxUI 模块的 WaveSideBarView |
|   2.3.0   | 优化 RxCamera 模块 |
|   2.2.9   | 更新 RxUI 模块 |
|   2.2.8   | 修复配置文件 |
|   2.2.7   | 新增适配 dimens 文件<br>适配平板等各种屏幕大小的设备 |
|   2.2.6   | 更新 RxMapScaleView 及资源文件 |
|   2.2.5   | 更新 RxCameraView <br>修复部分设备不支持16:9分辨率崩溃问题 |
|   2.2.4   | 更新数据处理工具 |
|   2.2.3   | 调整相机分辨率大小 |
|   2.2.2   | 整理配置文件 |
|   2.2.1   | 增加若干 Shape 资源 |
|   2.2.0   | 增加 ArcGis 坐标系换算方法(投影坐标系、GPS坐标系、设备屏幕坐标系) |
|   2.1.9   | 更新 RxAutoImageView 的屏幕适配大小 |
|   2.1.8   | 更新 RxCameraView 的参数与算法 |
|   2.1.7   | 新增 ArcGis 关于地图精准定位与行程轨迹的实现方法 |
|   2.1.6   | 更新 zip4j 压缩算法 |
|   2.1.5   | RxLocationTool 新增 GPS坐标转百度坐标 方法 |
|   2.1.4   | 新增 ArcGis 若干工具 |
|   2.1.3   | 更新 Gps 移动定位算法 |
|   2.1.2   | 优化 ArcGis 工具类 |
|   2.1.1   | 更新 GPS 定位工具类<br>更新配置文件 |
|   2.1.0   | 更新绘制文字与图片工具 |
|   2.0.9   | 调整安卓各版本下的相机适配 |
|   2.0.8   | 优化相机控件模块 |
|   2.0.7   | 新增相机控件模块 |
|   2.0.6   | 更新 ArcGis 工具<br>更新颜色资源 |
|   2.0.5   | 新增 ArcGis 地图比例尺控件<br>相机工具的优化 |
|   2.0.4   | 降低模块之间的耦合性<br>ArcGisMap 工具的优化 |
|   2.0.3   | 更新扫描二维码 Demo<br>更新日期选择 Dialog |
|   2.0.2   | 更新支付宝 SDK，新增支付宝支付 DEMO <br>更新相机工具 |
|   2.0.1   | 新增(高德/百度)地图导航工具<br>新增 ArcGis 工具类 |
| __2.0.0__ | 重构成多模块 |

# Demo介绍

## RxPhotoTool 操作 UCrop 裁剪图片

| 展示头像 | 选择头像 | 裁剪头像 |
| :----------: | :-------------: | :-------------: |
| ![展示头像](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_1.jpg) | ![选择头像](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_8.jpg) | ![裁剪头像](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_9.jpg) |

## 二维码与条形码的扫描与生成

| 扫描二维码 | 生成二维码 | 扫描条形码 |
| ---------- | ------------- | ------------- |
| ![扫描二维码](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_2.jpg) | ![生成二维码](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_3.jpg) | ![扫描条形码](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_10.jpg) |


## 常用的Dialog展示

| 确认弹窗 | 确认取消弹窗 | 输入框弹窗 |
| :----------: | :-------------: | :-------------: |
| ![确认弹窗](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_5.png) | ![确认取消弹窗](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_6.png) | ![输入框弹窗](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_7.png) |
| 选择日期弹窗 | 形状加载弹窗 | Acfun加载弹窗 |
| ![选择日期弹窗](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_11.png) | ![形状加载弹窗](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_12.png) | ![Acfun加载弹窗](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_13.png) |

## 其他功能展示

| WebView的封装（可播放视频） | RxTextTool操作Demo | RxToast的展示使用 |
| :----------: | :-------------: | :-------------:|
| ![WebView的封装（可播放视频）](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_14.png) | ![RxTextTool操作Demo](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_15.png) | ![RxToast的展示使用](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_18.png)|
| 进度条的艺术 | 网速控件 | 联系人侧边栏快速导航 |
| ![进度条的艺术](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_16.png) | ![网速控件](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_17.png) | ![联系人侧边栏快速导航](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_22.png)|
| 图片的缩放艺术 | 蛛网控件 | 仿斗鱼验证码控件 |
| ![图片的缩放艺术](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_19.png) | ![蛛网控件](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_20.png) | ![仿斗鱼验证码控件](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Picture/RxTool/screenshot_21.png)|

## DEMO 与 打赏

| Demo | 微信赞助 | 支付宝赞助 |
| :----------: | :----------: | :----------: |
| 快下载Demo运行试试吧<br>只展示了部分UI功能<br>功能性功能去源码里探索吧|  如果你帮助到了你<br>可以点右上角"Star"支持一下<br> 谢谢！^_^<br>你也还可以扫描下面的二维码打赏鼓励一下~ <br>请作者喝一杯咖啡。| 如果在捐赠留言中备注名称<br>将会被记录到列表中~ <br>如果你也是github开源作者<br>捐赠时可以留下github项目地址或者个人主页地址<br>链接将会被添加到列表中起到互相推广的作用 |
| ![RxTool](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Function/qrcode_apk.png) |  ![微信](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Function/qrcode_wechat.jpg) |   ![支付宝](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Function/qrcode_alipay.jpg) |
|扫描二维码 <br> 或者 <br> [点击下载](https://cdn.jsdelivr.net/gh/Tamsiree/Assets@master/Apk/RxTool.apk) |[捐赠名单](https://tamsiree.com/TechnicalResearch/Android/RxTool/Contributor)<br>备选[捐赠名单](https://tamsiree.com/RxTool/Contributors) | 闲聊群 <br><br>  ![技术的深度探索与论证](https://img.shields.io/badge/QQ%E7%BE%A4-435644020-brightgreen.svg) <br>[点击入群](https://shang.qq.com/wpa/qunwpa?idkey=a14a650c50413f43d5bb0399f5b6617a2cd09866ae09c5a1d7f3e0ba33962bae)<br>


---

> 欢迎大家共同参与维护这个仓库，有什么好的想法或者代码优化可以提交 `pull request`

---

# License

[反 996 许可证](#译文)  
```LICENSE
Copyright (c) <2016><Tamsiree>

"Anti 996" License Version 1.0 (Draft)

Permission is hereby granted to any individual or legal entity
obtaining a copy of this licensed work (including the source code,
documentation and / or related items, hereinafter collectively referred to as the "licensed work"), free of charge, to deal with the licensed work for any purpose, including without limitation, the rights to use, reproduce, modify, prepare derivative works of, distribute, publish and sublicense the licensed work, subject to the following conditions:

1. The individual or the legal entity must conspicuously display,
without modification, this License and the notice on each redistributed or derivative copy of the Licensed Work.

2. The individual or the legal entity must strictly comply with all
applicable laws, regulations, rules and standards of the jurisdiction relating to labor and employment where the individual is physically located or where the individual was born or naturalized; or where the legal entity is registered or is operating (whichever is stricter). In case that the jurisdiction has no such laws, regulations, rules and standards or its laws, regulations, rules and standards are unenforceable, the individual or the legal entity are required to comply with Core International Labor Standards.

3. The individual or the legal entity shall not induce, suggest or force
its employee(s), whether full-time or part-time, or its independent
contractor(s), in any methods, to agree in oral or written form, to
directly or indirectly restrict, weaken or relinquish his or her
rights or remedies under such laws, regulations, rules and standards
relating to labor and employment as mentioned above, no matter whether
such written or oral agreements are enforceable under the laws of the
said jurisdiction, nor shall such individual or the legal entity
limit, in any methods, the rights of its employee(s) or independent
contractor(s) from reporting or complaining to the copyright holder or
relevant authorities monitoring the compliance of the license about
its violation(s) of the said license.

THE LICENSED WORK IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE COPYRIGHT HOLDER BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN ANY WAY CONNECTION WITH THE
LICENSED WORK OR THE USE OR OTHER DEALINGS IN THE LICENSED WORK.
```

## 译文
```LICENSE
版权所有（c）<2016><Tamsiree>

反996许可证版本1.0

在符合下列条件的情况下，特此免费向任何得到本授权作品的副本（包括源代码、文件和/或相关内容，以
下统称为“授权作品”）的个人和法人实体授权：被授权个人或法人实体有权以任何目的处置授权作品，包括
但不限于使用、复制，修改，衍生利用、散布，发布和再许可：

1. 个人或法人实体必须在许可作品的每个再散布或衍生副本上包含以上版权声明和本许可证，不得自行修
改。

2. 个人或法人实体必须严格遵守与个人实际所在地或个人出生地或归化地、或法人实体注册地或经营地（
以较严格者为准）的司法管辖区所有适用的与劳动和就业相关法律、法规、规则和标准。如果该司法管辖区
没有此类法律、法规、规章和标准或其法律、法规、规章和标准不可执行，则个人或法人实体必须遵守国际
劳工标准的核心公约。

3. 个人或法人不得以任何方式诱导、暗示或强迫其全职或兼职员工或其独立承包人以口头或书面形式同意
直接或间接限制、削弱或放弃其所拥有的，受相关与劳动和就业有关的法律、法规、规则和标准保护的权利
或补救措施，无论该等书面或口头协议是否被该司法管辖区的法律所承认，该等个人或法人实体也不得以任
何方法限制其雇员或独立承包人向版权持有人或监督许可证合规情况的有关当局报告或投诉上述违反许可证
的行为的权利。

该授权作品是"按原样"提供，不做任何明示或暗示的保证，包括但不限于对适销性、特定用途适用性和非侵
权性的保证。在任何情况下，无论是在合同诉讼、侵权诉讼或其他诉讼中，版权持有人均不承担因本软件或
本软件的使用或其他交易而产生、引起或与之相关的任何索赔、损害或其他责任。
```


## 🌐 Web Resources & Interactive Index
- [CRAFT BLOCK WORLD BUILDING](https://eduquests.netlify.app/craft-block-world-building.html)
- [CATEGORY FPS 3](https://eduquests.pages.dev/category-fps-3.html)
- [WEDNESDAY ADDAMS BEAUTY SALON](https://quizzesarena.onrender.com/wednesday-addams-beauty-salon.html)
- [BLUE MUSHROOM CAT RUN](https://eduquests.pages.dev/blue-mushroom-cat-run.html)
- [CATEGORY STICKMAN 2](https://eduquests.pages.dev/category-stickman-2.html)
- [CATEGORY ART](https://brainquests.pages.dev/category-art.html)
- [BLOCK CRAFT 3D](https://eduquestspt.pages.dev/block-craft-3d.html)
- [TERMS](https://ilearnworld.pages.dev/terms.html)
- [PUSH TO GO](https://eduquestses.pages.dev/push-to-go.html)
- [PRIVACY](https://cryptotify.web.app/privacy.html)
- [SNAKE MASTERS](https://brainquests.pages.dev/snake-masters.html)
- [LAST TO LEAVE CIRCLE OBBY](https://eduquestsfr.pages.dev/last-to-leave-circle-obby.html)
- [CATEGORY THINKY 3](https://brainquests.pages.dev/category-thinky-3.html)
- [WORLD CUP SOCCER CAPS](https://eduquestspt.pages.dev/world-cup-soccer-caps.html)
- [MAGIC AND WIZARDS MAHJONG](https://ieduquests.web.app/magic-and-wizards-mahjong.html)
- [SKY MAZE CHALLENGE](https://learnaction.netlify.app/sky-maze-challenge.html)
- [HAMSTERCYCLE](https://learnaction.netlify.app/hamstercycle.html)
- [MY FARM](https://eduquestses.pages.dev/my-farm.html)
- [INDEX28](https://brainquests.pages.dev/index28.html)
- [FIX THE HOOF](https://eduquestspt.pages.dev/fix-the-hoof.html)
- [CARJAMCOLOR](https://eduquestsfr.pages.dev/carjamcolor.html)
- [UFO ATTACK](https://welearnaction.onrender.com/ufo-attack.html)
- [SWAT FORCE VS TERRORISTS](https://brainquests.pages.dev/swat-force-vs-terrorists.html)
- [MOJICON GARDEN JIGSOLITAIRE](https://eduquestsfr.pages.dev/mojicon-garden-jigsolitaire.html)
- [CATEGORY CUTE62](https://brainquests.pages.dev/category-cute62.html)
- [FOOTBALL HEADS 2025](https://ieduquests.web.app/football-heads-2025.html)
- [I AM SECURITY](https://brainquests.pages.dev/i-am-security.html)
- [WORLD SOLITAIRE TRIPEAKS ](https://eduquestspt.pages.dev/world-solitaire-tripeaks-.html)
- [PLANET MERGE](https://eduquestsfr.pages.dev/planet-merge.html)
- [MINI GOLF SAGA](https://welearnaction.onrender.com/mini-golf-saga.html)
- [OBBY PRISON CRAFT ESCAPE](https://eduquestsfr.pages.dev/obby-prison-craft-escape.html)
- [COOKING STORIES FUN CAFE GAME](https://eduquestsfr.pages.dev/cooking-stories-fun-cafe-game.html)
- [AMERICAN BLOCK SNIPER ONLINE](https://learnaction.netlify.app/american-block-sniper-online.html)
- [UNSCREW WOOD PUZZLE](https://learnaction.netlify.app/unscrew-wood-puzzle.html)
- [CATEGORY FPS174](https://learnaction.github.io/category-fps174.html)
- [CATEGORY CASUAL 6](https://brainquests.pages.dev/category-casual-6.html)
- [MEGA RAMPS ULTIMATE CAR RACES](https://brainquests.pages.dev/mega-ramps-ultimate-car-races.html)
- [SPIDER EVOLUTION](https://learnaction.netlify.app/spider-evolution.html)
- [MY FARM LIFE](https://eduquestsfr.pages.dev/my-farm-life.html)
- [SUPER NINJA BALLOON](https://eduquests.pages.dev/super-ninja-balloon.html)
- [CLOSED CITY](https://eduquests.pages.dev/closed-city.html)
- [KITTY SCRAMBLE](https://eduquestsfr.pages.dev/kitty-scramble.html)
- [DICTATOR SIMULATOR 1984](https://brainquests.pages.dev/dictator-simulator-1984.html)
- [BYEPASSHUB](https://eduquestses.pages.dev/byepasshub.html)
- [INDEX9](https://eduquestkr.pages.dev/index9.html)
- [I AM SECURITY](https://learnaction.netlify.app/i-am-security.html)
- [OTU](https://welearnaction.onrender.com/otu.html)
- [CATEGORY POOL](https://eduquests.pages.dev/category-pool.html)
- [BRAIN TEST IQ CHALLENGE 2](https://ieduquests.web.app/brain-test-iq-challenge-2.html)
- [HEXA SORT WINTER EDITION](https://eduquestsfr.pages.dev/hexa-sort-winter-edition.html)
- [ITALIAN BRAINROT DRAG MERGE PUZZLE](https://brainquests.pages.dev/italian-brainrot-drag-merge-puzzle.html)
- [ZOMBIE ROAD SHOOTER WITH DESTRUCTION](https://brainquests.pages.dev/zombie-road-shooter-with-destruction.html)
- [PICTURES RIDDLE](https://welearnaction.onrender.com/pictures-riddle.html)
- [BOLTS AND NUTS PUZZLE](https://eduquestsfr.pages.dev/bolts-and-nuts-puzzle.html)
- [PERFECT CAKE MAKER](https://eduquestsfr.pages.dev/perfect-cake-maker.html)
- [BUBBLE SHOOTER PRO 4](https://ieduquests.web.app/bubble-shooter-pro-4.html)
- [FIND STEAL BRAINROT 67 GAME](https://eduquestsfr.pages.dev/find-steal-brainrot-67-game.html)
- [CUBE STORIES ESCAPE](https://welearnaction.onrender.com/cube-stories-escape.html)
- [TERMS](https://thcskq.github.io/terms.html)
- [ZOMBIE CHASE](https://brainquests.pages.dev/zombie-chase.html)
- [POP THEM](https://eduquestsfr.pages.dev/pop-them.html)
- [PINTURILLO](https://brainquests.pages.dev/pinturillo.html)
- [COLOR BLOCK SORT](https://brainquests.pages.dev/color-block-sort.html)
- [HOSTAGE FISHES](https://ieduquests.web.app/hostage-fishes.html)
- [WORD CONNECT PUZZLE](https://brainquests.pages.dev/word-connect-puzzle.html)
- [ARROW OUT AND LINKER](https://ieduquests.web.app/arrow-out-and-linker.html)
- [HEX PLANET IDLE](https://eduquestsfr.pages.dev/hex-planet-idle.html)
- [EGG ADVENTURE MIRROR WORLD](https://eduquestspt.pages.dev/egg-adventure-mirror-world.html)
- [HIDDEN OBJECTS ISLAND](https://brainquests.pages.dev/hidden-objects-island.html)
- [MINI GRAND THEFT CITY](https://welearnaction.onrender.com/mini-grand-theft-city.html)
- [UNBLOCK IT 3D](https://eduquests.pages.dev/unblock-it-3d.html)
- [FLAG PUZZLE JAM COLLECT FLAGS](https://brainquests.pages.dev/flag-puzzle-jam-collect-flags.html)
- [HALLOWEEN MAKEUP TRENDS](https://welearnaction.onrender.com/halloween-makeup-trends.html)
- [SORTING BALLS](https://eduquestsfr.pages.dev/sorting-balls.html)
- [MERGE HAVEN](https://eduquestspt.pages.dev/merge-haven.html)
- [MINI GOLF SAGA](https://eduquests.pages.dev/mini-golf-saga.html)
- [MERGE BLOCKS 2048](https://eduquestspt.pages.dev/merge-blocks-2048.html)
- [JEWEL LINK](https://eduquests.pages.dev/jewel-link.html)
- [FIX DA BRAINROT](https://ieduquests.web.app/fix-da-brainrot.html)
- [BUBBLE POP LEGEND](https://eduquestses.pages.dev/bubble-pop-legend.html)
- [MINEBLOCKS 3D MAZE](https://brainquests.pages.dev/mineblocks-3d-maze.html)
- [IDLE SUPERMARKET TYCOON](https://eduquestses.pages.dev/idle-supermarket-tycoon.html)
- [SINGLE STROKE LINE DRAW](https://eduquestsfr.pages.dev/single-stroke-line-draw.html)
- [COLOR 3D BUMP IT UP](https://eduquests.pages.dev/color-3d-bump-it-up.html)
- [SUPER SLIME BLACK HOLE](https://eduquestspt.pages.dev/super-slime-black-hole.html)
- [CATEGORY CASUAL 3](https://learnaction.netlify.app/category-casual-3.html)
- [3D KID SLIDING PUZZLE](https://learnaction.netlify.app/3d-kid-sliding-puzzle.html)
- [INDEX20](https://eduquestkr.pages.dev/index20.html)
- [WOLF LIFE SIMULATOR](https://eduquestses.pages.dev/wolf-life-simulator.html)
- [MERGE SHOOTER](https://learnaction.netlify.app/merge-shooter.html)
- [SNAKES](https://brainquests.pages.dev/snakes.html)
- [INDEX32](https://eduquestspt.pages.dev/index32.html)
- [EMOJI CHALLENGE](https://eduquestsfr.pages.dev/emoji-challenge.html)
- [BLOCKY ARCHER RUN](https://eduquests.pages.dev/blocky-archer-run.html)
- [INDEX10](https://eduquestkr.pages.dev/index10.html)
- [INDEX5](https://welearnaction.onrender.com/index5.html)
- [MONSTER SQUAD RUSH](https://eduquestsfr.pages.dev/monster-squad-rush.html)
- [PAPERLY PAPER PLANE ADVENTURE](https://welearnaction.onrender.com/paperly-paper-plane-adventure.html)
- [ZEN SOLITAIRE](https://eduquestses.pages.dev/zen-solitaire.html)
- [MAGES SECRET](https://welearnaction.onrender.com/mages-secret.html)
- [MUKI WIZARD](https://eduquests.pages.dev/muki-wizard.html)
- [OBBY CHAMPIONS](https://eduquestses.pages.dev/obby-champions.html)
- [COLOR WOOD ANIMAL JAM](https://eduquestspt.pages.dev/color-wood-animal-jam.html)
- [MERMAIDCORE AESTHETICS](https://ieduquests.web.app/mermaidcore-aesthetics.html)
- [TRICKY CHALLENGES MINI GAMES](https://ieduquests.web.app/tricky-challenges-mini-games.html)
- [CONSTRUCTION SIMULATOR](https://eduquests.pages.dev/construction-simulator.html)
- [CUTE COLORING GAMES](https://eduquestsfr.pages.dev/cute-coloring-games.html)
- [BOOM STICK BAZOOKA](https://brainquests.pages.dev/boom-stick-bazooka.html)
- [HIDE MOODENG HIPPO](https://brainquests.pages.dev/hide-moodeng-hippo.html)
- [GOOBER SHOT](https://learnaction.netlify.app/goober-shot.html)
- [ROBBY BOMBERMAN](https://brainquests.pages.dev/robby-bomberman.html)
- [BUBBLE SHOOTER BLAST](https://brainquests.pages.dev/bubble-shooter-blast.html)
- [TRICKY ARROW 2](https://brainquests.pages.dev/tricky-arrow-2.html)
- [INDEX6](https://eduquestsfr.pages.dev/index6.html)
- [STICKMAN KOMBAT 2D](https://brainquests.pages.dev/stickman-kombat-2d.html)
- [SAFE MERGE](https://welearnaction.onrender.com/safe-merge.html)
- [SQUID GAME PLAYGROUND SHOOTER](https://ieduquests.web.app/squid-game-playground-shooter.html)
- [GROW WARSIO](https://eduquests.pages.dev/grow-warsio.html)
- [WORM ESCAPE](https://brainquests.pages.dev/worm-escape.html)
- [CATEGORY BLOODY29](https://eduquestkr.pages.dev/category-bloody29.html)
- [CRYPTOGRAM](https://learnaction.netlify.app/cryptogram.html)
- [TERMS](https://iskillquest.pages.dev/terms.html)
- [BAD EGG](https://ieduquests.web.app/bad-egg.html)
- [MERGE FELLAS ONLINE](https://eduquestkr.pages.dev/merge-fellas-online.html)
- [SEAT JAM 3D](https://learnaction.netlify.app/seat-jam-3d.html)
- [SORCERER MAHJONG MARVELS](https://eduquestsfr.pages.dev/sorcerer-mahjong-marvels.html)
- [DIG OUT OF PRISON](https://ieduquests.web.app/dig-out-of-prison.html)
- [PETS VS BEES](https://brainquests.pages.dev/pets-vs-bees.html)
- [DEEP FISHING](https://eduquests.pages.dev/deep-fishing.html)
- [SQUAREHEAD HERO](https://eduquestspt.pages.dev/squarehead-hero.html)
