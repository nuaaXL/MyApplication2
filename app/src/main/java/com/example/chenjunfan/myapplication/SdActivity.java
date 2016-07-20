package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by chenjunfan on 16/7/11.
 */
public class SdActivity extends Activity {
    private ProgressDialog prodialog;

    private TextView contentTV;
    private TextView locTV;
    private TextView payTV;
    private TextView kuaidiTV;
    private TextView userName;
    private TextView nameTV;
    private TextView accoutTV;
    private ImageView imageIV;
    private Button helpBT;
    private RelativeLayout nameRL,phoneRL,addressRL,helpRL,callRL;
    private TextView rnameTV,rphoneTV,raddressTV,noteTV;
    String rname,rphone,raddress,note;
    private int num;
    int tflag;
    private String number;

    String username,content,loc,pay,kuaidi,name,accout;


    private ImageView imageBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senddetails);
        nameRL= (RelativeLayout) findViewById(R.id.SD_name);
        rnameTV= (TextView) findViewById(R.id.tv_sd_rname);
        phoneRL= (RelativeLayout) findViewById(R.id.SD_phone);
        rphoneTV= (TextView) findViewById(R.id.tv_sd_phone);
        addressRL= (RelativeLayout) findViewById(R.id.SD_address);
        raddressTV= (TextView) findViewById(R.id.tv_sd_address);
        helpRL= (RelativeLayout) findViewById(R.id.rl_sd_help);
        callRL= (RelativeLayout) findViewById(R.id.rl_sd_call);
        noteTV= (TextView) findViewById(R.id.tv_sd_beizhu);

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
        userName = (TextView) findViewById(R.id.tv_sd_name);
        accoutTV = (TextView) findViewById(R.id.tv_sd_accout);
        imageIV = (ImageView) findViewById(R.id.iv_sd_image);
        helpBT = (Button) findViewById(R.id.btn_helpji);
        refresh();
    }

    public void refresh()
    {


        Thread t = new Thread(new Runnable() {


            @Override
            public void run() {
                SharedPreferences pre = getSharedPreferences("clickitemnum", MODE_PRIVATE);
                num = pre.getInt("num", 0);
                SQLiteDatabase db = openOrCreateDatabase("request.db", MODE_PRIVATE, null);
                db.execSQL("create table if not exists requesttb(num integer,time text,flag integer,publisher text" +
                        ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
                        "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");


                Cursor c = db.rawQuery("select * from requesttb where num=" + num, null);

                if (c != null) {
                    c.moveToNext();
                        content = c.getString(c.getColumnIndex("content"));
                        loc = c.getString(c.getColumnIndex("user_loc"));
                        rname=(c.getString(c.getColumnIndex("r_nameORmessage")));
                        rphone=(c.getString(c.getColumnIndex("r_phoneORphone")));
                        raddress=(c.getString(c.getColumnIndex("r_locORpackage_loc")));
                        note=(c.getString(c.getColumnIndex("infor")));
                        number=c.getString(c.getColumnIndex("p_number"));
                    freshhandler.sendMessage(new Message());

                        tflag = c.getInt(c.getColumnIndex("flag"));

                        if ((tflag-((tflag/1000)*1000))/100==1) {
                            pay = "货到付款";
                        } else {
                            pay = "寄方付款";
                        }
                        username=c.getString(c.getColumnIndex("publisher"));
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

                User user = new User();
                SQLiteDatabase db3 = openOrCreateDatabase("user.db", MODE_PRIVATE, null);
                db3.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
                        ",phone text,school text,point integer)");
                Cursor c3 = db3.rawQuery("select * from usertb", null);
                if (c3 != null) {
                    c3.moveToNext();



                    user.setUserId(c3.getString(c3.getColumnIndex("userId")));
                    if(user.getUserId().equals(number))
                    {
                        handlertouchme.sendMessage(new Message());
                    }




                }
                db3.close();
                c3.close();

                handler3.sendMessage(new Message());
            }
        });
        t.start();
    }
    Handler handlertouchme = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            if((tflag/10)%10==0)
            {
                Message msg2=new Message();
                msg2.obj="这是您自己的订单，正在等待被接单";
                helpRL.setVisibility(View.GONE);
                handler.sendMessage(msg2);
                Log.i("test", "handleMessage:handlertouchme ");

            }
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
            userName.setText(username);
            accoutTV.setText(accout);

        }
    };

    public void helpji(View view)
    {
        prodialog=new ProgressDialog(SdActivity.this);
        prodialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prodialog.setIndeterminate(true);
        prodialog.setMessage("正在抢单中");
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
                    Url = "http://" + getResources().getText(R.string.IP) + ":8080/Ren_Test/helpServlet?type=tohelp"+"&h_number="+user.getUserId()+"&h_phone="+user.getPhone()+"&num=" +
                            num+"&helper="+ URLEncoder.encode(user.getName(),"gbk");
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
                        msg.obj = "抢单成功！";
                        handlerunshow.sendMessage(new Message());

                        handler.sendMessage(msg);
                        handler4.sendMessage(msg);
                        //Toast.makeText(LoginActivity.this,"账户不存在！",Toast.LENGTH_SHORT).show();
                    } else if(user.getUserId() != null && user.getUserId().equals("-1")){
                        Message msg = new Message();
                        msg.obj = "抢单失败，下次再快一点哦~！";
                        handler.sendMessage(msg);
                        handlerunshow.sendMessage(new Message());

                    }
                    else if(user.getUserId() != null && user.getUserId().equals("-2"))
                    {
                        Message msg = new Message();
                        msg.obj = "亲你调皮了~别接自己发的单哦~";
                        handler.sendMessage(msg);
                        handlerunshow.sendMessage(new Message());

                    }


                } catch (Exception e) {
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

            Toast.makeText(SdActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
        }
    };
    Handler handler4 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            helpRL.setVisibility(View.GONE);
            callRL.setVisibility(View.VISIBLE);
            nameRL.setVisibility(View.VISIBLE);
            phoneRL.setVisibility(View.VISIBLE);
            addressRL.setVisibility(View.VISIBLE);

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

    Handler freshhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            rnameTV.setText(rname);
            rphoneTV.setText(rphone);
            raddressTV.setText(raddress);
            noteTV.setText(note);
        }
    };


}
