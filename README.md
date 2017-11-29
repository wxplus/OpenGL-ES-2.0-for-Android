# 项目说明

本项目包含了若干个OpenGL 2.0在Android上的应用的例子，

## 功能列表

- OpenGL_01_Simple_Color 实现最基本的绘制正方形
- OpenGL_02_Simple_Texture 实现最基本的加载图片Texture

## 项目开发问题记录

### OpenGL_01_Simple_Color

#### 几个可能让图形显示不出来的问题

1. 没有调用GLSurfaceView.setEGLContextClientVersion(2)来指定EGLContext的版本;
2. glsl文件中没有删掉这行代码`#version 120`
3. 在进行正交变换时，变换的Matrix没有进行Matrix.orithM初始化

## OpenGL_02_Simple_Texture

未遇到问题

## OpenGL_03_Simple_Object

未遇到问题