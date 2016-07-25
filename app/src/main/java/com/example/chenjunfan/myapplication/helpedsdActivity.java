package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by chenjunfan on 16/7/20.
 */
public class helpedsdActivity extends Activity {
    private TextView haccoutTV,hnameTV,contentTV,userlocTV,payTV,rnameTV,rphoneTV,raddressTV,kuaidiTV,noteTV,jifenTV;
    private RelativeLayout waitRL,finishRL,finishedRL;
    private Button waitBT,finishBT,finishedBT;
    private ImageButton callBT,msgBT;
    private String haccout,hname,content,userloc,pay,rname,rphone,raddress,kuaidi,note,hphone,touxiangURL=null,publisherid,picurl=null;
    private Bitmap tupianbit,touxiangbit;
    private int num,tflag,jifen;
    private ProgressDialog prodialog;
    private LinearLayout callhelperLL;
    private ImageView tupianIV,touxiangIV,imageBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helped_sd);

        imageBack = (ImageView) findViewById(R.id.img_back);
        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });

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
        touxiangIV = (ImageView) findViewById(R.id.sd_helper_image);
        tupianIV= (ImageView) findViewById(R.id.helped_sd_image);

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
        callhelperLL = (LinearLayout) findViewById(R.id.dsd_callhelper);
        jifenTV = (TextView) findViewById(R.id.tv_dsd_jifen);


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
//                db.execSQL("create table if not exists myrequesttb(num integer,time text,flag integer,publisher text" +
//                        ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
//                        "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");


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
                    jifen= c.getInt(c.getColumnIndex("point"));
                    publisherid=c.getString(c.getColumnIndex("h_number"));
                    picurl=c.getString(c.getColumnIndex("url"));


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
            handlergetpic.sendMessage(new Message());
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
            jifenTV.setText(jifen+"");
            if((tflag/10)%10==0)
            {
                waitRL.setVisibility(View.VISIBLE);
                finishRL.setVisibility(View.GONE);
                finishedRL.setVisibility(View.GONE);
                callhelperLL.setVisibility(View.GONE);
            }
            else if((tflag/10)%10==1)
            {
                waitRL.setVisibility(View.GONE);
                finishRL.setVisibility(View.VISIBLE);
                finishedRL.setVisibility(View.GONE);
                callhelperLL.setVisibility(View.VISIBLE);

            }
            else{
                waitRL.setVisibility(View.GONE);
                finishRL.setVisibility(View.GONE);
                finishedRL.setVisibility(View.VISIBLE);
                callhelperLL.setVisibility(View.VISIBLE);

            }

        }
    };

    Handler handlergetpic = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String Url;
                        Url = "http://" + getResources().getText(R.string.IP) + ":8080/Ren_Test/HeadServlet" + "?userId=" + publisherid;
                        Log.i("tag", Url);
                        URL url = new URL(Url);
                        URLConnection conn = url.openConnection();
                        conn.setRequestProperty("Accept-Charset", "gbk");
                        conn.setRequestProperty("contentType", "gbk");
                        conn.setReadTimeout(4000);
                        InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "gbk");
                        BufferedReader br = new BufferedReader(reader);
                        String str = br.readLine();
                        System.out.println(str);
                        Gson gson = new Gson();
                        List<User> userList = gson.fromJson(str, new TypeToken<List<User>>() {
                        }.getType());
                        User user = (User) userList.get(0);
                        if(user!=null) {
                            Log.i("user1", user.getUserId());
                            touxiangURL = user.getUrl();
                        }
                        pichandler.sendMessage(new Message());



                    }
                    catch (Exception e )
                    {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();




        }

    };
    Handler pichandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(touxiangURL!=null)
                        touxiangbit=getHttpBitmap("http://" + getResources().getText(R.string.IP) + "/nuaa/" + touxiangURL);
                        tupianbit=getHttpBitmap("http://" + getResources().getText(R.string.IP) + "/request/" + picurl);
                        setpichandler.sendMessage(new Message());
                    }
                    catch (Exception e )
                    {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    };

    Handler setpichandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            touxiangIV.setImageBitmap(touxiangbit);
            tupianIV.setImageBitmap(tupianbit);
        }
    };


    public void dsdmakecall(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+hphone));
        try {
            startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void dsdsendmsg(View view)
    {
        Uri smsToUri = Uri.parse("smsto:"+hphone);
        Intent intent = new Intent(Intent.ACTION_SENDTO,smsToUri);
        intent.putExtra("sms_body","你好，请问是你接了我的订单吗？");
        startActivity(intent);
    }

    public void dsdmakefinish(View view)
    {
        prodialog=new ProgressDialog(helpedsdActivity.this);
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

            Toast.makeText(helpedsdActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
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
    public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            Log.d("tag", url);
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
