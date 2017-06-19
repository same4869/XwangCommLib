package commlib.xun.com.commlib.utils;

/**
 * Created by xunwang on 17/6/19.
 */

public class AppUtils {
    private static long lastClickTime;

    /**
     * 判断是否快速点击，双击返回退出应用也可以用这个
     *
     * @return true是在最小时间内快速点击，false则不是
     */
    public static boolean isFastDoubleClick(int minTime) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < minTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
