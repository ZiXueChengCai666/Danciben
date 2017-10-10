package com.example.wufan.danciben.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wufan on 2017-9-23.
 */

public class wordsDBHelper extends SQLiteOpenHelper {
    private Context mcontext;
    private final static String DATABASE_NAME = "wordsdb";//数据库名字
    private final static int DATABASE_VERSION = 1;//数据库版本
    private final static String SQL_CREATE_DATABASE = "CREATE TABLE "
            + Words.Word.TABLE_NAME + " ("
            + Words.Word._ID   + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Words.Word.COLUMN_NAME_WORD + " TEXT unique" + ","
            + Words.Word.COLUMN_NAME_MEANING + " TEXT" + ","
            + Words.Word.COLUMN_NAME_SAMPLE + " TEXT" + " )";

    private final static String SQL_DELETE_DATABASE = "drop table if exists" + Words.Word.TABLE_NAME;

    public wordsDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        onCreate(sqLiteDatabase);
    }
}
