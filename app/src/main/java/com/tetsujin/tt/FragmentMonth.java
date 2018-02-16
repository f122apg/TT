package com.tetsujin.tt;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tetsujin.tt.adapter.MonthAdapter;

import java.util.Arrays;
import java.util.Calendar;

import static com.tetsujin.tt.ActivityMain.state;

public class FragmentMonth extends Fragment implements View.OnClickListener
{
    private View view;
    private int year;
    private int month;
    private String[] memoDate;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_month, container, false);
        state.setState(ManagementFragmentState.stateList.MONTH);
    
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        //Calendar.MONTHだけは取り得る値が0～11までなので1足す
        month = cal.get(Calendar.MONTH) + 1;
        memoDate = ActivityMain.memoHelper.GetRecordAll();
        
        //onClickイベントハンドラを設定
        ImageButton previousIBt = (ImageButton)view.findViewById(R.id.FrgMonth_PreviousMonth_imagebutton);
        ImageButton nextIBt = (ImageButton)view.findViewById(R.id.FrgMonth_NextMonth_imagebutton);
        previousIBt.setOnClickListener(this);
        nextIBt.setOnClickListener(this);
        
        //UIをアップデート
        updateUI();
    
        return view;
    }
    
    @Override
    public void onClick(View v)
    {
        if(v != null)
        {
            switch (v.getId())
            {
                //先月ボタンが押された時、月のデクリメントを行う
                case R.id.FrgMonth_PreviousMonth_imagebutton:
                    //月が1月だったら去年の12月に設定
                    if(month == 1)
                    {
                        year --;
                        month = 12;
                    }
                    else
                        month --;
                    break;
                    
                //来月ボタンが押された時、月のインクリメントを行う
                case R.id.FrgMonth_NextMonth_imagebutton:
                    //月が12月だったら来年の1月に設定
                    if(month == 12)
                    {
                        year ++;
                        month = 1;
                    }
                    else
                        month ++;
                    break;
            }
    
            //UIをアップデート
            updateUI();
        }
    }
    
    //渡された値を元にUIをアップデートする
    private void updateUI()
    {
        TextView yyyymm_tv = (TextView)view.findViewById(R.id.FrgMonth_YYYYMM_textview);
        yyyymm_tv.setText(year + "/" + month);
    
        GridView gv = (GridView) view.findViewById(R.id.FrgMonth_Calendar_gridview);
        MonthAdapter cgva = new MonthAdapter(getContext(), getMonthCalendar(year, month), year, month, memoDate);
        gv.setAdapter(cgva);
    }
    
    //1か月のカレンダーを持つ配列を作成
    @NonNull
    private String[][] getMonthCalendar(int year, int month)
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month - 1, 1);
        
        //初期化
        String[][] retDay = new String[7][7];
        //初期値""で埋める
        for (String[] row : retDay)
        {
            Arrays.fill(row, "");
        }
        //配列の一番上に曜日を格納
        retDay[0] = new String[]
            {
                getResources().getString(R.string.week_sunday),
                getResources().getString(R.string.week_monday),
                getResources().getString(R.string.week_tuesday),
                getResources().getString(R.string.week_wednesday),
                getResources().getString(R.string.week_thursday),
                getResources().getString(R.string.week_friday),
                getResources().getString(R.string.week_saturday)
            };
        
        //渡された月の最大日数を取得
        int maxDate = cal.getActualMaximum(Calendar.DATE);
    
        //日をカウントする
        int dayCount = 1;
        boolean initComplete = false;
        //最大日数までdayCountをインクリメントし続け、dayCountを日として設定
        for (int row = 1; row < 7; row++)
        {
            for (int column = 0; column < 7; column++)
            {
                //columnの初期化が行われたかチェックする
                if (!initComplete)
                {
                    initComplete = true;
                    column = cal.get(Calendar.DAY_OF_WEEK) - 1;
                }
                
                //日を設定
                retDay[row][column] = String.valueOf(dayCount);
                //日を進める
                dayCount++;
                
                //日が月の最大日数を超えたら完成された表を返す
                if (dayCount > maxDate)
                    return retDay;
            }
        }
        
        return null;
    }
}