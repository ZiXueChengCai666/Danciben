package com.example.wufan.danciben.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.wufan.danciben.Activity.MainActivity;
import com.example.wufan.danciben.Activity.SearchActivity;
import com.example.wufan.danciben.R;
import com.example.wufan.danciben.Sqlite.SQLUtil;
import com.example.wufan.danciben.Sqlite.Words;
import com.example.wufan.danciben.Sqlite.wordsDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class DanFragment extends Fragment {
    private ListView list;
    private SQLUtil sqlUtil;
    SQLiteDatabase db ;
    private wordsDBHelper wordsDBHelper;
    LinkedList<Map<String,Object>> linkedList;

    public DanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            View DanFragmentView = inflater.inflate(R.layout.fragment_dan, container, false);


            list = (ListView) DanFragmentView.findViewById(R.id.lstWords);
            registerForContextMenu(list);
            wordsDBHelper = new wordsDBHelper(getActivity());
            sqlUtil = new SQLUtil(getActivity());

            setWordsListView();
        return DanFragmentView;
    }

    private void setWordsListView() {

        linkedList = sqlUtil.SelectUserSql();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), linkedList, R.layout.item1,
                new String[]{
                        Words.Word.COLUMN_NAME_WORD,
                        Words.Word.COLUMN_NAME_MEANING,
                },
                new int[]{
                        R.id.textViewWord,
                        R.id.textViewMeaning,
                });
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String w = linkedList.get(i).get(Words.Word.COLUMN_NAME_WORD) + "";
                String y = linkedList.get(i).get(Words.Word.COLUMN_NAME_MEANING) + "";
                String j = linkedList.get(i).get(Words.Word.COLUMN_NAME_SAMPLE) + "";

                int mCurrentOrientation = getResources().getConfiguration().orientation;

                if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {///shu
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    intent.putExtra("w", w);
                    intent.putExtra("y", y);
                    intent.putExtra("j", j);
                    startActivity(intent);
                }
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int ii, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("确定删除单词?").setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String w = linkedList.get(ii).get(Words.Word.COLUMN_NAME_WORD) + "";
                        sqlUtil.DeleteUserSql(w);
                        setWordsListView();
                    }
                })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

                return true;
            }


        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setWordsListView();
    }
}
