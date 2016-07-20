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
public class helpedsdActivity extends Activity {
    private TextView haccoutTV,hnameTV,contentTV,userlocTV,payTV,rnameTV,rphoneTV,raddressTV,kuaidiTV,noteTV;
    private RelativeLayout waitRL,finishRL,finishedRL;
    private Button waitBT,finishBT,finishedBT;
    private ImageButton callBT,msgBT;
    private String haccout,hname,content,userloc,pay,rname,rphone,raddress,kuaidi,note,hphone;
    private int num,tflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helped_sd);

        initview();
        refresh();
    }

    public void initview()
    {
        haccoutTV = (TextView) findViewById(R.id.tv_dsd_haccount);
        hnameTV = (TextView) findViewById(R.id.tv_dsd_hname);
        contentTV = (TextView) findViewById(R.id.tv_dsd_content);
        userlocTV = (TextView) findViewById(R.id.tv_dsd_userloc);
        payTV = (TextView) findViewById(R.id.tv_dsd_pay);
        rnameTV = (TextView) findViewById(R.id.tv_dsd_rname);
        rphoneTV = (TextView) findViewById(R.id.tv_dsd_rphone);
        raddressTV = (TextView) findViewById(R.id.tv_dsd_raddress);
        kuaidiTV = (TextView) findViewById(R.id.tv_dsd_kuaidi);
        noteTV = (TextView) findViewById(R.id.tv_dsd_note);
        waitRL = (RelativeLayout) findViewById(R.id.rl_dsd_wait);
        finishRL = (RelativeLayout) findViewById(R.id.rl_dsd_finish);
        finishedRL= (RelativeLayout) findViewById(R.id.rl_dsd_finished);
        waitBT = (Button) findViewById(R.id.btn_dsd_wait);
        finishBT = (Button) findViewById(R.id.btn_dsd_finish);
        finishedBT = (Button) findViewById(R.id.btn_dsd_finished);
        callBT = (ImageButton) findViewById(R.id.btn_dsd_call);
        msgBT = (ImageButton) findViewById(R.id.btn_dsd_msg);


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
                    raddress=(c.getString(c.getColumnIndex("r_locORpackage_loc")));
                    note=(c.getString(c.getColumnIndex("infor")));
                    hphone=c.getString(c.getColumnIndex("h_phone"));


                    tflag = c.getInt(c.getColumnIndex("flag"));

                    if ((tflag-((tflag/1000)*1000))/100==1) {
                        pay = "货到付款";
                    } else {
                        pay = "寄方付款";
                    }
                    hname=c.getString(c.getColumnIndex("helper"));
                    haccout=c.getString(c.getColumnIndex("h_number"));
                    switch (tflag/1000)
                    {
                        case 1:
                            kuaidi="顺丰快递";
                            break;
                        case  2:
                            kuaidi="圆通快递";
                            break;
                        case  3:
                            kuaidi="申通快递";
                            break;
                        case 4:
                            kuaidi="中通快递";
                            break;
                        case 5:
                            kuaidi="天天快递";
                            break;
                        case 6:
                            kuaidi="韵达快递";
                            break;
                        case 7:
                            kuaidi="百世汇通";
                            break;
                        default:
                            kuaidi="未知";
                            break;
                    }
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
            payTV.setText(pay);
            rnameTV.setText(rname);
            rphoneTV.setText(rphone);
            raddressTV.setText(raddress);
            kuaidiTV.setText(kuaidi);
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
