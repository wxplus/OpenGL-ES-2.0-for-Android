package wxplus.opengles2forandroid.obj;


import wxplus.opengles2forandroid.obj.base.Point;
import wxplus.opengles2forandroid.obj.base.Square;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;
import static wxplus.opengles2forandroid.utils.Constants.VERTEX_COUNT_SQUARE;

/**
 * @author WangXiaoPlus
 * @date 2017/11/19
 */

public class Table extends Object {
    protected Point center;
    protected float width;
    protected float height;

    public Table(Point center, float width, float height) {
        this.center = center;
        this.width = width;
        this.height = height;
        vertexData = new float[VERTEX_COUNT_SQUARE * FLOATS_PER_VERTEX];

        addSquare(new Square(center, width, height));

    }
}
