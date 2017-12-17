package wxplus.opengles2forandroid;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.obj.Skybox;
import wxplus.opengles2forandroid.programs.SkyboxShaderProgram;
import wxplus.opengles2forandroid.programs.TextureShaderProgram;
import wxplus.opengles2forandroid.utils.TextureUtils;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by hi on 2017/10/29.
 */

public class OpenGL_04_Skybox extends BaseActivity {

    protected GLSurfaceView mGlView;
    protected SimpleColorRenderer mRenderer;

    protected static final int sFovy = 45; // 透视投影的视角，90度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlView = new GLSurfaceView(this);
        // Request an OpenGL ES 2.0 compatible context.
        mGlView.setEGLContextClientVersion(2);
        mRenderer = new SimpleColorRenderer();
        mGlView.setRenderer(mRenderer);
        setContentView(mGlView);
        mGlView.setOnTouchListener(new View.OnTouchListener() {
            float previousX, previousY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        previousX = event.getX();
                        previousY = event.getY();
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        final float deltaX = event.getX() - previousX;
                        final float deltaY = event.getY() - previousY;

                        previousX = event.getX();
                        previousY = event.getY();

                        mGlView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                mRenderer.handleTouchDrag(deltaX, deltaY);
                            }
                        });
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    protected float[] mViewMatrix = new float[16];
    protected float[] mProjectionMatrix = new float[16];
    protected float[] mProjectionViewMatrix = new float[16];

    protected SkyboxShaderProgram mSkyboxProgram;

    protected int mSkyboxTexture;

    protected Skybox mSkybox;

    public class SimpleColorRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            mSkyboxProgram = new SkyboxShaderProgram(mActivity);
            mSkyboxTexture = TextureUtils.loadCubeMap(mActivity,
                    new int[]{R.drawable.left, R.drawable.right,
                            R.drawable.bottom, R.drawable.top,
                            R.drawable.front, R.drawable.back}
            );
            mSkybox = new Skybox();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // Set the OpenGL viewport to fill the entire surface.
            glViewport(0, 0, width, height);
            float screenAspect = width * 1.0f / height;
            Matrix.perspectiveM(mProjectionMatrix, 0, sFovy, screenAspect, 1f, 10f);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            glClear(GL_COLOR_BUFFER_BIT);
            setIdentityM(mViewMatrix, 0);
            rotateM(mViewMatrix, 0, -xRotation, 0, 1f, 0);
            rotateM(mViewMatrix, 0, -yRotation, 1f, 0, 0);
            multiplyMM(mProjectionViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
            mSkyboxProgram.bindData(mProjectionViewMatrix, mSkyboxTexture, mSkybox);
            mSkybox.draw();
        }

        protected float xRotation, yRotation;

        public void handleTouchDrag(float deltaX, float deltaY) {
            xRotation += deltaX / 16f;
            yRotation += deltaY / 16f;

            if (yRotation < -90) {
                yRotation = -90;
            } else if (yRotation > 90) {
                yRotation = 90;
            }
        }
    }

}
