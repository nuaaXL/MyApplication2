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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by chenjunfan on 16/7/20.
 */
public class helprdActivity extends Activity  {
    private TextView nameTV,accoutTV,contentTV,userlocTV,rnameTV,rphoneTV,pckidTV,noteTV;
    private RelativeLayout callRL,finishRL;
    private Button callBT,messageBT,finishBT;
    String name,accout,content,userloc,rname,rphone,pckid,note,pphone;
    int num,tflag;


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_rd);
        initview();
        refresh();

    }

    public void initview()
    {
        nameTV= (TextView) findViewById(R.id.tv_prd_name);
        accoutTV = (TextView) findViewById(R.id.tv_prd_accout);
        contentTV = (TextView) findViewById(R.id.tv_prd_content);
        userlocTV = (TextView) findViewById(R.id.tv_prd_loc);
        rnameTV = (TextView) findViewById(R.id.tv_prd_rname);
        rphoneTV = (TextView) findViewById(R.id.tv_prd_rphone);
        pckidTV = (TextView) findViewById(R.id.tv_prd_pckid);
        noteTV = (TextView) findViewById(R.id.tv_prd_note);
        callRL = (RelativeLayout) findViewById(R.id.rl_prd_call);
        callBT = (Button) findViewById(R.id.btn_prd_call);
        messageBT= (Button) findViewById(R.id.btn_prd_message);
        finishRL = (RelativeLayout) findViewById(R.id.rl_prd_finish);
        finishBT = (Button) findViewById(R.id.btn_prd_finished);




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
                    pphone=c.getString(c.getColumnIndex("p_phone"));
                    pckid = c.getString(c.getColumnIndex("nullORpackage_Id"));

                    tflag = c.getInt(c.getColumnIndex("flag"));

//                    if ((tflag-((tflag/1000)*1000))/100==1) {
//                        pay = "货到付款";
//                    } else {
//                        pay = "寄方付款";
//                    }
                    name=c.getString(c.getColumnIndex("publisher"));
                    accout=c.getString(c.getColumnIndex("p_number"));

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
            accoutTV.setText(accout);
            nameTV.setText(name);
            contentTV.setText(content);
            userlocTV.setText(userloc);
            rnameTV.setText(rname);
            rphoneTV.setText(rphone);
            pckidTV.setText(pckid);
            noteTV.setText(note);
            if((tflag/10)%10==2)
            {
                Toast.makeText(helprdActivity.this,"in2",Toast.LENGTH_LONG).show();
                callRL.setVisibility(View.GONE);
                finishRL.setVisibility(View.VISIBLE);
            }
            else
            {
                callRL.setVisibility(View.VISIBLE);
                finishRL.setVisibility(View.GONE);
            }

        }
    };
}
