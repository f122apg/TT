package com.tetsujin.tt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tetsujin.tt.ActivityMain;
import com.tetsujin.tt.R;
import com.tetsujin.tt.database.TimeTable;

import java.util.ArrayList;

public class LessonListAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private TimeTable[] items;

    public LessonListAdapter(Context context, TimeTable[] objects, int weekday, boolean setPastTimeTable, boolean calledFromCFPAdapter)
    {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        //時間割データが存在しているかチェックし、存在していたら曜日毎のデータを抽出する
        if(objects != null)
        {
            //CustomFragmentPagerAdapterから呼ばれていない、かつ、
            //setPastTimeTableがfalseならば(過去の時間割を設定したくない)場合は
            //今日の時間割データだけを追加する
            if(!calledFromCFPAdapter && !setPastTimeTable)
            {
                //抽出した曜日データを一時的に格納するArrayList
                ArrayList<TimeTable> list = new ArrayList<>();
                //今日の曜日だけの時間割データを抽出
                for (TimeTable value : objects)
                {
                    //時間割データが現在の曜日と一致していたら"今日"の時間割データとして追加する
                    if (value.getWeekDayNumber() == Integer.parseInt(ActivityMain.getTodayWeekDay(true)))
                        list.add(value);
                }
    
                this.items = list.toArray(new TimeTable[list.size()]);
            }
            //過去の時間割データを設定する
            else if(setPastTimeTable)
            {
                //抽出した曜日データを一時的に格納するArrayList
                ArrayList<TimeTable> list = new ArrayList<>();
                //過去の曜日だけの時間割データを抽出
                for (TimeTable value : objects)
                {
                    //時間割データが指定した曜日と一致していたら"過去"の時間割データとして追加する
                    if (value.getWeekDayNumber() == weekday)
                        list.add(value);
                }
    
                this.items = list.toArray(new TimeTable[list.size()]);
            }
            //CustomFragmentPagerAdapterから渡された時間割データをすべてitemsに挿入
            else
            {
                this.items = objects;
            }
        }
        else
            this.items = null;
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = this.inflater.inflate(R.layout.listview_items, null);
        }
        
        //itemsがnullではないならば時間割の表示を行う
        if(this.items != null)
        {
            TimeTable item = this.items[position];

            TextView starttime = (TextView) convertView.findViewById(R.id.starttime_item_textview);
            TextView endtime = (TextView) convertView.findViewById(R.id.endtime_item_textview);
            TextView name = (TextView) convertView.findViewById(R.id.name_item_textview);

            starttime.setText(item.getStartTime());
            endtime.setText(item.getEndTime());
            name.setText(item.getLessonName());
        }

        return convertView;
    }
}
