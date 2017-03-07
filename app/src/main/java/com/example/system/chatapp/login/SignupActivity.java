package com.example.system.chatapp.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.system.chatapp.R;
import com.example.system.chatapp.utils.PagerContainer;
import com.viewpagerindicator.CirclePageIndicator;

public class SignupActivity extends Activity {

    CirclePageIndicator oCirclePageIndicator;
    TextView vT_as_login,vT_as_signup;

    PagerContainer mContainer;
    ViewPager pager;
    PagerAdapter adapter;
    int[] mResources = {
            R.drawable.pager_tree,
            R.drawable.pager_two,
            R.drawable.pager_one,
            R.drawable.pager_four,
            R.drawable.pager_one,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        oCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.cP_awt_placeIndicator);
        vT_as_login= (TextView) findViewById(R.id.vT_as_login);
        vT_as_signup= (TextView) findViewById(R.id.vT_as_signup);

        mContainer = (PagerContainer) findViewById(R.id.pager_container);
        pager = mContainer.getViewPager();
        adapter = new MyPagerAdapter(this);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());
        pager.setPageMargin(15);
        pager.setClipChildren(false);
        oCirclePageIndicator.setViewPager(pager);



        vT_as_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login_intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(login_intent);
            }
        });

        vT_as_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login_intent=new Intent(SignupActivity.this,RegisterActivity.class);
                startActivity(login_intent);
            }
        });

    }

    private  class MyPagerAdapter extends  PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public MyPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.adapter_img, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.pager_add_image);
            imageView.setImageResource(mResources[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }




}
