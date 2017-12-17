package wxplus.opengles2forandroid;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.obj.Puck;
import wxplus.opengles2forandroid.obj.Table;
import wxplus.opengles2forandroid.obj.base.Mallet;
import wxplus.opengles2forandroid.obj.base.Point;
import wxplus.opengles2forandroid.programs.ColorShaderProgram;
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

public class OpenGL_03_Simple_Object extends BaseActivity {

    protected GLSurfaceView mGlView;


    protected static final int sFovy = 90; // 透视投影的视角，90度
    protected static final float sZ = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlView = new GLSurfaceView(this);
        // Request an OpenGL ES 2.0 compatible context.
        mGlView.setEGLContextClientVersion(2);
        mGlView.setRenderer(new CusRenderer());
        setContentView(mGlView);
    }

    protected float[] mModelMatrix = new float[16];
    protected float[] mViewMatrix = new float[16];
    protected float[] mProjectionMatrix = new float[16];
    protected float[] mProjectionViewMatrix = new float[16];
    protected float[] mProjectionViewModelMatrix = new float[16];

    protected TextureShaderProgram mTextureProgram;
    protected ColorShaderProgram mColorProgram;

    protected int mTexture;

    protected Table mTable;
    protected Puck mPuck;
    protected Mallet mTopMallet;
    protected Mallet mBottomMallet;

    public class CusRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            mTextureProgram = new TextureShaderProgram(mActivity);
            mColorProgram = new ColorShaderProgram(mActivity);
            mTexture = TextureUtils.loadTexture(mActivity, R.drawable.air_hockey_surface);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // Set the OpenGL viewport to fill the entire surface.
            glViewport(0, 0, width, height);
            float focalLength = (float) (1 / Math.tan(Math.toRadians(sFovy / 2)));
            float screenAspect = width * 1.0f / height;
            Matrix.perspectiveM(mProjectionMatrix, 0, sFovy, screenAspect, 1f, 10f);
            setIdentityM(mViewMatrix, 0);
            translateM(mViewMatrix, 0, 0, 0, -sZ);
            rotateM(mViewMatrix, 0, -60, 1f, 0f, 0f);
            // Multiply the view and projection matrices together.
            multiplyMM(mProjectionViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
            // 初始化Objects
            mTable = new Table(new Point(0, 0, 0), sZ * screenAspect / focalLength, sZ / focalLength);
            mPuck = new Puck(new Point(0, 0, 0), 0.1f, 0.1f, 100);
            mTopMallet = new Mallet(new Point(0f, 0f, 0f), 0.05f, 0.1f, 0.1f, 0.05f, 100, 100);
            mBottomMallet = new Mallet(new Point(0f, 0f, 0f), 0.05f, 0.1f, 0.1f, 0.05f, 100, 100);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            glClear(GL_COLOR_BUFFER_BIT);
            // 绘制Table
            positionTableInScene();
            mTextureProgram.bindData(mProjectionViewModelMatrix, mTexture, mTable);
            mTable.draw();
            // 绘制Puck
            positionPuckInScene();
            mColorProgram.bindData(mProjectionViewModelMatrix, mPuck, 1f, 0f, 0f);
            mPuck.draw();
            // top mallet
            positionTopMalletInScene();
            mColorProgram.bindData(mProjectionViewModelMatrix, mTopMallet, 0, 1f, 0);
            mTopMallet.draw();
            // bottom mallet
            positionBottomMalletInScene();
            mColorProgram.bindData(mProjectionViewModelMatrix, mBottomMallet, 0, 0, 1f);
            mBottomMallet.draw();
        }
    }

    protected void positionTableInScene() {
        setIdentityM(mModelMatrix, 0);
        multiplyMM(mProjectionViewModelMatrix, 0, mProjectionViewMatrix, 0, mModelMatrix, 0);
    }

    protected void positionPuckInScene() {
        setIdentityM(mModelMatrix, 0);
        translateM(mModelMatrix, 0, 0, 0, 0.05f);
        multiplyMM(mProjectionViewModelMatrix, 0, mProjectionViewMatrix, 0, mModelMatrix, 0);
    }

    protected void positionTopMalletInScene() {
        setIdentityM(mModelMatrix, 0);
        translateM(mModelMatrix, 0, 0, 0.5f, 0.05f);
        multiplyMM(mProjectionViewModelMatrix, 0, mProjectionViewMatrix, 0, mModelMatrix, 0);
    }

    protected void positionBottomMalletInScene() {
        setIdentityM(mModelMatrix, 0);
        translateM(mModelMatrix, 0, 0, -0.5f, 0.05f);
        multiplyMM(mProjectionViewModelMatrix, 0, mProjectionViewMatrix, 0, mModelMatrix, 0);
    }

}
