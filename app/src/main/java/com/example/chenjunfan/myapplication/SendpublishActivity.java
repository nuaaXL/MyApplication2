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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
public class SendpublishActivity extends Activity implements View.OnClickListener{
    private Button fabuButton;
    private EditText contentET;
    private EditText locET;
    private EditText nameET;
    private EditText phoneET;
    private EditText addressET;
    private RadioGroup payRG;
    private EditText noteET;
    private ImageView imageBack;
    private Spinner spinner;
    private EditText pointET;
    private int flag=1;
    private int point=0;
    private int restpoint=0;


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            Toast.makeText(SendpublishActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sendpublish);
        pointET = (EditText) findViewById(R.id.et_sp_jifen) ;
        fabuButton = (Button) findViewById(R.id.btn_sp_fabu);
        imageBack = (ImageView) findViewById(R.id.img_back);
        contentET = (EditText) findViewById(R.id.et_sp_content);
        locET = (EditText) findViewById(R.id.et_sp_loc);
        nameET = (EditText) findViewById(R.id.et_sp_name);
        phoneET = (EditText) findViewById(R.id.et_sp_phone);
        addressET= (EditText) findViewById(R.id.et_sp_address);
        payRG= (RadioGroup) findViewById(R.id.RG_pay);
        noteET= (EditText) findViewById(R.id.et_sp_note);
        imageBack.setOnClickListener(this) ;
        spinner = (Spinner) findViewById(R.id.sp_kuaidi);

        payRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.rb_sp_hdfk:
                        flag=flag%200;
                        flag+=100;
                        break;
                    case R.id.rb_sp_jffk:
                        flag=flag%100;
                        flag+=200;

                        break;
                }
            }
        });



// 建立spinner数据源
        String[] mItems = getResources().getStringArray(R.array.languages);
// 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//绑定 Adapter到控件
        spinner .setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = getResources().getStringArray(R.array.languages);
                if (languages[pos].equals("顺丰快递")) {
                    flag+=1000;
                }
                else  if(languages[pos].equals("圆通快递")) {
                    flag+=2000;
                }
                else if(languages[pos].equals("申通快递"))
                {
                    flag+=3000;
                }
                else if(languages[pos].equals("中通快递"))
                {
                    flag+=4000;
                }
                else if(languages[pos].equals("天天快递"))
                {
                    flag+=5000;
                }
                else if(languages[pos].equals("韵达快递"))
                {
                    flag+=6000;
                }
                else if(languages[pos].equals("百世汇通"))
                {
                    flag+=7000;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }


    @Override
    public void onClick(View view) {

        finish();

    }


    public void submit(View view)
    {


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

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
                    point=Integer.parseInt(pointET.getText().toString());
                }

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
                else if(nameET.getText().toString().equals(""))
                {
                    msg.obj="请输入您的姓名";
                    handler.sendMessage(msg);
                }
                else if(phoneET.getText().toString().equals(""))
                {
                    msg.obj="请输入您的手机号码";
                    handler.sendMessage(msg);
                }
                else if((user.getPoint()-point)<0) {


                    msg.obj="您的积分剩余为:"+user.getPoint()+",不足以悬赏，请重新输入悬赏积分！";
                    handler.sendMessage(msg);
                }
                else {
                    Request request = new Request();




                    }
                    try{
                        Message msg2 = new Message();
                        String Url;
                        Url = "http://" + getResources().getText(R.string.IP) + ":8080/Ren_Test/requestServlet" + "?type=add" +"&flag=" +flag+
                                "&publisher="+URLEncoder.encode(user.getName(),"gbk")+"&p_number="+user.getUserId()+"&p_phone="+user.getPhone()+"&user_loc="+ URLEncoder.encode(locET.getText().toString(),"gbk")+"&content="+URLEncoder.encode(contentET.getText().toString(),"gbk")+
                                "&infor="+URLEncoder.encode(noteET.getText().toString(),"gbk")+"&r_nameORmessage="+URLEncoder.encode(nameET.getText().toString(),"gbk")+"&r_locORpackage_loc="+URLEncoder.encode(addressET.getText().toString(),"gbk")+"&r_phoneORphone="+phoneET.getText().toString()+
                                "&nullORpackage_Id="+URLEncoder.encode("xx","gbk")+"&point="+point;

//
                        locET.getText().toString();
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
                            Intent intent = new Intent(SendpublishActivity.this,HomeActivity.class);
                            msg.obj="发布成功";
                            db.execSQL("update usertb set point ="+user.getPoint());

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
                    db.close();
                    c.close();


                }



        });

        t.start();
    }

}
