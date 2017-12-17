package wxplus.opengles2forandroid.programs;

import android.content.Context;

import wxplus.opengles2forandroid.R;
import wxplus.opengles2forandroid.obj.HeightMap;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;

/**
 * @author WangXiaoPlus
 * @date 2017/12/17
 */

public class HeightMapShaderProgram extends ShaderProgram {

    protected final int uMatrixLocation;
    protected final int aPositionLocation;

    public HeightMapShaderProgram(Context context) {
        super(context, R.raw.heightmap_vertex_shader, R.raw.heightmap_fragment_shader);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
    }

    public void bindData(float[] matrix, HeightMap heightMap) {
        glUseProgram(program);
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        // 设置顶点数据
        glVertexAttribPointer(aPositionLocation, FLOATS_PER_VERTEX, GL_FLOAT, false, 0, heightMap.getVertexBuffer());
        glEnableVertexAttribArray(aPositionLocation);
    }

}
