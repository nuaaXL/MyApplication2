package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenjunfan on 16/7/10.
 */
public class HomeActivity extends Activity implements AdapterView.OnItemClickListener {
    private ImageView homeIV;
    private ImageView meIV;
    private RelativeLayout homeRL;
    private RelativeLayout wodeRL;
    private ListView mainList;
    private SimpleAdapter mainListAdp;
    private List<Map<String, Object>> dataList;
    private RelativeLayout homeLL;
    private RelativeLayout meRL;
    private Button xxbjButton;
    private Button wdjfButton;
    private Button settingButton;
    private FloatingActionButton actionC;
    private TextView accoutTV;
    private TextView nameTV;
    private User user = new User();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        accoutTV = (TextView) findViewById(R.id.tv_accout);
        nameTV = (TextView) findViewById(R.id.tv_name);
        homeIV = (ImageView) findViewById(R.id.IV_home);
        meIV = (ImageView) findViewById(R.id.IV_me);
        homeRL = (RelativeLayout) findViewById(R.id.RL_home);
        wodeRL = (RelativeLayout) findViewById(R.id.RL_me);
        mainList = (ListView) findViewById(R.id.LVmain);
        homeLL = (RelativeLayout) findViewById(R.id.LLhome);
        meRL = (RelativeLayout) findViewById(R.id.RLme);
        xxbjButton = (Button) findViewById(R.id.btn_xxbj);
        wdjfButton = (Button) findViewById(R.id.btn_wdjf);
        settingButton = (Button) findViewById(R.id.btn_setting);
/*
    取user数据库:
 */
        SQLiteDatabase db = openOrCreateDatabase("user.db",MODE_ENABLE_WRITE_AHEAD_LOGGING,null);
        db.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
                ",phone text,school text,point integer)");

        Cursor c = db.rawQuery("select * from usertb",null);
        if(c!=null)
        {
            while(c.moveToNext()){
                       /* Log.i("info","id:"+c.getString(c.getColumnIndex("userId")));
                        Log.i("info", "name:"+c.getString(c.getColumnIndex("name")));
                        Log.i("info", "passwd:"+c.getString(c.getColumnIndex("passwd")));
                        Log.i("info", "gender:"+c.getInt(c.getColumnIndex("gender")));
                        Log.i("info", "phone:"+c.getString(c.getColumnIndex("phone")));
                        Log.i("info", "school:"+c.getString(c.getColumnIndex("school")));
                        Log.i("info", "point:"+c.getInt(c.getColumnIndex("point")));*/

                user.setUserId(c.getString(c.getColumnIndex("userId")));
                user.setName(c.getString(c.getColumnIndex("name")));
                user.setPasswd(c.getString(c.getColumnIndex("passwd")));
                user.setGender(c.getInt(c.getColumnIndex("gender")));
                user.setPhone(c.getString(c.getColumnIndex("phone")));
                user.setSchool(c.getString(c.getColumnIndex("school")));
                user.setPoint(c.getInt(c.getColumnIndex("point")));


            }
        }

        /*
        显示到me里：
         */
        nameTV.setText(user.getName());
        accoutTV.setText(user.getUserId());

        dataList = new ArrayList<Map<String, Object>>();
        mainListAdp = new SimpleAdapter(this, getData(), R.layout.item_main, new String[]{"pic", "text", "flag"}, new int[]{R.id.pic, R.id.text, R.id.flag});
        mainList.setAdapter(mainListAdp);
        mainList.setOnItemClickListener(this);



        actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("我要取快递");
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReceivepublishActivity.class);
                startActivity(intent);
            }
        });

        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        menuMultipleActions.addButton(actionC);



        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.white));

        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SendpublishActivity.class);
                startActivity(intent);
            }
        });


        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });


        wdjfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Point_viewActivity.class);
                startActivity(intent);
            }
        });
        xxbjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Person_visionActivity.class);
                startActivity(intent);
            }
        });
        homeRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                homeIV.setImageDrawable(getResources().getDrawable(R.drawable.home_pressed));
                meIV.setImageDrawable(getResources().getDrawable(R.drawable.me_normal));
                homeLL.setVisibility(View.VISIBLE);
                meRL.setVisibility(View.INVISIBLE);
                homeLL.hasFocus();


            }
        });

        wodeRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeIV.setImageDrawable(getResources().getDrawable(R.drawable.home_normal));
                meIV.setImageDrawable(getResources().getDrawable(R.drawable.me_pressed));
                homeLL.setVisibility(View.INVISIBLE);
                meRL.setVisibility(View.VISIBLE);
                meRL.hasFocus();

            }
        });


    }

    private List<Map<String, Object>> getData() {
        for (int i = 0; i < 20; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pic", R.mipmap.ic_launcher);

            if (i % 2 == 0) {
                map.put("flag", 0);
                map.put("text", "取快递demo");
            } else {
                map.put("flag", 1);
                map.put("text", "寄快递demo");

            }
            dataList.add(map);
        }
        return dataList;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mainList.getCheckedItemIds();

        if ((dataList.get(i) + "").split("=")[3].equals("0}") == true) {
            Intent intent = new Intent(HomeActivity.this, RdActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(HomeActivity.this, SdActivity.class);
            startActivity(intent);
        }

    }


}
