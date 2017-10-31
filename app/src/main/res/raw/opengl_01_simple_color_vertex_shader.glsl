uniform mat4 u_Matrix; // 正交变换

attribute vec4 a_Position; // 外部赋值

void main() {

    gl_Position = u_Matrix * a_Position;

}
