package com.tetsujin.tt.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tetsujin.tt.FragmentMain;
import com.tetsujin.tt.R;
import com.tetsujin.tt.database.TimeTable;

import java.util.Arrays;
import java.util.Calendar;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static com.tetsujin.tt.ActivityMain.activityMain;
import static com.tetsujin.tt.ActivityMain.timeTable;

public class LessonListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private TimeTable[] items;

    public LessonListAdapter(Context context, TimeTable[] objects)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.items = objects;
    }

    public int getCount()
    {
        //曜日分：5 と 時間割データ分
        return 5 + items.length;
    }

    public Object getItem(int position)
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
                return items[position - 5];
        }
    }

    public long getItemId(int position)
    {
        if(position >= 5)
            return items[position - 5].getTimeTableID();
        else
            return -1;
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

        //getItemの返り値がString(曜日)であった場合そのまま文字列を設定し、
        //Stringではない場合、TimeTableなのでgetterを用いて授業名を取得して設定
        if(getItem(position) instanceof String)
            tv.setText((String)getItem(position));
        else
        {
            TimeTable data = (TimeTable)getItem(position);

            if(data.getWeekDay() == Calendar.WEDNESDAY
                    && position % 5 == 2)
            {
                tv.setText(data.getLessonName());
            }
        }

        tv.setWidth(context.getResources().getDimensionPixelSize(R.dimen.width_height_50dp));
        tv.setHeight(context.getResources().getDimensionPixelSize(R.dimen.width_height_50dp));
        tv.setGravity(Gravity.CENTER);
        
        return tv;
    }
}