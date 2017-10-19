package com.tetsujin.tt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Xml;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MainActivity extends AppCompatActivity {

    String timetabledata_url = "http://tetsujin.azurewebsites.net/api/schedules";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //日付取得と表示
        TextView date_tv = (TextView)findViewById(R.id.date_textview);
        date_tv.setText(android.text.format.DateFormat. format("MM/dd(E)", Calendar.getInstance()));

        findViewById(R.id.B2_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                GetTimeTable();
            }
        });
    }

    protected void GetTimeTable() {
        HttpURLConnection con = null;

        try
        {
            final URL url = new URL(timetabledata_url);
            //パス
            final String filename = "timetable.xml";

            con = (HttpURLConnection) url.openConnection();
            con.connect();

            final int status = con.getResponseCode();
            if(status == HttpURLConnection.HTTP_OK)
            {
                //xmlファイル取得
                final InputStream input = con.getInputStream();
                final DataInputStream datainput = new DataInputStream(input);

                final FileOutputStream fileOutput = new FileOutputStream(filename);
                final DataOutputStream dataOut = new DataOutputStream(fileOutput);

                final byte[] buffer = new byte[4096];
                int readByte = 0;

                while((readByte = datainput.read(buffer)) != -1)
                {
                    dataOut.write(buffer, 0, readByte);
                }

                dataOut.close();
                fileOutput.close();
                datainput.close();

                //XMLをListViewの形式に直す
//                XmlPullParser xmlpp = Xml.newPullParser();
//                try
//                {
//                    xmlpp.setInput(input, "UTF-8");
//                    int eventtype = xmlpp.getEventType();
//                    while(eventtype != XmlPullParser.END_DOCUMENT)
//                    {
//                        if(eventtype == XmlPullParser.START_DOCUMENT)
//                            System.out.println("Start Document");
//                        else if(eventtype == XmlPullParser.START_TAG)
//                            System.out.println("Start tag "+xmlpp.getName());
//                        else if(eventtype == XmlPullParser.END_TAG)
//                            System.out.println("End tag "+xmlpp.getName());
//                        else if(eventtype == XmlPullParser.TEXT)
//                            System.out.println("Text "+xmlpp.getText());
//                            eventtype = xmlpp.next();//increment
//                    }
//                } catch (XmlPullParserException xmlppe)
//                {
//                    xmlppe.printStackTrace();
//                }
            }


        } catch (IOException e1)
        {
            e1.printStackTrace();
        }
        finally
        {
            if(con != null)
            {
                con.disconnect();
            }
        }
    }
}
