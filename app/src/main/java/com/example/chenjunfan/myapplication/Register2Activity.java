package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
public class Register2Activity extends Activity implements View.OnClickListener {
    private Button submitButton;
    private Button pickdateButton;
    private TextView dateText;

    private Calendar cal;
    private int year;
    private int month;
    private int day;
    private Button selectImage;
    private ImageView imageBack;
    private ImageView imageView;
    private String picPath = null;
    private EditText nameEt;
    private RadioGroup group;

    private String userId, name, passwd, phone, school;
    private int gender, point;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            String str= (String) msg.obj;

            Toast.makeText(Register2Activity.this,str,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        submitButton = (Button) findViewById(R.id.btn_submit);
        pickdateButton = (Button) findViewById(R.id.btn_pickDate);
        dateText = (TextView) findViewById(R.id.tv_date);
        imageBack = (ImageView) findViewById(R.id.img_back);
        selectImage = (Button) findViewById(R.id.selectImage);
        imageView = (ImageView) findViewById(R.id.headphoto);
        nameEt = (EditText) findViewById(R.id.et_r_name);
        group = (RadioGroup) findViewById(R.id.gp_r_group2);

        SharedPreferences pre = getSharedPreferences("register", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pre.edit();
        editor.putInt("gender", 1);
        editor.commit();

        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });

        selectImage.setOnClickListener(this);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            SharedPreferences pre2 = getSharedPreferences("register", MODE_PRIVATE);
            SharedPreferences.Editor editor2 = pre2.edit();

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_r_nan:
                        editor2.putInt("gender", 1);
                        editor2.commit();
                        break;
                    case R.id.rb_r_nv:
                        editor2.putInt("gender", 0);
                        editor2.commit();
                        break;

                }

            }
        });

        /*submitButton.setOnClickListener(new View.OnClickListener() {
            SharedPreferences pre3 = getSharedPreferences("register", MODE_PRIVATE);
            SharedPreferences.Editor editor3 = pre3.edit();

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register2Activity.this, Register3Activity.class);
                if (nameEt.getText().toString().equals("")) {
                    Toast.makeText(Register2Activity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                } else {

                    userId = pre3.getString("userId", "");
                    name = nameEt.getText().toString();
                    passwd = pre3.getString("passwd", "");
                    phone = pre3.getString("phone", "");
                    school = pre3.getString("school", "");
                    gender = pre3.getInt("gender", 3);
                    point = 0;
                    regist();
                   try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pre3=getSharedPreferences("registerflag",MODE_PRIVATE);
                    if(pre3.getString("flag","").toString().equals("1")) {

                        startActivity(intent);
                    }
                    else if(pre3.getString("flag","").toString().equals("-1"))
                    {
                        Toast.makeText(Register2Activity.this, "账户已存在！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Register2Activity.this, "服务器存在问题，注册失败，请稍候再试", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });*/


        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);

        pickdateButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.selectImage:
                /***
                 * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
                 */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
                break;

            case R.id.btn_pickDate:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateText.setText(i + "年" + (i1 + 1) + "月" + i2 + "日");
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
                String[] pojo = {MediaStore.Images.Media.DATA};

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

   /* public void regist() {


        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    String Url;

                    Url = "http://192.168.191.1:8080/Ren_Test/modifyServlet" + "?userId=" + userId +"&name="+ URLEncoder.encode(name,"gbk")+"&gender="+gender+"&passwd=" + passwd + "&phone=" + phone + "&school=" + school + "&actionCode=register";
                    Log.i("tag", Url);
                    SharedPreferences pre3 = getSharedPreferences("registerflag", MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = pre3.edit();
                    editor3.remove("flag");
                    editor3.commit();
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
                    User user1 = (User) userList.get(0);
                    Log.i("user", user1.getUserId());
                    editor3.putString("flag",user1.getUserId());
                    editor3.commit();


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        });

       t.start();}*/

    public void makesubmit(View view) {

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                SharedPreferences pre3 = getSharedPreferences("register", MODE_PRIVATE);
                SharedPreferences.Editor editor3 = pre3.edit();
                Intent intent = new Intent(Register2Activity.this, Register3Activity.class);
                if (nameEt.getText().toString().equals("")) {
                    Toast.makeText(Register2Activity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                } else {

                    userId = pre3.getString("userId", "");
                    name = nameEt.getText().toString();
                    passwd = pre3.getString("passwd", "");
                    phone = pre3.getString("phone", "");
                    school = pre3.getString("school", "");
                    gender = pre3.getInt("gender", 3);
                    point = 0;

                    try {
                        String Url;

                        Url = "http://192.168.0.109:8080/Ren_Test/modifyServlet" + "?userId=" + userId + "&name=" + URLEncoder.encode(name, "gbk") + "&gender=" + gender + "&passwd=" + passwd + "&phone=" + phone + "&school=" + school + "&actionCode=register";
                        Log.i("tag", Url);
                        SharedPreferences pre4 = getSharedPreferences("registerflag", MODE_PRIVATE);
                        SharedPreferences.Editor editor4 = pre4.edit();
                        editor4.remove("flag");
                        editor4.commit();
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
                        User user1 = (User) userList.get(0);
                        Log.i("user", user1.getUserId());
                        editor4.putString("flag", user1.getUserId());
                        editor4.commit();
                        pre3 = getSharedPreferences("registerflag", MODE_PRIVATE);
                        if (pre3.getString("flag", "").toString().equals("1")) {

                            startActivity(intent);
                        } else if (pre3.getString("flag", "").toString().equals("-1")) {

                            Message msg = new Message();
                            msg.obj="账户已存在！";
                            handler.sendMessage(msg);
                            //Toast.makeText(Register2Activity.this, "账户已存在！", Toast.LENGTH_SHORT).show();
                        } else {

                            Message msg = new Message();
                            msg.obj="服务器存在问题，注册失败，请稍候再试！";
                            handler.sendMessage(msg);
                            //Toast.makeText(Register2Activity.this, "服务器存在问题，注册失败，请稍候再试", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                }

            }
        });


        t.start();


    }


}








