package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by chenjunfan on 16/7/11.
 */
public class RdActivity extends Activity{

    private ImageView imageBack;
    private TextView nameTV;
    private TextView accoutTV;
    private TextView contentTV;
    private TextView locTV;
    private TextView noteTV;

    private String name,loc,content,accout,note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivedetails);
        nameTV = (TextView) findViewById(R.id.tv_rd_name);
        accoutTV = (TextView) findViewById(R.id.tv_rd_accout);
        locTV = (TextView) findViewById(R.id.tv_rd_loc);
        noteTV = (TextView) findViewById(R.id.tv_rd_note);
        contentTV= (TextView) findViewById(R.id.tv_rd_content);
        imageBack = (ImageView) findViewById(R.id.img_back);
        refresh();
        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
    }



    public void refresh()
    {


        Thread t = new Thread(new Runnable() {


            @Override
            public void run() {
                SharedPreferences pre = getSharedPreferences("clickitemnum", MODE_PRIVATE);
                int num = pre.getInt("num", 0);
                SQLiteDatabase db = openOrCreateDatabase("request.db", MODE_PRIVATE, null);
                db.execSQL("create table if not exists requesttb(num integer,time text,flag integer,publisher text" +
                        ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
                        "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");


                Cursor c = db.rawQuery("select * from requesttb where num=" + num, null);

                if (c != null) {
                    while (c.moveToNext()) {
                        content = c.getString(c.getColumnIndex("content"));
                        loc = c.getString(c.getColumnIndex("user_loc"));
                        name=c.getString(c.getColumnIndex("r_nameORmessage"));
                        accout=c.getString(c.getColumnIndex("p_number"));
                        note=c.getString(c.getColumnIndex("infor"));





                    }
                }

                handler3.sendMessage(new Message());
            }
        });
        t.start();
    }
    Handler handler3 = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            contentTV.setText(content);
            locTV.setText(loc);
            nameTV.setText(name);
            accoutTV.setText(accout);
            noteTV.setText(note);

        }
    };

}
