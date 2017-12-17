package wxplus.opengles2forandroid.obj;

import android.graphics.Bitmap;
import android.graphics.Color;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glDrawElements;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;

/**
 * @author WangXiaoPlus
 * @date 2017/12/17
 */

public class HeightMap extends Object {
    protected static final String TAG = HeightMap.class.getSimpleName();

    protected final int width, height;

    public HeightMap(Bitmap bmp) {
        if (bmp == null) {
            throw new NullPointerException(TAG + ", bmp is null");
        }
        width = bmp.getWidth();
        height = bmp.getHeight();
        if (width * height > Math.pow(2, 16)) {
            throw new RuntimeException(TAG + ", the width or height of bitmap is 0...");
        } else if (width * height <= 0) {
            throw new RuntimeException(TAG + ", the width or height of bitmap is 0...");
        }
        mVertexData = loadBitmapData(bmp);
        mIndexData = createIndexData();
        drawTaskList.add(new DrawTask() {
            @Override
            public void draw() {
                glDrawElements(GL_TRIANGLES, mIndexData.length, GL_UNSIGNED_SHORT, getIndexBuffer());
            }
        });
    }

    /**
     * Copy the heightmap data into a vertex buffer object.
     */
    protected float[] loadBitmapData(Bitmap bitmap) {
        final int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap.recycle();

        final float[] heightmapVertices =
                new float[width * height * FLOATS_PER_VERTEX];
        int offset = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // The heightmap will lie flat on the XZ plane and centered
                // around (0, 0), with the bitmap width mapped to X and the
                // bitmap height mapped to Z, and Y representing the height. We
                // assume the heightmap is grayscale, and use the value of the
                // red color to determine the height.
                final float xPosition = ((float) col / (float) (width - 1)) - 0.5f;
                final float yPosition =
                        (float) Color.red(pixels[(row * width) + col]) / (float) 255;
                final float zPosition = ((float) row / (float) (height - 1)) - 0.5f;

                heightmapVertices[offset++] = xPosition;
                heightmapVertices[offset++] = yPosition;
                heightmapVertices[offset++] = zPosition;
            }
        }
        return heightmapVertices;
    }

    /**
     * Create an index buffer object for the vertices to wrap them together into
     * triangles, creating indices based on the width and height of the
     * heightmap.
     */
    private short[] createIndexData() {
        int vertexCount = (width - 1) * (height - 1) * 2 * 3; // 有(width - 1) * (height - 1)个正方形，每个正方形可以分成两个三角形，每个三角形三个顶点
        final short[] indexData = new short[vertexCount];
        int offset = 0;

        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width - 1; col++) {
                // Note: The (short) cast will end up underflowing the number
                // into the negative range if it doesn't fit, which gives us the
                // right unsigned number for OpenGL due to two's complement.
                // This will work so long as the heightmap contains 65536 pixels
                // or less.
                short topLeftIndexNum = (short) (row * width + col);
                short topRightIndexNum = (short) (row * width + col + 1);
                short bottomLeftIndexNum = (short) ((row + 1) * width + col);
                short bottomRightIndexNum = (short) ((row + 1) * width + col + 1);

                // Write out two triangles.
                indexData[offset++] = topLeftIndexNum;
                indexData[offset++] = bottomLeftIndexNum;
                indexData[offset++] = topRightIndexNum;

                indexData[offset++] = topRightIndexNum;
                indexData[offset++] = bottomLeftIndexNum;
                indexData[offset++] = bottomRightIndexNum;
            }
        }

        return indexData;
    }
}
