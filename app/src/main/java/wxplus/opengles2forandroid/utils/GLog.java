package wxplus.opengles2forandroid.utils;

import android.util.Log;

import static wxplus.opengles2forandroid.utils.GlobalConfig.DEBUG;

/**
 * @author WangXiaoPlus
 * @date 2017/11/19
 */

public class GLog {

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }
    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }
}
