package com.tetsujin.tt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tetsujin.tt.R;
import com.tetsujin.tt.database.TimeTable;

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
        this.items = objects;
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
        return 0;
    }

    //ListViewの構成を行う
    @Override
    public View getView(int postition, View convertView, ViewGroup parent)
    {
        TimeTable item = this.items[postition];

        if(convertView == null)
        {
            convertView = this.inflater.inflate(R.layout.listview_items, null);
        }
        
        //TODO:TimeTableクラスからデータを取得するように書き換える
        TextView id = (TextView)convertView.findViewById(R.id.id_item_textview);
        TextView starttime = (TextView)convertView.findViewById(R.id.starttime_item_textview);
        TextView endtime = (TextView)convertView.findViewById(R.id.endtime_item_textview);
        TextView name = (TextView)convertView.findViewById(R.id.name_item_textview);

        id.setText(String.valueOf(item.getTimeTableID()));
        starttime.setText(item.getStartTime());
        endtime.setText(item.getEndTime());
        name.setText(item.getLessonName());

        return convertView;
    }
}
