package com.tetsujin.tt;

import java.util.HashMap;
import java.util.Map;

public class ManagementFragmentState
{
    //Fragmentの状態を示す
    public enum stateList
    {
        MAIN,
        WEEK,
        MONTH,
        NOTIFICATION;
    }
    
    /*
        フィールド
     */
    //Fragmentの状態を管理
    private stateList state;

    ManagementFragmentState()
    {
        //stateの初期化
        setState(stateList.MAIN);
    }
    
    public stateList getState()
    {
        return state;
    }

    public void setState(stateList fragment)
    {
        this.state = fragment;
    }
    
    //今の状態が指定された状態と一緒ならtrue
    public boolean equal(stateList state)
    {
        return getState() == state;
    }
}
