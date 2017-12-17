uniform mat4 u_Matrix; // 正交变换

attribute vec4 a_Position; // 顶点坐标
attribute vec2 a_TextureCoordinates; // Texture坐标

varying vec2 v_TextureCoordinates; // 透传给Fragment Shader

void main()
{
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_Matrix * a_Position;

}