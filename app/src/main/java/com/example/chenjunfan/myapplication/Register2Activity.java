package com.example.chenjunfan.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
    private ProgressDialog prodialog2;

    private Button selectImage;
    private ImageView imageBack;
    private ImageView imageView;
    private String picPath = null;
    private EditText nameEt;
    private RadioGroup group;

    private String userId, name, passwd, phone, school,picturePath,userurl="";
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
        prodialog2 = new ProgressDialog(Register2Activity.this);
        prodialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prodialog2.setIndeterminate(true);
        prodialog2.setCancelable(false);
        prodialog2.setMessage("正在压缩和上传图片，请稍等");


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
                        editor2.putInt("gender", 2);
                        editor2.commit();
                        break;

                }

            }
        });



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
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                this.startActivityForResult(i, 1);// startActivityForResult(i, "1");
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

                        Url = "http://"+getResources().getText(R.string.IP)+":8080/Ren_Test/modifyServlet" + "?userId=" + userId + "&name=" + URLEncoder.encode(name, "gbk") + "&gender=" + gender + "&passwd=" +
                                passwd + "&phone=" + phone + "&school=" + URLEncoder.encode(school,"gbk") + "&actionCode=register"+"&url="+userurl;
                        Log.i("tag", Url);
                        SharedPreferences pre4 = getSharedPreferences("registerflag", MODE_PRIVATE);
                        SharedPreferences.Editor editor4 = pre4.edit();
                        editor4.remove("flag");
                        editor4.commit();
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
                        Message msg = new Message();
                        msg.obj = "服务器连接超时，请检查网络设置";
                        handler.sendMessage(msg);
                    }


                }

            }
        });


        t.start();


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
    Handler handlerImage = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg.arg1 == 1) {

                transform(picturePath);

            }
        }
    };

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
    Handler getHandlerunshow2 = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            prodialog2.cancel();
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
                          //  resultText.setText("conn...");
                            System.out.println("hello....onStart");
                        }


                        @Override
                        public void onLoading(long total, long current,
                                              boolean isUploading) {

                            super.onLoading(total, current, isUploading);

                            //resultText.setText(current + "/" + total);
                        }


                        @Override
                        public void onFailure(HttpException error, String msg) {
                            System.out.println("hello....fail");
                            error.printStackTrace();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            System.out.println("hello....onSuccess");
                            //resultText.setText("onSuccess");
                            Message msg = new Message();
                            msg.obj = arg0.result.toString();
                            System.out.println(msg.obj);
                            userurl=arg0.result.toString();
                            getHandlerunshow2.sendMessage(new Message());

                        }
                    });



        }
    };




}








