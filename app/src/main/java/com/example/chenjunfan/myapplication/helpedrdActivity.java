package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by chenjunfan on 16/7/20.
 */
public class helpedrdActivity extends Activity {
private TextView haccoutTV,hnameTV,contentTV,userlocTV,rnameTV,rphoneTV,pckidTV,noteTV;
    private ImageButton callBT,msgBT;
    private Button waitBT,finishBT,finishedBT;
    RelativeLayout waitRL,finishRL,finishedRL;
    String haccout,hname,content,userloc,rname,rphone,pckid,note;
    int num;
    int tflag;
    String hphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helped_rd);
        initview();
        refresh();



    }
    public void initview()
    {
        haccoutTV = (TextView) findViewById(R.id.tv_drd_haccount);
        hnameTV = (TextView) findViewById(R.id.tv_drd_hname);
        callBT = (ImageButton) findViewById(R.id.btn_drd_call);
        msgBT= (ImageButton) findViewById(R.id.btn_drd_msg);
        contentTV = (TextView) findViewById(R.id.tv_drd_content);
        userlocTV= (TextView) findViewById(R.id.tv_drd_userloc);
        rnameTV = (TextView) findViewById(R.id.tv_drd_rname);
        rphoneTV = (TextView) findViewById(R.id.tv_drd_rphone);
        pckidTV = (TextView) findViewById(R.id.tv_drd_pckid);
        noteTV = (TextView) findViewById(R.id.tv_drd_note);
        waitRL = (RelativeLayout) findViewById(R.id.rl_drd_wait);
        waitBT = (Button) findViewById(R.id.btn_drd_wait);
        finishRL = (RelativeLayout) findViewById(R.id.rl_drd_finish);
        finishBT = (Button) findViewById(R.id.btn_drd_finish);
        finishedRL = (RelativeLayout) findViewById(R.id.rl_drd_finished);
        finishedBT = (Button) findViewById(R.id.btn_drd_finished);


    }

    public void refresh()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pre = getSharedPreferences("clickitemnum", MODE_PRIVATE);
                num = pre.getInt("num", 0);
                Log.i("num", "run: "+num);
                SQLiteDatabase db = openOrCreateDatabase("request.db", MODE_PRIVATE, null);
                db.execSQL("create table if not exists myrequesttb(num integer,time text,flag integer,publisher text" +
                        ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
                        "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");


                Cursor c = db.rawQuery("select * from myrequesttb where num=" + num, null);

                if (c != null) {
                    c.moveToNext();
                    content = c.getString(c.getColumnIndex("content"));
                    userloc = c.getString(c.getColumnIndex("user_loc"));
                    rname=(c.getString(c.getColumnIndex("r_nameORmessage")));
                    rphone=(c.getString(c.getColumnIndex("r_phoneORphone")));
                    note=(c.getString(c.getColumnIndex("infor")));
                    hphone=c.getString(c.getColumnIndex("h_phone"));
                    pckid = c.getString(c.getColumnIndex("nullORpackage_Id"));

                    tflag = c.getInt(c.getColumnIndex("flag"));

//                    if ((tflag-((tflag/1000)*1000))/100==1) {
//                        pay = "货到付款";
//                    } else {
//                        pay = "寄方付款";
//                    }
                    hname=c.getString(c.getColumnIndex("helper"));
                    haccout=c.getString(c.getColumnIndex("h_number"));

                    freshhandler.sendMessage(new Message());



                }


                //

            }
        });
        t.start();
    }

    Handler freshhandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            haccoutTV.setText(haccout);
            hnameTV.setText(hname);
            contentTV.setText(content);
            userlocTV.setText(userloc);
            rnameTV.setText(rname);
            rphoneTV.setText(rphone);
            pckidTV.setText(pckid);
            noteTV.setText(note);
            if((tflag/10)%10==0)
            {
                waitRL.setVisibility(View.VISIBLE);
                finishRL.setVisibility(View.GONE);
                finishedRL.setVisibility(View.GONE);
            }
            else if((tflag/10)%10==1)
            {
                waitRL.setVisibility(View.GONE);
                finishRL.setVisibility(View.VISIBLE);
                finishedRL.setVisibility(View.GONE);
            }
            else{
                waitRL.setVisibility(View.GONE);
                finishRL.setVisibility(View.GONE);
                finishedRL.setVisibility(View.VISIBLE);
            }

        }
    };


}
