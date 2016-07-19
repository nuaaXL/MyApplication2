package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;


/**
 * Created by chenjunfan on 16/7/10.
 */
public class Register1Activity extends Activity{
    private Button nextBotton;
    private ImageView imageBack;
    private EditText userIdEt;
    private EditText passwdEt;
    private EditText passwd2Et;
    private EditText phoneEt;
    private RadioGroup schoolRg;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "提交验证码成功",
                            Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 已经验证
                    Toast.makeText(getApplicationContext(), "验证码已经发送",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(Register1Activity.this, des,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        cn.smssdk.SMSSDK.initSDK(this,"1513e51962d93","226a31d374bfc36ed5d3219f6adb228e");




        SharedPreferences pre = getSharedPreferences("register",MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("school","nuaa_new");
        editor.commit();
        imageBack = (ImageView) findViewById(R.id.img_back);
        userIdEt = (EditText) findViewById(R.id.et_r_userId);
        passwdEt = (EditText) findViewById(R.id.et_r_passwd);
        passwd2Et = (EditText) findViewById(R.id.et_r_passwd2);
        phoneEt = (EditText) findViewById(R.id.et_r_phone);
        schoolRg = (RadioGroup) findViewById(R.id.gp_r_group);
        messageBT= (Button) findViewById(R.id.btn_getmessage);
        messageET= (EditText) findViewById(R.id.et_message);


        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
        nextBotton = (Button) findViewById(R.id.btn_register_next);

        schoolRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                SharedPreferences pre2 = getSharedPreferences("register",MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pre2.edit();
                switch (i){
                    case R.id.rb_r_1:
                        editor2.putString("school","nuaa_new");
                        editor2.commit();
                        break;
                    case R.id.rb_r_2:
                        editor2.putString("school","nuaa_old");
                        editor2.commit();
                        break;
                }

            }
        });

        nextBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register1Activity.this,Register2Activity.class);
                SharedPreferences pre3 = getSharedPreferences("register",MODE_PRIVATE);
                SharedPreferences.Editor editor3 = pre3.edit();
                if(userIdEt.getText().toString().equals(""))
                {
                    Toast.makeText(Register1Activity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(passwdEt.getText().toString().equals(""))
                {
                    Toast.makeText(Register1Activity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(passwd2Et.getText().toString().equals(""))
                {
                    Toast.makeText(Register1Activity.this,"请确认密码",Toast.LENGTH_SHORT).show();
                }
                else if(passwdEt.getText().toString().equals(passwd2Et.getText().toString())==false)
                {
                    Toast.makeText(Register1Activity.this,"两次输入的密码不匹配，请重新输入",Toast.LENGTH_SHORT).show();

                }
                else if(phoneEt.getText().toString().equals(""))
                {
                    Toast.makeText(Register1Activity.this,"请输入手机号",Toast.LENGTH_SHORT).show();

                }
                else {

                    editor3.putString("userId",userIdEt.getText().toString());
                    editor3.putString("passwd",passwdEt.getText().toString());
                    editor3.putString("phone",phoneEt.getText().toString());
                    editor3.commit();
                    startActivity(intent);
                }
            }
        });



    }


    private class SmsObserver extends ContentObserver {

        public SmsObserver(Handler handler) {
            super(handler);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onChange(boolean selfChange) {
            // TODO Auto-generated method stub
            StringBuilder sb = new StringBuilder();
            Cursor cursor = getContentResolver().query(
                    Uri.parse("content://sms/inbox"), null, null, null, null);
            cursor.moveToNext();
            sb.append("body=" + cursor.getString(cursor.getColumnIndex("body")));

            System.out.println(sb.toString());
            Pattern pattern = Pattern.compile("[^0-9]");
            Matcher matcher = pattern.matcher(sb.toString());
            CodeText = matcher.replaceAll("");
            CodeEd.setText(CodeText);
            cursor.close();
            super.onChange(selfChange);
        }
    }

    public void getm(View view)
    {

        switch (v.getId()) {
            case R.id.getCode: // 获取验证码的过程.
                if (!TextUtils.isEmpty(PhoneEd.getText().toString())) {
                    getContentResolver().registerContentObserver(
                            Uri.parse("content://sms"), true,
                            new SmsObserver(new Handler()));
                    SMSSDK.getVerificationCode("86", PhoneEd.getText().toString());
                    phone = PhoneEd.getText().toString();

                } else {
                    Toast.makeText(MainActivity.this, "电话号码不能为空", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case R.id.Indentity:
                SMSSDK.submitVerificationCode("86", phone, CodeEd.getText()
                        .toString());
                break;
        }
    }

}
