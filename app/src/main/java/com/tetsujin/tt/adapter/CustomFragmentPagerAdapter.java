package com.tetsujin.tt.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tetsujin.tt.FragmentWeek;
import com.tetsujin.tt.database.TimeTable;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter
{

    //各曜日の時間割データを一元管理
    private ArrayList<ArrayList<String[]>> weekdata = new ArrayList<>();
    //日付データ
    private ArrayList<String> datedata = new ArrayList<>();

    //コンストラクタ
    public CustomFragmentPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    //時間割データを各曜日のタブへ送る
    @Override
    public Fragment getItem(int position)
    {
        ArrayList<String[]> weekdata = weekdata.get(position);

        //Fragmentへデータを送信
        Bundle bundle = new Bundle();
        bundle.putInt("datalength", weekdata.size());
        for (int i = 0; i < weekdata.size(); i ++)
        {
            bundle.putStringArray("data" + i, weekdata.get(i));
        }

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
        return datedata.get(position);
    }

    //時間割データをセット
    public void setdata(Serializable data)
    {

    }

    //日付データをセット
    public void adddate(String date)
    {
        datedata.add(date);
    }
}
