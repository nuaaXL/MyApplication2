package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by chenjunfan on 16/7/10.
 */
public class SendpublishActivity extends Activity implements View.OnClickListener{
    private Button fabuButton;

    private ImageView imageBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sendpublish);

        fabuButton = (Button) findViewById(R.id.btn_fabu);
        imageBack = (ImageView) findViewById(R.id.img_back);

        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
        fabuButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

    }

}
