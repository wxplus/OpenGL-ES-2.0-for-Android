package wxplus.opengles2forandroid;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Bundle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.utils.TextureUtils;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static wxplus.opengles2forandroid.utils.Constants.BYTES_PER_FLOAT;

/**
 * Created by hi on 2017/10/29.
 */

public class OpenGL_01_Simple_Color extends BaseActivity {

    protected GLSurfaceView mGlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlView = new GLSurfaceView(this);
        // Request an OpenGL ES 2.0 compatible context.
        mGlView.setEGLContextClientVersion(2);
        mGlView.setRenderer(new SimpleColorRenderer());
        // Check if the system supports OpenGL ES 2.0.
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager
                .getDeviceConfigurationInfo();
        final boolean supportsEs2 =
                configurationInfo.reqGlEsVersion >= 0x20000
                        || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86")));
        setContentView(mGlView);
    }

    protected float[] mVertexArray = new float[] { // OpenGL的坐标是[-1, 1]，这里的Vertex正好定义了一个居中的正方形
            // triangle fan x, y
            0,     0,
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f,  0.5f,
            -0.5f,  0.5f,
            -0.5f, -0.5f
    };
    protected FloatBuffer mVertexBuffer;
    protected float[] mProjectionMatrix = new float[16];

    protected int uMatrixLocation;
    protected int aPositionLocation;
    protected int uColorLocation;

    public class  SimpleColorRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            // 初始化顶点数据
            mVertexBuffer = ByteBuffer.allocateDirect(mVertexArray.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(mVertexArray);
            String vertexShaderStr = TextureUtils.readShaderCodeFromResource(mActivity, R.raw.color_vertex_shader);
            String fragmentShaderStr = TextureUtils.readShaderCodeFromResource(mActivity, R.raw.color_fragment_shader);
            // 创建Shader
            final int vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
            final int fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(vertexShaderId, vertexShaderStr);
            glShaderSource(fragmentShaderId, fragmentShaderStr);
            glCompileShader(vertexShaderId);
            glCompileShader(fragmentShaderId);
            final int[] compileStatus = new int[1];
            glGetShaderiv(fragmentShaderId, GL_COMPILE_STATUS,
                    compileStatus, 0);
            // 创建Program
            final int programId = glCreateProgram();
            glAttachShader(programId, vertexShaderId);
            glAttachShader(programId, fragmentShaderId);
            glLinkProgram(programId);
            // 启用这个Program
            glUseProgram(programId);
            // 找到需要赋值的变量
            uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
            aPositionLocation = glGetAttribLocation(programId, "a_Position");
            uColorLocation = glGetUniformLocation(programId, "u_Color");
            // 填充数据
            glUniform4f(uColorLocation, 1, 0, 0, 1);
            mVertexBuffer.position(0);
            glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 0, mVertexBuffer);
            glEnableVertexAttribArray(aPositionLocation);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);
            // 正交变换，只考虑竖屏的情况
            float rate = height * 1.0f / width;
            Matrix.orthoM(mProjectionMatrix, 0, -1, 1, -rate, rate, -1, 1); // 正交变换，防止界面拉伸
            glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            glClear(GL_COLOR_BUFFER_BIT);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
        }
    }

}
