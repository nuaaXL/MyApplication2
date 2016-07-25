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
import android.widget.ImageView;
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
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by chenjunfan on 16/7/11.
 */
public class RdActivity extends Activity{

    private ProgressDialog prodialog;


    private ImageView imageBack,touxiangIV;
    private TextView userName;
    private TextView nameTV;
    private TextView accoutTV;
    private TextView contentTV;
    private TextView locTV;
    private TextView noteTV,jifenTV;
    private Button helpBT;
    private Button callBT;
    private Button smsgBT;
    private int num;
    private String username,name,loc,content,accout,note;
    private RelativeLayout nameRL,phoneRL,packidRL,helpRL,callRL;
    private Request request;
    private String packid,r_phone,r_name;
    private TextView rnameTV,rphoneTV,packidTV;
    private String pphone;
    private String touxiangURL,publisherid;
    int flag,point;
    String number;
    int behelp=0;
    private Bitmap touxiangbit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivedetails);
        userName = (TextView) findViewById(R.id.tv_rd_name);
        nameTV = (TextView) findViewById(R.id.tv_rd_rname);
        accoutTV = (TextView) findViewById(R.id.tv_rd_accout);
        locTV = (TextView) findViewById(R.id.tv_rd_loc);
        noteTV = (TextView) findViewById(R.id.tv_rd_note);
        contentTV = (TextView) findViewById(R.id.tv_rd_content);
        imageBack = (ImageView) findViewById(R.id.img_back);
        helpBT = (Button) findViewById(R.id.btn_rd_helpqu);
        callBT = (Button) findViewById(R.id.btn_rd_call);
        helpRL= (RelativeLayout) findViewById(R.id.ld_rd_help);
        callRL= (RelativeLayout) findViewById(R.id.ld_rd_call);
        smsgBT = (Button) findViewById(R.id.btn_rd_message);
        jifenTV = (TextView) findViewById(R.id.tv_rd_jifen);
        touxiangIV = (ImageView) findViewById(R.id.iv_rd_avatar);

        nameRL = (RelativeLayout) findViewById(R.id.RD_name);
        phoneRL= (RelativeLayout) findViewById(R.id.RD_phone);
        packidRL = (RelativeLayout) findViewById(R.id.RD_number);
        rnameTV = (TextView) findViewById(R.id.tv_rd_rname);
        rphoneTV = (TextView) findViewById(R.id.tv_rd_phone);
        packidTV = (TextView) findViewById(R.id.tv_rd_number);


        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
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
//                db.execSQL("create table if not exists requesttb(num integer,time text,flag integer,publisher text" +
//                        ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
//                        "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");


                Cursor c = db.rawQuery("select * from requesttb where num=" + num, null);

                if (c != null) {
                    while (c.moveToNext()) {
                        content = c.getString(c.getColumnIndex("content"));
                        loc = c.getString(c.getColumnIndex("user_loc"));
                        username=c.getString(c.getColumnIndex("publisher"));
                        name=c.getString(c.getColumnIndex("r_nameORmessage"));
                        accout=c.getString(c.getColumnIndex("p_number"));
                        note=c.getString(c.getColumnIndex("infor"));
                        packid=c.getString(c.getColumnIndex("nullORpackage_Id"));
                        r_phone=c.getString(c.getColumnIndex("r_phoneORphone"));
                        r_name=c.getString(c.getColumnIndex("r_nameORmessage"));
                        flag = c.getInt(c.getColumnIndex("flag"));
                        number = c.getString(c.getColumnIndex("p_number"));
                        pphone = c.getString(c.getColumnIndex("p_phone"));
                        point = c.getInt(c.getColumnIndex("point"));
                        publisherid = c.getString(c.getColumnIndex("p_number"));
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
    Handler handler3 = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            handlergetpic.sendMessage(new Message());
            contentTV.setText(content);
            locTV.setText(loc);
            userName.setText(username);
            nameTV.setText(name);
            accoutTV.setText(accout);
            noteTV.setText(note);
            rnameTV.setText(r_name);
            rphoneTV.setText(r_phone);
            packidTV.setText(packid);
            jifenTV.setText(point+"");
            if((flag%100-flag%10)/10==1)
            {
               helpRL.setVisibility(View.GONE);
                callRL.setVisibility(View.VISIBLE);
                nameRL.setVisibility(View.VISIBLE);
                phoneRL.setVisibility(View.VISIBLE);
                packidRL.setVisibility(View.VISIBLE);

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
                        Log.i("user1", user.getUserId());
                        touxiangURL=user.getUrl();
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
                        touxiangbit=getHttpBitmap("http://" + getResources().getText(R.string.IP) + "/nuaa/" + touxiangURL);
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
        }
    };


    public void makehelp(View view)
    {
        prodialog=new ProgressDialog(RdActivity.this);
        prodialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prodialog.setIndeterminate(true);
        prodialog.setMessage("正在抢单中");
        handlershow.sendMessage(new Message());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                SQLiteDatabase db = openOrCreateDatabase("user.db", MODE_PRIVATE, null);
//                db.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
//                        ",phone text,school text,point integer)");
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
                        handler.sendMessage(msg);
                        handler2.sendMessage(msg);
                        handlerunshow.sendMessage(new Message());

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

            Toast.makeText(RdActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
        }
    };
    Handler handler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            helpRL.setVisibility(View.GONE);
            callRL.setVisibility(View.VISIBLE);
            nameRL.setVisibility(View.VISIBLE);
            phoneRL.setVisibility(View.VISIBLE);
            packidRL.setVisibility(View.VISIBLE);
            behelp=1;
            SharedPreferences pre = getSharedPreferences("refreshflag",MODE_PRIVATE);
            SharedPreferences.Editor editor = pre.edit();
            editor.putInt("flag",behelp);
            editor.commit();

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
    Handler handlertouchme = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            if((flag/10)%10==0)
            {
                Message msg2=new Message();
                msg2.obj="这是您自己的订单，正在等待被接单";
                helpRL.setVisibility(View.GONE);
                handler.sendMessage(msg2);
                Log.i("test", "handleMessage:handlertouchme ");

            }
        }
    };
    public void makerdcall(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+pphone));
        try {
            startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void sendrdmsg(View view)
    {
        Uri smsToUri = Uri.parse("smsto:"+pphone);
        Intent intent = new Intent(Intent.ACTION_SENDTO,smsToUri);
        intent.putExtra("sms_body","你好，我已接单");
        startActivity(intent);
    }

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
