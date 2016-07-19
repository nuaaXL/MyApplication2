package com.example.chenjunfan.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
public class Fragment_helpReceive extends Fragment {
    private User user;
    private List<Request> dataList=new ArrayList<Request>();
    private List<Map<String,Object>>datamapList = new ArrayList<Map<String, Object>>();
    private ListView mainList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view=inflater.inflate(R.layout.help_receive, container, false);
        mainList= (ListView) container.getChildAt(R.id.LVhelp_receive);
        
        return view;
    }

    private void getDataFromNetwork() {



        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String Url;
                    Url="http://"+getResources().getText(R.string.IP)+":8080/Ren_Test/requestServlet"+"?type=me_p"+"&num=-1"+"&userId="+;
                    Log.i("tag",Url);
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


    public List<Map<String, Object>> acking(List<Request> requests)
    {
        //List<Map<String,Object>> req=new ArrayList<Map<String,Object>>();
        Request mid=new Request();
       // Log.i("in", "----------- "+num);
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

            if(mid.getNum()!=0&&flag==2)//寄
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
            else if(mid.getNum()!=0&&flag==1)
            {
                map.put("IV_flag",R.drawable.sflag);
                map.put("content",mid.getContent());
                map.put("flag",mid.getFlag());
                map.put("location",mid.getR_locORpackage_loc());
                map.put("num",mid.getNum());
                map.put("name",mid.publisher);
                String str = mid.getTime();
                String [] time=str.split("-");
                str=time[0]+"年"+time[1]+"月"+time[2]+"日"+time[3]+"点"+time[4]+"分";
                map.put("time",str);
                datamapList.add(map);
            }


        }
       // Log.i("in", "----------- "+num);
        return datamapList;
    }

}