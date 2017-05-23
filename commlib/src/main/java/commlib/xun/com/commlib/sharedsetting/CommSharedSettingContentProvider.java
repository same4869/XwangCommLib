package commlib.xun.com.commlib.sharedsetting;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by xunwang on 17/5/23.
 */

public class CommSharedSettingContentProvider extends ContentProvider {
    private static final String TAG = CommSharedSettingContentProvider.class.getSimpleName();

    public static final String AUTHORITY = CommSharedSettingContentProvider.class.getName();

    private SharedSettingDbHelper mDbHelper;

    public static Uri getContentUri() {
        return Uri.parse("content://" + AUTHORITY);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new SharedSettingDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        return db.query(SharedSettingDbHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long rowID = db.insert(SharedSettingDbHelper.TABLE_NAME, null, values);
        if (rowID > 0) {
            return ContentUris.withAppendedId(getContentUri(), rowID);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = db.delete(SharedSettingDbHelper.TABLE_NAME, selection, selectionArgs);

        // 更新数据时，通知其他ContentObserver
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = db.update(SharedSettingDbHelper.TABLE_NAME, values, selection, selectionArgs);
        // 更新数据时，通知其他ContentObserver
        if (count != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    public static class SharedSettingDbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "shared_setting.db";
        private static final String TABLE_NAME = "SETTING";
        public static final int DATABASE_VERSION = 1;
        public static final String COLUMN_KEY = "_KEY";
        public static final String COLUMN_VALUE = "_VALUE";
        public static final String COLUMN_EXTRA = "_EXTRA";
        private final String[] SQL_CREATE_TABLE = {
                "CREATE TABLE " + TABLE_NAME + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_KEY + " VARCHAR(100),"
                        + COLUMN_VALUE + " VARCHAR(100),"
                        + COLUMN_EXTRA + " BLOB"
                        + ");"
        };

        public SharedSettingDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            executeBatch(SQL_CREATE_TABLE, db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String[] tasks = {
                    "DROP TABLE IF EXISTS " + TABLE_NAME};
            executeBatch(tasks, db);
            onCreate(db);
        }

        /**
         * 批量执行Sql语句
         * e
         *
         * @param sqls
         * @param db
         */
        private void executeBatch(String[] sqls, SQLiteDatabase db) {
            if (sqls == null)
                return;

            db.beginTransaction();
            try {
                int len = sqls.length;
                for (int i = 0; i < len; i++) {
                    db.execSQL(sqls[i]);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }

        }
    }
}
