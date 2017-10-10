package com.example.wufan.danciben.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wufan.danciben.Fragment.DanFragment;
import com.example.wufan.danciben.Fragment.SouFragment;
import com.example.wufan.danciben.R;
import com.example.wufan.danciben.Sqlite.SQLUtil;
import com.example.wufan.danciben.Sqlite.wordsDBHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    wordsDBHelper mDbHelper;
    private SQLUtil sqlUtil;
    private Button buttonSou;
    private TextView w,y,e;
    private TextView tianjia;
    DanFragment Dan = new DanFragment();
    SouFragment Sou = new SouFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int mCurrentOrientation = getResources().getConfiguration().orientation;
        sqlUtil = new SQLUtil(getApplicationContext());
        FragmentManager fragmentManager = getSupportFragmentManager();








        if(mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT)//shu
        {
            setContentView(R.layout.activity_main);
            fragmentManager.beginTransaction().replace(R.id.fragmentView, Dan).commit();
            buttonSou = (Button)findViewById(R.id.button_sou);
            buttonSou.setOnClickListener(this);

        }
        else if(mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE)//heng
        {
            setContentView(R.layout.activity_main_heng);
            tianjia = (TextView)findViewById(R.id.result_shoucang);
            tianjia.setOnClickListener(this);
            fragmentManager.beginTransaction().replace(R.id.fragmentView, Sou).commit();

            tianjia.setVisibility(tianjia.INVISIBLE);
        }

        w = (TextView)findViewById(R.id.result_word) ;
        y = (TextView)findViewById(R.id.result_yinbiao);
        e = (TextView)findViewById(R.id.result_jieshi);





        mDbHelper = new wordsDBHelper(this);



    }

    public void setTextView(String ww,String wy,String we){
        w.setText(ww);
        y.setText(wy);
        e.setText(we);
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        int mCurrentOrientation = getResources().getConfiguration().orientation;
        if(mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {//heng
            switch (view.getId()) {
                case R.id.result_shoucang:
                    tianjia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            InsertDialog();
                        }
                    });
                    break;
            }
        }

        else if(mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT){///shu
            switch (view.getId()) {
                case R.id.button_sou:
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    startActivity(intent);

                    break;
            }
        }




        fragmentTransaction.commit();
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



    protected void onDestroy() {
        super.onDestroy();
        sqlUtil.des();
    }

    private void InsertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("确定添加单词到单词本?").setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord = ((TextView) findViewById(R.id.result_word)).getText().toString();
                        String strYinBiao = ((TextView) findViewById(R.id.result_yinbiao)).getText().toString();
                        String strMeaning = ((TextView) findViewById(R.id.result_jieshi)).getText().toString();
                        sqlUtil.InsertUserSql(strWord, strYinBiao, strMeaning);

                        Sou.ref();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    public void shouTianjia(){
        tianjia.setVisibility(tianjia.VISIBLE);
    }
    public void notshouTianjia(){
        tianjia.setVisibility(tianjia.INVISIBLE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //1.点击返回键条件成立
        if(keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 1)

            System.exit(-1);

        else if(keyCode == KeyEvent.KEYCODE_BACK  && event.getAction() == KeyEvent.ACTION_DOWN)
            Toast.makeText(MainActivity.this,"长按返回键退出",Toast.LENGTH_SHORT).show();


        return true;
    }



}
