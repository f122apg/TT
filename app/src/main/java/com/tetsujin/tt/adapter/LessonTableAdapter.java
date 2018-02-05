package com.tetsujin.tt.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tetsujin.tt.R;

public class LessonTableAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[][] items;

    public LessonTableAdapter(Context context, String[][] objects)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.items = objects;
    }

    public int getCount()
    {
        //曜日分 + 二次元配列の長さ * 一次元配列の長さ
        return 5 + items.length * items[0].length;
    }

    public String getItem(int position)
    {
        switch (position)
        {
            case 0:
                return "月";
            case 1:
                return "火";
            case 2:
                return "水";
            case 3:
                return "木";
            case 4:
                return "金";
            default:
                return items[(position - 5) / 5][(position - 5) % 5];
        }
    }

    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView tv;
    
        //Viewが再利用できるならばそれを利用する
        if (convertView == null)
            tv = new TextView(context);
        else
            tv = (TextView)convertView;
        
        tv.setText(getItem(position));
        tv.setWidth(context.getResources().getDimensionPixelSize(R.dimen.size_50dp));
        tv.setHeight(context.getResources().getDimensionPixelSize(R.dimen.size_50dp));
        tv.setGravity(Gravity.CENTER);
        //タップされた時のアニメーションを無効化するために、clickableをtrueにする
        tv.setClickable(true);
        
        return tv;
    }
}