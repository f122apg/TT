package com.tetsujin.tt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Week_TabFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_week, container, false);
        ListView lv = (ListView)v.findViewById(R.id.fragment_lv);

        //アクティビティから送られた値を受け取る
        Bundle args = getArguments();
        String arg_tab = args.getString("tab");
        String[][] value = new String[args.getInt("datalength")][];
        for (int i = 0; i < value.length; i ++)
        {
            value[i] = args.getStringArray("data" + i);
            System.out.println("ID:" + value[i][0] + ", 授業名:" + value[i][1] + ", 曜日:" + value[i][4]);
        }
        String[] weekname = new String[]
                {
                        getResources().getString(R.string.week_monday),
                        getResources().getString(R.string.week_tuesday),
                        getResources().getString(R.string.week_wednesday),
                        getResources().getString(R.string.week_thursday),
                        getResources().getString(R.string.week_friday)
                };

        Toast.makeText(this.getContext(), weekname[0], Toast.LENGTH_SHORT).show();
        CustomAdapter ca = new CustomAdapter(v.getContext(), value);
        lv.setAdapter(ca);


        //受け取った値を元に各曜日の処理をする
        //月
        if(arg_tab == weekname[0])
        {
            Toast.makeText(this.getContext(), weekname[0], Toast.LENGTH_SHORT).show();
        }//火
        else if(arg_tab == weekname[1])
        {
            Toast.makeText(this.getContext(), weekname[1], Toast.LENGTH_SHORT).show();
        }//水
        else if(arg_tab == weekname[2])
        {
            Toast.makeText(this.getContext(), weekname[2], Toast.LENGTH_SHORT).show();
        }//木
        else if(arg_tab == weekname[3])
        {
            Toast.makeText(this.getContext(), weekname[3], Toast.LENGTH_SHORT).show();
        }//金
        else if(arg_tab == weekname[4])
        {
            Toast.makeText(this.getContext(), weekname[4], Toast.LENGTH_SHORT).show();
        }

        return v;
    }
}
