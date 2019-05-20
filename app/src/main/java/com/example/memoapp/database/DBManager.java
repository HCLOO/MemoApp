package com.example.memoapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.memoapp.MApplication;
import com.example.memoapp.bean.InfoBean;
import com.example.memoapp.util.DBConstant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBManager {

    private static DBManager dbManager;
    private static DBOpenHelper dbOpenHelper;
    private static SQLiteDatabase database;

    public static synchronized DBManager getInstance() {
        if(dbManager==null) {
            synchronized (DBManager.class) {
                if(dbManager==null) {
                    dbManager=new DBManager();
                }
            }
        }
        return dbManager;
    }

    private static synchronized DBOpenHelper getDBOpenHelper() {
        if(dbOpenHelper==null) {
            synchronized (DBOpenHelper.class) {
                if(dbOpenHelper==null) {
                    dbOpenHelper=new DBOpenHelper(MApplication.getContext());
                }
            }
        }
        return dbOpenHelper;
    }

    public static synchronized SQLiteDatabase getDatabase() {
        if(database==null) {
            synchronized (SQLiteDatabase.class) {
                database=getDBOpenHelper().getWritableDatabase();
            }
        }
        return database;
    }

    public void insertToInfoTable(String content) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date=new Date(System.currentTimeMillis());
        String dateAndTime=simpleDateFormat.format(date);
        ContentValues values=new ContentValues();
        values.put(DBConstant.CONTENT_INFO,content);
        values.put(DBConstant.ATTRIBUTE_INFO,DBConstant.DEFAULT);
        values.put(DBConstant.DATE_IFNO,dateAndTime);
        getDatabase().insert(DBConstant.TABLE_NAME_INFO,null,values);
    }

    public void deleteToInfoTable(int id) {
        getDatabase().delete(DBConstant.TABLE_NAME_INFO,
                DBConstant.ID_INFO+"=?",
                new String[]{Integer.toString(id)});
    }

    public List<InfoBean> getInfoList() {
        List<InfoBean> infoBeanList=new ArrayList<>();
        Cursor cursor=getDatabase().query(DBConstant.TABLE_NAME_INFO,null,
                null,null,null,null,null);
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                int id=cursor.getInt(cursor.getColumnIndex(DBConstant.ID_INFO));
                String content=cursor.getString(cursor.getColumnIndex(DBConstant.CONTENT_INFO));
                int attribute=cursor.getInt(cursor.getColumnIndex(DBConstant.ATTRIBUTE_INFO));
                String date=cursor.getString(cursor.getColumnIndex(DBConstant.DATE_IFNO));
                infoBeanList.add(new InfoBean(id,content,attribute,date));
            }
            cursor.close();
        }
        return infoBeanList;
    }

    public void updateToInfoTable(int id,int attribute) {
        ContentValues values=new ContentValues();
        values.put(DBConstant.ATTRIBUTE_INFO,attribute);
        getDatabase().update(DBConstant.TABLE_NAME_INFO,values,
                DBConstant.ID_INFO+"=?",
                new String[]{Integer.toString(id)});
    }

    public int getNumOfUnfinishTask() {
        int num=0;
        Cursor cursor=getDatabase().query(DBConstant.TABLE_NAME_INFO,null,
                DBConstant.ATTRIBUTE_INFO+"=?", new String[]{Integer.toString(DBConstant.DEFAULT)},
                null,null,null);
        if(cursor!=null) {
            num=cursor.getCount();
            cursor.close();
        }
        Log.e( "getNumOfUnfinishTask: ",num+"");
        return num;
    }
}
