package com.example.wufan.danciben.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wufan.danciben.Activity.MainActivity;
import com.example.wufan.danciben.R;
import com.example.wufan.danciben.Sqlite.Words;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by wufan on 2017-9-28.
 */

public class My_adapter extends BaseAdapter {
    private LinkedList<Map<String, Object>> linkedList;
    private LayoutInflater inflater;
    private Context context;
    private String ww, wy, wj;

    public My_adapter(LinkedList<Map<String, Object>> linkedList, Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.linkedList = linkedList;
    }

    @Override
    public int getCount() {
        if (linkedList == null) {
            return 0;
        } else
            return linkedList.size();
    }

    @Override
    public Object getItem(int i) {
        return linkedList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Vholder vholder = null;
        if (view == null) {
            vholder = new Vholder();
            view = inflater.inflate(R.layout.item1, null);
            vholder.textView1 = view.findViewById(R.id.textViewWord);
            vholder.textView2 = view.findViewById(R.id.textViewMeaning);
            view.setTag(vholder);
        } else {
            vholder = (Vholder) view.getTag();
        }
        vholder.textView1.setText(linkedList.get(i).get(Words.Word.COLUMN_NAME_WORD) + "");
        vholder.textView2.setText(linkedList.get(i).get(Words.Word.COLUMN_NAME_MEANING) + "");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ww = (String) linkedList.get(i).get(Words.Word.COLUMN_NAME_WORD);
                wy = (String) linkedList.get(i).get(Words.Word.COLUMN_NAME_MEANING);
                wj = (String) linkedList.get(i).get(Words.Word.COLUMN_NAME_SAMPLE);

            }
        });
        return view;
    }

    public class Vholder {
        private TextView textView1;
        private TextView textView2;
    }

    public void setLinkedList(LinkedList<Map<String, Object>> linkedList) {
        this.linkedList = linkedList;
        notifyDataSetChanged();
    }

}
