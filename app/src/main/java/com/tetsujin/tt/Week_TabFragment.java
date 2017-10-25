package com.tetsujin.tt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Week_TabFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //アクティビティから送られた値を受け取る
        Bundle source_args = getArguments();
        String args_str = source_args.getString("tab");
        String[] weekname = new String[]
                {
                        getResources().getString(R.string.week_monday),
                        getResources().getString(R.string.week_tuesday),
                        getResources().getString(R.string.week_wednesday),
                        getResources().getString(R.string.week_thursday),
                        getResources().getString(R.string.week_friday)
                };


        System.out.println(args_str);
        //受け取った値を元に各曜日の処理をする
        //月
//        if(args_str == weekname[0])
//        {
//            Toast.makeText(this.getContext(), weekname[0], Toast.LENGTH_SHORT).show();
//        }//火
//        else if(args_str == weekname[1])
//        {
//            Toast.makeText(this.getContext(), weekname[1], Toast.LENGTH_SHORT).show();
//        }//水
//        else if(args_str == weekname[2])
//        {
//            Toast.makeText(this.getContext(), weekname[2], Toast.LENGTH_SHORT).show();
//        }//木
//        else if(args_str == weekname[3])
//        {
//            Toast.makeText(this.getContext(), weekname[3], Toast.LENGTH_SHORT).show();
//        }//金
//        else if(args_str == weekname[4])
//        {
//            Toast.makeText(this.getContext(), weekname[4], Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_week, container, false);
    }
}
