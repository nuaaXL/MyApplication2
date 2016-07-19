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
import java.util.List;

/**
 * Created by chenjunfan on 16/7/11.
 */
public class RdActivity extends Activity{

    private ImageView imageBack;
    private TextView userName;
    private TextView nameTV;
    private TextView accoutTV;
    private TextView contentTV;
    private TextView locTV;
    private TextView noteTV;
    private Button helpBT;
    private Button callBT;
    private int num;
    private String username,name,loc,content,accout,note;
    private RelativeLayout nameRL,phoneRL,packidRL;
    private Request request;
    private String packid,r_phone,r_name;
    private TextView rnameTV,rphoneTV,packidTV;
    int flag;
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
                db.execSQL("create table if not exists requesttb(num integer,time text,flag integer,publisher text" +
                        ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
                        "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");


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
            userName.setText(username);
            nameTV.setText(name);
            accoutTV.setText(accout);
            noteTV.setText(note);
            rnameTV.setText(r_name);
            rphoneTV.setText(r_phone);
            packidTV.setText(packid);
            if((flag%100-flag%10)/10==1)
            {
                helpBT.setVisibility(View.GONE);
                callBT.setVisibility(View.VISIBLE);
                nameRL.setVisibility(View.VISIBLE);
                phoneRL.setVisibility(View.VISIBLE);
                packidRL.setVisibility(View.VISIBLE);

            }

        }
    };

    public void makehelp(View view)
    {
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
                            num+"&helper="+user.getName();
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
                        helpBT.setVisibility(View.GONE);
                        callBT.setVisibility(View.VISIBLE);
                        handler.sendMessage(msg);
                        handler2.sendMessage(msg);
                        //Toast.makeText(LoginActivity.this,"账户不存在！",Toast.LENGTH_SHORT).show();
                    } else {
                        Message msg = new Message();
                        msg.obj = "抢单失败，下次再快一点哦~！";
                        handler.sendMessage(msg);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
            nameRL.setVisibility(View.VISIBLE);
            phoneRL.setVisibility(View.VISIBLE);
            packidRL.setVisibility(View.VISIBLE);

        }
    };

}
