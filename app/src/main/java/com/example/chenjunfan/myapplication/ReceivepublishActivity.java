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

/**
 * Created by chenjunfan on 16/7/10.
 */
public class ReceivepublishActivity extends Activity {

    private ImageView imageBack;
    private EditText contentET;
    private EditText locET;
    private EditText rnameET;
    private EditText rphoneET;
    private EditText pgidET;
    private EditText noteET;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_receivepublish);

        imageBack = (ImageView) findViewById(R.id.img_back);
        contentET= (EditText) findViewById(R.id.et_rp_content);
        locET = (EditText) findViewById(R.id.et_rp_loc);
        rnameET= (EditText) findViewById(R.id.et_rp_rname);
        rphoneET = (EditText) findViewById(R.id.et_rp_rphone);
        pgidET = (EditText) findViewById(R.id.et_rp_pgid);
        noteET = (EditText) findViewById(R.id.et_rp_note);




        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
    }

    public void rsubmit(View view)
    {


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg=new Message();
                if(contentET.getText().toString().equals(""))
                {
                    msg.obj="请输入包裹内容";
                    handler.sendMessage(msg);
                }
                else if(locET.getText().toString().equals(""))
                {
                    msg.obj="请输入您的位置";
                    handler.sendMessage(msg);
                }
                else if(rnameET.getText().toString().equals(""))
                {
                    msg.obj="收件人名字";
                    handler.sendMessage(msg);
                }
                else if(rphoneET.getText().toString().equals(""))
                {
                    msg.obj="请输入收件人手机号码";
                    handler.sendMessage(msg);
                }
                else {
                    Request request = new Request();
                    User user = new User();

                    SQLiteDatabase db = openOrCreateDatabase("user.db", MODE_ENABLE_WRITE_AHEAD_LOGGING, null);
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
                    try{
                        String Url;
                        Url = "http://" + getResources().getText(R.string.IP) + ":8080/Ren_Test/requestServlet" + "?type=add" +"&flag=2" +
                                "&publisher="+ URLEncoder.encode(user.getName(),"gbk")+"&p_number="+user.getUserId()+"&p_phone="+user.getPhone()+"&user_loc="+ URLEncoder.encode(locET.getText().toString(),"gbk")+"&content="+URLEncoder.encode(contentET.getText().toString(),"gbk")+
                                "&infor="+URLEncoder.encode(noteET.getText().toString(),"gbk")+"&r_nameORmessage="+URLEncoder.encode(rnameET.getText().toString(),"gbk")+"&r_locORpackage_loc="+URLEncoder.encode(locET.getText().toString(),"gbk")+"&r_phoneORphone="+rphoneET.getText().toString()+
                                "&nullORpackage_Id="+URLEncoder.encode(pgidET.getText().toString(),"gbk");

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
                        if (user.getUserId().toString().equals("1"))
                        {
                            HomeActivity.ActivityA.finish();
                            Intent intent = new Intent(ReceivepublishActivity.this,HomeActivity.class);
                            msg.obj="发布成功";
                            handler.sendMessage(msg);
//                            SharedPreferences pre = getSharedPreferences("publishflag",MODE_PRIVATE);
//                            SharedPreferences.Editor editor = pre.edit();
//                            editor.putString("flag","1");
//                            editor.commit();
                            startActivity(intent);

                            finish();

                        }
                        else if(user.getUserId().toString().equals("-1"))
                        {
                            msg.obj="失败";
                            handler.sendMessage(msg);
                        }
                        else
                        {
                            msg.obj="服务器问题";
                            handler.sendMessage(msg);
                        }
                    }
                    catch (Exception e)
                    {
                        msg.obj="服务器出现问题，请稍候再试";
                        handler.sendMessage(msg);
                        e.printStackTrace();
                    }



                }


            }
        });
        t.start();
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            Toast.makeText(ReceivepublishActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
        }
    };




}
