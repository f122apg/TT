package com.tetsujin.tt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tetsujin.tt.adapter.CustomListViewAdapter;

public class FragmentWeek extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_week, container, false);
        ListView lv = (ListView)v.findViewById(R.id.FrgWeek_listview);

        //CustomFragmentPagerAdapterから送られた値を受け取る
        Bundle args = getArguments();
        String[][] value = new String[args.getInt("datalength")][];
        for (int i = 0; i < value.length; i ++)
        {
            value[i] = args.getStringArray("data" + i);
            System.out.println("ID:" + value[i][0] + ", 授業名:" + value[i][1] + ", 曜日:" + value[i][4]);
        }

        //ListViewに現在のデータを適用
        CustomListViewAdapter ca = new CustomListViewAdapter(v.getContext(), value);
        lv.setAdapter(ca);

        return v;
    }
}
