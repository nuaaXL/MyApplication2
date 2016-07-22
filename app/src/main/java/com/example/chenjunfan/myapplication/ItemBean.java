package com.example.chenjunfan.myapplication;

import android.graphics.Bitmap;

/**
 * Created by chenjunfan on 16/7/21.
 */
public class ItemBean {

    int Imagedone;
    int Imageflag,num,flag,jifen;
    Bitmap Imagepic;
    public String content,username,place,time;

    public ItemBean(String time, int num, int flag, Bitmap imagepic, int imagedone,
                    int imageflag, String content, int jifen, String username, String place) {
        this.time = time;
        this.num = num;
        this.flag = flag;
        this.Imagepic = imagepic;
        this.Imagedone = imagedone;
        this.Imageflag = imageflag;
        this.content = content;
        this.jifen = jifen;
        this.username = username;
        this.place = place;
    }

    public Bitmap getImagepic() {
        return Imagepic;
    }

    public void setImagepic(Bitmap imagepic) {
        Imagepic = imagepic;
    }

    public int getImagedone() {
        return Imagedone;
    }

    public void setImagedone(int imagedone) {
        Imagedone = imagedone;
    }

    public int getImageflag() {
        return Imageflag;
    }

    public void setImageflag(int imageflag) {
        Imageflag = imageflag;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getJifen() {
        return jifen;
    }

    public void setJifen(int jifen) {
        this.jifen = jifen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
