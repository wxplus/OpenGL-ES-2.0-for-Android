/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package wxplus.opengles2forandroid.programs;

import android.content.Context;

import wxplus.opengles2forandroid.R;
import wxplus.opengles2forandroid.obj.Object;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;

public class ColorShaderProgram extends ShaderProgram {
    // Uniform locations
    private final int uMatrixLocation;
    private final int uColorLocation;

    // Attribute locations
    private final int aPositionLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.opengl_01_simple_color_vertex_shader,
                R.raw.opengl_01_simple_color_fragment_shader);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uColorLocation = glGetUniformLocation(program, U_COLOR);

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
    }


    public void bindData(float[] m, Object obj, float r, float g, float b) {
        // 使用这个Program
        glUseProgram(program);
        // 矩阵变换
        glUniformMatrix4fv(uMatrixLocation, 1, false, m, 0);
        // 设置颜色
        glUniform4f(uColorLocation, r, g, b, 1f);
        // 设置顶点数据
        glVertexAttribPointer(aPositionLocation, FLOATS_PER_VERTEX, GL_FLOAT, false, 0, obj.getVertexBuffer());
        glEnableVertexAttribArray(aPositionLocation);
    }
}
