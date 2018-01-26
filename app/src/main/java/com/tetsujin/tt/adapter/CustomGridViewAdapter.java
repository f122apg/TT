package com.tetsujin.tt.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tetsujin.tt.R;

public class CustomGridViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[][] items;

    public CustomGridViewAdapter(Context context, String[][] objects)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.items = objects;
    }

    public int getCount() {
        return 49;
    }

    public Object getItem(int position) {
        return items[position / 7][position % 7];
    }

    public long getItemId(int position) {
        return position;
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

        tv.setText((String) getItem(position));
        //TextViewの位置を調整
        tv.setWidth(context.getResources().getDimensionPixelSize(R.dimen.Width_Height_50dp));
        tv.setHeight(context.getResources().getDimensionPixelSize(R.dimen.Width_Height_50dp));
        tv.setGravity(Gravity.CENTER);

        return tv;
    }
}