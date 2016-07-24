package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class ChangepasswdActivity extends Activity {
    private ImageView imageBack;
    private EditText rawpw;
    private EditText newpw;
    private EditText newpw2;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            String str= (String) msg.obj;

            Toast.makeText(ChangepasswdActivity.this,str,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepasswd);
        imageBack = (ImageView) findViewById(R.id.img_back);

        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });

        rawpw= (EditText) findViewById(R.id.raw_passwd);
        newpw= (EditText) findViewById(R.id.new_passwd);
        newpw2=(EditText) findViewById(R.id.confirm_new_passwd);
    }


    public void modify(View view)
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ChangepasswdActivity.this, LoginActivity.class);
                SQLiteDatabase db = openOrCreateDatabase("user.db", MODE_ENABLE_WRITE_AHEAD_LOGGING, null);
                User user = new User();
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
                if(rawpw.getText().toString().equals(""))
                {
                    Message msg = new Message();
                    msg.obj = "请输入旧密码";
                    handler.sendMessage(msg);
                }
                else if (!user.getPasswd().equals(rawpw.getText().toString())) {
                    Message msg = new Message();
                    msg.obj = "新旧密码不匹配，请重试！";
                    handler.sendMessage(msg);
                }
                else if(newpw.getText().toString().equals(""))
                {
                    Message msg = new Message();
                    msg.obj = "请输入新密码";
                    handler.sendMessage(msg);
                }
                else if(!(newpw.getText().toString().equals(newpw2.getText().toString())))
                {
                    Message msg = new Message();
                    msg.obj = "两次输入的密码不匹配，请重新输入";
                    handler.sendMessage(msg);
                }
                else

                {

                    try {
                        String Url;
                        Url = "http://"+getResources().getText(R.string.IP)+":8080/Ren_Test/modifyServlet" + "?name=" + URLEncoder.encode(user.getName(), "gbk") + "&gender=" + user.getGender() + "&passwd=" + newpw.getText() + "&phone="
                                + user.getPhone() + "&school=" + URLEncoder.encode(user.getSchool(), "gbk") + "&actionCode=modifyConfirm" + "&userId=" + user.getUserId();
                        Log.i("url", Url);
                        db.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
                                ",phone text,school text,point integer)");

                        URL url = new URL(Url);
                        URLConnection conn = url.openConnection();
                        conn.setRequestProperty("Accept-Charset", "gbk");
                        conn.setRequestProperty("contentType", "gbk");
                        conn.setReadTimeout(3000);
                        InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "gbk");
                        BufferedReader br = new BufferedReader(reader);
                        String str = br.readLine();
                        System.out.println(str);
                        Gson gson = new Gson();
                        List<User> userList = gson.fromJson(str, new TypeToken<List<User>>() {
                        }.getType());
                        User user2;
                        user2 = (User) userList.get(0);
                        Log.i("user2", user2.getUserId());

                        if(user2.getUserId().equals("1"))
                        {
                            Message msg = new Message();
                            msg.obj = "修改密码成功，请重新登录";
                            handler.sendMessage(msg);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Message msg = new Message();
                            msg.obj = "修改密码失败，请稍候再试";
                            handler.sendMessage(msg);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.obj = "服务器连接超时，请检查网络设置";
                        handler.sendMessage(msg);
                    }
                }
            }
        });

        t.start();
    }






}
