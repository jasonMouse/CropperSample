# CropperSample
主要用于解决图片裁剪固定缩放相关问题的demo  
![图片](https://github.com/jasonMouse/CropperSample/blob/master/app/src/main/assets/picture_1.jpg)
![图片](https://github.com/jasonMouse/CropperSample/blob/master/app/src/main/assets/picture_2.jpg)
![图片](https://github.com/jasonMouse/CropperSample/blob/master/app/src/main/assets/picture_3.jpg)
![图片](https://github.com/jasonMouse/CropperSample/blob/master/app/src/main/assets/picture_4.jpg)
![图片](https://github.com/jasonMouse/CropperSample/blob/master/app/src/main/assets/picture_5.jpg)
![图片](https://github.com/jasonMouse/CropperSample/blob/master/app/src/main/assets/picture_6.jpg)
![图片](https://github.com/jasonMouse/CropperSample/blob/master/app/src/main/assets/picture_7.jpg)
```
<declare-styleable name="CropImageView">
    <!--辅助线的状态 默认是触碰时展示-->
    <attr name="guidelines">
        <!--关闭-->
        <enum name="off" value="0"/>
        <!--触碰时展示-->
        <enum name="onTouch" value="1"/>
        <!--一直展示-->
        <enum name="on" value="2"/>
    </attr>
    <!--是否固定宽度和高度的比例 默认是关闭-->
    <attr name="fixAspectRatio" format="boolean"/>
    <!--宽度比例-->
    <attr name="aspectRatioX" format="integer"/>
    <!--高度比例-->
    <attr name="aspectRatioY" format="integer"/>

    <!--边框的宽度 默认 1.5dp-->
    <attr name="borderWidth" format="dimension" />
    <!--边框的颜色 默认 white_translucent-->
    <attr name="borderColor" format="color" />
    <!--角落手柄的长度 默认 20dp-->
    <attr name="cornerLength" format="dimension" />
    <!--角落手柄的宽度 默认 2.5dp-->
    <attr name="cornerWidth" format="dimension" />
    <!--角落手柄的颜色 默认 白色-->
    <attr name="cornerColor" format="color" />
    <!--辅助线的宽度 默认 0.5dp-->
    <attr name="guidelineWidth" format="dimension" />
    <!--辅助线的颜色 默认 white_translucent-->
    <attr name="guidelineColor" format="color" />
    <!--未被选中区域的背景颜色 默认 black_translucent -->
    <attr name="surroundingAreaBg" format="color" />
</declare-styleable>
```
