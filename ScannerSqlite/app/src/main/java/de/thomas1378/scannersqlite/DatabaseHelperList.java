package de.thomas1378.scannersqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelperList extends SQLiteOpenHelper {
    //Name der Database
    public static final String DATABASE_NAME = "listgoods.db";
    //Name der Tabelle
    public static final String TABLE_NAME = "listgoods_table";
    //Spalten
    public static final String COL_1 = "ID";
    public static final String COL_2 = "GOODS";


    public DatabaseHelperList(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, GOODS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertDataList(String goods) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, goods);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertName(String goods) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, goods);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor getGoods(String goods) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select GOODS from " + TABLE_NAME + " WHERE goods='" + goods + "' ", null);

        return res;
    }


    public Cursor getId(String goodsL) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " WHERE " +COL_2+"='"+goodsL+"' ", null);
        return res;
    }

    public boolean updateData(String id, String goods){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,goods);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});
        return true;
    }
    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});

    }

    public Integer delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID=?",new String[] {id});
        //return db.delete(TABLE_NAME, "GOODS='"+eintrag+"' ",new String[] {eintrag});

    }

    public Integer deleteF(String flag1){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "FLAG = ?"  ,new String[] {flag1});
        //"FLAG = ?"
        //new String[] {flag1}
    }

    public boolean checkGoodsIsOkay(String goods){
        boolean okay = false;
        SQLiteDatabase db = this.getWritableDatabase();
        //Count wird als Interger zurückgegeben
        Cursor res = db.rawQuery("Select COUNT(*) FROM " +TABLE_NAME+ " WHERE goods='"+goods+"' ", null);
        res.moveToFirst();
        if(res.getInt(0) == 0){
            okay = true;
        }
        res.close();
        db.close();
        return okay;
    }

    public boolean checkFlagIsOkay(String flag){
        boolean okay = false;
        SQLiteDatabase db = this.getWritableDatabase();
        //Count wird als Interger zurückgegeben
        Cursor res = db.rawQuery("Select COUNT(*) FROM " +TABLE_NAME+ " WHERE goods='"+flag+"' ", null);
        res.moveToFirst();
        if(res.getInt(0) == 0){
            okay = true;
        }
        res.close();
        db.close();
        return okay;
    }

}

