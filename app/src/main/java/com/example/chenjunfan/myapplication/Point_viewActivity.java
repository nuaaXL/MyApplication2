package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import c.b.BP;
import c.b.PListener;

/**
 * Created by chenjunfan on 16/7/10.
 */
public class Point_viewActivity extends Activity{
    private TextView pointTV;
    private ProgressDialog prodialog;






    private ImageView imageBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prodialog=new ProgressDialog(Point_viewActivity.this);
        prodialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prodialog.setIndeterminate(true);
        prodialog.setMessage("正在生成订单");

        BP.init(this,"a0f03a59258733c127695d4ff069a685");

        setContentView(R.layout.activity_point_view);

        imageBack = (ImageView) findViewById(R.id.img_back);
        pointTV= (TextView) findViewById(R.id.tv_nowpoint);

        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });

        SQLiteDatabase db = openOrCreateDatabase("user.db", MODE_PRIVATE, null);
        db.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
                ",phone text,school text,point integer)");
        Cursor c = db.rawQuery("select * from usertb", null);
        if (c != null) {
            while (c.moveToNext()) {



              pointTV.setText(c.getString(c.getColumnIndex("point")));


            }
        }
        db.close();
        c.close();



    }
    public void getpoint(View view)
    {


                prodialog.show();


                BP.pay("商品名称", "商品描述", 0.02, false, new PListener(){
                    @Override
                    public void orderId(String s) {
                        Message msg = new Message();
                       msg.obj="订单id:"+s;

                    }

                    @Override
                    public void succeed() {
                        Toast.makeText(Point_viewActivity.this,"success",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void fail(int i, String s) {
                        Toast.makeText(Point_viewActivity.this,"fail "+i+" "+s,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void unknow() {
                        Toast.makeText(Point_viewActivity.this,"unknow",Toast.LENGTH_SHORT).show();
                    }
                });

            }

    @Override
    protected void onResume() {
        super.onResume();
        prodialog.cancel();
    }
}
