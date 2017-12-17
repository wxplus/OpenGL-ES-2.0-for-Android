package wxplus.opengles2forandroid;

import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.obj.HeightMap;
import wxplus.opengles2forandroid.obj.Skybox;
import wxplus.opengles2forandroid.programs.HeightMapShaderProgram;
import wxplus.opengles2forandroid.programs.SkyboxShaderProgram;
import wxplus.opengles2forandroid.utils.TextureUtils;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * @author WangXiaoPlus
 * @date 2017/12/17
 */

public class OpenGL_05_HeightMap extends BaseActivity {

    protected GLSurfaceView mGlView;
    protected CusRenderer mRenderer;

    protected static final int sFovy = 90; // 透视投影的视角，90度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlView = new GLSurfaceView(this);
        // Request an OpenGL ES 2.0 compatible context.
        mGlView.setEGLContextClientVersion(2);
        mRenderer = new CusRenderer();
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

    protected float[] mModelMatrix = new float[16];
    protected float[] mViewMatrix = new float[16];
    protected float[] mProjectionMatrix = new float[16];
    protected float[] mProjectionViewModelMatrix = new float[16]; // mvp matrix

    protected SkyboxShaderProgram mSkyboxProgram;
    protected int mSkyboxTexture;
    protected Skybox mSkybox;

    protected HeightMapShaderProgram mHeightMapShaderProgram;
    protected HeightMap mHeightMap;

    public class CusRenderer implements GLSurfaceView.Renderer {

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
            mHeightMapShaderProgram = new HeightMapShaderProgram(mActivity);
            mHeightMap = new HeightMap(BitmapFactory.decodeResource(getResources(), R.mipmap.heightmap));
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // Set the OpenGL viewport to fill the entire surface.
            glViewport(0, 0, width, height);
            float screenAspect = width * 1.0f / height;
            Matrix.perspectiveM(mProjectionMatrix, 0, sFovy, screenAspect, 1f, 100f);
            setIdentityM(mViewMatrix, 0);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            drawSkybox();
            drawHeightMap();
        }

        public void drawHeightMap() {
            // Expand the heightmap's dimensions, but don't expand the height as
            // much so that we don't get insanely tall mountains.
            setIdentityM(mModelMatrix, 0);
            translateM(mModelMatrix, 0, 0, -1.5f, -2f);
            scaleM(mModelMatrix, 0, 100f, 10f, 100f);
            updateMvpMatrix();
            mHeightMapShaderProgram.bindData(mProjectionViewModelMatrix, mHeightMap);
            mHeightMap.draw();
        }

        public void drawSkybox() {
            setIdentityM(mModelMatrix, 0);
            updateMvpMatrix();
            mSkyboxProgram.bindData(mProjectionViewModelMatrix, mSkyboxTexture, mSkybox);
            mSkybox.draw();
        }

        protected float xRotation, yRotation;
        public void handleTouchDrag(float deltaX, float deltaY) {
            xRotation = deltaX / 16f;
            yRotation = deltaY / 16f;
            rotateM(mViewMatrix, 0, -xRotation, 0, 1f, 0);
            rotateM(mViewMatrix, 0, -yRotation, 1f, 0, 0);
        }
    }

    public void updateMvpMatrix() {
        float[] temp = new float[16];
        multiplyMM(temp, 0, mViewMatrix, 0, mModelMatrix, 0);
        multiplyMM(mProjectionViewModelMatrix, 0, mProjectionMatrix, 0, temp, 0);
    }


}
