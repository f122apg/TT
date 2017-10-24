package com.tetsujin.tt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private String[][] items;

    public CustomAdapter(Context context, String[][] objects)
    {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.items = objects;
    }

    @Override
    public int getCount()
    {
        return items.length;
    }

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

    @Override
    public View getView(int postition, View convertView, ViewGroup parent)
    {
        String[] item = this.items[postition];

        if(convertView == null)
        {
            convertView = this.inflater.inflate(R.layout.listview_items, null);
        }

        TextView id = (TextView)convertView.findViewById(R.id.id_item_textview);
        TextView starttime = (TextView)convertView.findViewById(R.id.starttime_item_textview);
        TextView endtime = (TextView)convertView.findViewById(R.id.endtime_item_textview);
        TextView name = (TextView)convertView.findViewById(R.id.name_item_textview);

        id.setText(item[0]);
        starttime.setText(item[2]);
        endtime.setText(item[3]);
        name.setText(item[1]);

        return convertView;
    }
}
