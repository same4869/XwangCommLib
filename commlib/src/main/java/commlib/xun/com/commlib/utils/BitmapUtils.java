package commlib.xun.com.commlib.utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xunwang on 17/6/19.
 */

public class BitmapUtils {

    /**
     * 保存bitmap到指定文件路径下，需要文件系统权限
     *
     * @param bitmap
     * @param filePath
     * @param fileName
     */
    public static void saveBitmap(Bitmap bitmap, String filePath, String fileName) {
        if (bitmap == null) {
            return;
        }
        File f = new File(filePath, fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
