package com.tetsujin.tt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.GridView;

import com.tetsujin.tt.adapter.CustomGridViewAdapter;
import com.tetsujin.tt.adapter.CustomListViewAdapter;

import java.util.Calendar;

public class FragmentMonth extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.fragment_month, container, false);

        String[][] hyou =
                {
                        {"日", "月", "火", "水", "木", "金", "土"},

                        {"", "", "", "", "", "1", "2"},
                        {"3", "4", "5", "6", "7", "8", "9"},
                        {"10", "11", "12", "13", "14", "15", "16"},
                        {"17", "18", "19", "20", "21", "22", "23"},
                        {"24", "25", "26", "27", "28", "29", "30"},
                        {"31", "", "", "", "", "", ""}
                };

        GridView gv = (GridView)v.findViewById(R.id.gv);
        CustomGridViewAdapter cgva = new CustomGridViewAdapter(getContext(), hyou);
        gv.setAdapter(cgva);

        return v;
    }

}