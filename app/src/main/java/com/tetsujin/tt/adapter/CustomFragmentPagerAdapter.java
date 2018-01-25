package com.tetsujin.tt.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tetsujin.tt.FragmentWeek;
import com.tetsujin.tt.database.TimeTable;

import java.util.ArrayList;
import java.util.Calendar;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter
{

    //各曜日の時間割データを一括管理
    private ArrayList<ArrayList<TimeTable>> weeklist;
    //日付データ
    private ArrayList<String> datelist = new ArrayList<>();

    //コンストラクタ
    public CustomFragmentPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    //時間割データを各曜日のタブへ送る
    @Override
    public Fragment getItem(int position)
    {
        //Fragmentへデータを送信
        Bundle bundle = new Bundle();
        //特定の曜日の時間割データを送信
        bundle.putParcelableArrayList("timetable", weeklist.get(position));

        FragmentWeek fw = new FragmentWeek();
        fw.setArguments(bundle);

        return fw;
    }

    //タブの個数を返す
    @Override
    public int getCount()
    {
        return 5;
    }

    //タブの名前を返す
    @Override
    public CharSequence getPageTitle(int position)
    {
        return datelist.get(position);
    }

    //時間割データを各曜日ごとにソートしてセット
    public void setdata(TimeTable[] data)
    {
        //一時的に各曜日ごとにデータをセットする
        //0 = Monday, 1 = Tuesday...
        ArrayList<TimeTable>[] temp = new ArrayList[5];
        //初期化
        for(int i = 0; i < 5; i ++)
        {
            temp[i] = new ArrayList<>();
        }
        weeklist = new ArrayList<>();
        
        //各曜日の時間割データを各配列に挿入する
        for (TimeTable value : data)
        {
            switch (value.getWeekDay())
            {
                case Calendar.MONDAY:
                    temp[0].add(value);
                    break;
                case Calendar.TUESDAY:
                    temp[1].add(value);
                    break;
                case Calendar.WEDNESDAY:
                    temp[2].add(value);
                    break;
                case Calendar.THURSDAY:
                    temp[3].add(value);
                    break;
                case Calendar.FRIDAY:
                    temp[4].add(value);
                    break;
            }
        }
        
        //一時的にセットしたデータを一元管理用リストにセット
        for(int i = 0; i < 5; i ++)
        {
            weeklist.add(i, temp[i]);
        }
    }

    //日付データをセット
    public void adddate(String date)
    {
        datelist.add(date);
    }

    //日付がどこに挿入されているか検索する
    public int getPosFromDate(String date)
    {
        return datelist.indexOf(date);
    }
}
