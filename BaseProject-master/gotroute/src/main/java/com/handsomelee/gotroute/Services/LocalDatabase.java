package com.handsomelee.gotroute.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import com.handsomelee.gotroute.MainActivity;

import java.util.Date;
import java.util.UUID;

public class LocalDatabase extends SQLiteOpenHelper {
  
  public static final String DATABASE_NAME = "Gotroute.db";
  public static final String TABLE_NAME = "User_table";
  public static final String COL_1 = "ID";
  public static final String COL_2 = "DATE";
  public static final String COL_3 = "RefreshTime";
  
  public LocalDatabase(Context context) {
    super(context, DATABASE_NAME, null, 1);
  }
  
  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(String.format("create table %s ( ID TEXT PRIMARY KEY, Date DATE, RefreshTime LONG)", TABLE_NAME));
  }
  
  @Override
  public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    db.execSQL("");
  }
  
  public void createTable() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL(String.format("create table if not exists %s ( ID TEXT PRIMARY KEY, Date DATE, RefreshTime LONG)", TABLE_NAME));
    insertData(UUID.randomUUID().toString(), new Date().toString(), 30L);
  }
  
  public void dropTable() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("drop table " + TABLE_NAME);
    Log.v("drop", "das");
  }
  
  public void insertData(String id, String dateTime, Long refreshTime) {
//    Cursor cursor = getData();
//    if(!cursor.isNull(0)){
//      return;
//    }
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COL_1, id);
    contentValues.put(COL_2, dateTime);
    contentValues.put(COL_3, refreshTime);
    
    if (db.insert(TABLE_NAME, null, contentValues) == -1) {
      Toast.makeText(MainActivity.mActivity, "Failed Insert", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(MainActivity.mActivity, "Sucessed Insert", Toast.LENGTH_SHORT).show();
    }
  }
  
  public void updateRefreshTime(Long time) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COL_3, time);
    db.update(TABLE_NAME, contentValues, null, null);
  }
  
  public Cursor getData() {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
    return cursor;
  }
}
