package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.List;

/**
 * Created by chenjunfan on 16/7/20.
 */
public class helpsdActivity extends Activity {
    private TextView accoutTV,nameTV,contentTV,userlocTV,payTV,rnameTV,rphoneTV,raddressTV,kuaidiTV,noteTV,jifenTV;
    private Button callBT,messageBT,hfinishBT;
    private RelativeLayout callRL,finishiRL;
    private String accout;
    private String name;
    private String content;
    private String userloc;
    private String pay;
    private String rname;
    private String rphone;
    private String raddress;
    private String kuaidi;
    private String note;
    private String number;
    private String pphone;
    private String touxiangURL,picurl,publisherid;
    private ImageView touxiangIV,tupianIV,imageBack;
    private int jifen;
    private int num,tflag;
    private Bitmap touxiangbit,tupianbit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_sd);

        imageBack = (ImageView) findViewById(R.id.img_back);
        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
        initview();

    }
    public void initview()
    {
        accoutTV = (TextView) findViewById(R.id.tv_psd_accout);
        nameTV =(TextView) findViewById(R.id.tv_psd_name);
        contentTV = (TextView) findViewById(R.id.tv_psd_content);
        userlocTV = (TextView) findViewById(R.id.tv_psd_loc);
        payTV =(TextView)findViewById(R.id.tv_psd_pay);
        rnameTV = (TextView) findViewById(R.id.tv_psd_rname);
        rphoneTV = (TextView) findViewById(R.id.tv_psd_rphone);
        raddressTV = (TextView) findViewById(R.id.tv_psd_address);
        kuaidiTV = (TextView) findViewById(R.id.tv_psd_kuaidi);
        noteTV = (TextView) findViewById(R.id.tv_psd_beizhu);
        callBT = (Button) findViewById(R.id.btn_psd_call);
        messageBT = (Button) findViewById(R.id.btn_psd_message);
        hfinishBT = (Button) findViewById(R.id.btn_help_finished);
        callRL = (RelativeLayout) findViewById(R.id.rl_psd_call);
        finishiRL = (RelativeLayout) findViewById(R.id.rl_psd_finish);
        jifenTV = (TextView) findViewById(R.id.tv_psd_jifen);
        touxiangIV= (ImageView) findViewById(R.id.iv_psd_touxiang);
        tupianIV = (ImageView) findViewById(R.id.help_sd_image);

        refresh();


    }

    public void refresh()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pre = getSharedPreferences("clickitemnum", MODE_PRIVATE);
                num = pre.getInt("num", 0);
                Log.i("num", "run: "+num);
                SQLiteDatabase db = openOrCreateDatabase("request.db", MODE_PRIVATE, null);
//                db.execSQL("create table if not exists myrequesttb(num integer,time text,flag integer,point text,publisher text" +
//                        ",p_number text,p_phone text,helper text,h_number text,h_phone text,user_loc text,content text," +
//                        "infor text,r_nameORmessage text,r_locORpackage_loc text,r_phoneORphone text,nullORpackage_Id text)");


                Cursor c = db.rawQuery("select * from myrequesttb where num=" + num, null);

                if (c != null) {
                    c.moveToNext();
                    content = c.getString(c.getColumnIndex("content"));
                    userloc = c.getString(c.getColumnIndex("user_loc"));
                    rname=(c.getString(c.getColumnIndex("r_nameORmessage")));
                    rphone=(c.getString(c.getColumnIndex("r_phoneORphone")));
                    raddress=(c.getString(c.getColumnIndex("r_locORpackage_loc")));
                    note=(c.getString(c.getColumnIndex("infor")));
                    number=c.getString(c.getColumnIndex("p_number"));
                    pphone=c.getString(c.getColumnIndex("p_phone"));
                    jifen = c.getInt(c.getColumnIndex("point"));
                    publisherid=c.getString(c.getColumnIndex("p_number"));
                    picurl=c.getString(c.getColumnIndex("url"));
                    tflag = c.getInt(c.getColumnIndex("flag"));

                    if ((tflag-((tflag/1000)*1000))/100==1) {
                        pay = "货到付款";
                    } else {
                        pay = "寄方付款";
                    }
                    name=c.getString(c.getColumnIndex("publisher"));
                    accout=c.getString(c.getColumnIndex("p_number"));
                    switch (tflag/1000)
                    {
                        case 1:
                            kuaidi="顺丰快递";
                            break;
                        case  2:
                            kuaidi="圆通快递";
                            break;
                        case  3:
                            kuaidi="申通快递";
                            break;
                        case 4:
                            kuaidi="中通快递";
                            break;
                        case 5:
                            kuaidi="天天快递";
                            break;
                        case 6:
                            kuaidi="韵达快递";
                            break;
                        case 7:
                            kuaidi="百世汇通";
                            break;
                        default:
                            kuaidi="未知";
                            break;
                    }
                    freshhandler.sendMessage(new Message());



                }


                //

            }
        });
        t.start();
    }

    Handler freshhandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handlergetpic.sendMessage(new Message());
            accoutTV.setText(accout);
            nameTV.setText(name);
            contentTV.setText(content);
            userlocTV.setText(userloc);
            payTV.setText(pay);
            rnameTV.setText(rname);
            rphoneTV.setText(rphone);
            raddressTV.setText(raddress);
            kuaidiTV.setText(kuaidi);
            noteTV.setText(note);
            jifenTV.setText(jifen+"");
            if((tflag/10)%10==2)
            {
                Toast.makeText(helpsdActivity.this,"in2",Toast.LENGTH_LONG).show();

                callRL.setVisibility(View.GONE);
                finishiRL.setVisibility(View.VISIBLE);
            }
            else
            {
                callRL.setVisibility(View.VISIBLE);
                finishiRL.setVisibility(View.GONE);
            }

        }
    };


    Handler handlergetpic = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String Url;
                        Url = "http://" + getResources().getText(R.string.IP) + ":8080/Ren_Test/HeadServlet" + "?userId=" + publisherid;
                        Log.i("tag", Url);
                        URL url = new URL(Url);
                        URLConnection conn = url.openConnection();
                        conn.setRequestProperty("Accept-Charset", "gbk");
                        conn.setRequestProperty("contentType", "gbk");
                        conn.setReadTimeout(4000);
                        InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "gbk");
                        BufferedReader br = new BufferedReader(reader);
                        String str = br.readLine();
                        System.out.println(str);
                        Gson gson = new Gson();
                        List<User> userList = gson.fromJson(str, new TypeToken<List<User>>() {
                        }.getType());
                        User user = (User) userList.get(0);
                        Log.i("user1", user.getUserId());
                        touxiangURL=user.getUrl();
                        pichandler.sendMessage(new Message());



                    }
                    catch (Exception e )
                    {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();




        }

    };
    Handler pichandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        touxiangbit=getHttpBitmap("http://" + getResources().getText(R.string.IP) + "/nuaa/" + touxiangURL);
                        tupianbit=getHttpBitmap("http://" + getResources().getText(R.string.IP) + "/request/" + picurl);
                        setpichandler.sendMessage(new Message());
                    }
                    catch (Exception e )
                    {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    };

    Handler setpichandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            touxiangIV.setImageBitmap(touxiangbit);
            tupianIV.setImageBitmap(tupianbit);
        }
    };


    public void psdmakecall(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+pphone));
        try {
            startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void psdsendmsg(View view)
    {
        Uri smsToUri = Uri.parse("smsto:"+pphone);
        Intent intent = new Intent(Intent.ACTION_SENDTO,smsToUri);
        intent.putExtra("sms_body","你好，我已接单");
        startActivity(intent);
    }


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


}
