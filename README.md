# CircleProgressBar
最近项目里面需要做一个如下的控件。
<br />![测试图片](https://github.com/Amoryan/CircleProgressBar/raw/master/screenShort/1.png)
<br />无奈不想用别人做好的，于是自己就做了一个，先上效果图。
<br />![测试图片](https://github.com/Amoryan/CircleProgressBar/raw/master/screenShort/result.gif)
<br />支持以下属性
##Paint相关属性
strokeWidth指定paint的宽度
<br />capStyle对应于Paint.Cap
##绘制相关属性
radius进度条的绘制半径，如果使用这个半径测量出来的宽高大于了最大的值，则不会使用这个值，而是取最小兼容值
<br />startDegree从哪个其实角度开始画
<br />rotateDegree旋转角度
<br />sweepDegree绘制角度
<br />比如rotate为0，startDegree为0，sweepDegree为360，就是从3点钟方向绘制360度
##颜色相关属性
backColor设置底色
<br />useGradient是否使用渐变色
<br />progressColor如果不使用渐变色，这个属性会设置进度条的颜色
<br />startColor和endColor如果使用渐变色，这两个属性是设置起始和终止颜色
<br />##动效相关属性
<br />openAnimation是否开启动画
<br />animVelocity进度变化的速度
<br />##进度相关属性
<br />maxProgress设置最大进度
<br />progress设置进度
