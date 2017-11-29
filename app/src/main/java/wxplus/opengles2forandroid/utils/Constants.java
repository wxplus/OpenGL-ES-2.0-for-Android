package wxplus.opengles2forandroid.utils;

import java.security.PublicKey;

/**
 * Created by hi on 2017/10/29.
 */

public class Constants {
    public static final int BYTES_PER_FLOAT = 4; // 一个float有4个byte
    public static final int FLOATS_PER_VERTEX = 3; // 一个顶点用三个float表示(x, y, z)
    public static final int FLOATS_PER_TEXTURE_VERTEX = 2; // texture的一个顶点用2个float表示

    public static final int VERTEX_COUNT_SQUARE = 4 + 1 + 1; // 正方形需要几个Vertex
}
