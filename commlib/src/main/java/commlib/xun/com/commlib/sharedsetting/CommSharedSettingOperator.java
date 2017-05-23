package commlib.xun.com.commlib.sharedsetting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import commlib.xun.com.commlib.exception.JsonFormatException;
import commlib.xun.com.commlib.json.CommJsonToBeanHandler;

/**
 * Created by xunwang on 17/5/23.
 */

public class CommSharedSettingOperator {
    /**
     * 根据你key查询value
     *
     * @param key 键值
     * @return value
     */
    public static String queryValue(Context context, String key) {
        Cursor cursor = query(context, key);
        String value = null;
        if (cursor != null) {
            value = cursor.getString(cursor.getColumnIndex(CommSharedSettingContentProvider.SharedSettingDbHelper
                    .COLUMN_VALUE));
            cursor.close();
        }
        return value;
    }

    /**
     * 根据你key查询额外数据
     *
     * @param key 键值
     * @return 额外数据 blob
     */
    public static byte[] queryExtra(Context context, String key) {
        Cursor cursor = query(context, key);
        byte[] value = null;
        if (cursor != null) {
            value = cursor.getBlob(cursor.getColumnIndex(CommSharedSettingContentProvider.SharedSettingDbHelper
                    .COLUMN_EXTRA));
            cursor.close();
        }
        return value;
    }


    public static Cursor query(Context context, String key) {
        String selection = CommSharedSettingContentProvider.SharedSettingDbHelper.COLUMN_KEY + " = ? ";
        String[] selectionArgs = {key};
        Cursor cursor = context.getContentResolver().query(CommSharedSettingContentProvider
                .getContentUri(), null, selection, selectionArgs, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) {
            return cursor;
        } else {
            cursor.close();
            return null;
        }
    }

    /**
     * 存储
     *
     * @param key   键值
     * @param value 内容
     * @param extra 额外数据
     */
    public static void save(Context context, String key, String value, Object extra) {
        byte[] data = null;
        try {
            data = CommJsonToBeanHandler.getInstance().toByteArray(extra);
        } catch (JsonFormatException e) {
            e.printStackTrace();
        }
        ContentValues values = new ContentValues();
        values.put(CommSharedSettingContentProvider.SharedSettingDbHelper.COLUMN_VALUE, value);
        values.put(CommSharedSettingContentProvider.SharedSettingDbHelper.COLUMN_EXTRA, data);
        String selection = CommSharedSettingContentProvider.SharedSettingDbHelper.COLUMN_KEY + " = ? ";
        String[] selectionArgs = {key};
        int count = context.getContentResolver().update(CommSharedSettingContentProvider
                .getContentUri(), values, selection, selectionArgs);
        if (count == 0) {
            values.put(CommSharedSettingContentProvider.SharedSettingDbHelper.COLUMN_KEY, key);
            context.getContentResolver().insert(CommSharedSettingContentProvider.getContentUri
                    (), values);
        }
    }

    /**
     * 根据你key查询boolean
     *
     * @param key 键值
     * @return
     */
    public static boolean queryBlnValue(Context context, String key) {
        String value = queryValue(context, key);
        if ("true".equals(value)) {
            return true;
        }
        return false;
    }

    /**
     * 根据你key查询int
     *
     * @param key 键值
     * @return
     */
    public static int queryIntValue(Context context, String key, int defaultValue) {
        String value = queryValue(context, key);
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        Integer integer = 0;
        integer = Integer.getInteger(value);
        return integer;
    }
}
