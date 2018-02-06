package com.tetsujin.tt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tetsujin.tt.task.TaskGetTimeTable;

import static com.tetsujin.tt.ActivityMain.activityMain;
import static com.tetsujin.tt.ManagementFragmentState.stateList;

public class FragmentHeader extends Fragment implements View.OnClickListener
{
    private boolean isChangeFragment = false;
    //Fragmentの状態を管理する
    private ManagementFragmentState state = new ManagementFragmentState();
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_header, container, false);
        
        v.findViewById(R.id.FrgHeader_week_button).setOnClickListener(this);
        v.findViewById(R.id.FrgHeader_month_button).setOnClickListener(this);
//        /*
//            onClickListeners
//        */
//        //B1 1週間の時間割に遷移する
//        v.findViewById(R.id.FrgHeader_week_button).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //ヘッダーにある画像を変えるためにリソースIDを保持する変数
//                final int resourceid;
//
//                //FragmentWeekContainerの表示
//                if(!state.equal(stateList.MAIN))
//                {
//                    Bundle bundle = new Bundle();
//
//                    //Fragmentに渡すデータを準備する
//                    TimeTable[] timeTableAll = timeTableHelper.GetRecordAll();
//
//                    //FragmentWeekContainerに時間割データを渡す
//                    if(timeTable != null)
//                    {
//                        bundle.putBoolean("isNull", false);
//                        bundle.putSerializable("timetable", timeTableAll);
//                    }
//                    else
//                        bundle.putBoolean("isNull", true);
//
//                    //FragmentWeekContainerに値を渡し、表示する
//                    FragmentWeekContainer frgwc = new FragmentWeekContainer();
//                    frgwc.setArguments(bundle);
//                    activityMain.showFragment(frgwc);
//
//                    //状態をFragmentWeekに変更
//                    state.setState(ManagementFragmentState.stateList.WEEK);
//                }
//                //FragmentMainの表示
//                else
//                {
//                    FragmentMain frgmain = new FragmentMain();
//                    activityMain.showFragment(frgmain);
//
//                    //状態をFragmentWeekに変更
//                    state.setState(ManagementFragmentState.stateList.WEEK);
//                }
//
//                resourceid = 1;
//
//                /*
//                    アニメーション処理
//                */
//                //Fragmentの表示と共に、B1ボタンを矢印に変更するアニメーションを開始させる
//                final ImageButton ib = (ImageButton)v.findViewById(R.id.FrgHeader_week_button);
//                //アニメーションの読み込みとアニメーションにかける時間を設定
//                Animation fadeout_anim = AnimationUtils.loadAnimation(v.getContext(), R.anim.icon_fadeout);
//                fadeout_anim.setDuration(250);
//                final View finalv = v;
//                //リスナーを設定
//                fadeout_anim.setAnimationListener(new Animation.AnimationListener()
//                {
//                    @Override
//                    public void onAnimationStart(Animation animation){}
//
//                    //アニメーション終了時、ボタンの画像を変更しアニメーションをかける
//                    @Override
//                    public void onAnimationEnd(Animation animation)
//                    {
//                        ib.setImageResource(resourceid);
//                        Animation fadein_anim = AnimationUtils.loadAnimation(finalv.getContext(), R.anim.icon_fadein);
//                        fadein_anim.setDuration(250);
//                        ib.startAnimation(fadein_anim);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation){}
//                });
//                //アニメーションを開始
//                ib.startAnimation(fadeout_anim);
//
//                //フラグを反転
//                isChangeFragment = !isChangeFragment;
//            }
//        });
//
//        //B3 1か月のカレンダーに遷移する
//        v.findViewById(R.id.FrgHeader_month_button).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //ヘッダーにある画像を変えるためにリソースIDを保持する変数
//                final int resourceid;
//
//                //FragmentWeekContainerの表示
//                if(!isChangeFragment)
//                {
//                    //リソースIDを左矢印に変更
//                    resourceid = R.drawable.icon_arrow_back;
//
//                    FragmentMonth frgmonth = new FragmentMonth();
//                    activityMain.showFragment(frgmonth);
//                }
//                //FragmentMainの表示
//                else
//                {
//                    //リソースIDを一か月のカレンダーに変更
//                    resourceid = R.drawable.icon_month;
//                    FragmentMain frgmain = new FragmentMain();
//                    activityMain.showFragment(frgmain);
//                }
//
//                /*
//                    アニメーション処理
//                */
//                //Fragmentの表示と共に、B1ボタンを矢印に変更するアニメーションを開始させる
//                final ImageButton ib = (ImageButton)v.findViewById(R.id.FrgHeader_month_button);
//                //アニメーションの読み込みとアニメーションにかける時間を設定
//                Animation fadeout_anim = AnimationUtils.loadAnimation(v.getContext(), R.anim.icon_fadeout);
//                fadeout_anim.setDuration(250);
//                final View finalv = v;
//                //リスナーを設定
//                fadeout_anim.setAnimationListener(new Animation.AnimationListener()
//                {
//                    @Override
//                    public void onAnimationStart(Animation animation){}
//
//                    //アニメーション終了時、ボタンの画像を変更しアニメーションをかける
//                    @Override
//                    public void onAnimationEnd(Animation animation)
//                    {
//                        ib.setImageResource(resourceid);
//                        Animation fadein_anim = AnimationUtils.loadAnimation(finalv.getContext(), R.anim.icon_fadein);
//                        fadein_anim.setDuration(250);
//                        ib.startAnimation(fadein_anim);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation){}
//                });
//                //アニメーションを開始
//                ib.startAnimation(fadeout_anim);
//
//                //フラグを反転
//                isChangeFragment = !isChangeFragment;
//            }
//        });

        /*************************************************************/
        /* デバッグ用 */
        /*************************************************************/
        v.findViewById(R.id.debug_getdata_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                TaskGetTimeTable t = new TaskGetTimeTable(activityMain);
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
        Toast.makeText(v.getContext(), "clicked", Toast.LENGTH_SHORT).show();
        ManagementFragmentState oldState = state.clone();
        
        switch (vButton.getId())
        {
            case R.id.FrgHeader_week_button:
                //状態がweekではなかったら、weekに変更
                //weekであったら、mainに変更
                if(!state.equal(stateList.WEEK))
                    state.setState(stateList.WEEK);
                else
                    state.setState(stateList.MAIN);
                break;
            case R.id.FrgHeader_month_button:
                //状態がmonthではなかったら、monthに変更
                //monthであったら、mainに変更
                if(!state.equal(stateList.MONTH))
                    state.setState(stateList.MONTH);
                else
                    state.setState(stateList.MAIN);
                break;
        }
        
        System.out.println("old f:" + oldState.getn() + ", id:" + oldState.getnid());
        System.out.println("now f:" + state.getn() + ", id:" + state.getnid());
        
        /*
            アニメーション処理
        */
        final ImageButton iButton = (ImageButton)vButton;
        
        //アニメーションの読み込みとアニメーションにかける時間を設定
        Animation fadeOutAnime = AnimationUtils.loadAnimation(v.getContext(), R.anim.icon_fadeout);
        fadeOutAnime.setDuration(250);
        //リスナーを設定
        fadeOutAnime.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}
        
            //アニメーション終了時、ボタンの画像を変更しアニメーションをかける
            @Override
            public void onAnimationEnd(Animation animation)
            {
                iButton.setImageResource(state.getResourceid(state.getState()));
                Animation fadein_anim = AnimationUtils.loadAnimation(v.getContext(), R.anim.icon_fadein);
                fadein_anim.setDuration(250);
                iButton.startAnimation(fadein_anim);
            }
        
            @Override
            public void onAnimationRepeat(Animation animation){}
        });
        //アニメーションを開始
        iButton.startAnimation(fadeOutAnime);
    }
}
