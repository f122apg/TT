package com.tetsujin.tt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tetsujin.tt.R;

public class NotificationListAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private String[][] items;

    public NotificationListAdapter(Context context, String[][] objects)
    {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        if(objects != null)
        {
            items = objects;
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
    public String[] getItem(int position)
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = this.inflater.inflate(R.layout.nflistview_items, null);
        }
        
        //itemsがnullではないならば時間割の表示を行う
        if(this.items != null)
        {
            String[] item = this.items[position];

            TextView sendDate = (TextView) convertView.findViewById(R.id.senddate_item_textview);
            TextView sender = (TextView) convertView.findViewById(R.id.sender_item_textview);
            TextView title = (TextView) convertView.findViewById(R.id.title_item_textview);
    
            sendDate.setText(item[0]);
            sender.setText(item[1]);
            title.setText(item[2]);
        }
        else
        {
            TextView sendDate = (TextView) convertView.findViewById(R.id.senddate_item_textview);
            TextView sender = (TextView) convertView.findViewById(R.id.sender_item_textview);
            TextView title = (TextView) convertView.findViewById(R.id.title_item_textview);
    
            sendDate.setVisibility(View.INVISIBLE);
            sender.setVisibility(View.INVISIBLE);
            title.setText("お知らせはありません。");
        }

        return convertView;
    }
}
