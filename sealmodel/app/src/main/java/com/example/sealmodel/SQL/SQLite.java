package com.example.sealmodel.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLite extends SQLiteOpenHelper {
    String TAG = SQLite.class.getSimpleName();
    String TableName;


    public SQLite(@Nullable Context context
            , @Nullable String dataBaseName
            , @Nullable SQLiteDatabase.CursorFactory factory, int version, String TableName) {
        super(context, dataBaseName, factory, version);
        this.TableName = TableName;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLTable = "CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "stampCount TEXT, " +
                "stampId1 TEXT," +
                "stampId2 TEXT," +
                "stampId3 TEXT," +
                "stampId4 TEXT," +
                "stampId5 TEXT," +
                "stampId6 TEXT" +
                ");";
        db.execSQL(SQLTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL = "DROP TABLE " + TableName;
        db.execSQL(SQL);
    }

    /*=======================================自定義方法區↓=======================================*/

    //檢查資料表狀態，若無指定資料表則新增
    public void chickTable(){
        Cursor cursor = getWritableDatabase().rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0){
                getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "stampCount TEXT, " +
                        "stampId1 TEXT," +
                        "stampId2 TEXT," +
                        "stampId3 TEXT," +
                        "stampId4 TEXT," +
                        "stampId5 TEXT," +
                        "stampId6 TEXT" +
                        ");");


            }

            cursor.close();
        }

    }
    //取得有多少資料表
    public ArrayList<String> getTables(){
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master", null);
        ArrayList<String> tables = new ArrayList<>();
        while (cursor.moveToNext()){
            String getTab = new String (cursor.getBlob(0));
            if (getTab.contains("android_metadata")){}
            else if (getTab.contains("sqlite_sequence")){}
            else tables.add(getTab.substring(0,getTab.length()-1));

        }
        return tables;
    }
    //新增資料
    public void addData(String stampCount,String stampId1,String stampId2,String stampId3,String stampId4,String stampId5,String stampId6) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stampCount",stampCount);
        values.put("stampId1",stampId1);
        values.put("stampId2",stampId2);
        values.put("stampId3",stampId3);
        values.put("stampId4",stampId4);
        values.put("stampId5",stampId5);
        values.put("stampId6",stampId6);
        db.insert(TableName, null, values);
    }

    //顯示所有資料
    public ArrayList<HashMap<String, String>> showAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName, null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String id = c.getString(0);
            String stampCount = c.getString(c.getColumnIndex("stampCount"));
            String stampId1 = c.getString(c.getColumnIndex("stampId1"));
            String stampId2 = c.getString(c.getColumnIndex("stampId2"));
            String stampId3 = c.getString(c.getColumnIndex("stampId3"));
            String stampId4 = c.getString(c.getColumnIndex("stampId4"));
            String stampId5 = c.getString(c.getColumnIndex("stampId5"));
            String stampId6 = c.getString(c.getColumnIndex("stampId6"));
            hashMap.put("id", id);
            hashMap.put("stampCount",stampCount);
            hashMap.put("stampId1",stampId1);
            hashMap.put("stampId2",stampId2);
            hashMap.put("stampId3",stampId3);
            hashMap.put("stampId4",stampId4);
            hashMap.put("stampId5",stampId5);
            hashMap.put("stampId6",stampId6);
            arrayList.add(hashMap);
        }
        return arrayList;
    }
    //以id搜尋特定資料
    public ArrayList<HashMap<String,String>> searchById(String getId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE _id =" + "'" + getId + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String id = c.getString(0);
            String stampCount = c.getString(1);
            String stampId1 = c.getString(2);
            String stampId2 = c.getString(3);
            String stampId3 = c.getString(4);
            String stampId4 = c.getString(5);
            String stampId5 = c.getString(6);
            String stampId6 = c.getString(7);
            hashMap.put("id", id);
            hashMap.put("stampCount",stampCount);
            hashMap.put("stampId1",stampId1);
            hashMap.put("stampId2",stampId2);
            hashMap.put("stampId3",stampId3);
            hashMap.put("stampId4",stampId4);
            hashMap.put("stampId5",stampId5);
            hashMap.put("stampId6",stampId6);
            arrayList.add(hashMap);
        }
        return arrayList;
    }




    //修改資料(簡單)
    public void modifyEZ(String id, String stampCount,String stampId1,String stampId2,String stampId3,String stampId4,String stampId5,String stampId6) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stampCount",stampCount);
        values.put("stampId1",stampId1);
        values.put("stampId2",stampId2);
        values.put("stampId3",stampId3);
        values.put("stampId4",stampId4);
        values.put("stampId5",stampId5);
        values.put("stampId6",stampId6);

        db.update(TableName, values, "_id = " + id, null);
    }
    //刪除全部資料
    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM"+TableName);
    }
    //以id刪除資料(簡單)
    public void deleteByIdEZ(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName,"_id = " + id,null);
    }

    /*=======================================自定義方法區↑=======================================*/

}
