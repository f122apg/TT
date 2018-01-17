package com.tetsujin.tt;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.tetsujin.tt.FragmentMain.memoDB;

/************************************************************************/
/************************************************************************/
/************************************************************************/
//デバッグ用フラグメント
/************************************************************************/
/************************************************************************/
/************************************************************************/
public class FragmentDebug extends Fragment
{
    private int containerwidth;
    
    public FragmentDebug()
    {
        // Required empty public constructor
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_debug, container, false);
        TextView tv = (TextView)v.findViewById(R.id.FrgTest_Debug_textview);
    
        try
        {
            Cursor r = memoDB.rawQuery("SELECT * FROM Memo", null);
            
            if(r.getCount() != 0)
            {
                StringBuilder sb = new StringBuilder();
                while (r.moveToNext())
                {
                    sb.append(r.getCount() + " = Date:" + r.getString(0) + ", Content:" + r.getString(1) + "\n");
                }
    
                tv.setText(sb.toString());
            }
            else
                tv.setText("null");
        }
        catch (Exception e)
        {
        }
    
        return v;
    }
}
