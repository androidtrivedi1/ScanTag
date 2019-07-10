package com.notbytes.barcodereader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataHelper {

    SQLiteDatabase sqlitedatabase;
    Context context;
    SQLiteHelper sqlitehelper;

    String databasename = "test";
    public String TableName = "barcode";
    int database_version = 2;
    ArrayList<String> name_list = new ArrayList<String>();

    public ArrayList<DataClass> arraylistdata = new ArrayList<>();

    public DataHelper(Context c) {
        context = c;
    }

    public DataHelper openToRead() throws SQLiteException {
        sqlitehelper = new SQLiteHelper(context, databasename, null, database_version);
        sqlitedatabase = sqlitehelper.getReadableDatabase();
        return this;

    }

    public DataHelper openToWrite() throws SQLiteException {
        sqlitehelper = new SQLiteHelper(context, databasename, null, database_version);
        sqlitedatabase = sqlitehelper.getWritableDatabase();
        return this;
    }

    public void close() {
        sqlitedatabase.close();
    }

    public long insert(String item_no,String gram) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("item_no", item_no);
        contentvalues.put("gram", gram);
        return sqlitedatabase.insert(TableName, null, contentvalues);
    }
    public float retriveTotal() {
        name_list.clear();
        float ff = 0f;
        String name;
        Cursor cursor = sqlitedatabase.rawQuery("select * from barcode'" +"';", null);
       /* try {
            if (cursor.moveToFirst()) {
                do {
                    str_name = cursor.getString(cursor.getColumnIndex("name"));
                }
                while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {

        } finally {
            cursor.close();
        }*/
        try {
            if (cursor.moveToFirst()) {
                do {
                    name = cursor.getString(cursor.getColumnIndex("gram"));

                    name_list.add(name);
                }
                while (cursor.moveToNext());
            }
            cursor.close();

            for(int i = 0;i<name_list.size();i++)
                ff = ff + (Float.parseFloat(name_list.get(i)));

        } catch (Exception e) {

        } finally {
            cursor.close();
        }
        return ff;

    }
    public ArrayList<DataClass> retrivedata() {

        arraylistdata.clear();
        Cursor cursor = sqlitedatabase.rawQuery("select * from barcode;", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    DataClass data = new DataClass();
                    data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    data.setItem_no(cursor.getString(cursor.getColumnIndex("item_no")));
                    data.setGram(cursor.getString(cursor.getColumnIndex("gram")));

                    arraylistdata.add(data);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            return arraylistdata;
        } catch (Exception e) {

        } finally {
            cursor.close();
        }
        return arraylistdata;
    }
    public boolean removeFromList(int id){
        try {
            ContentValues value = new ContentValues();
            int i = sqlitedatabase.delete(TableName, "id = '" + id + "'", null);
            if(i>0){
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name, CursorFactory factory,
                            int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("create table " + TableName + "(id INTEGER PRIMARY KEY,item_no text,gram text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + TableName);
            onCreate(db);
        }
    }
}