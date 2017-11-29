package wxplus.opengles2forandroid.obj.base;


public class Cylinder {
        public Point center; // 中心
        public float radius; // 半径
        public float height; // 高度

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }