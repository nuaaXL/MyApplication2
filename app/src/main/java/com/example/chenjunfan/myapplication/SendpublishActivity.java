package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by chenjunfan on 16/7/10.
 */
public class SendpublishActivity extends Activity implements View.OnClickListener{
    private Button fabuButton,selectpicBT;
    private EditText contentET;
    private EditText locET;
    private EditText nameET;
    private EditText phoneET;
    private EditText addressET;
    private RadioGroup payRG;
    private EditText noteET;
    private ImageView imageBack,photoIV;
    private Spinner spinner;
    private String picturePath;
    private String URL=null;
    private int scflag=0;

    private EditText pointET;
    private int flag=1;
    private int point=0;
    private int restpoint=0;
    private ProgressDialog prodialog;
    private ProgressDialog prodialog2;

    Handler handlerImage = new Handler(){
        public void handleMessage(android.os.Message msg) {

            if(msg.arg1==1){
                    transform(picturePath);

            }
        }
    };


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            Toast.makeText(SendpublishActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
        }
    };
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

    Handler handlershow2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            prodialog2.show();
        }
    };
    Handler handlerunshow2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestParams params = new RequestParams();
                    params.addQueryStringParameter("method", "upload");

                    params.addQueryStringParameter("path", "/sdcard/Note/temp.jpg");
                    params.addBodyParameter("file", new File("/sdcard/Note/temp.jpg"));

                    HttpUtils http = new HttpUtils();
                    http.send(HttpRequest.HttpMethod.POST,
                            "http://"+getResources().getText(R.string.IP)+":8080/Ren_Test/reuploadServlet", params,
                            new RequestCallBack<String>() {


                                    @Override
                                    public void onStart() {
                                        System.out.println("hello....onStart");
                                    }



                                    @Override
                                public void onLoading(long total, long current,
                                                      boolean isUploading) {

                                    super.onLoading(total, current, isUploading);

//                                resultText.setText(current + "/" + total);
                                }



                                @Override
                                public void onFailure(HttpException error, String msg) {
                                    System.out.println("hello....fail");
                                    Message msgt =new Message();
                                    msgt.obj="上传失败，请重新上传";
                                    error.printStackTrace();
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> arg0) {
                                    System.out.println("hello....onSuccess");
//                                resultText.setText("onSuccess");
                                    URL=arg0.result.toString();
                                    System.out.println(arg0.result.toString());
                                    scflag=1;
                                }
                            });
                    Message msgf = new Message();
                    msgf.obj="上传完成";
                    System.out.println("上传完成");
                    handler.sendMessage(msgf);
                    getHandlerunshow2.sendMessage(new Message());
                }
            });
            t.start();




        }
    };

    Handler getHandlerunshow2 = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            prodialog2.cancel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prodialog2=new ProgressDialog(SendpublishActivity.this);
        prodialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prodialog2.setIndeterminate(true);
        prodialog2.setCancelable(false);
        prodialog2.setMessage("正在压缩和上传图片，请稍等");

        setContentView(R.layout.activity_sendpublish);
        pointET = (EditText) findViewById(R.id.et_sp_jifen) ;
        fabuButton = (Button) findViewById(R.id.btn_sp_fabu);
        imageBack = (ImageView) findViewById(R.id.img_back);
        contentET = (EditText) findViewById(R.id.et_sp_content);
        locET = (EditText) findViewById(R.id.et_sp_loc);
        nameET = (EditText) findViewById(R.id.et_sp_name);
        phoneET = (EditText) findViewById(R.id.et_sp_phone);
        addressET= (EditText) findViewById(R.id.et_sp_address);
        payRG= (RadioGroup) findViewById(R.id.RG_pay);
        noteET= (EditText) findViewById(R.id.et_sp_note);
        imageBack.setOnClickListener(this) ;
        spinner = (Spinner) findViewById(R.id.sp_kuaidi);
        selectpicBT = (Button) findViewById(R.id.sp_selectImage);
        photoIV = (ImageView) findViewById(R.id.sp_photo);


        selectpicBT.setOnClickListener(this);



        payRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.rb_sp_hdfk:
                        flag=flag%200;
                        flag+=100;
                        break;
                    case R.id.rb_sp_jffk:
                        flag=flag%100;
                        flag+=200;

                        break;
                }
            }
        });



// 建立spinner数据源
        String[] mItems = getResources().getStringArray(R.array.languages);
// 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//绑定 Adapter到控件
        spinner .setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = getResources().getStringArray(R.array.languages);
                if (languages[pos].equals("顺丰快递")) {
                    flag=flag-flag/1000*1000;
                    flag+=1000;
                }
                else  if(languages[pos].equals("圆通快递")) {
                    if(flag>=1000)
                    flag=flag-flag/1000*1000;

                    flag+=2000;
                }
                else if(languages[pos].equals("申通快递"))
                {
                    if(flag>=1000)
                        flag=flag-flag/1000*1000;

                    flag+=3000;
                }
                else if(languages[pos].equals("中通快递"))
                {
                    if(flag>=1000)
                        flag=flag-flag/1000*1000;
                    flag+=4000;
                }
                else if(languages[pos].equals("天天快递"))
                {
                    if(flag>=1000)
                        flag=flag-flag/1000*1000;
                    flag+=5000;
                }
                else if(languages[pos].equals("韵达快递"))
                {
                    if(flag>=1000)
                        flag=flag-flag/1000*1000;
                    flag+=6000;
                }
                else if(languages[pos].equals("百世汇通"))
                {
                    if(flag>=1000)
                        flag=flag-flag/1000*1000;
                    flag+=7000;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sp_selectImage:
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                this.startActivityForResult(i, 1);// startActivityForResult(i, "1");
                break;





            default:
                break;

        }
    }


    public void submit(View view)
    {

        prodialog=new ProgressDialog(SendpublishActivity.this);
        prodialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prodialog.setIndeterminate(true);
        prodialog.setMessage("正在发布");
        prodialog.setCancelable(false);


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                User user = new User();
                Message msg = new Message();
                SQLiteDatabase db = openOrCreateDatabase("user.db", MODE_ENABLE_WRITE_AHEAD_LOGGING, null);
                db.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
                        ",phone text,school text,point integer,url text)");
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
                    if(pointET!=null&&!pointET.getText().toString().equals(""))
                        point = Integer.parseInt(pointET.getText().toString());
                    else
                    {
                        Message msg2 = new Message();
                        msg2.obj="请输入您要悬赏的积分";
                        handler.sendMessage(msg2);
                    }
                }


                if (contentET.getText().toString().equals("")) {

                    msg.obj = "请输入包裹内容";
                    handler.sendMessage(msg);
                } else if (locET.getText().toString().equals("")) {
                    msg.obj = "请输入您的位置";
                    handler.sendMessage(msg);
                } else if (nameET.getText().toString().equals("")) {
                    msg.obj = "请输入您的姓名";
                    handler.sendMessage(msg);
                } else if (phoneET.getText().toString().equals("")) {
                    msg.obj = "请输入您的手机号码";
                    handler.sendMessage(msg);
                } else if ((user.getPoint() - point) < 0) {


                    msg.obj = "您的积分剩余为:" + user.getPoint() + ",不足以悬赏，请重新输入悬赏积分！";
                    handler.sendMessage(msg);
                } else if(scflag==0)
                {
                    msg.obj = "请上传详情图片";
                    handler.sendMessage(msg);
                }

                else{
                    handlershow.sendMessage(new Message());

                    Request request = new Request();


                    try {
                        Message msg2 = new Message();
                        String Url;
                        Url = "http://" + getResources().getText(R.string.IP) + ":8080/Ren_Test/requestServlet" + "?type=add" + "&flag=" + flag +
                                "&publisher=" + URLEncoder.encode(user.getName(), "gbk") + "&p_number=" + user.getUserId() + "&p_phone=" + user.getPhone() + "&user_loc=" + URLEncoder.encode(locET.getText().toString(), "gbk") + "&content=" + URLEncoder.encode(contentET.getText().toString(), "gbk") +
                                "&infor=" + URLEncoder.encode(noteET.getText().toString(), "gbk") + "&r_nameORmessage=" + URLEncoder.encode(nameET.getText().toString(), "gbk") + "&r_locORpackage_loc=" + URLEncoder.encode(addressET.getText().toString(), "gbk") + "&r_phoneORphone=" + phoneET.getText().toString() +
                                "&nullORpackage_Id=" + URLEncoder.encode("xx", "gbk") + "&point=" + point+"&url="+URL;

//
                        locET.getText().toString();
                        Log.i("tag", Url);
                        URL url = new URL(Url);
                        URLConnection conn = url.openConnection();
                        conn.setRequestProperty("Accept-Charset", "gbk");
                        conn.setRequestProperty("contentType", "gbk");
                        conn.setReadTimeout(2000);
                        InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "gbk");
                        BufferedReader br = new BufferedReader(reader);
                        String str = br.readLine();
                        System.out.println(str);
                        Gson gson = new Gson();
                        List<User> userList = gson.fromJson(str, new TypeToken<List<User>>() {
                        }.getType());
                        user = (User) userList.get(0);
                        Log.i("user1", user.getUserId());
                        if (user.getUserId().toString().equals("1")) {
                            HomeActivity.ActivityA.finish();
                            Intent intent = new Intent(SendpublishActivity.this, HomeActivity.class);
                            msg.obj = "发布成功";
                            db.execSQL("update usertb set point =" + user.getPoint());

                            handler.sendMessage(msg);
                            handlerunshow.sendMessage(new Message());


//                            SharedPreferences pre = getSharedPreferences("publishflag",MODE_PRIVATE);
//                            SharedPreferences.Editor editor = pre.edit();
//                            editor.putString("flag","1");
//                            editor.commit();
                            startActivity(intent);

                            finish();

                        } else if (user!=null&&user.getUserId().toString().equals("-1")) {
                            msg.obj = "失败";
                            handler.sendMessage(msg);
                            handlerunshow.sendMessage(new Message());


                        } else if(user!=null&&user.getUserId().toString().equals("-2")){
                            msg.obj = "内容中含有敏感词汇，请修改";
                            handler.sendMessage(msg);
                            handlerunshow.sendMessage(new Message());


                        }
                    } catch (Exception e) {
                        msg.obj = "服务器出现问题，请稍候再试";
                        handler.sendMessage(msg);
                        handlerunshow.sendMessage(new Message());

                        e.printStackTrace();
                    }
                    db.close();
                    c.close();


                }
            }



        });

        t.start();
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

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Thread t =new Thread(new Runnable() {
            @Override
            public void run() {
                if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);

                    //tv.setText(picturePath);

                    System.out.println("=============picturePath======" + picturePath);

                    Message msg = handlerImage.obtainMessage();
                    msg.arg1 = 1;
                    handlerImage.sendMessage(msg);

                    cursor.close();
                }
            }

        });
        t.start();







    }

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>200) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩

            System.out.println("while:"+(baos.toByteArray().length / 1024));
            baos.reset();//重置baos即清空baos
            if(options>20)
            options -= 10;//每次都减少10
            else
            {
                options-=1;
            }

            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public void saveMyBitmap(Bitmap mBitmap)  {
        File f = new File( "/sdcard/Note/temp.jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //将图片的长和宽缩小味原来的1/2

    private BitmapFactory.Options getBitmapOption(int inSampleSize){
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    private void  transform (final String filePath)
    {
        Thread t =new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("正在处理和上传图片");
                handlershow2.sendMessage(new Message());

                Bitmap bitmap=BitmapFactory.decodeFile(filePath,getBitmapOption(2));
                bitmap = compressImage(bitmap);
                saveMyBitmap(bitmap);
                handlerIV.sendMessage(new Message());
                handlerunshow2.sendMessage(new Message());

            }
        });
        t.start();


    }

        Handler handlerIV = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("压缩完成！");
                photoIV.setImageBitmap(BitmapFactory.decodeFile("/sdcard/Note/temp.jpg"));
            }
        };




}




