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

public class CustomHorizontalListViewAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;

    public CustomHorizontalListViewAdapter(Context context)
    {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    //アイテムの個数を返す
    @Override
    public int getCount()
    {
        return 1;
    }

    //アイテムを返す
    @Override
    public Object getItem(int position)
    {
        return 0;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    //ListViewの構成を行う
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = this.inflater.inflate(R.layout.horizontallistview_items, null);
        }

        return convertView;
    }
}
