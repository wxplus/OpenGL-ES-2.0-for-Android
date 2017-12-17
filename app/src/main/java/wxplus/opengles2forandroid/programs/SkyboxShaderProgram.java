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
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;

public class SkyboxShaderProgram extends ShaderProgram {
    protected static final String TAG = SkyboxShaderProgram.class.getSimpleName();

    protected final int uMatrixLocation;
    protected final int uTextureUnitLocation;
    protected final int aPositionLocation;

    public SkyboxShaderProgram(Context context) {
        super(context, R.raw.skybox_vertex_shader,
                R.raw.skybox_fragment_shader);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
    }

    public void bindData(float[] matrix, int textureId, Object obj) {
        glUseProgram(program);

        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureId);
        glUniform1i(uTextureUnitLocation, 0);

        // 设置顶点数据
        glVertexAttribPointer(aPositionLocation, FLOATS_PER_VERTEX, GL_FLOAT, false, 0, obj.getVertexBuffer());
        glEnableVertexAttribArray(aPositionLocation);
    }
}
