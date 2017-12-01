package wxplus.opengles2forandroid.obj;

import wxplus.opengles2forandroid.obj.base.Circle;
import wxplus.opengles2forandroid.obj.base.Cylinder;
import wxplus.opengles2forandroid.obj.base.Point;

import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;

/**
 * @author WangXiaoPlus
 * @date 2017/11/19
 * <p>
 * 冰球
 */

public class Puck extends Object {

    public float radius; // 半径
    public float height; // 高度
    public Point center = new Point(0, 0, 0); // 中心点
    public int pointCountAroundCircle; // 圆的顶点数

    public Puck(Point center, float radius, float height, int pointCountAroundCircle) {
        this.radius = radius;
        this.height = height;
        this.center = center;
        this.pointCountAroundCircle = pointCountAroundCircle;

        vertexData = new float[floatSizeOfVertexs(sizeOfCircleInVertex(pointCountAroundCircle) + sizeOfCylinderInVertex(pointCountAroundCircle))];
        addCircle(new Circle(new Point(center.x, center.y, center.z + height / 2), radius), pointCountAroundCircle)
                .addOpenCylinder(new Cylinder(center, radius, height), pointCountAroundCircle);

    }
}
