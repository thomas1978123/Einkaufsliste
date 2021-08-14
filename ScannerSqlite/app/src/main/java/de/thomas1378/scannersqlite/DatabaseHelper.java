package de.thomas1378.scannersqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Name der Database
    public static final String DATABASE_NAME = "goods.db";
    //Name der Tabelle
    public static final String TABLE_NAME = "goods_table";
    //Spalten
    public static final String COL_1 = "ID";
    public static final String COL_2 = "SCANID";
    public static final String COL_3 = "GOODS";
    public static final String COL_4 = "FLAG";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +TABLE_NAME+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SCANID TEXT, GOODS TEXT, FLAG TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String scanid, String goods){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,scanid);
        contentValues.put(COL_3,goods);
        long result = db.insert(TABLE_NAME,null,contentValues);

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insertName(String goods){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3,goods);
        long result = db.insert(TABLE_NAME,null,contentValues);

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " +TABLE_NAME, null);
        return res;
    }

    public Cursor getId(String goodsL) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " WHERE " +COL_3+"='"+goodsL+"' ", null);
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + "  WHERE ("+COL_3+") like lower('%"+goodsL+"%') ", null);

        return res;
    }

    public Cursor getIdFlag(String goodsL) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " WHERE " +COL_3+"='"+goodsL+"' ", null);
        //Cursor res = db.rawQuery("Select * from " + TABLE_NAME + "  WHERE ("+COL_3+") like lower('%"+goodsL+"%') ", null);

        return res;
    }

    public Cursor getAllD(String goodsD) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " WHERE " +COL_1+"='"+goodsD+"' ", null);
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " WHERE lower (\" WHERE goods='\"+goods+\"' \") like lower('%"+goodsD+"%') ", null);

        return res;
    }

    public Cursor getGoodsScan(String goodsScan) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " WHERE " +COL_2+"='"+goodsScan+"' ", null);
        return res;
    }

    public Cursor getFlag1(String goodsL) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " WHERE " +COL_2+"='"+goodsL+"' ", null);
        return res;
    }

    public Cursor getGoods(String goods){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " +TABLE_NAME+ " WHERE goods='"+goods+"' ", null);

        return res;
    }

    public boolean updateFlag0(String id, String flag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_4,flag);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});
        return true;
    }

    public boolean updateFlag1(String id, String flag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_4,flag);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});
        return true;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});

    }

    public Integer delete(String id1){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id1});
        //return db.delete(TABLE_NAME, "ID = '"+i+"'",new String[] {i});
        //return db.delete(TABLE_NAME, null,null);
        //return db.delete(TABLE_NAME, "GOODS = ?",new String[] {goods});

    }

    public Integer deleteAll(String id1){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null,null);
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

    public boolean checkScanIdIsOkay(String scanid){
        boolean okay = false;
        SQLiteDatabase db = this.getWritableDatabase();
        //Count wird als Interger zurückgegeben
        Cursor res = db.rawQuery("Select COUNT(*) FROM " +TABLE_NAME+ " WHERE scanid='"+scanid+"' ", null);
        res.moveToFirst();
        if(res.getInt(0) == 0){
            okay = true;
        }
        res.close();
        db.close();
        return okay;
    }

}
