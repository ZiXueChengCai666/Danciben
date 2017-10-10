package com.example.wufan.danciben.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wufan.danciben.Gson.Glo;
import com.example.wufan.danciben.Gson.Jsonres;
import com.example.wufan.danciben.Gson.NetThread;
import com.example.wufan.danciben.R;
import com.example.wufan.danciben.Sqlite.SQLUtil;
import com.example.wufan.danciben.Sqlite.wordsDBHelper;
import com.google.gson.Gson;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener{

    private EditText search_edit;
    private Button search_button;
    TextView search_result_word,search_result_yinbiao,search_result_jieshi,search_result_shoucang;
    String w,y,j;
    private SQLUtil sqlUtil;
    wordsDBHelper mDbHelper;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sqlUtil = new SQLUtil(getApplicationContext());


        search_edit = (EditText)findViewById(R.id.search_edit);
        search_button = (Button)findViewById(R.id.search_button);
        search_result_word = (TextView)findViewById(R.id.search_result_word);
        search_result_yinbiao = (TextView)findViewById(R.id.search_result_yinbiao);
        search_result_jieshi = (TextView)findViewById(R.id.search_result_jieshi);
        search_result_shoucang = (TextView)findViewById(R.id.search_result_shoucang);

        search_button.setOnClickListener(this);
        search_result_shoucang.setOnClickListener(this);
        //search_edit.setOnEditorActionListener(this);
        search_edit.setOnKeyListener(this);

        mDbHelper = new wordsDBHelper(this);

        Intent intent = getIntent();
        w = intent.getStringExtra("w");
        y = intent.getStringExtra("y");
        j = intent.getStringExtra("j");
        search_result_shoucang.setVisibility(search_result_shoucang.INVISIBLE);


        setView();



    }

    public void jisuan(){


            String str = (search_edit).getText().toString();

            NetThread netThread = new NetThread("http://www.cycycd.com/api/youdao");
            netThread.setWord(str);
            netThread.start();
            try {
                netThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            try {
                Jsonres jsonres = gson.fromJson(Glo.getres, Jsonres.class);
                w = search_edit.getText().toString();
                y = "/" + jsonres.phonetic + "/";
                j = jsonres.exp;
                search_result_shoucang.setVisibility(search_result_shoucang.VISIBLE);

            }
            catch (Exception e) {
                w = "没有搜索结果";
                y="";
                j ="";

                search_result_shoucang.setVisibility(search_result_shoucang.INVISIBLE);
            }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.search_button:
                if(!search_edit.getText().toString().equals("")) {
                    jisuan();
                    setView();


                }
                break;
            case R.id.search_result_shoucang:
                    InsertDialog();
                break;
        }

    }

//    @Override
//    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//        return (keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER);
//
//    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER) {
            if(!search_edit.getText().toString().equals("")) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                jisuan();
                search_result_word.setText(w);
                search_result_yinbiao.setText(y);
                search_result_jieshi.setText(j);

                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                    return true;
                }
            }
        }
        return false;
    }

    private void InsertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setMessage("确定添加单词到单词本?").setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String strWord = ((TextView) findViewById(R.id.search_result_word)).getText().toString();
                String strYinBiao = ((TextView) findViewById(R.id.search_result_yinbiao)).getText().toString();
                String strMeaning = ((TextView) findViewById(R.id.search_result_jieshi)).getText().toString();
                sqlUtil.InsertUserSql(strWord, strYinBiao, strMeaning);
            }
        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public void setView(){
        search_result_word.setText(w);
        search_result_yinbiao.setText(y);
        search_result_jieshi.setText(j);
    }
}
