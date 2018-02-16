package com.tetsujin.tt;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tetsujin.tt.database.TimeTable;
import com.tetsujin.tt.task.TaskGetTimeTable;

import static com.tetsujin.tt.ActivityMain.activityMain;
import static com.tetsujin.tt.ActivityMain.getToDay;
import static com.tetsujin.tt.ActivityMain.state;
import static com.tetsujin.tt.ActivityMain.timeTable;
import static com.tetsujin.tt.ActivityMain.timeTableHelper;
import static com.tetsujin.tt.ManagementFragmentState.stateList;
import static com.tetsujin.tt.FragmentMain.todaydate;

public class FragmentHeader extends Fragment implements View.OnClickListener
{
    private boolean isChangeFragment = false;
    private View v;
    private SQLiteDatabase timeTableDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_header, container, false);
        timeTableDB = timeTableHelper.getWritableDatabase();

        //各ImageButtonにonclicklistenerを設定
        v.findViewById(R.id.FrgHeader_main_button).setOnClickListener(this);
        v.findViewById(R.id.FrgHeader_week_button).setOnClickListener(this);
        v.findViewById(R.id.FrgHeader_month_button).setOnClickListener(this);

        v.findViewById(R.id.debug_dbcreate_button).setVisibility(View.INVISIBLE);
        v.findViewById(R.id.debug_dbrm_button).setVisibility(View.INVISIBLE);
        v.findViewById(R.id.debug_getdata_button).setVisibility(View.INVISIBLE);

        /*************************************************************/
        /* デバッグ用 */
        /*************************************************************/
        v.findViewById(R.id.debug_getdata_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                TaskGetTimeTable t = new TaskGetTimeTable();
                t.execute("http://tetsujintimes.azurewebsites.net/api/TimeTables/cd/2/4");

//                //Tableの行一覧表示
//                if(!isChangeFragment)
//                {
//                    FragmentDebug f = new FragmentDebug();
//                    activityMain.showFragment(f);
//                    isChangeFragment = !isChangeFragment;
//                }
//                else
//                {
//                    FragmentMain frgmain = new FragmentMain();
//                    activityMain.showFragment(frgmain);
//                    isChangeFragment = !isChangeFragment;
//                }
            }
        });

        v.findViewById(R.id.debug_dbrm_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                activityMain.deleteDatabase("TimeTableDB");
            }
        });

        v.findViewById(R.id.debug_dbcreate_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Tableの行一覧表示
                if(!isChangeFragment)
                {
                    FragmentDebug f = new FragmentDebug();
                    activityMain.showFragment(f);
                    isChangeFragment = !isChangeFragment;
                }
                else
                {
                    FragmentMain frgmain = new FragmentMain();
                    activityMain.showFragment(frgmain);
                    isChangeFragment = !isChangeFragment;
                }
            }
        });

        return v;
    }

    @Override
    public void onClick(View vButton)
    {
        Fragment fragment = null;

        switch (vButton.getId())
        {
            case R.id.FrgHeader_main_button:
                //stateがメインの画面ではない または 今日の日付ではない場合、メインの画面にする
                if(!state.equal(stateList.MAIN) || !todaydate.equals(getToDay()) )
                {
                    fragment = new FragmentMain();
                }
                break;
            case R.id.FrgHeader_week_button:
                //stateが１週間の画面ではなかったら、１週間の画面にする
                if(!state.equal(stateList.WEEK))
                {
                    Bundle bundle = new Bundle();

                    //Fragmentに渡すデータを準備する
                    TimeTable[] timeTableAll = timeTableHelper.GetRecordAll(timeTableDB);

                    //FragmentWeekContainerに時間割データを渡す
                    if (timeTable != null)
                    {
                        bundle.putBoolean("isNull", false);
                        bundle.putSerializable("timetable", timeTableAll);
                    } else bundle.putBoolean("isNull", true);

                    //FragmentWeekContainerに値を渡し、表示する
                    fragment = new FragmentWeekContainer();
                    fragment.setArguments(bundle);
                }
                break;
            case R.id.FrgHeader_month_button:
                //stateが月の画面ではなかったら、月の画面にする
                if(!state.equal(stateList.MONTH))
                {
                    fragment = new FragmentMonth();
                }
                break;
        }

        if(fragment != null)
        {
            animated((ImageButton) vButton);
            activityMain.showFragment(fragment);
        }
    }

    private void animated(ImageButton target)
    {
        final ImageButton iButton = target;

        //透明度を1fから0fに250ミリ秒かけて、アニメーションさせる
        ViewCompat.animate(iButton)
                .alpha(0f)
                .setDuration(250)
                .setListener(new ViewPropertyAnimatorListenerAdapter()
                    {
                        //透明度を0fから1fに150ミリ秒かけて、アニメーションさせる
                        @Override public void onAnimationEnd (View view)
                        {
                            ViewCompat.animate(iButton)
                                    .alpha(1f)
                                    .setDuration(250)
                                    .start();
                        }
                    })
                .start();
    }
}
