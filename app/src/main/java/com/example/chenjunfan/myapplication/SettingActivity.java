package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by chenjunfan on 16/7/10.
 */
public class SettingActivity extends Activity {

    private ImageView imageBack;
    private TextView changePasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        imageBack = (ImageView) findViewById(R.id.img_back);
        changePasswd = (TextView) findViewById(R.id.Changepasswd);

        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
        changePasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ChangepasswdActivity.class);
                startActivity(intent);
            }
        });
    }
}
