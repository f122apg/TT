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

import com.tetsujin.tt.database.DBMemo;

import java.io.File;

import static com.tetsujin.tt.ActivityMain.activityMain;
import static com.tetsujin.tt.ActivityMain.memoDBHelper;
import static com.tetsujin.tt.ActivityMain.memodb;

public class FragmentHeader extends Fragment
{
    private boolean isChangeFragment = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_header, container, false);

        final String[][] testdata = new String[][]
                {
                        {"1", "卒業制作", "09:00", "10:30", "月"},
                        {"2", "クラウドコンピューティング", "10:40", "12:10", "月"},
                        {"3", "データベース応用", "09:00", "12:10", "火"},
                        {"4", "Linux実習", "13:00", "16:10", "火"},
                        {"5", "キャリアデザイン3", "09:00", "14:30", "水"},
                        {"6", "オブジェクト指向プログラミング実習1 S1", "09:00", "12:10", "木"},
                        {"7", "ソフトウェアデザイン S1", "13:00", "14:30", "木"},
                        {"8", "合同資格対策講座", "09:00", "14:30", "金"},
                };

        /* onClickListeners */
        //B1 1週間の時間割に遷移する
        v.findViewById(R.id.Header_B1_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final int resourceid;

                //FragmentWeekContainerの表示
                if(!isChangeFragment)
                {
                    resourceid = R.drawable.icon_arrow_back;

                    //FragmentWeekContainerに時間割データを渡す
                    Bundle bundle = new Bundle();
                    bundle.putInt("datalength", testdata.length);
                    for (int i = 0; i < testdata.length; i++)
                    {
                        bundle.putStringArray("data" + i, testdata[i]);
                    }

                    //FragmentWeekContainerに値を渡し、表示する
                    FragmentWeekContainer frgwc = new FragmentWeekContainer();
                    frgwc.setArguments(bundle);
                    activityMain.showFragment(frgwc);
                }
                //FragmentMainの表示
                else
                {
                    resourceid = R.drawable.icon_week;
                    FragmentMain frgmain = new FragmentMain();
                    activityMain.showFragment(frgmain);
                }

                /* アニメーション処理 */
                //Fragmentの表示と共に、B1ボタンを矢印に変更するアニメーションを開始させる
                final ImageButton ib = (ImageButton)v.findViewById(R.id.Header_B1_button);
                //アニメーションの読み込みとアニメーションにかける時間を設定
                Animation fadeout_anim = AnimationUtils.loadAnimation(v.getContext(), R.anim.icon_fadeout);
                fadeout_anim.setDuration(250);
                final View finalv = v;
                //リスナーを設定
                fadeout_anim.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation){}

                    //アニメーション終了時、ボタンの画像を変更しアニメーションをかける
                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        ib.setImageResource(resourceid);
                        Animation fadein_anim = AnimationUtils.loadAnimation(finalv.getContext(), R.anim.icon_fadein);
                        fadein_anim.setDuration(250);
                        ib.startAnimation(fadein_anim);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation){}
                });
                //アニメーションを開始
                ib.startAnimation(fadeout_anim);

                //フラグを反転
                isChangeFragment = !isChangeFragment;
            }
        });

        /*************************************************************/
        /* デバッグ用 */
        /*************************************************************/
        v.findViewById(R.id.debug_getdata_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
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
    
        v.findViewById(R.id.debug_dbrm_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                File file = new File("/data/data/com.tetsujin.tt/databases/" + DBMemo.DB_NAME);
                if(file.exists())
                {
                    file.delete();
                    Toast.makeText(activityMain, "DB deleted", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(activityMain, "DB not found", Toast.LENGTH_SHORT).show();
            }
        });
    
        v.findViewById(R.id.debug_dbcreate_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                memodb = memoDBHelper.getWritableDatabase();
                Toast.makeText(activityMain, "DB created", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

}
