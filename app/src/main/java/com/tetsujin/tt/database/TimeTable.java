package com.tetsujin.tt.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeTable implements Parcelable
{
    //データベース名
    final static String DB_NAME = "TimeTableDB";
    //テーブル名
    final static String TABLE_NAME = "TimeTable";
    //テーブルの列名
    final static String COLUMN_TIMETABLEID = "TimeTableID";
    final static String COLUMN_LESSONCODE = "LessonCode";
    final static String COLUMN_LESSONNAME = "LessonName";
    final static String COLUMN_WEEKDAY = "WeekDay";
    final static String COLUMN_STARTTIME = "StartTime";
    final static String COLUMN_ENDTIME = "EndTime";
    final static String COLUMN_CLASSROOMNAME = "ClassRoomName";
    final static String COLUMN_TEACHERID = "TeacherID";
    final static String COLUMN_TEACHERNAME = "TeacherName";
    final static String COLUMN_DESCRIPTION = "Description";

    //テーブルを作成するクエリ
    final static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_TIMETABLEID + " INTEGER PRIMARY KEY," +
            COLUMN_LESSONCODE + " TEXT NOT NULL," +
            COLUMN_LESSONNAME + " TEXT NOT NULL," +
            COLUMN_WEEKDAY + " INTEGER NOT NULL," +
            COLUMN_STARTTIME + " TEXT NOT NULL," +
            COLUMN_ENDTIME + " TEXT NOT NULL," +
            COLUMN_CLASSROOMNAME + " TEXT NOT NULL," +
            COLUMN_TEACHERID + " INTEGER NOT NULL," +
            COLUMN_TEACHERNAME + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION + " TEXT" + ");";

    //すべてのレコードを取得するクエリ
    final static String GET_RECORD_ALL = "SELECT * FROM " + TABLE_NAME + ";";

    //IDに合致するすべてのレコードを取得するクエリ
    final static String GET_RECORD_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TIMETABLEID + " = ?;";

    //特定の曜日に合致するすべてのレコードを取得するクエリ
    final static String GET_RECORD_AT_WEEKDAY_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_WEEKDAY + " = ?;";

    //時間割のID
    private int TimeTableID;
    //授業コードは13文字
    private String LessonCode;
    private String LessonName;
    //java.util.Calendarの形式に沿う
    private int WeekDay;
    private String StartTime;
    private String EndTime;
    private String ClassRoomName;
    private int TeacherID;
    private String TeacherName;
    private String Description;

    /*
        コンストラクタ
     */
    //Builderで使用される
    private TimeTable(Builder builder)
    {
        this.TimeTableID = builder.TimeTableID;
        this.LessonCode = builder.LessonCode;
        this.LessonName = builder.LessonName;
        this.WeekDay = builder.WeekDay;
        this.StartTime = builder.StartTime;
        this.EndTime = builder.EndTime;
        this.ClassRoomName = builder.ClassRoomName;
        this.TeacherID = builder.TeacherID;
        this.TeacherName = builder.TeacherName;
        this.Description = builder.Description;
    }

    //Parcelを復元する
    public TimeTable(Parcel in)
    {
        this.TimeTableID = in.readInt();
        this.LessonCode = in.readString();
        this.LessonName = in.readString();
        this.WeekDay = in.readInt();
        this.StartTime = in.readString();
        this.EndTime = in.readString();
        this.ClassRoomName = in.readString();
        this.TeacherID = in.readInt();
        this.TeacherName = in.readString();
        this.Description = in.readString();
    }

    /*
        ゲッター
     */
    public int getTimeTableID()
    {
        return this.TimeTableID;
    }

    public String getLessonCode()
    {
        return this.LessonCode;
    }

    public String getLessonName()
    {
        return this.LessonName;
    }

    public int getWeekDay()
    {
        return this.WeekDay;
    }

    public String getStartTime()
    {
        return this.StartTime;
    }

    public String getEndTime()
    {
        return this.EndTime;
    }

    public String getClassRoomName()
    {
        return this.ClassRoomName;
    }

    public int getTeacherID()
    {
        return this.TeacherID;
    }

    public String getTeacherName()
    {
        return this.TeacherName;
    }

    public String getDescription()
    {
        return this.Description;
    }

    /*
        Parcel
     */
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.TimeTableID);
        dest.writeString(this.LessonCode);
        dest.writeString(this.LessonName);
        dest.writeInt(this.WeekDay);
        dest.writeString(this.StartTime);
        dest.writeString(this.EndTime);
        dest.writeString(this.ClassRoomName);
        dest.writeInt(this.TeacherID);
        dest.writeString(this.TeacherName);
        dest.writeString(this.Description);
    }

    public static final Parcelable.Creator<TimeTable> CREATOR = new Parcelable.Creator<TimeTable>()
    {
        @Override
        public TimeTable createFromParcel(Parcel source)
        {
            return new TimeTable(source);
        }

        @Override
        public TimeTable[] newArray(int size)
        {
            return new TimeTable[size];
        }
    };

    /*
        ビルダー
     */
    public static class Builder
    {
        //時間割のID
        private int TimeTableID;
        //授業コードは13文字
        private String LessonCode;
        private String LessonName;
        //java.util.Calendarの形式に沿う
        //-1でエラーとする
        private int WeekDay;
        private String StartTime;
        private String EndTime;
        private String ClassRoomName;
        private int TeacherID;
        private String TeacherName;
        private String Description;

        public Builder(int timeTableID)
        {
            TimeTableID = timeTableID;
        }

        public Builder LessonCode(String lessonCode)
        {
            LessonCode = lessonCode;
            return this;
        }

        public Builder LessonName(String lessonName)
        {
            LessonName = lessonName;
            return this;
        }

        public Builder WeekDay(int weekDay)
        {
            WeekDay = weekDay;
            return this;
        }

        public Builder StartTime(String startTime)
        {
            StartTime = startTime;
            return this;
        }

        public Builder EndTime(String endTime)
        {
            EndTime = endTime;
            return this;
        }

        public Builder ClassRoomName(String classRoomName)
        {
            ClassRoomName = classRoomName;
            return this;
        }

        public Builder TeacherID(int teacherID)
        {
            TeacherID = teacherID;
            return this;
        }

        public Builder TeacherName(String teacherName)
        {
            TeacherName = teacherName;
            return this;
        }

        public Builder Description(String description)
        {
            Description = description;
            return this;
        }

        public TimeTable build()
        {
            //月～金までのどれかかチェックして、違うならばthrowする
            if (!(WeekDay >= Calendar.MONDAY && WeekDay <= Calendar.FRIDAY)) throw new IllegalStateException("曜日の値が不正です。");

            //HH:mm形式の文字列が存在するかチェック(find())して、存在していたらgroup()で取得
            //存在しない場合throwする
            Pattern p = Pattern.compile("[0-9]{2}:[0-9]{2}");
            Matcher m = p.matcher(StartTime);
            if (m.find()) StartTime = m.group();
            else throw new IllegalStateException("StartTime内にHH:mmが存在しません。");

            m = p.matcher(EndTime);
            if (m.find()) EndTime = m.group();
            else throw new IllegalStateException("EndTime内にHH:mmが存在しません。");

            //フィールドの値が正常ならばbuilderをコンストラクタに渡す
            return new TimeTable(this);
        }
    }

    //授業の詳細を表示する関数
    public String showDescription()
    {
        if (!getDescription().equals("null")) return getDescription();
        else return "なし";
    }
    
    private static Boolean isNull(Object object)
    {
        return object == null;
    }

    //TimeTableの配列を元に曜日ごとにソートしArrayListに挿入
    public static ArrayList<TimeTable>[] createWeekDayList(TimeTable[] data)
    {
        //0 = Monday, 1 = Tuesday...
        ArrayList<TimeTable>[] retValue = new ArrayList[5];
        //初期化
        for (int i = 0; i < 5; i++)
        {
            retValue[i] = new ArrayList<>();
        }
    
        //時間割データがnullだったら、ソートせずにそのまま値を渡す
        if(isNull(data))
            return retValue;

        //各曜日の時間割データを各配列に挿入する
        for (TimeTable value : data)
        {
            switch (value.getWeekDay())
            {
                case Calendar.MONDAY:
                    retValue[0].add(value);
                    break;
                case Calendar.TUESDAY:
                    retValue[1].add(value);
                    break;
                case Calendar.WEDNESDAY:
                    retValue[2].add(value);
                    break;
                case Calendar.THURSDAY:
                    retValue[3].add(value);
                    break;
                case Calendar.FRIDAY:
                    retValue[4].add(value);
                    break;
            }
        }

        return retValue;
    }

    //曜日ごとに分けた時間割データから表を生成する
    public static String[][] createTableValue(ArrayList<TimeTable>[] value)
    {
        String[][] retValue = new String[8][5];
        //初期化
        for (String[] v : retValue)
        {
            Arrays.fill(v, "");
        }

        //retValueにvalueを入れたかどうか検知するためのカウンタ 曜日分だけ生成
        int[] insertCount = new int[5];
        
        //表における行
        for(int row = 0; row < 8; row ++ )
        {
            //表における列
            for (int column = 0; column < 5; column++)
            {
                //行(縦)より各曜日の値(value[column])の挿入されている数が大きい場合、
                //その曜日にまだ挿入できるデータがあると判定して挿入を行う
                if(row < value[column].size())
                {
                    String insertData = value[column].get(insertCount[column]).getLessonName();

                    //挿入するデータが表示できる文字数を超えた場合、一部省略する
                    if(insertData.length() > 23)
                        insertData = insertData.substring(0, insertData.length() - 2) + "..";
                    
                    //データを挿入
                    retValue[row][column] = insertData;
                    insertCount[column]++;
                }
            }
        }
        
        return retValue;
    }
}
