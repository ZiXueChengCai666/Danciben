package com.example.wufan.danciben.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wufan.danciben.Activity.SearchActivity;
import com.example.wufan.danciben.Gson.Glo;
import com.example.wufan.danciben.Gson.Jsonres;
import com.example.wufan.danciben.Gson.NetThread;
import com.example.wufan.danciben.Activity.MainActivity;
import com.example.wufan.danciben.R;
import com.example.wufan.danciben.Sqlite.SQLUtil;
import com.example.wufan.danciben.Sqlite.Words;
import com.example.wufan.danciben.Sqlite.wordsDBHelper;
import com.example.wufan.danciben.adapter.My_adapter;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by wufan on 2017-9-22.
 */

public class SouFragment extends Fragment implements
        TextView.OnEditorActionListener,
        View.OnClickListener,
        View.OnKeyListener
{



    private ListView list;
    private Button buttonsou;
    private EditText editTextsou;
    private String w,y,j;
    private SQLUtil sqlUtil;
    private wordsDBHelper wordsDBHelper;
    private  My_adapter my_adapter;
    private LinkedList<Map<String,Object>> c;
    SQLiteDatabase db ;



    public SouFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        wordsDBHelper = new wordsDBHelper(getActivity());
        View SouFragment = inflater.inflate(R.layout.fragment_sou,container,false);

        list = (ListView)SouFragment.findViewById(R.id.list_Words);
        buttonsou = (Button) SouFragment.findViewById(R.id.buttonsou);
        editTextsou = (EditText)SouFragment.findViewById(R.id.edittext_sou);

        editTextsou.setOnEditorActionListener(this);
        buttonsou.setOnClickListener(this);
        editTextsou.setOnKeyListener(this);
        MainActivity activity = (MainActivity) getActivity();


        db  = wordsDBHelper.getWritableDatabase();
        sqlUtil= new SQLUtil(activity);

        registerForContextMenu(list);
        setWordsListView();

        return SouFragment;
    }


    public void Jisuan(){
        String str = (editTextsou).getText().toString();
        MainActivity activity = (MainActivity) getActivity();
        NetThread netThread = new NetThread("http://www.cycycd.com/api/youdao");
        netThread.setWord(str);
        netThread.start();
        try {
            netThread.join();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        Gson gson = new Gson();
        try {
            Jsonres jsonres = gson.fromJson(Glo.getres, Jsonres.class);
            w = editTextsou.getText().toString();
            y = "/" + jsonres.phonetic + "/";
            j = jsonres.exp;
            activity.shouTianjia();
        }
        catch (Exception ee)
        {
            w = "没有搜索结果";
            y = "";
            j = "";
            activity.notshouTianjia();
        }
    }

    private void setWordsListView() {

        c = sqlUtil.SelectUserSql();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), c, R.layout.item1,
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
                 w = c.get(i).get(Words.Word.COLUMN_NAME_WORD) + "";
                 y = c.get(i).get(Words.Word.COLUMN_NAME_MEANING) +"";
                 j = c.get(i).get(Words.Word.COLUMN_NAME_SAMPLE) +"";

                int mCurrentOrientation = getResources().getConfiguration().orientation;
                if(mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {//heng
                    MainActivity activity = (MainActivity)getActivity();
                    activity.setTextView(w,y,j);
                    activity.notshouTianjia();

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
                        w = c.get(ii).get(Words.Word.COLUMN_NAME_WORD) + "";
                        sqlUtil.DeleteUserSql(w);
                        ref();


                    }
                })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

                return false;
            }

        });
    }

    @Override
    public void onClick(View view) {
        int mCurrentOrientation = getResources().getConfiguration().orientation;
        if(!editTextsou.getText().toString().equals("")) {
            if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {  //heng
                Jisuan();
                MainActivity activity = (MainActivity) getActivity();
                activity.setTextView(w, y, j);
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return (keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER) {
            if(!editTextsou.getText().toString().equals("")) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                Jisuan();
                MainActivity activity = (MainActivity) getActivity();
                activity.setTextView(w, y, j);
                if (imm.isActive()) {

                    imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    return true;
                }
            }
        }
        return false;
    }


    public void ref(){
        setWordsListView();

    }



}
