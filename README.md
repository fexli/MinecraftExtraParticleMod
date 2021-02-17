
# Minecraft Extra Particle Mod(Fabric Version)  
这是一个我的世界粒子mod的模板mod，它提供了我的世界粒子管理器从而可以使您编写的粒子被快速的添加到游戏中(使用/particle指令即可)、同时附带有几种基础的粒子(命名空间为fexli)以供您在编写自己的粒子时进行参考。这几种粒子的说明文档在[说明-0.0.8-b.xlsx](./说明-0.0.8-b.xlsx)中，您可以打开进行查看。另外在release中我已上传最新的0.0.8e版本的成品，需要fabric-api来使用  
### 注意L本模组支持ReplayMod但是请不要瞬间拖动Replay时间轴避免因瞬间加载的粒子过多导致游戏卡顿/崩溃(这个是Replay的锅)！  
## 0x00 开始之前の唠叨  
  
### 我的世界更多粒子模组，构建于Fabric+1.14.4版本  
(算是之前心血来潮看粒子好好康所以想写点东西去实现以下挖下的坑，如今终于算是圆上了(~~其实早就写完了但是一直在忙别的+写mc的Python指令所以就一直没发 dbq~~)，用mod做了几个视频，并且帮了好几位粒子圈的大大~至少我感觉还是有很多收获的~也不算是白写了)  
最初的灵感&动力来自于津津大大([@不背完牛津高阶不改名](https://space.bilibili.com/110924762))，当时fabric-particle-api因为完全没法用所以自己找资料写了一个ParticleManagerImpl实现的粒子注入(也是本模粒子能成功添加的组核心代码)后来有灵感有想法就去修修改改，最终呈现出来的就是这个~~奇奇怪怪的有些杂乱的~~模组  
其实这个mod里面除了粒子还写了很多东西(Python/函数式)但是由于我也不知道某位神秘的julao是谁所以一直不知道找谁要授权(根据julao模组受到的启发)所以也没有把代码拿到台面上来~~秀~~，但是在particle/future和particle/dumped还存有一些遗迹，有兴趣的小伙伴可以pull下来改一下Command来激活~  
因为是我第一次接触Java所以难免有些幼稚的代码(真的是第一次接触Java QAQ之前都没碰过)希望大佬们不要笑话我~如果我代码有问题的话也希望大家多多issue和pr~  
### 如果有会继承的大佬们请帮帮忙看一下com.fexli.particle.particles.bulletscreen以及com.fexli.particle.particles.bulletscreen.ertype要怎么简化..万分感谢！！  
(还有一个1.16.4的版本以后有时间也会push上来的(?))  
### 最后也祝Minecraft越来越好~ 粒子圈的各位大大们也越来越火~ 如果这个mod对大家有帮助就最好了  
from fe_x_li @ DFVBStudio 2021.2.17/Happy 牛 Year  
  
## 0x01 初识·粒子结构  
单一的粒子具有两种结构体，一个是粒子本身，其基类为net.minecraft.client.particle.Particle,向上被继承有BillboardParticle->SpriteBillboardParticle->AnimatedParticle...，其内容为粒子的创建以及粒子的所有属性，包括颜色、大小、透明度、粒子tick计算(位置、颜色、透明度变化)、粒子透明度属性、粒子消亡等等。  
二是粒子效果(ParticleEffect)其基类为net.minecraft.particle.ParticleEffect，它控制着由服务端生成的粒子通过数据传输至客户端的packing和unpack协议  
在编写粒子时主要处理的便是粒子本身(即Particle)对于有特殊参数（如初始运动方向，特殊颜色等）则需要借助ParticleEffect进行构建。  
## 0x02 创建第一个粒子  
未完待续...  
