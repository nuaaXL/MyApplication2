package com.example.chenjunfan.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
 * Created by 李计芃 on 2016/7/17.
 */
public class help extends Fragment implements AdapterView.OnItemClickListener {
    private User user =new User();
    private List<Request> dataList = new ArrayList<Request>();
    private List<Map<String, Object>> datamapList = new ArrayList<Map<String, Object>>();
    private ListView mainList;
    private ProgressDialog prodialog;
    private int firstflag=0;
    SimpleAdapter mainListAdp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.help, container, false);


        mainList = (ListView) view.findViewById(R.id.LVhelp_receive);

        mainListAdp = new SimpleAdapter(getActivity(), datamapList, R.layout.item_main, new String[]{"pic", "IV_flag", "content","flag","location","num","name","time","point","done"}, new int[]{R.id.pic, R.id.IV_flag, R.id.item_content,R.id.flag,R.id.item_place,R.id.tv_num,R.id.item_username,R.id.item_time,R.id.tv_jifen,R.id.iv_done});
        mainList.setAdapter(mainListAdp);
        mainList.setOnItemClickListener(this);
        getDataFromNetwork();



        return view;
    }

    private void getDataFromNetwork() {
        prodialog=new ProgressDialog(getActivity());
        prodialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prodialog.setIndeterminate(true);
        prodialog.setMessage("正在刷新");


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
                            "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");
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
                                    "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");
                            db5.execSQL("insert into myrequesttb(num,time,flag,point,publisher,p_number,p_phone,helper,h_number,h_phone,user_loc,content,infor," +
                                    "r_nameORmessage,r_locORpackage_loc,r_phoneORphone,nullORpackage_Id)values(" + request.getNum() + ",'" + request.getTime() + "'," +
                                    request.getFlag() +","+request.getPoint()+ ",'" + request.getPublisher() + "','" + request.getP_number() + "','" + request.getP_phone() + "','" + request.getHelper()
                                    + "','" + request.getH_number() + "','" + request.getH_phone() + "','" + request.getUser_loc() + "','" + request.getContent() + "','" +
                                    request.getInfor() + "','" + request.getR_nameORmessage() + "','" + request.getR_locORpackage_loc() + "','" + request.getR_phoneORphone() +
                                    "','" + request.getNullORpackage_Id() + "')");
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
                    //  HomeActivity.this.findViewById(R.id.load_layout).setVisibility(View.GONE);
                }

                    handlerunshow.sendMessage(new Message());




            }
        });
        t2.start();


    }


    public List<Map<String, Object>> acking(List<Request> requests) {
        //List<Map<String,Object>> req=new ArrayList<Map<String,Object>>();
        Request mid = new Request();
        // Log.i("in", "----------- "+num);
        for (int i = 0; i < requests.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            mid = requests.get(i);
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
            int flag = mid.getFlag();
            flag = flag%10;

            if (mid.getNum() != 0 && flag == 2)//寄
            {
                map.put("IV_flag", R.drawable.rflag);
                map.put("content", mid.getContent());
                map.put("flag", mid.getFlag());
                map.put("location", mid.getUser_loc());
                map.put("num", mid.getNum());
                map.put("name", mid.publisher);
                map.put("point",mid.getPoint());
                String str = mid.getTime();
                String[] time = str.split("-");
                str = time[0] + "年" + time[1] + "月" + time[2] + "日" + time[3] + "点" + time[4] + "分";
                map.put("time", str);
                if((flag%100)/10==1)
                {
                    map.put("done",R.mipmap.iv_accept);
                }
                else if((flag%100)/10==2)
                {
                    map.put("done",R.mipmap.iv_done);
                }
                datamapList.add(map);
            } else if (mid.getNum() != 0 && flag == 1) {
                map.put("IV_flag", R.drawable.sflag);
                map.put("content", mid.getContent());
                map.put("flag", mid.getFlag());
                map.put("location", mid.getUser_loc());
                map.put("num", mid.getNum());
                map.put("point",mid.getPoint());
                map.put("name", mid.publisher);
                String str = mid.getTime();
                String[] time = str.split("-");
                str = time[0] + "年" + time[1] + "月" + time[2] + "日" + time[3] + "点" + time[4] + "分";
                map.put("time", str);
                if((flag%100)/10==1)
                {
                    map.put("done",R.mipmap.iv_accept);
                }
                else if((flag%100)/10==2)
                {
                    map.put("done",R.mipmap.iv_done);
                }
                datamapList.add(map);
            }


        }
        // Log.i("in", "----------- "+num);
        return datamapList;
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
            mainListAdp.notifyDataSetChanged();
            // HomeActivity.this.findViewById(R.id.load_layout).setVisibility(View.GONE);


        }

    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {



            Object obj=datamapList.get(i).get("flag");
            Object obj2=datamapList.get(i).get("num");
            int n=(int)obj2;
            int temp = (int)obj;
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
}