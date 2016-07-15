package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    private Button selectImage;
    private ImageView imageView;
    private String picPath = null;
    private EditText nameET;
    private RadioGroup genderGP;
    private RadioGroup schoolGP;
    private int gender;
    private String school;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            String str= (String) msg.obj;

            Toast.makeText(Person_visionActivity.this,str,Toast.LENGTH_SHORT).show();
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


            genderGP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.rb_m_nan:
                            gender=1;
                            break;
                        case R.id.rb_m_nv:
                            gender=0;
                            break;

                    }

                }
            });

            schoolGP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.rb_m_new:
                            school="将军路";
                            break;
                        case R.id.rb_m_old:
                            school="明故宫";
                            break;

                    }

                }
            });
        }






    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.selectImage_modify:
                /***
                 * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
                 */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
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
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
//            Log.e(TAG, "uri = " + uri);
            try {
                String[] pojo = { MediaStore.Images.Media.DATA };

                Cursor cursor = managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = this.getContentResolver();
                    int colunm_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(colunm_index);
                    /***
                     * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
                     * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
                     */
                    if (path.endsWith("jpg") || path.endsWith("png")) {
                        picPath = path;
                        Bitmap bitmap = BitmapFactory.decodeStream(cr
                                .openInputStream(uri));
                        imageView.setImageBitmap(bitmap);
                    } else {
                        alert();
                    }
                } else {
                    alert();
                }

            } catch (Exception e) {
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您选择的不是有效的图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        picPath = null;
                    }
                }).create();
        dialog.show();
    }

    public void modify(View view)
    {
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
                    Url = "http://192.168.4.100:8080/Ren_Test/modifyServlet" + "?name=" + URLEncoder.encode(user.getName(), "gbk") + "&gender=" + user.getGender() + "&passwd=" + user.getPasswd() + "&phone="
                            + user.getPhone() + "&school=" + URLEncoder.encode(user.getSchool(), "gbk") + "&actionCode=modifyConfirm" + "&userId=" + user.getUserId();
                    Log.i("url", Url);
                    db.execSQL("create table if not exists usertb(userId text,name text,passwd text,gender integer" +
                            ",phone text,school text,point integer)");

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
                    user2 = (User) userList.get(0);
                    Log.i("user2", user2.getUserId());
                    if ((user2.getUserId().toString().equals("1"))) {

                        db.execSQL("delete from usertb");
                        db.execSQL("insert into usertb(userId,name,passwd,gender,phone,school,point) values('" + user.getUserId() + "','" + user.getName() + "','"
                                + user.getPasswd() + "'," + user.getGender() + ",'" + user.getPhone() + "','" + user.getSchool() + "'," + user.getPoint() + ")");
                        db.close();
                        Message msg=new Message();
                        msg.obj="修改成功，返回主界面";
                        handler.sendMessage(msg);
                        finish();
                    }
                    else
                    {
                        Message msg=new Message();
                        msg.obj="修改失败，请稍候再试";
                        handler.sendMessage(msg);
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
}
