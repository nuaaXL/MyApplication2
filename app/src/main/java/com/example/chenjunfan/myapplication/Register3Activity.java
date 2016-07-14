package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by chenjunfan on 16/7/11.
 */
public class Register3Activity  extends Activity {
    Button R3areturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);
        R3areturn = (Button) findViewById(R.id.btn_register3_return);

        R3areturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register3Activity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
