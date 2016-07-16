package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
    private TextView homeTV;
    private TextView meTV;
    private ListView mainList;
    private SimpleAdapter mainListAdp;
    private List<Request> dataList=new ArrayList<Request>();
    private List<Map<String,Object>>datamapList = new ArrayList<Map<String, Object>>();
    private RelativeLayout homeLL;
    private RelativeLayout meRL;
    private LinearLayout editLL;
    private LinearLayout coinLL;
    private LinearLayout settingLL;
    private FloatingActionButton actionC;
    private TextView accoutTV;
    private TextView nameTV;
    private User user = new User();
    private TextView genderTV;
    private int num=-1;

    @Override
    protected void onRestart() {
        super.onRestart();
        refresh();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     *
     */

   /* Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            Log.i("handl", "-----------"+num);
            super.handleMessage(msg);

//             dataList = (List) msg.obj;
//            for (int i = 0; i < dataList.size(); i++) {
//
//                Request request = (Request) dataList.get(i);
//                SQLiteDatabase db = openOrCreateDatabase("request.db",MODE_ENABLE_WRITE_AHEAD_LOGGING,null);
//                db.execSQL("create table if not exists requesttb(num integer,time text,flag integer,publisher text" +
//                        ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
//                        "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");
//                db.execSQL("insert into requesttb(num,time,flag,publisher,p_number,p_phone,helper,h_number,h_phone,user_loc,content,infor" +
//                        "r_nameORmessage,r_locORpackage_loc,r_phoneORphone,nullORpackage_Id)values("+request.getNum()+",'"+request.getTime()+"',"+
//                request.getFlag()+",'"+request.getPublisher()+"','"+request.getP_number()+"','"+request.getP_phone()+"','"+request.getHelper()
//                +"','"+request.getH_number()+"','"+request.getH_phone()+"','"+request.getUser_loc()+"','"+request.getContent()+"','"+
//                request.getInfor()+"','"+request.getR_nameORmessage()+"','"+request.getR_locORpackage_loc()+"','"+request.getR_phoneORphone()+
//                "','"+request.getNullORpackage_Id());
//                db.close();
//                num=request.getNum();
//            }
            Log.i("handl2", "----------- "+num);


        }
    };*/
    Handler handler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            Toast.makeText(HomeActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
        }
    };

    Handler handler3 = new Handler() //更新user
    {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            nameTV.setText(user.getName());
            accoutTV.setText(user.getUserId());
            switch (user.getGender()) {
                case 1:
                    genderTV.setText("男");
                    break;
                case 2:
                    genderTV.setText("女");
                    break;
                default:
                    genderTV.setText("未知");
            }
            Toast.makeText(HomeActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
        }
    };

    Handler handler4 = new Handler()//更新适配器
    {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            mainListAdp.notifyDataSetChanged();


        }
    };








    public List<Map<String, Object>> acking(List<Request> requests)
    {
        //List<Map<String,Object>> req=new ArrayList<Map<String,Object>>();
        Request mid=new Request();
        Log.i("in", "----------- "+num);
        for(int i=0;i<requests.size();i++)
        {
            Map<String,Object> map=new HashMap<String,Object>();
            mid=requests.get(i);
            /*map.put("num", mid.getNum());
            map.put("time", mid.getTime());
            map.put("flag", mid.getFlag());
            map.put("publisher", mid.getPublisher());
            map.put("p_number", mid.getP_number());
            map.put("p_phone",mid.getP_phone());
            map.put("helper", mid.getHelper());
            map.put("h_number", mid.getH_number());
            map.put("h_phone", mid.getH_phone());
            map.put("user_loc", mid.getUser_loc());
            map.put("content", mid.getContent());
            map.put("info", mid.getInfor());
            map.put("r_nameORmessage", mid.getR_nameORmessage());
            map.put("r_locORpackage_loc", mid.getR_locORpackage_loc());
            map.put("r_phoneORphone", mid.getR_phoneORphone());
            map.put("nullORpackage", mid.getNullORpackage_Id());*/
            if(mid.getFlag()==2)//寄
            {
                map.put("IV_flag",R.drawable.rflag);
                map.put("content",mid.getContent());
                map.put("flag",mid.getFlag());
                map.put("location",mid.getUser_loc());
                map.put("num",mid.getNum());
            }
            else//取
            {
                map.put("IV_flag",R.drawable.sflag);
                map.put("content",mid.getContent());
                map.put("flag",mid.getFlag());
                map.put("location",mid.getR_locORpackage_loc());
                map.put("num",mid.getNum());
            }
            datamapList.add(map);
        }
        Log.i("in", "----------- "+num);
        return datamapList;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.i("主线程", "getdatafromnetwork后 ");
        SQLiteDatabase db5 = openOrCreateDatabase("request.db",MODE_PRIVATE,null);
        db5.execSQL("create table if not exists requesttb(num integer,time text,flag integer,publisher text" +
                ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
                "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");
        db5.execSQL("delete from requesttb");
        db5.close();
        getDataFromNetwork();


        accoutTV = (TextView) findViewById(R.id.tv_accout);
        nameTV = (TextView) findViewById(R.id.tv_name);
        homeIV = (ImageView) findViewById(R.id.IV_home);
        meIV = (ImageView) findViewById(R.id.IV_me);
        homeRL = (RelativeLayout) findViewById(R.id.RL_home);
        wodeRL = (RelativeLayout) findViewById(R.id.RL_me);
        mainList = (ListView) findViewById(R.id.LVmain);
        homeLL = (RelativeLayout) findViewById(R.id.LLhome);
        meRL = (RelativeLayout) findViewById(R.id.RLme);
        editLL = (LinearLayout) findViewById(R.id.LL_edit);
        coinLL = (LinearLayout) findViewById(R.id.LL_coin);
        settingLL = (LinearLayout) findViewById(R.id.LL_setting);
        genderTV= (TextView) findViewById(R.id.tv_gender);
        homeTV = (TextView) findViewById(R.id.tv_home);
        meTV = (TextView) findViewById(R.id.tv_me);
/*
    取user数据库:
 */
      /*  SQLiteDatabase db = openOrCreateDatabase("user.db",MODE_ENABLE_WRITE_AHEAD_LOGGING,null);
        db.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
                ",phone text,school text,point integer)");

        Cursor c = db.rawQuery("select * from usertb",null);

        if(c!=null)
        {
            Log.i("c", ""+(c==null));
            Log.i("c.movetonext", ""+c.moveToNext());
            while(c.moveToNext()){


                user.setUserId(c.getString(c.getColumnIndex("userId")));
                user.setName(c.getString(c.getColumnIndex("name")));
                user.setPasswd(c.getString(c.getColumnIndex("passwd")));
                user.setGender(c.getInt(c.getColumnIndex("gender")));
                user.setPhone(c.getString(c.getColumnIndex("phone")));
                user.setSchool(c.getString(c.getColumnIndex("school")));
                user.setPoint(c.getInt(c.getColumnIndex("point")));

                Log.i("usermain",c.getString(c.getColumnIndex("userId")));
                Log.i("usermain", c.getString(c.getColumnIndex("name")));
                Log.i("usermain", c.getString(c.getColumnIndex("passwd")));
                Log.i("usermain", c.getInt(c.getColumnIndex("gender"))+"");
                Log.i("usermain",c.getString(c.getColumnIndex("phone")) );
                Log.i("usermain", c.getString(c.getColumnIndex("school")));
                Log.i("usermain", c.getInt(c.getColumnIndex("point"))+"");



            }
        }
        db.close();
        c.close();

        /*
        显示到me里：
         */
        /*nameTV.setText(user.getName());
        accoutTV.setText(user.getUserId());
        switch (user.getGender())
        {
            case 1:
                genderTV.setText("男");
                break;
            case 2:
                genderTV.setText("女");
                break;
            default:
                genderTV.setText("未知");
        }*/
        refresh();


        mainListAdp = new SimpleAdapter(this, datamapList, R.layout.item_main, new String[]{"pic", "IV_flag", "content","flag","location","num"}, new int[]{R.id.pic, R.id.IV_flag, R.id.item_content,R.id.flag,R.id.item_place,R.id.tv_num});
        mainList.setAdapter(mainListAdp);
        mainList.setOnItemClickListener(this);



        actionC = new FloatingActionButton(getBaseContext());
        actionC.setIcon(R.mipmap.receive);
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


        settingLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });


        coinLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Point_viewActivity.class);
                startActivity(intent);
            }
        });
        editLL.setOnClickListener(new View.OnClickListener() {
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
                homeTV.setTextColor(getResources().getColor(R.color.button_g));
                meTV.setTextColor(getResources().getColor(R.color.textgrey));

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
                meTV.setTextColor(getResources().getColor(R.color.button_g));
                homeTV.setTextColor(getResources().getColor(R.color.textgrey));

                meRL.hasFocus();

            }
        });


    }

    private void getDataFromNetwork() {


        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String Url;
                    Url="http://"+getResources().getText(R.string.IP)+":8080/Ren_Test/requestServlet"+"?type=all"+"&num="+num;
                    Log.i("tag",Url);
                    Log.i("num",num+"");
                    URL url = new URL(Url);
                    URLConnection conn = url.openConnection();

                    Message msg = new Message();



                    conn.setRequestProperty("Accept-Charset", "gbk");
                    conn.setRequestProperty("contentType", "gbk");

                    msg.obj = "数据接收";
                    handler2.sendMessage(msg);




                    conn.setReadTimeout(6000);

                    InputStream stream = conn.getInputStream();

                        InputStreamReader reader = new InputStreamReader(stream, "gbk");

                        BufferedReader br = new BufferedReader(reader);
                        String str="";
                        String line="";

                        while((line=br.readLine())!=null)
                        {
                            str+=line;
                        }

                        System.out.println("ddddddddddddd" + str);


                        Gson gson = new Gson();
                        List<Request> requestList = gson.fromJson(str, new TypeToken<List<Request>>() {
                        }.getType());
                        // Request req = (Request) requestList.get(0);

                    /*for (Request reg2 : requestList) {
                        System.out.println(user.getUserName());
                    }*/


                   /* Message msg = new Message();
                    msg.obj = requestList;
                    handler.sendMessage(msg);*/

                        dataList = requestList;
                        for (int i = 0; i < dataList.size(); i++) {

                            Request request = (Request) dataList.get(i);
                            SQLiteDatabase db = openOrCreateDatabase("request.db", MODE_ENABLE_WRITE_AHEAD_LOGGING, null);
                            db.execSQL("create table if not exists requesttb(num integer,time text,flag integer,publisher text" +
                                    ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
                                    "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");
                            db.execSQL("insert into requesttb(num,time,flag,publisher,p_number,p_phone,helper,h_number,h_phone,user_loc,content,infor,"+
                                    "r_nameORmessage,r_locORpackage_loc,r_phoneORphone,nullORpackage_Id)values(" + request.getNum() + ",'" + request.getTime() + "'," +
                                    request.getFlag() + ",'" + request.getPublisher() + "','" + request.getP_number() + "','" + request.getP_phone() + "','" + request.getHelper()
                                    + "','" + request.getH_number() + "','" + request.getH_phone() + "','" + request.getUser_loc() + "','" + request.getContent() + "','" +
                                    request.getInfor() + "','" + request.getR_nameORmessage() + "','" + request.getR_locORpackage_loc() + "','" + request.getR_phoneORphone() +
                                    "','" + request.getNullORpackage_Id()+"')");
                            db.close();
                            num = request.getNum();


                        }

                    acking(dataList);
                    handler4.sendMessage(new Message());


                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = "服务器无响应";
                    handler2.sendMessage(msg);
                }


            }
        });


        t2.start();


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

    public void refresh() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
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

        /*
        显示到me里：
         */
                Message msg=new Message();
                msg.obj="更新资料";
                handler3.sendMessage(msg);

                db.close();
                c.close();
            }
        }
        );t.start();
    }



}


 /*for (int i = 0; i < 12; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            switch (i) {
                case 0:
                    map.put("flag", 0);
                    map.put("IV_flag",R.drawable.rflag);
                    map.put("content","辣条");
                    map.put("location","南区36栋");
                    map.put("pic",R.mipmap.latiao);
                    break;

                case 1:
                    map.put("flag", 0);
                    map.put("IV_flag",R.drawable.rflag);
                    map.put("content","飞机模型");
                    map.put("location","一号楼");
                    map.put("pic",R.mipmap.plane);
                    break;

                case 2:
                    map.put("flag", 1);
                    map.put("IV_flag",R.drawable.sflag);
                    map.put("content","iPad");
                    map.put("location","D3教学楼");
                    map.put("pic",R.mipmap.ipad);
                    break;

                case 3:
                    map.put("flag", 0);
                    map.put("IV_flag",R.drawable.rflag);
                    map.put("content","篮球");
                    map.put("location","灯光球场");
                    map.put("pic",R.mipmap.ball);
                    break;

                case 4:
                    map.put("flag", 0);
                    map.put("IV_flag",R.drawable.rflag);
                    map.put("content","台灯");
                    map.put("location","怡园22栋");
                    map.put("pic",R.mipmap.light);
                    break;

                case 5:
                    map.put("flag", 1);
                    map.put("IV_flag",R.drawable.sflag);
                    map.put("content","一只篮球");
                    map.put("location","图书馆门口");
                    map.put("pic",R.mipmap.ball2);
                    break;

                case 6:
                    map.put("flag", 0);
                    map.put("IV_flag",R.drawable.rflag);
                    map.put("content","水杯");
                    map.put("location","一号楼");
                    map.put("pic",R.mipmap.bottle);
                    break;

                case 7:
                    map.put("flag", 1);
                    map.put("IV_flag",R.drawable.sflag);
                    map.put("content","相机");
                    map.put("location","怡园19栋");
                    map.put("pic",R.mipmap.camera);
                    break;

                case 8:
                    map.put("flag", 1);
                    map.put("IV_flag",R.drawable.sflag);
                    map.put("content","一箱零食");
                    map.put("location","三号楼");
                    map.put("pic",R.mipmap.food);
                    break;

                case 9:
                    map.put("flag", 0);
                    map.put("IV_flag",R.drawable.rflag);
                    map.put("content","雨伞");
                    map.put("location","慧一");
                    map.put("pic",R.mipmap.umbre);
                    break;

                case 10:
                    map.put("flag", 1);
                    map.put("IV_flag",R.drawable.sflag);
                    map.put("content","一双鞋子");
                    map.put("location","博园15栋");
                    map.put("pic",R.mipmap.shoe);
                    break;

                case 11:
                    map.put("flag", 1);
                    map.put("IV_flag",R.drawable.sflag);
                    map.put("content","平板电脑保护套");
                    map.put("location","四号楼");
                    map.put("pic",R.mipmap.protect);
                    break;

                default:break;
            }


            dataList.add(map);
        }*/

