package com.example.buffalogrillapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class MenuDataBase {

        public final static int SYNC_OK=1;
        public final static int SYNC_FAILED=0;

        public final static String TABLE_NAME="Menu";
        public final static String NAME="name";
        public final static String CATEGORY="category";
        public final static String DURE_DE_VIE="duré_d_vie";
        public final static String MODE="mode_d_emploie";
        public final static String TEMPERATURE="temperature";

        public final static String[] menuColumns = {
                "id",
                NAME,
                CATEGORY,
                DURE_DE_VIE,
                MODE,
                TEMPERATURE,
        };

        public static void InsertData(SQLiteDatabase db, String name,String category, String duré, String mode, int tmp){
            ContentValues values=new ContentValues();
            values.put(MenuDataBase.NAME,name);
            values.put(MenuDataBase.CATEGORY,category);
            values.put(MenuDataBase.DURE_DE_VIE,duré);
            values.put(MenuDataBase.MODE,mode);
            values.put(MenuDataBase.TEMPERATURE,tmp);

            db.insert(MenuDataBase.TABLE_NAME,null,values);
        }


        public static int UpdateProfile(String tableName,ContentValues values, SQLiteDatabase db, DBhelper helper,String[] columns) {

            db = helper.getReadableDatabase();

            int userid=0;
            Cursor cursor=db.query(tableName,columns,null,null,null,null,null,null);
            if(cursor.getCount()>0){

                while(cursor.moveToNext()) userid = cursor.getInt(cursor.getColumnIndex("id"));
            }

            // Which row to update, based on the userid column
            String selection =  "id = ?";
            String[] selectionArgs = { String.valueOf(userid) };

            int count = db.update(
                    tableName,
                    values,
                    selection,
                    selectionArgs);

            //Return how many rows updated
            return count;
        }

        public static Menu findMenu(String table,String column,String searchThis,DBhelper helper){
            String query = "Select * FROM " + table + " WHERE " + column
                    + " = \"" + searchThis + "\"";

            SQLiteDatabase db=helper.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            Menu menu=new Menu();
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                menu.setId(Integer.parseInt(cursor.getString(0)));
                menu.setName(cursor.getString(1));
                menu.setDuré_d_vie(cursor.getString(2));
                menu.setTemperature(cursor.getInt(4));
                cursor.close();
            } else {
                Log.d("Nazim","Nothing found");
                menu=null;
            }
            db.close();

            return menu;

        }
        public static void getUsers(String table, String column, String searchThis, DBhelper helper, ArrayList list){
            String query = "Select * FROM " + table + " WHERE " + column
                    + " = \"" + searchThis + "\"";

            SQLiteDatabase db=helper.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            Menu menu=new Menu();
            byte i=0;
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
//                user.setId(Integer.parseInt(cursor.getString(0)));
//                user.setName(cursor.getString(1));
//                user.setLinkdin(cursor.getString(9));
//                user.setBytes(cursor.getBlob(7));
                    list.add(cursor.getString(cursor.getColumnIndex("name")));
                    list.add(i,menu);
                    i++;
                    cursor.moveToNext();
                }
                cursor.close();
            } else {
                Log.d("Nazim","Nothing found");
                menu=null;
            }
            db.close();
        }
    }

