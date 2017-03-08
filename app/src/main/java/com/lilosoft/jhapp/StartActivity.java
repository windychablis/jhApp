package com.lilosoft.jhapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class StartActivity extends Activity {
    private ViewPager viewPager;
    private ArrayList<View> views;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int [] layouts=new int[]{R.layout.page_item1,R.layout.page_item2,R.layout.page_item3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        views=new ArrayList<View>();
        View view1=getLayoutInflater().inflate(layouts[0],null);
        View view2=getLayoutInflater().inflate(layouts[1],null);
        View view3=getLayoutInflater().inflate(layouts[2],null);
        Button btn= (Button) view3.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,MainActivity.class));
            }
        });
        views.add(view1);
        views.add(view2);
        views.add(view3);
        myViewPagerAdapter=new MyViewPagerAdapter(views);
        viewPager.setAdapter(myViewPagerAdapter);

    }

    class MyViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> views;

        public MyViewPagerAdapter(ArrayList<View> mViews) {
            this.views = mViews;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(views.get(position % views.size()));

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager)container).addView(views.get(position % views.size()), 0);
            return views.get(position % views.size());
        }


    }
}
