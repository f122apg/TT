package com.tetsujin.tt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.Calendar;

public class FragmentMonth extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.fragment_month, container, false);
    
        CustomCalendarView cal = (CustomCalendarView)v.findViewById(R.id.Calendar1);
//        CalendarView cv = (CalendarView)v.findViewById(R.id.FrgMonth_month_calendarview);
//
//        cv.setDateTextAppearance(R.style.daycolor);
        
        return v;
    }
    
}
