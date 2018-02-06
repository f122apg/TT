package com.tetsujin.tt;

import java.util.HashMap;
import java.util.Map;

public class ManagementFragmentState implements Cloneable
{
    //Fragmentの状態を示す
    public enum stateList
    {
        MAIN,
        WEEK,
        MONTH,
        NOTIFICATION;
    }
    
    //FragmentHeaderで使用されるアイコンを示す
    public enum resource
    {
        ARROW(R.drawable.icon_arrow_back),
        WEEK(R.drawable.icon_week),
        MONTH(R.drawable.icon_month),
        NOTIFICATION(R.drawable.icon_notification);
        
        private int id;
    
        resource(int id)
        {
            this.id = id;
        }
    
        public int getResouce()
        {
            return id;
        }
    }
    
    /*
        フィールド
     */
    //Fragmentの状態を管理
    private stateList state = stateList.MAIN;
    //各Fragmentのアイコンの設定状況を管理
    private Map<stateList, resource> resourceId = new HashMap<>();

    ManagementFragmentState()
    {
        //resourceidの初期化処理
        resourceId.put(stateList.WEEK, resource.WEEK);
        resourceId.put(stateList.MONTH, resource.MONTH);
        resourceId.put(stateList.NOTIFICATION, resource.NOTIFICATION);
    }
    
    public stateList getState()
    {
        return state;
    }
    
    public String getn()
    {
        switch (state)
        {
            case MAIN:
                return "main";
            case WEEK:
                return "week";
            case MONTH:
                return "month";
            case NOTIFICATION:
                return "notification";
            default:
                return "unknown";
        }
    }
    
    public String getnid()
    {
        if(resourceId.get(state) == null)
            return "null";
            
        switch (resourceId.get(state))
        {
            case WEEK:
                return "weekid";
            case MONTH:
                return "monthid";
            case NOTIFICATION:
                return "notificationid";
            default:
                return "unknown";
        }
    }
    
    public void setState(stateList fragment)
    {
        this.state = fragment;
        
        setResouceId(fragment);
    }
    
    public int getResourceid(stateList fragment)
    {
        return resourceId.get(fragment).getResouce();
    }
    
    public void setResouceId(stateList fragment)
    {
        switch (fragment)
        {
            case MAIN:
                System.out.println("main");
            case WEEK:
                System.out.println("week");
            case MONTH:
                System.out.println("month");
            case NOTIFICATION:
                System.out.println("notification");
            default:
                System.out.println("unknown");
        }
        
        switch (fragment)
        {
            case WEEK:
                if(getResourceid(fragment) == resource.WEEK.getResouce())
                    resourceId.put(fragment, resource.ARROW);
                else
                    resourceId.put(fragment, resource.WEEK);
            case MONTH:
                if(getResourceid(fragment) == resource.MONTH.getResouce())
                    resourceId.put(fragment, resource.ARROW);
                else
                    resourceId.put(fragment, resource.MONTH);
            case NOTIFICATION:
                if(getResourceid(fragment) == resource.NOTIFICATION.getResouce())
                    resourceId.put(fragment, resource.ARROW);
                else
                    resourceId.put(fragment, resource.NOTIFICATION);
        }
    }
    
    //今の状態が指定された状態と一緒ならtrue
    public boolean equal(stateList state)
    {
        return getState() == state;
    }
    
    @Override
    public ManagementFragmentState clone()
    {
        ManagementFragmentState retValue = null;
        
        try
        {
            retValue = (ManagementFragmentState)super.clone();
            retValue.resourceId = new HashMap<>();
            retValue.resourceId.putAll(resourceId);
            
            return retValue;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}