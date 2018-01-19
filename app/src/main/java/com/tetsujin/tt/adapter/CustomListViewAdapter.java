package com.tetsujin.tt.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tetsujin.tt.ActivityMain;
import com.tetsujin.tt.R;
import com.tetsujin.tt.database.TimeTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CustomListViewAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private TimeTable[] items;

    public CustomListViewAdapter(Context context, TimeTable[] objects)
    {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        //時間割データが存在しているかチェックし、存在していたら曜日毎のデータを抽出する
        if(objects != null)
        {
            //抽出した曜日データを一時的に格納するArrayList
            ArrayList<TimeTable> list = new ArrayList<>();
            //今日の曜日だけの時間割データを抽出
            for (TimeTable value : objects)
            {
                if (value.getWeekDay().equals(ActivityMain.getToDayWeekDay(false, null)))
                {
                    list.add(value);
                }
            }

            this.items = list.toArray(new TimeTable[list.size()]);
        }
        else
        {
            this.items = null;
        }
    }

    //アイテムの個数を返す
    @Override
    public int getCount()
    {
        return items.length;
    }

    //アイテムを返す
    @Override
    public Object getItem(int position)
    {
        return items[position];
    }

    @Override
    public long getItemId(int position)
    {
        return items[position].getTimeTableID();
    }

    //ListViewの構成を行う
    @Override
    public View getView(int postition, View convertView, ViewGroup parent)
    {
        //itemsがnullではないなら時間割データを表示する
        if(this.items != null)
        {
            TimeTable item = this.items[postition];

            TextView id = (TextView) convertView.findViewById(R.id.id_item_textview);
            TextView starttime = (TextView) convertView.findViewById(R.id.starttime_item_textview);
            TextView endtime = (TextView) convertView.findViewById(R.id.endtime_item_textview);
            TextView name = (TextView) convertView.findViewById(R.id.name_item_textview);

            id.setText(String.valueOf(item.getTimeTableID()));
            starttime.setText(item.getStartTime());
            endtime.setText(item.getEndTime());
            name.setText(item.getLessonName());
        }

        if (convertView == null)
        {
            convertView = this.inflater.inflate(R.layout.listview_items, null);
        }

        return convertView;
    }
}
