package com.tetsujin.tt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

import java.util.ArrayList;

public class WeekActivity extends FragmentActivity implements TabHost.OnTabChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        //mainactivityから渡された値を受け取る
        Intent intent = getIntent();
        ArrayList<String[]> monday_value = new ArrayList<>();
        ArrayList<String[]> tuesday_value = new ArrayList<>();
        ArrayList<String[]> wednesday_value = new ArrayList<>();
        ArrayList<String[]> thursday_value = new ArrayList<>();
        ArrayList<String[]> friday_value = new ArrayList<>();

        for(int i = 0; i < intent.getIntExtra("datalength", 0); i ++)
        {
            String[] tmpvalue = intent.getStringArrayExtra("data" + i);
            //月
            if(tmpvalue[4].equals(getResources().getString(R.string.week_monday)))
                monday_value.add(tmpvalue);
            //火
            else if(tmpvalue[4].equals(getResources().getString(R.string.week_tuesday)))
                tuesday_value.add(tmpvalue);
            //水
            else if(tmpvalue[4].equals(getResources().getString(R.string.week_wednesday)))
                wednesday_value.add(tmpvalue);
            //木
            else if(tmpvalue[4].equals(getResources().getString(R.string.week_thursday)))
                thursday_value.add(tmpvalue);
            //金
            else if(tmpvalue[4].equals(getResources().getString(R.string.week_friday)))
                friday_value.add(tmpvalue);
        }

        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.content);

        TabHost.TabSpec[] tab = new TabHost.TabSpec[5];
        Bundle[] bundle = new Bundle[5];
        String[] weekname = new String[]
                {
                        getResources().getString(R.string.week_monday),
                        getResources().getString(R.string.week_tuesday),
                        getResources().getString(R.string.week_wednesday),
                        getResources().getString(R.string.week_thursday),
                        getResources().getString(R.string.week_friday)
                };

        //曜日ごとのタブを生成
        for(int i = 0; i < tab.length; i ++)
        {
            //タブのインスタンス生成
            tab[i] = tabHost.newTabSpec(weekname[i]);
            //タブに識別子を付ける
            tab[i].setIndicator(weekname[i]);
            //タブ毎に値を渡す
            bundle[i] = new Bundle();
            bundle[i].putString("tab", weekname[i]);
            //月
            if(i == 0)
            {
                bundle[i].putInt("datalength", monday_value.size());
                for(int j = 0; j < monday_value.size(); j ++)
                {
                    bundle[i].putStringArray("data" + j, monday_value.get(j));
                }
            }
            //火
            else if(i == 1)
            {
                bundle[i].putInt("datalength", tuesday_value.size());
                for(int j = 0; j < tuesday_value.size(); j ++)
                {
                    bundle[i].putStringArray("data" + j, tuesday_value.get(j));
                }
            }
            //水
            else if(i == 2)
            {
                bundle[i].putInt("datalength", wednesday_value.size());
                for(int j = 0; j < wednesday_value.size(); j ++)
                {
                    bundle[i].putStringArray("data" + j, wednesday_value.get(j));
                }
            }
            //木
            else if(i == 3)
            {
                bundle[i].putInt("datalength", thursday_value.size());
                for(int j = 0; j < thursday_value.size(); j ++)
                {
                    bundle[i].putStringArray("data" + j, thursday_value.get(j));
                }
            }
            //金
            else if(i == 4)
            {
                bundle[i].putInt("datalength", friday_value.size());
                for(int j = 0; j < friday_value.size(); j ++)
                {
                    bundle[i].putStringArray("data" + j, friday_value.get(j));
                }
            }

            tabHost.addTab(tab[i], Week_TabFragment.class, bundle[i]);
        }

        tabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String tabId) {
        Log.d("onTabChanged", "tabId: " + tabId);
    }
}
