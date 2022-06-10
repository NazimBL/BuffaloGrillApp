package com.example.buffalogrillapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBhelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private final static String DB_NAME="MenuDB.db";

    private static final String CREATE_TABLE="create table if not exists "+MenuDataBase.TABLE_NAME+
            " (id integer primary key autoincrement, "+
            MenuDataBase.NAME+" text, "+
            MenuDataBase.CATEGORY+" text, "+
            MenuDataBase.DURE_DE_VIE+" text, "+
            MenuDataBase.MODE+" text, "+
            MenuDataBase.TEMPERATURE+" integer )";


    private static final String DROP_TABLE="DROP TABLE IF EXISTS " + MenuDataBase.TABLE_NAME;


    public DBhelper(Context context){

        super(context,DB_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        Log.d("Nazim","user table Creadted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

}