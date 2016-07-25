package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;


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
    private EditText messageET;
    private Button messageBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);



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
    public void getm(View view)
    {

    }

}
