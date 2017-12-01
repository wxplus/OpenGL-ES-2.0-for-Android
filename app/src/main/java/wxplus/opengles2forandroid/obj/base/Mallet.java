package wxplus.opengles2forandroid.obj.base;

import wxplus.opengles2forandroid.obj.Object;

/**
 * @author WangXiaoPlus
 * @date 2017/11/19
 * <p>
 * 球棍
 */

public class Mallet extends Object {

    public Point center; // 中心
    public float radiusTop; // 上面的圆柱体的半径
    public float heightTop; // 上面的圆柱体的高度
    public float radiusBottom; // 上面的圆柱体的半径
    public float heightBottom; // 上面的圆柱体的高度
    public int pointCountAroundCircleTop; // 顶部圆的顶点数
    public int pointCountAroundCircleBottom; // 底部圆的顶点数

    public Mallet(Point center, float radiusTop, float radiusBottom, float heightTop, float heightBottom, int pointCountAroundCircleTop, int pointCountAroundCircleBottom) {
        this.radiusTop = radiusTop;
        this.radiusBottom = radiusBottom;
        this.heightTop = heightTop;
        this.heightBottom = heightBottom;
        this.center = center;
        this.pointCountAroundCircleTop = pointCountAroundCircleTop;
        this.pointCountAroundCircleBottom = pointCountAroundCircleBottom;

        vertexData = new float[floatSizeOfVertexs(
                sizeOfCircleInVertex(pointCountAroundCircleTop) + sizeOfCircleInVertex(pointCountAroundCircleBottom)
                        + sizeOfCylinderInVertex(pointCountAroundCircleTop) + sizeOfCylinderInVertex(pointCountAroundCircleBottom)
        )]; // 两个Circle，两个Cylinder
        addCircle(new Circle(new Point(center.x, center.y, center.z + heightTop), radiusTop), pointCountAroundCircleTop)
                .addCircle(new Circle(new Point(center.x, center.y, center.z), radiusBottom), pointCountAroundCircleBottom)
                .addOpenCylinder(new Cylinder(new Point(center.x, center.y, center.z + heightTop / 2), radiusTop, heightTop), pointCountAroundCircleTop)
                .addOpenCylinder(new Cylinder(new Point(center.x, center.y, center.z - heightBottom / 2), radiusBottom, heightBottom), pointCountAroundCircleBottom);

    }
}
