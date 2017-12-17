package wxplus.opengles2forandroid.obj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import wxplus.opengles2forandroid.obj.base.Circle;
import wxplus.opengles2forandroid.obj.base.Cylinder;
import wxplus.opengles2forandroid.obj.base.Square;
import wxplus.opengles2forandroid.programs.ShaderProgram;
import wxplus.opengles2forandroid.utils.GlobalConfig;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static wxplus.opengles2forandroid.utils.Constants.BYTES_PER_FLOAT;
import static wxplus.opengles2forandroid.utils.Constants.BYTES_PER_SHORT;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;
import static wxplus.opengles2forandroid.utils.Constants.VERTEX_COUNT_SQUARE;

/**
 * @author WangXiaoPlus
 * @date 2017/11/19
 * <p>
 * 图形的基类，提供共有方法
 */

public class Object {
    public static final String TAG = Object.class.getSimpleName();

    protected FloatBuffer mVertexBuffer;
    protected float[] mVertexData;
    protected int offset = 0;

    protected ShortBuffer mIndexBuffer;
    protected short[] mIndexData;

    protected FloatBuffer mTextureBuffer;
    protected static final float[] TEXTURE_DATA = {
            // Order of coordinates:  S, T

            // Triangle Fan
            0.5f, 0.5f,
            0f, 0f,
            1f, 0f,
            1f, 1f,
            0f, 1f,
            0f, 0f
    };

    protected List<DrawTask> drawTaskList = new ArrayList<>();

    public Object addSquare(Square square) {
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int vertexCount = VERTEX_COUNT_SQUARE;
        if (mVertexData == null || mVertexData.length - offset < vertexCount * FLOATS_PER_VERTEX) {
            return this;
        }
        // 先确定正方形中心的坐标
        mVertexData[offset++] = square.center.x;
        mVertexData[offset++] = square.center.y;
        mVertexData[offset++] = square.center.z;
        // 左下角
        mVertexData[offset++] = square.center.x - square.widht / 2;
        mVertexData[offset++] = square.center.y - square.height / 2;
        mVertexData[offset++] = square.center.z;
        // 右下角
        mVertexData[offset++] = square.center.x + square.widht / 2;
        mVertexData[offset++] = square.center.y - square.height / 2;
        mVertexData[offset++] = square.center.z;
        // 右上角
        mVertexData[offset++] = square.center.x + square.widht / 2;
        mVertexData[offset++] = square.center.y + square.height / 2;
        mVertexData[offset++] = square.center.z;
        // 左上角
        mVertexData[offset++] = square.center.x - square.widht / 2;
        mVertexData[offset++] = square.center.y + square.height / 2;
        mVertexData[offset++] = square.center.z;
        // 左下角(triangle fan)
        mVertexData[offset++] = square.center.x - square.widht / 2;
        mVertexData[offset++] = square.center.y - square.height / 2;
        mVertexData[offset++] = square.center.z;
        drawTaskList.add(new DrawTask() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, vertexCount);
            }
        });
        return this;
    }

    public Object addCircle(Circle circle, int pointCount) {
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int vertexCount = sizeOfCircleInVertex(pointCount);
        if (mVertexData == null || mVertexData.length - offset < vertexCount * FLOATS_PER_VERTEX) {
            if (GlobalConfig.DEBUG) {
                throw new IndexOutOfBoundsException(TAG + ", addCircle, mVertexData is not big enough");
            }
            return this;
        }
        // 先确定圆心的坐标
        mVertexData[offset++] = circle.center.x;
        mVertexData[offset++] = circle.center.y;
        mVertexData[offset++] = circle.center.z;
        // 循环赋值，确定圆上顶点的坐标(最后一个顶点赋值两次)
        float radian = (float) (2 * Math.PI / pointCount); // 先计算出每一份的弧度值
        for (int i = 0; i <= pointCount; i++) {
            mVertexData[offset++] = circle.center.x + circle.radius * (float) Math.cos(radian * i);
            mVertexData[offset++] = circle.center.y + circle.radius * (float) Math.sin(radian * i);
            mVertexData[offset++] = circle.center.z;
        }
        drawTaskList.add(new DrawTask() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, vertexCount);
            }
        });
        return this;
    }

    public Object addOpenCylinder(Cylinder cylinder, int pointCount) {
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int vertexCount = sizeOfCylinderInVertex(pointCount);
        if (mVertexData == null || mVertexData.length - offset < vertexCount * FLOATS_PER_VERTEX) {
            if (GlobalConfig.DEBUG) {
                throw new IndexOutOfBoundsException(TAG + ", addOpenCylinder, mVertexData is not big enough");
            }
            return this;
        }
        float topZ = cylinder.center.z + cylinder.height / 2;
        float bottomZ = cylinder.center.z - cylinder.height / 2;
        float radian = (float) (2 * Math.PI / pointCount); // 先计算出每一份的弧度值
        // 依次赋值
        for (int i = 0; i <= pointCount; i++) {
            float x = cylinder.center.x + cylinder.radius * (float) Math.cos(i + radian);
            float y = cylinder.center.y + cylinder.radius * (float) Math.sin(i + radian);
            // top
            mVertexData[offset++] = x;
            mVertexData[offset++] = y;
            mVertexData[offset++] = topZ;
            // bottom
            mVertexData[offset++] = x;
            mVertexData[offset++] = y;
            mVertexData[offset++] = bottomZ;
        }
        drawTaskList.add(new DrawTask() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, vertexCount);
            }
        });
        return this;
    }

    public FloatBuffer getVertexBuffer() {
        if (mVertexBuffer == null) {
            mVertexBuffer = ByteBuffer
                    .allocateDirect(mVertexData.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(mVertexData);
        }
        mVertexBuffer.position(0);
        return mVertexBuffer;
    }

    public FloatBuffer getTextureBuffer() {
        if (mTextureBuffer == null) {
            mTextureBuffer = ByteBuffer
                    .allocateDirect(TEXTURE_DATA.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(TEXTURE_DATA);
        }
        mTextureBuffer.position(0);
        return mTextureBuffer;
    }

    public ShortBuffer getIndexBuffer() {
        if (mIndexBuffer == null) {
            mIndexBuffer = ByteBuffer
                    .allocateDirect(mIndexData.length * BYTES_PER_SHORT)
                    .order(ByteOrder.nativeOrder())
                    .asShortBuffer()
                    .put(mIndexData);
        }
        mIndexBuffer.position(0);
        return mIndexBuffer;
    }

    public void draw() {
        for (int i = 0; i < drawTaskList.size(); i++) {
            drawTaskList.get(i).draw();
        }
    }

    public int sizeOfCircleInVertex(int pointCount) {
        return pointCount + 2;
    }

    public int sizeOfCylinderInVertex(int pointCount) {
        return (pointCount + 1) * 2;
    }

    public int floatSizeOfVertexs(int vertexCount) {
        return FLOATS_PER_VERTEX * vertexCount;
    }

    public interface DrawTask {
        void draw();
    }
}
