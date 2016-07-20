package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class HomeActivity extends Activity implements AdapterView.OnItemClickListener,LoadListView.ILoadListener,SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeLayout;
    private ImageView homeIV;
    private ImageView meIV;
    private RelativeLayout homeRL;
    private RelativeLayout wodeRL;
    private TextView homeTV;
    private TextView meTV;
    private LoadListView mainList;
    private SimpleAdapter mainListAdp;
    private List<Request> dataList=new ArrayList<Request>();
    private List<Map<String,Object>>datamapList = new ArrayList<Map<String, Object>>();
    private RelativeLayout homeLL;
    private RelativeLayout meRL;
    private LinearLayout odersLL;
    private LinearLayout editLL;
    private LinearLayout coinLL;
    private LinearLayout settingLL;
    private FloatingActionButton actionC;
    private TextView accoutTV;
    private TextView nameTV;
    private User user = new User();
    private TextView genderTV;
    private int num=-1;
    static Activity ActivityA;
    private  TextView timeTV;
    private boolean isLoading;
    int lastvisibleitem,totalitemcount;

    @Override
    protected void onRestart() {
        super.onRestart();


        }

    @Override
    protected void onResume() {
        super.onResume();
        int flag = 0;
        SharedPreferences pre = getSharedPreferences("refreshflag", MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        flag = pre.getInt("flag", 0);
        if (flag == 1) {
            num=-1;
            datamapList.removeAll(datamapList);
            getDataFromNetwork();
            refresh();
            Toast.makeText(HomeActivity.this,"onResume"+flag,Toast.LENGTH_LONG).show();
        } else {

        }

        editor.remove("flag");
        editor.commit();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     *
     */



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
            int flag=mid.getFlag();
            flag=flag-(flag/10*10);

            if(mid.getNum()!=0&&flag==2)
            {
                map.put("IV_flag",R.drawable.rflag);
                map.put("content",mid.getContent());
                map.put("flag",mid.getFlag());
                map.put("location",mid.getUser_loc());
                map.put("num",mid.getNum());
                map.put("name",mid.publisher);
                String str = mid.getTime();
               String []time =str.split("-");
                str=time[0]+"年"+time[1]+"月"+time[2]+"日"+time[3]+"点"+time[4]+"分";
                map.put("time",str);
                datamapList.add(map);
            }
            else if(mid.getNum()!=0&&flag==1)//寄
            {
                map.put("IV_flag",R.drawable.sflag);
                map.put("content",mid.getContent());
                map.put("flag",mid.getFlag());
                map.put("location",mid.getUser_loc());
                map.put("num",mid.getNum());
                map.put("name",mid.publisher);
                String str = mid.getTime();
                String [] time=str.split("-");
                str=time[0]+"年"+time[1]+"月"+time[2]+"日"+time[3]+"点"+time[4]+"分";
                map.put("time",str);
                datamapList.add(map);
            }


        }
        Log.i("in", "----------- "+num);
        return datamapList;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActivityA=this;
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
        mainList = (LoadListView) findViewById(R.id.LVmain);
        mainList.setInterface(this);

        homeLL = (RelativeLayout) findViewById(R.id.LLhome);
        meRL = (RelativeLayout) findViewById(R.id.RLme);
        odersLL = (LinearLayout) findViewById(R.id.LL_orders);
        editLL = (LinearLayout) findViewById(R.id.LL_edit);
        coinLL = (LinearLayout) findViewById(R.id.LL_coin);
        settingLL = (LinearLayout) findViewById(R.id.LL_setting);
        genderTV= (TextView) findViewById(R.id.tv_gender);
        homeTV = (TextView) findViewById(R.id.tv_home);
        meTV = (TextView) findViewById(R.id.tv_me);
        timeTV = (TextView) findViewById(R.id.item_time);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.button_g);

        refresh();


        mainListAdp = new SimpleAdapter(this, datamapList, R.layout.item_main, new String[]{"pic", "IV_flag", "content","flag","location","num","name","time"}, new int[]{R.id.pic, R.id.IV_flag, R.id.item_content,R.id.flag,R.id.item_place,R.id.tv_num,R.id.item_username,R.id.item_time});
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

        odersLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Help_odersActivity.class);
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


                        dataList = requestList;
                        for (int i = 0; i < dataList.size(); i++) {

                            Request request = (Request) dataList.get(i);
                            if(request.getNum()!=0) {
                                SQLiteDatabase db = openOrCreateDatabase("request.db", MODE_ENABLE_WRITE_AHEAD_LOGGING, null);
                                db.execSQL("create table if not exists requesttb(num integer,time text,flag integer,publisher text" +
                                        ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
                                        "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");
                                db.execSQL("insert into requesttb(num,time,flag,publisher,p_number,p_phone,helper,h_number,h_phone,user_loc,content,infor," +
                                        "r_nameORmessage,r_locORpackage_loc,r_phoneORphone,nullORpackage_Id)values(" + request.getNum() + ",'" + request.getTime() + "'," +
                                        request.getFlag() + ",'" + request.getPublisher() + "','" + request.getP_number() + "','" + request.getP_phone() + "','" + request.getHelper()
                                        + "','" + request.getH_number() + "','" + request.getH_phone() + "','" + request.getUser_loc() + "','" + request.getContent() + "','" +
                                        request.getInfor() + "','" + request.getR_nameORmessage() + "','" + request.getR_locORpackage_loc() + "','" + request.getR_phoneORphone() +
                                        "','" + request.getNullORpackage_Id() + "')");
                                db.close();
                                num = request.getNum();

                            }
                            else
                            {
                                    msg.obj = "已经显示全部条目";
                                    handler2.sendMessage(msg);


                                num=0;
                                break;

                            }


                        }
                    if(num!=0)
                        num--;
                        acking(dataList);
                        handler4.sendMessage(new Message());



                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = "服务器无响应";
                    handler2.sendMessage(msg);
                  //  HomeActivity.this.findViewById(R.id.load_layout).setVisibility(View.GONE);
                }


            }
        });
        t2.start();


    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        try {



            Object obj=datamapList.get(i).get("flag");
            Object obj2=datamapList.get(i).get("num");
            int n=(int)obj2;
            int temp = (int)obj;
            SharedPreferences pre=getSharedPreferences("clickitemnum",MODE_PRIVATE);
            SharedPreferences.Editor editor =pre.edit();
            editor.putInt("num",n);
            editor.commit();
            if (temp%10==2) {//取
                Intent intent = new Intent(HomeActivity.this, RdActivity.class);
                startActivity(intent);
            } else {//寄
                Intent intent = new Intent(HomeActivity.this, SdActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
         */     Message msg = new Message();

                handler3.sendMessage(msg);


                db.close();
                c.close();
            }
        }
        );

        t.start();
    }

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
            //Toast.makeText(HomeActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
        }
    };

    Handler handler4 = new Handler()//更新适配器
    {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            isLoading=false;

            mainListAdp.notifyDataSetChanged();
           // HomeActivity.this.findViewById(R.id.load_layout).setVisibility(View.GONE);


        }
    };




    @Override
    public void onLoad() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataFromNetwork();
                mainList.loadComplete();
            }
        },1700);

    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
                datamapList.removeAll(datamapList);
                num=-1;
                getDataFromNetwork();
            }
        }, 1000);
    }

}


