package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by chenjunfan on 16/7/10.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    private Button loginButton;
    private Button registButton;
    private EditText idEditText;
    private EditText passwordEditText;
    private User user=new User();
    private CheckBox remeberpw;
    String id,passwd;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

         String str= (String) msg.obj;

            Toast.makeText(LoginActivity.this,str,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences pre = getSharedPreferences("remeberUser",MODE_PRIVATE);
        idEditText = (EditText) findViewById(R.id.et_id);
        passwordEditText = (EditText) findViewById(R.id.et_pw);
        remeberpw = (CheckBox) findViewById(R.id.remeberPasswd);

        idEditText.setText(pre.getString("userId",""));
        passwordEditText.setText(pre.getString("passwd",""));
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(LoginActivity.this, Register1Activity.class);
        startActivity(intent);
    }

    public void LOGIN(View view)
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                SharedPreferences pre2 = getSharedPreferences("remeberUser",MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pre2.edit();
                id=idEditText.getText().toString();
                passwd=passwordEditText.getText().toString();
                try {
                    String Url;
                    Url="http://192.168.191.1:8080/Ren_Test/login"+"?userId="+id+"&passwd="+passwd;
                    Log.i("tag",Url);
                    SQLiteDatabase db = openOrCreateDatabase("user.db",MODE_ENABLE_WRITE_AHEAD_LOGGING,null);
                    db.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
                            ",phone text,school text,point integer)");
                    db.execSQL("delete from usertb");
                    db.close();
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
                    user = (User) userList.get(0);
                    Log.i("user1", user.getUserId());
                    /*db.execSQL("insert into usertb(userId,name,passwd,gender,phone,school,point) values('"+user.getUserId()+"','"+user.getName()+"','"
                            +user.getPasswd()+"',"+user.getGender()+",'"+user.getPhone()+"','"+user.getSchool()+"',"+user.getPoint()+")");
                    db.close();
                    SQLiteDatabase db2 = openOrCreateDatabase("user.db",MODE_ENABLE_WRITE_AHEAD_LOGGING,null);
                    db2.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
                            ",phone text,school text,point integer)");*/


                    if(user.getUserId()!=null&&user.getUserId().equals("-1"))
                    {
                        Message msg = new Message();
                        msg.obj="账户不存在！";
                        handler.sendMessage(msg);
                        //Toast.makeText(LoginActivity.this,"账户不存在！",Toast.LENGTH_SHORT).show();
                    }
                    else if(user.getUserId()!=null&&user.getUserId().equals("-2"))
                    {
                        Message msg = new Message();
                        msg.obj="密码错误！";
                        handler.sendMessage(msg);
                       // Toast.makeText(LoginActivity.this,"密码错误！",Toast.LENGTH_SHORT).show();
                    }
                    else if(user.getUserId()!=null&&user.getUserId().equals(idEditText.getText().toString())){


                        editor2.putString("userId",idEditText.getText().toString().trim());
                        editor2.commit();

                        if(remeberpw.isChecked())
                        {
                            String passwd= passwordEditText.getText().toString().trim();
                            editor2.putString("passwd",passwordEditText.getText().toString().trim());
                            editor2.commit();

                        }
                        else
                        {
                            editor2.remove("passwd");
                            editor2.commit();
                        }
                        startActivity(intent);
                        Message msg = new Message();
                        msg.obj="登录成功";
                        handler.sendMessage(msg);
                        finish();
                    }
                    else
                    {
                        Message msg = new Message();
                        msg.obj="登录失败，请稍候再试";
                        handler.sendMessage(msg);
                        //Toast.makeText(LoginActivity.this,"登录失败，请稍候再试",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = "服务器连接超时，请检查网络设置";
                    handler.sendMessage(msg);
                }


            }
        });

        t.start();
    }
}


