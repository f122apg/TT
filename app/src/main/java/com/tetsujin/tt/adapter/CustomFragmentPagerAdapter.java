package com.tetsujin.tt.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tetsujin.tt.FragmentWeek;

import java.util.ArrayList;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter
{

    //各曜日の時間割データを一元管理
    private ArrayList<ArrayList<String[]>> weekdatas = new ArrayList<>();
    //日付データ
    private ArrayList<String> datedatas = new ArrayList<>();

    //コンストラクタ
    public CustomFragmentPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    //時間割データを各曜日のタブへ送る
    @Override
    public Fragment getItem(int position)
    {
        ArrayList<String[]> weekdata = weekdatas.get(position);

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
        return datedatas.get(position);
    }

    //時間割データをセット
    public void setdatas(ArrayList<ArrayList<String[]>> datas)
    {
        weekdatas.addAll(datas);
    }

    //日付データをセット
    public void adddate(String date)
    {
        datedatas.add(date);
    }
}
