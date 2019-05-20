package com.example.memoapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.memoapp.util.DBConstant;

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(@Nullable Context context) {
        super(context, DBConstant.DATABASE_NAME, null, DBConstant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //初始版本创建的表
        sqLiteDatabase.execSQL(DBConstant.CREATE_INFO_TABLE);
        //更新版本
        onUpgrade(sqLiteDatabase,sqLiteDatabase.getVersion()+1,DBConstant.DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        int current=i+1;
        while (i1>=current) {
            switch (current) {
                case 2:
                    Log.e("onUpgrade: ","2");
                    break;
                case 3:
                    Log.e("onUpgrade: ","3");
                    break;
                default:break;
            }
            ++current;
        }
    }
}
