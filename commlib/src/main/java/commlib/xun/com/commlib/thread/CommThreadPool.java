package commlib.xun.com.commlib.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 公共线程池类
 * Created by xunwang on 17/5/23.
 */

public class CommThreadPool {
    private static volatile Executor sPool;
    private static volatile Handler sHandler;
    private static Handler sUiHandler = new Handler(Looper.getMainLooper());

    public static final Executor getExecutor() {
        if (sPool != null) {
            return sPool;
        }

        synchronized (CommThreadPool.class) {
            if (sPool == null) {
                sPool = Executors.newCachedThreadPool();
            }
        }

        return sPool;
    }

    public static final Handler getSerialHandler() {
        if (sHandler != null) {
            return sHandler;
        }

        synchronized (CommThreadPool.class) {
            if (sHandler == null) {
                HandlerThread thread = new HandlerThread("serial-looper");
                thread.start();
                sHandler = new Handler(thread.getLooper());
            }
        }

        return sHandler;
    }

    /**
     * 一般使用这个，在子线程中执行
     * @param runnable
     */
    public static void poolExecute(Runnable runnable) {
        getExecutor().execute(runnable);
    }

    public static void serialExecute(Runnable runnable) {
        getSerialHandler().post(runnable);
    }

    public static void runOnUiThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        }

        sUiHandler.post(runnable);
    }
}
