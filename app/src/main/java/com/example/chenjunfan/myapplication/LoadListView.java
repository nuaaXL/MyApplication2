package com.example.chenjunfan.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by chenjunfan on 16/7/18.
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener{

    View footer;
    int totalitemcount;
    int lastvisibleitem;
    boolean isLoading;
    ILoadListener iLoadListener;

    public LoadListView(Context context) {
        super(context);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private  void  initView(Context context)
    {
        LayoutInflater infalter = LayoutInflater.from(context);
        footer=infalter.inflate(R.layout.footer_layout,null);
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
        this.addFooterView(footer);
        this.setOnScrollListener(this);

    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if(totalitemcount==lastvisibleitem&&scrollState==SCROLL_STATE_IDLE)
        {
            if(!isLoading)
            {
                isLoading=true;
                footer.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
                iLoadListener.onLoad();

            }

        }
    }
    public void loadComplete()
    {
        isLoading=false;
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastvisibleitem=firstVisibleItem+visibleItemCount;
        totalitemcount=totalItemCount;
    }

    public  void setInterface(ILoadListener iLoadListener){
        this.iLoadListener=iLoadListener;
    }


    public  interface ILoadListener{
        public void onLoad();
    }
}
