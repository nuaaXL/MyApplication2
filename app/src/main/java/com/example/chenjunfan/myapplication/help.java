package com.example.chenjunfan.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李计芃 on 2016/7/17.
 */
public class help extends Fragment implements AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeLayout;
    private User user =new User();
    private List<Request> dataList = new ArrayList<Request>();
    private List<ItemBean> itemBeanList =new ArrayList<ItemBean>();
    private MyAdapter myAdapter;
    private ListView mainList;
    private ProgressDialog prodialog;
    private int firstflag=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.help, container, false);


        mainList = (ListView) view.findViewById(R.id.LVhelp_receive);
        myAdapter = new MyAdapter(getActivity(),itemBeanList);
        mainList.setAdapter(myAdapter);
        mainList.setOnItemClickListener(this);
        getDataFromNetwork();

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.help_swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.button_g);



        return view;
    }

    private void getDataFromNetwork() {
        prodialog=new ProgressDialog(getActivity());
        prodialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prodialog.setIndeterminate(true);
        prodialog.setMessage("正在刷新");
        prodialog.setCancelable(false);



        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(firstflag==0)

                    {
                        handlershow.sendMessage(new Message());
                        firstflag=1;
                    }

                    SQLiteDatabase db3 = getActivity().openOrCreateDatabase("request.db",getActivity().MODE_PRIVATE,null);

                    db3.execSQL("create table if not exists myrequesttb(num integer,time text,flag integer,point integer,publisher text" +
                         ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
                            "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text,url text)");
                    db3.execSQL("drop table myrequesttb");
                    db3.close();

                    SQLiteDatabase db = getActivity().openOrCreateDatabase("user.db", getActivity().MODE_PRIVATE, null);
//                    db.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
//                            ",phone text,school text,point integer)");
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

                    db.close();
                    c.close();


                    String Url;
                    Url = "http://" + getResources().getText(R.string.IP) + ":8080/Ren_Test/requestServlet" + "?type=me_h" + "&num=-1" + "&userId=" + user.getUserId();
                    Log.i("tag", Url);
                    URL url = new URL(Url);
                    URLConnection conn = url.openConnection();

                    Message msg = new Message();


                    conn.setRequestProperty("Accept-Charset", "gbk");
                    conn.setRequestProperty("contentType", "gbk");


                    conn.setReadTimeout(6000);

                    InputStream stream = conn.getInputStream();

                    InputStreamReader reader = new InputStreamReader(stream, "gbk");

                    BufferedReader br = new BufferedReader(reader);
                    String str = "";
                    String line = "";

                    while ((line = br.readLine()) != null) {
                        str += line;
                    }

                    System.out.println("ddddddddddddd" + str);


                    Gson gson = new Gson();
                    List<Request> requestList = gson.fromJson(str, new TypeToken<List<Request>>() {
                    }.getType());


                    dataList = requestList;
                    for (int i = 0; i < dataList.size(); i++) {

                        Request request = (Request) dataList.get(i);
                        if (request.getNum() != 0) {
                            SQLiteDatabase db5 = getActivity().openOrCreateDatabase("request.db", getActivity().MODE_ENABLE_WRITE_AHEAD_LOGGING, null);
                            db5.execSQL("create table if not exists myrequesttb(num integer,time text,flag integer,point integer,publisher text" +
                                    ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
                                    "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text,url text)");
                            db5.execSQL("insert into myrequesttb(num,time,flag,point,publisher,p_number,p_phone,helper,h_number,h_phone,user_loc,content,infor," +
                                    "r_nameORmessage,r_locORpackage_loc,r_phoneORphone,nullORpackage_Id,url)values(" + request.getNum() + ",'" + request.getTime() + "'," +
                                    request.getFlag() +","+request.getPoint()+ ",'" + request.getPublisher() + "','" + request.getP_number() + "','" + request.getP_phone() + "','" + request.getHelper()
                                    + "','" + request.getH_number() + "','" + request.getH_phone() + "','" + request.getUser_loc() + "','" + request.getContent() + "','" +
                                    request.getInfor() + "','" + request.getR_nameORmessage() + "','" + request.getR_locORpackage_loc() + "','" + request.getR_phoneORphone() +
                                    "','" + request.getNullORpackage_Id() +"','"+request.getUrl()+ "')");
                            db5.close();

                        } else {
                            msg.obj = "已经显示全部条目";
                            handler2.sendMessage(msg);

                        }


                    }

                    acking(dataList);
                    handler4.sendMessage(new Message());


                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = "服务器无响应";
                    handler2.sendMessage(msg);
                }

                    handlerunshow.sendMessage(new Message());




            }
        });
        t2.start();


    }


    public List<ItemBean> acking(List<Request> requests)
    {
        //List<Map<String,Object>> req=new ArrayList<Map<String,Object>>();
        Request mid=new Request();
        Bitmap iImagepic=null;
        int iImagedone=0,iImageflag=0,iflag=0,inum=0,ijifen=0;
        String icontent=null,iusername=null,iplace=null,itime = null;
        for(int i=0;i<requests.size();i++)
        {
            mid=requests.get(i);
            int tflag=mid.getFlag();



            if(mid.getNum()!=0&&tflag%10==2)
            {
                Resources res = getResources();
                if(tflag/1000==1)
                {
                    iImagepic= BitmapFactory.decodeResource(res, R.drawable.sf);
                }
                else if(tflag/1000==2)
                {
                    iImagepic= BitmapFactory.decodeResource(res, R.drawable.yt);
                }
                else if(tflag/1000==3)
                {
                    iImagepic= BitmapFactory.decodeResource(res, R.drawable.st);
                }
                else if(tflag/1000==4)
                {
                    iImagepic= BitmapFactory.decodeResource(res, R.drawable.zt);
                }
                else if(tflag/1000==5)
                {
                    iImagepic= BitmapFactory.decodeResource(res, R.drawable.tt);
                }
                else if(tflag/1000==6)
                {
                    iImagepic= BitmapFactory.decodeResource(res, R.drawable.yd);
                }
                else if(tflag/1000==7)
                {
                    iImagepic= BitmapFactory.decodeResource(res, R.drawable.bs);
                }
                iImageflag=R.drawable.rflag;
                icontent=mid.getContent();
                iflag=mid.getFlag();
                iplace=mid.getUser_loc();
                inum=mid.getNum();
                iusername=mid.publisher;
                ijifen=mid.getPoint();
                String str = mid.getTime();
                String []ttime =str.split("-");
                str=ttime[0]+"年"+ttime[1]+"月"+ttime[2]+"日"+ttime[3]+"点"+ttime[4]+"分";
                itime=str;
                if(tflag%100/10==1)
                {
                   iImagedone=R.drawable.iv_accept;
                }
                else if(tflag%100/10==2)
                {
                    iImagedone=R.drawable.iv_done;

                }
                itemBeanList.add(new ItemBean(itime,inum,iflag,iImagepic,iImagedone,iImageflag,icontent,ijifen,iusername,iplace));

            }
            else if(mid.getNum()!=0&&tflag%10==1)//寄
            {
                iImagepic=getHttpBitmap("http://"+getResources().getText(R.string.IP)+"/request/"+mid.getUrl());
                iImageflag=R.drawable.sflag;
                icontent=mid.getContent();
                iflag=mid.getFlag();
                iplace=mid.getUser_loc();
                inum=mid.getNum();
                iusername=mid.publisher;
                ijifen=mid.getPoint();
                String str = mid.getTime();
                String []ttime =str.split("-");
                str=ttime[0]+"年"+ttime[1]+"月"+ttime[2]+"日"+ttime[3]+"点"+ttime[4]+"分";
                itime=str;
                if(tflag%100/10==1)
                {
                    iImagedone=R.drawable.iv_accept;
                }
                else if(tflag%100/10==2)
                {
                    iImagedone=R.drawable.iv_done;
                }

                itemBeanList.add(new ItemBean(itime,inum,iflag,iImagepic,iImagedone,iImageflag,icontent,ijifen,iusername,iplace));
            }


        }
        return itemBeanList;
    }

    Handler handler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    Handler handler4 = new Handler()//更新适配器
    {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            myAdapter.notifyDataSetChanged();
            // HomeActivity.this.findViewById(R.id.load_layout).setVisibility(View.GONE);


        }

    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            int n=itemBeanList.get(i).getNum();
            int temp = itemBeanList.get(i).getFlag();
            SharedPreferences pre=getActivity().getSharedPreferences("clickitemnum",getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor =pre.edit();
            editor.putInt("num",n);
            Log.i("putnum", "num: "+ n);
            editor.commit();
            if (temp%10==2) {//取
                Log.i("tag", "取");
                Intent intent = new Intent(getActivity(), helprdActivity.class);
                startActivity(intent);
            } else {//寄
                Intent intent = new Intent(getActivity(), helpsdActivity.class);
                Log.i("tag", "寄");
                startActivity(intent);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    Handler handlershow = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            prodialog.show();
        }
    };
    Handler handlerunshow = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            prodialog.cancel();
        }
    };
    public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            Log.d("tag", url);
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               itemBeanList =new ArrayList<ItemBean>();
                getDataFromNetwork();
                mSwipeLayout.setRefreshing(false);
            }
        }, 1000);
    }
}