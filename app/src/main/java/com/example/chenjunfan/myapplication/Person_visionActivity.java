package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.List;

/**
 * Created by chenjunfan on 16/7/10.
 */
public class Person_visionActivity  extends Activity implements View.OnClickListener {


    private Button modifydateButton;
    private TextView datetoModify;
    private ImageView imageBack;
    private Calendar cal;
    private int year;
    private int month;
    private int day;
    private static String requestURL = "http://192.168.191.1:8080/NanhangServer/uploadServlet";
    private TextView resultText;
    private String picturePath;
    private Button selectImage;
    private Button btnUpload;
    private ImageView imageView;
    private EditText nameET;
    private RadioGroup genderGP;
    private RadioGroup schoolGP;
    private RadioButton nanRB,nvRB,newRB,oldRB;
    private ProgressDialog prodialog2;
    private int gender;
    private String school;
    private User user = new User();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            String str = (String) msg.obj;

            Toast.makeText(Person_visionActivity.this, str, Toast.LENGTH_SHORT).show();
        }
    };

    Handler handlerImage = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg.arg1 == 1) {

                transform(picturePath);

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_person_vision);
        modifydateButton = (Button) findViewById(R.id.btn_modifyDate);
        datetoModify = (TextView) findViewById(R.id.tv_modifydate);
        imageBack = (ImageView) findViewById(R.id.img_back);
        selectImage = (Button) findViewById(R.id.selectImage_modify);
        imageView = (ImageView) findViewById(R.id.headphoto_modify);
        nameET = (EditText) findViewById(R.id.et_iname);
        genderGP = (RadioGroup) findViewById(R.id.RG_igender);
        schoolGP = (RadioGroup) findViewById(R.id.RG_ischool);
        resultText = (TextView) findViewById(R.id.imagePath);
        prodialog2 = new ProgressDialog(Person_visionActivity.this);
        prodialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prodialog2.setIndeterminate(true);
        prodialog2.setCancelable(false);
        nanRB = (RadioButton) findViewById(R.id.rb_m_nan);
        nvRB = (RadioButton) findViewById(R.id.rb_m_nv);
        oldRB= (RadioButton) findViewById(R.id.rb_m_old);
        newRB= (RadioButton) findViewById(R.id.rb_m_new);
        prodialog2.setMessage("正在压缩和上传图片，请稍等");


        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);


        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });

        modifydateButton.setOnClickListener(this);
        selectImage.setOnClickListener(this);


        SQLiteDatabase db = openOrCreateDatabase("user.db", MODE_ENABLE_WRITE_AHEAD_LOGGING, null);
        Cursor c = db.rawQuery("select * from usertb", null);
        if (c != null) {
            while (c.moveToNext()) {


                nameET.setText(c.getString(c.getColumnIndex("name")));
                if(c.getInt(c.getColumnIndex("gender"))==1)
                {
                    nanRB.setChecked(true);
                    gender=1;
                }
                else
                {
                    nvRB.setChecked(true);
                    gender=2;
                }
                if(c.getString(c.getColumnIndex("school")).equals("nuaa_old"))
                {
                    oldRB.setChecked(true);
                    school="nuaa_old";
                }
                else
                {
                    newRB.setChecked(true);
                    school="nuaa_new";
                }


            }
        }



        genderGP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_m_nan:
                        gender = 1;
                        break;
                    case R.id.rb_m_nv:
                        gender = 2;
                        break;

                }

            }
        });

        schoolGP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_m_new:
                        school = "nuaa_new";
                        break;
                    case R.id.rb_m_old:
                        school = "nuaa_old";
                        break;

                }

            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.selectImage_modify:
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                this.startActivityForResult(i, 1);// startActivityForResult(i, "1");
                break;


            case R.id.btn_modifyDate:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        datetoModify.setText(i + "年" + (i1 + 1) + "月" + i2 + "日");
                    }
                }, year, cal.get(Calendar.MONTH), day).show();


            default:
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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


    public void modify(View view) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Person_visionActivity.this, HomeActivity.class);
                SQLiteDatabase db = openOrCreateDatabase("user.db", MODE_ENABLE_WRITE_AHEAD_LOGGING, null);
                User user = new User();
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
                user.setName(nameET.getText().toString());
                user.setGender(gender);
                user.setSchool(school);


                try {
                    String Url;
                    Url = "http://" + getResources().getText(R.string.IP) + ":8080/Ren_Test/modifyServlet" + "?name=" + URLEncoder.encode(user.getName(), "gbk") + "&gender=" + user.getGender() + "&passwd=" + user.getPasswd() + "&phone="
                            + user.getPhone() + "&school=" + URLEncoder.encode(user.getSchool(), "gbk") + "&actionCode=modifyConfirm" + "&userId=" + user.getUserId();
                    Log.i("url", Url);
                    db.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
                            ",phone text,school text,point integer,url text)");

                    URL url = new URL(Url);
                    URLConnection conn = url.openConnection();
                    conn.setRequestProperty("Accept-Charset", "gbk");
                    conn.setRequestProperty("contentType", "gbk");
                    conn.setReadTimeout(3000);
                    InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "gbk");
                    BufferedReader br = new BufferedReader(reader);
                    String str = br.readLine();
                    System.out.println(str);
                    Gson gson = new Gson();
                    List<User> userList = gson.fromJson(str, new TypeToken<List<User>>() {
                    }.getType());
                    User user2;
                    user = user2 = (User) userList.get(0);
                    Log.i("user2", user2.getUserId());
                    if ((user2.getUserId().toString().equals("1"))) {

                        db.execSQL("delete from usertb");
                        db.execSQL("insert into usertb(userId,name,passwd,gender,phone,school,point,url) values('" + user.getUserId() + "','" + user.getName() + "','"
                                + user.getPasswd() + "'," + user.getGender() + ",'" + user.getPhone() + "','" + user.getSchool() + "'," + user.getPoint() + ",'" + user.getUrl() + "')");
                        db.close();
                        Message msg = new Message();
                        msg.obj = "修改成功，返回登录界面";
                        handler.sendMessage(msg);
                        HomeActivity.ActivityA.finish();
                        Intent intent2 = new Intent(Person_visionActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                    } else {
                        Message msg = new Message();
                        msg.obj = "修改失败，请稍候再试";
                        handler.sendMessage(msg);
                        db.close();
                        c.close();

                        finish();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = "服务器连接超时，请检查网络设置";
                    handler.sendMessage(msg);
                }
            }
        });

        t.start();
    }

    Handler handler_suc = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            final String str = (String) msg.obj;

            Log.i("str", str);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {


                    SQLiteDatabase db = openOrCreateDatabase("user.db", MODE_ENABLE_WRITE_AHEAD_LOGGING, null);
                    User user = new User();
                    Cursor c = db.rawQuery("select * from usertb", null);
                    if (c != null) {
                        while (c.moveToNext()) {


                            user.setUserId(c.getString(c.getColumnIndex("userId")));

                        }
                    }


                    db.close();
                    c.close();

                    try {
                        String Url;
                        Url = "http://" + getResources().getText(R.string.IP) + ":8080/Ren_Test/modifyServlet" + "?actionCode=pic_apply" + "&userId=" + user.getUserId() + "&url=" + str;
                        System.out.println(Url);
                        URL url = new URL(Url);
                        URLConnection conn = url.openConnection();

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
                        List<User> userList = gson.fromJson(str, new TypeToken<List<User>>() {
                        }.getType());

                        user = (User) userList.get(0);
                        Log.i("user1", user.getUserId());

                        if (user.getUserId().toString().equals("1")) {
                            Message msg2 = new Message();
                            msg2.obj = "图片保存成功！";
                            getHandlerunshow2.sendMessage(new Message());
                            handler.sendMessage(msg2);
                        } else {
                            Message msg2 = new Message();
                            msg2.obj = "图片保存失败！";
                            getHandlerunshow2.sendMessage(new Message());

                            handler.sendMessage(msg2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getHandlerunshow2.sendMessage(new Message());

                    }


                }
            });
            t.start();
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

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 200) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩

            System.out.println("while:" + (baos.toByteArray().length / 1024));
            baos.reset();//重置baos即清空baos
            if (options > 20)
                options -= 10;//每次都减少10
            else {
                options -= 1;
            }

            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public void saveMyBitmap(Bitmap mBitmap) {
        File f = new File("/sdcard/Note/temp.jpg");
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

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    private void transform(final String filePath) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("正在处理和上传图片");
                handlershow2.sendMessage(new Message());

                Bitmap bitmap = BitmapFactory.decodeFile(filePath, getBitmapOption(2));
                bitmap = compressImage(bitmap);
                saveMyBitmap(bitmap);
                handlerIV.sendMessage(new Message());
                handlerunshow2.sendMessage(new Message());

            }
        });
        t.start();


    }

    Handler handlerIV = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            System.out.println("压缩完成！");
            imageView.setImageBitmap(BitmapFactory.decodeFile("/sdcard/Note/temp.jpg"));
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

            RequestParams params = new RequestParams();
            params.addQueryStringParameter("method", "upload");
            params.addQueryStringParameter("path", "/sdcard/Note/temp.jpg");
            params.addBodyParameter("file", new File( "/sdcard/Note/temp.jpg"));
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    "http://" + getResources().getText(R.string.IP) + ":8080/Ren_Test/uploadServlet", params,
                    new RequestCallBack<String>() {


                        @Override
                        public void onStart() {
                            resultText.setText("conn...");
                            System.out.println("hello....onStart");
                        }


                        @Override
                        public void onLoading(long total, long current,
                                              boolean isUploading) {

                            super.onLoading(total, current, isUploading);

                            resultText.setText(current + "/" + total);
                        }


                        @Override
                        public void onFailure(HttpException error, String msg) {
                            System.out.println("hello....fail");
                            error.printStackTrace();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            System.out.println("hello....onSuccess");
                            resultText.setText("onSuccess");
                            Message msg = new Message();
                            msg.obj = arg0.result.toString();
                            System.out.println(msg.obj);
                            handler_suc.sendMessage(msg);
                        }
                    });



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
}
