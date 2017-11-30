package com.tetsujin.tt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

import com.tetsujin.tt.adapter.CustomListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

/***********************************************************************/
//時間割データをネットワーク経由で取得し、ListViewアダプタにセットする非同期のクラス
/***********************************************************************/

//引数の意味
//String = Activityからスレッドへ渡す変数の型
//    Activityからスレッドを呼び出すexecuteの引数
//    doInBackgroundの引数
//Void = 進捗度合を表示するときに利用する型
//    onProgressUpdateの引数
//String[][] = バックグラウンド処理完了時に受け取る型
//    doInBackgroundの戻り値
//    onPostExecuteの引数
public class TaskGetTimeTable extends AsyncTask<String, Void, String[][]>
{
    private Activity activityMain;
    public ProgressDialog progressdialog;

    public TaskGetTimeTable(Activity activity)
    {
        this.activityMain = activity;
    }

    //キャンセルできないプログレスダイアログを表示する
    @Override
    protected void onPreExecute()
    {
        this.progressdialog = new ProgressDialog(this.activityMain);
        this.progressdialog.setMessage(activityMain.getResources().getString(R.string.getdata));
        this.progressdialog.setCancelable(false);
        this.progressdialog.show();
        return;
    }

    //非同期処理
    //...は可変長引数の意味
    @Override
    protected String[][] doInBackground(String... urlstr)
    {
        HttpURLConnection httpconnect = null;
        //返り値 時間割情報を返す
        String[][] retvalues = new String[0][];
        //JsonArrayの構文として正しいかどうか正規表現でチェックする文
        //^\{\s*"[^\s]+"\s*\:
        final String PATTERN_JSONARRAY = "^\\{\\s*\"[^\\s]+\"\\s*\\:";
        String JSON_arrayname = "data";

        try {
            //StringからURLを生成
            final URL url = new URL(urlstr[0]);
            //URL先に接続
            httpconnect = (HttpURLConnection) url.openConnection();
            httpconnect.connect();

            //接続した結果のステータスを取得
            final int httpstatus = httpconnect.getResponseCode();
            //ステータスが200 HTTP_OKの場合、JSONのparseを行う
            if (httpstatus == HttpURLConnection.HTTP_OK)
            {
                //InputStreamを取得
                InputStream inputst = httpconnect.getInputStream();
                //InputStreamReader、StringBuilderを用いて、InputStreamをStringに変換
                InputStreamReader streader = new InputStreamReader(inputst);
                StringBuilder strbuilder = new StringBuilder();

                //InputStreamReaderを読み込んで、StringBuilderに追記する
                char[] buf = new char[1024];
                int read;
                while (0 <= (read = streader.read(buf)))
                {
                    strbuilder.append(buf, 0, read);
                }
                //JSONの配列構文かどうか判断して、正しくないなら正しい構文に整形する
                if(strbuilder.toString().matches(PATTERN_JSONARRAY) == false)
                {
                    strbuilder.insert(0, "{ \"" + JSON_arrayname + "\" :");
                    strbuilder.append("}");
                }
                else
                {
                    //JSONの配列名を取得
                    String tmp = strbuilder.toString();
                    JSON_arrayname = tmp.substring(tmp.indexOf("\"") + 1, tmp.indexOf("\"", tmp.indexOf("\"") + 1));
                }

                String timetable_data = strbuilder.toString();

                try {
                    JSONObject json = new JSONObject(timetable_data);
                    //JSONの配列を取得
                    JSONArray datas = json.getJSONArray(JSON_arrayname);
                    retvalues = new String[datas.length()][4];
                    //JSONの配列の中にある値を返り値に挿入
                    for (int i = 0; i < datas.length(); i ++)
                    {
                        //配列内の値を取得
                        json = datas.getJSONObject(i);
                        //要素をStringで取得
                        String starttime = json.getString("StartDateTime");
                        String endtime = json.getString("EndDateTime");
                        //HH:mmだけ取得できるよう加工する
                        String convert_starttime = starttime.substring(starttime.lastIndexOf('T') + 1, starttime.lastIndexOf('T') + 6);
                        String convert_endtime = endtime.substring(endtime.lastIndexOf('T') + 1, endtime.lastIndexOf('T') + 6);
                        retvalues[i] = new String[]{ json.getString("Id"), json.getString("Title"), convert_starttime, convert_endtime };
                    }

                    //昇順でソートを行う
                    for (int i = 0; i < retvalues.length; i ++)
                    {
                        Arrays.sort(retvalues, new Comparator<String[]>()
                        {
                            @Override
                            public int compare(String[] o1, String[] o2)
                            {
                                //StartDateTimeが同じだった場合、Idを基準としてソートする
                                if(o1[2].compareTo(o2[2]) == 0)
                                {
                                    //Stringで比較するとUnicodeでソートしてしまうため、Integerで比較する
                                    int tmp1 = Integer.parseInt(o1[0]);
                                    int tmp2 = Integer.parseInt(o2[0]);

                                    if(tmp1 > tmp2)
                                        return 1;
                                    else if(tmp1 < tmp2)
                                        return -1;
                                    else
                                        return 0;
                                }
                                else
                                    return o1[2].compareTo(o2[2]);
                            }
                        });
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                streader.close();
                inputst.close();
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (httpconnect != null)
            {
                httpconnect.disconnect();
            }
        }

        return retvalues;
    }

    //doInBackgroundの処理が終わった後に実行される
    //取得した時間割データを自作アダプタに渡し、ListViewで表示
    @Override
    protected void onPostExecute(String[][] values) {
        ListView timetable_lv = (ListView)this.activityMain.findViewById(R.id.AyMain_timetable_listview);

        CustomListViewAdapter adapter = new CustomListViewAdapter(this.activityMain, values);
        timetable_lv.setAdapter(adapter);

        //プログレスダイアログを閉じる
        if(this.progressdialog != null && this.progressdialog.isShowing())
        {
            this.progressdialog.dismiss();
        }
    }
}