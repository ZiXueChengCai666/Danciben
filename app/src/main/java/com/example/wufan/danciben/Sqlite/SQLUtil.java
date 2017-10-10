package com.example.wufan.danciben.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by wufan on 2017-9-24.
 */

public class SQLUtil {
    private Context context;
    private wordsDBHelper wordsDBHelper;

    public SQLUtil(Context context) {
        this.context = context;
        wordsDBHelper = new wordsDBHelper(context);
    }
    public void InsertUserSql(String strWord,String strMeaning,String strSample){
        try {
            String sql = "insert into words(word,meaning,sample) values(?,?,?)";
            SQLiteDatabase db = wordsDBHelper.getWritableDatabase();
            db.execSQL(sql, new String[]{strWord, strMeaning, strSample});
        }
        catch (Exception e){
            Toast.makeText(context,"单词已存在",Toast.LENGTH_LONG).show();
        }
    }
    public void DeleteUserSql(String str){

        SQLiteDatabase db = wordsDBHelper.getWritableDatabase();
         String selection = Words.Word.COLUMN_NAME_WORD + " = ?";
        String[] selectionArgs = {str};
        db.delete(Words.Word.TABLE_NAME, selection, selectionArgs);
    }


    public LinkedList<Map<String,Object>> SelectUserSql(){
        LinkedList<Map<String,Object>> linkedList = new LinkedList<>();
        SQLiteDatabase db = wordsDBHelper.getWritableDatabase();
        Cursor c=db.query(Words.Word.TABLE_NAME, new String[]{Words.Word.COLUMN_NAME_WORD, Words.Word.COLUMN_NAME_MEANING,Words.Word.COLUMN_NAME_SAMPLE}, null, null, null, null , Words.Word.COLUMN_NAME_WORD,null);
        while (c.moveToNext()) {
            Map<String, Object> map = new HashMap<>();
            map.put(Words.Word.COLUMN_NAME_WORD, c.getString(c.getColumnIndex(Words.Word.COLUMN_NAME_WORD)));
            map.put(Words.Word.COLUMN_NAME_MEANING, c.getString(c.getColumnIndex(Words.Word.COLUMN_NAME_MEANING)));
            map.put(Words.Word.COLUMN_NAME_SAMPLE, c.getString(c.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE)));
            Log.v("siodjijfifcg",c.getString(c.getColumnIndex(Words.Word.COLUMN_NAME_WORD))+c.getString(c.getColumnIndex(Words.Word.COLUMN_NAME_MEANING)));
            linkedList.add(map);
        }
        return linkedList;
    }

    public void DeleteAllUserSql(){
        SQLiteDatabase db = wordsDBHelper.getWritableDatabase();
        String str = "delete  from " + Words.Word.TABLE_NAME;
        db.execSQL(str);
    }

















    public  void des(){
        wordsDBHelper.close();
    }
}
