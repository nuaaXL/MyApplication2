package com.example.chenjunfan.myapplication;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Help_odersActivity extends FragmentActivity {



    private ImageView imageBack;
    private List<Fragment> fragmentList;
    private ImageView cursor;
    private ViewPager mPager;
    private TextView receive, send;
    private int currIndex;//当前页卡编号
    private int bmpw;//横线图片宽度
    private int offset;//图片移动的偏移量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oders);
        imageBack = (ImageView) findViewById(R.id.img_back);
        imageBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });

        InitTextView();
        initCursorPos();
        InitViewPager();


    }

    public void InitTextView(){
        receive = (TextView)findViewById(R.id.tv_guid1);
        send = (TextView)findViewById(R.id.tv_guid2);

        receive.setOnClickListener(new txListener(0));
        send.setOnClickListener(new txListener(1));
    }

    public class txListener implements View.OnClickListener{
        private int index=0;

        public txListener(int i) {
            index =i;
        }
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mPager.setCurrentItem(index);
        }
    }

    //初始化指示器位置
    public void initCursorPos() {
        // 初始化动画
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpw = BitmapFactory.decodeResource(getResources(), R.mipmap.a)
                .getWidth();// 获取图片宽度

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 2 - bmpw) / 2;// 计算偏移量

        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    public void InitViewPager(){
        mPager = (ViewPager)findViewById(R.id.vp_help);
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new help());
        fragmentList.add(new helped());

        //给ViewPager设置适配器
        mPager.setAdapter(new FragAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);//设置当前显示标签页为第一页
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器
    }


    //页面改变监听器
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int one = offset *2 +bmpw;//两个相邻页面的偏移量

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            if (arg0 == 0) {
                receive.setTextColor(getResources().getColor(R.color.button_g));
                send.setTextColor(getResources().getColor(R.color.half_black));
            } else {
                receive.setTextColor(getResources().getColor(R.color.half_black));
                send.setTextColor(getResources().getColor(R.color.button_g));
            }
            Animation animation = new TranslateAnimation(currIndex*one,arg0*one,0,0);//平移动画
            currIndex = arg0;
            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
            animation.setDuration(200);//动画持续时间0.2秒
            cursor.startAnimation(animation);//是用ImageView来显示动画的
        }
    }

}
