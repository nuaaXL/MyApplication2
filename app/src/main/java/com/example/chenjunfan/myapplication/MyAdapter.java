package com.example.chenjunfan.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chenjunfan on 16/7/21.
 */
public class MyAdapter extends BaseAdapter {
    private  List<ItemBean> mList;
    private LayoutInflater mInflater;

    public MyAdapter (Context context,List<ItemBean> list){
        mList = list;
        mInflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {

        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView =mInflater.inflate(R.layout.item_main,null);
            viewHolder.picIV = (ImageView) convertView.findViewById(R.id.pic);
            viewHolder.doneIV = (ImageView) convertView.findViewById(R.id.iv_done);
            viewHolder.flagIV = (ImageView) convertView.findViewById(R.id.IV_flag);
            viewHolder.timeTV = (TextView) convertView.findViewById(R.id.item_time);
            viewHolder.numTV = (TextView) convertView.findViewById(R.id.tv_num);
            viewHolder.flagTV = (TextView) convertView.findViewById(R.id.flag);
            viewHolder.contentTV = (TextView) convertView.findViewById(R.id.item_content);
            viewHolder.jifenTV = (TextView) convertView.findViewById(R.id.tv_jifen);
            viewHolder.usernameTV = (TextView) convertView.findViewById(R.id.item_username);
            viewHolder.placeTV = (TextView) convertView.findViewById(R.id.item_place);

            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ItemBean bean = mList.get(position);
        viewHolder.picIV.setImageBitmap(bean.Imagepic);
        viewHolder.doneIV.setImageResource(bean.Imagedone);
        viewHolder.flagIV.setImageResource(bean.Imageflag);
        viewHolder.timeTV.setText(bean.time);
        viewHolder.numTV.setText(bean.num+"");
        viewHolder.flagTV.setText(bean.flag+"");
        viewHolder.contentTV.setText(bean.content);
        viewHolder.jifenTV.setText(bean.jifen+"");
        viewHolder.usernameTV.setText(bean.username);
        viewHolder.placeTV.setText(bean.place);
        return convertView;
    }
    class ViewHolder{
        public ImageView picIV,doneIV,flagIV;
        public TextView timeTV,numTV,flagTV,contentTV,jifenTV,usernameTV,placeTV;
    }


}
