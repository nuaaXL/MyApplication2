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
import android.widget.Toast;

/**
 * Created by chenjunfan on 16/7/11.
 */
public class SdActivity extends Activity {

    private TextView contentTV;
    private TextView locTV;
    private TextView payTV;
    private TextView kuaidiTV;
    private TextView nameTV;
    private TextView accoutTV;
    private ImageView imageIV;

    String content,loc,pay,kuaidi,name,accout;


    private ImageView imageBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senddetails);
        imageBack = (ImageView) findViewById(R.id.img_back);
        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
        contentTV = (TextView) findViewById(R.id.tv_sd_content);
        locTV = (TextView) findViewById(R.id.tv_sd_loc);
        payTV = (TextView) findViewById(R.id.tv_sd_pay);
        kuaidiTV = (TextView) findViewById(R.id.tv_sd_kuaidi);
        nameTV = (TextView) findViewById(R.id.tv_sd_name);
        accoutTV = (TextView) findViewById(R.id.tv_sd_accout);
        imageIV = (ImageView) findViewById(R.id.iv_sd_image);
        refresh();
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
                        int tflag = c.getInt(c.getColumnIndex("flag"));

                        if ((tflag-((tflag/1000)*1000))/100==1) {
                            pay = "货到付款";
                        } else {
                            pay = "寄方付款";
                        }
                        name=c.getString(c.getColumnIndex("publisher"));
                        accout=c.getString(c.getColumnIndex("p_number"));
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



                    }
                }

                handler3.sendMessage(new Message());
            }
        });
        t.start();
    }
    Handler handler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            Toast.makeText(SdActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
        }
    };
    Handler handler3 = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            contentTV.setText(content);
            locTV.setText(loc);
            payTV.setText(pay);
            kuaidiTV.setText(kuaidi);
            nameTV.setText(name);
            accoutTV.setText(accout);

        }
    };

}
