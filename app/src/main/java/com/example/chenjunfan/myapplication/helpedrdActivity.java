package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

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
    private ProgressDialog prodialog;


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

    public void drdmakecall(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+hphone));
        try {
            startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void drdsendmsg(View view)
    {
        Uri smsToUri = Uri.parse("smsto:"+hphone);
        Intent intent = new Intent(Intent.ACTION_SENDTO,smsToUri);
        intent.putExtra("sms_body","你好，请问是你接了我的订单吗？");
        startActivity(intent);
    }

    public void drdmakefinish(View view)
    {
        prodialog=new ProgressDialog(helpedrdActivity.this);
        prodialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prodialog.setIndeterminate(true);
        prodialog.setMessage("正在完成订单");
        handlershow.sendMessage(new Message());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                SQLiteDatabase db = openOrCreateDatabase("user.db", MODE_PRIVATE, null);
                db.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
                        ",phone text,school text,point integer)");
                Cursor c = db.rawQuery("select * from usertb", null);
                if (c != null) {
                    while (c.moveToNext()) {


                        user.setUserId(c.getString(c.getColumnIndex("userId")));
                        user.setName(c.getString(c.getColumnIndex("name")));
                        user.setPasswd(c.getString(c.getColumnIndex("passwd")));
                        user.setGender(c.getInt(c.getColumnIndex("gender")));
                        user.setPhone(c.getString(c.getColumnIndex("phone")));
                        user.setSchool(c.getString(c.getColumnIndex("school")));
                        user.setPoint(c.getInt(c.getColumnIndex("point")));


                    }
                }

                db.close();
                c.close();

                try {
                    String Url;
                    Url = "http://" + getResources().getText(R.string.IP) + ":8080/Ren_Test/helpServlet?type=helped"+"&num="+
                            num;
                    Log.i("tag", Url);
                    URL url = new URL(Url);
                    URLConnection conn = url.openConnection();
                    conn.setRequestProperty("Accept-Charset", "gbk");
                    conn.setRequestProperty("contentType", "gbk");
                    conn.setReadTimeout(2000);
                    InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "gbk");
                    BufferedReader br = new BufferedReader(reader);
                    String str = br.readLine();
                    System.out.println(str);
                    Gson gson = new Gson();
                    List<User> userList = gson.fromJson(str, new TypeToken<List<User>>() {
                    }.getType());
                    user = (User) userList.get(0);
                    Log.i("user1", user.getUserId());
                    if (user.getUserId() != null && user.getUserId().equals("1")) {
                        Message msg = new Message();
                        msg.obj = "订单完成";
                        handler.sendMessage(msg);
                        handler2.sendMessage(msg);
                        handlerunshow.sendMessage(new Message());

                        //Toast.makeText(LoginActivity.this,"账户不存在！",Toast.LENGTH_SHORT).show();
                    } else if(user.getUserId() != null && user.getUserId().equals("-1")){
                        Message msg = new Message();
                        msg.obj = "订单已完成";
                        handler.sendMessage(msg);
                        handlerunshow.sendMessage(new Message());


                    }
                    else if(user.getUserId() != null && user.getUserId().equals("-2"))
                    {
                        Message msg = new Message();
                        msg.obj = "完成订单失败，请稍候再试";
                        handler.sendMessage(msg);
                        handlerunshow.sendMessage(new Message());


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    handlerunshow.sendMessage(new Message());

                }
            }
        });
        t.start();
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            Toast.makeText(helpedrdActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
        }
    };
    Handler handler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            waitRL.setVisibility(View.GONE);
            finishRL.setVisibility(View.GONE);
            finishedRL.setVisibility(View.VISIBLE);
//            behelp=1;
//            SharedPreferences pre = getSharedPreferences("refreshflag",MODE_PRIVATE);
//            SharedPreferences.Editor editor = pre.edit();
//            editor.putInt("flag",behelp);
//            editor.commit();

        }
    };
    Handler handlershow = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            prodialog.show();
        }
    };
    Handler handlerunshow = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            prodialog.cancel();
        }
    };


}
