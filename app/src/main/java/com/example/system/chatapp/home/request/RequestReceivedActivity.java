package com.example.system.chatapp.home.request;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.system.chatapp.R;
//import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RequestReceivedActivity extends AppCompatActivity {

    ListView vL_arr_requestlist;
    TextView vT_tb_headtext, vT_ul_mobile, vT_ul_name;
    ImageView vI_tl_back;
    LinearLayout vL_ul_userlayout;
    ArrayList<FriendsDetailsbean> friendList;
    SharedPreferences login_pref;
    String email;
    ViewPager viewPager;
    TabLayout vT_agm_tabLayout;
   // private FirebaseListAdapter<FriendsDetailsbean> usersadapter;
    DatabaseReference reference;
    //requestAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_received);
        try{
            ActionBar bar=getSupportActionBar();
            bar.hide();
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        initializeViews();
    }

    private void initializeViews() {
        vT_tb_headtext = (TextView) findViewById(R.id.vT_tb_headtext);
        vI_tl_back = (ImageView) findViewById(R.id.vI_tl_back);
        viewPager= (ViewPager) findViewById(R.id.vP_arr_notificationpager);
        vT_agm_tabLayout= (TabLayout) findViewById(R.id.vT_agm_tabLayout);

       // vL_arr_requestlist = (ListView) findViewById(R.id.vL_arr_requestlist);
        login_pref = getSharedPreferences("LoginDetails", 0);
        email = login_pref.getString("email", null);
        setValues();
    }

    private void setValues() {
        vT_tb_headtext.setText("REQUESTS");
        friendList = new ArrayList<>();
        vI_tl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // adapter=new requestAdapter(RequestReceivedActivity.this);
        // vL_arr_requestlist.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  displayRequestsList();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PendingRequestFragment());
        adapter.addFragment(new AcceptedRequestsFragment());

        viewPager.setAdapter(adapter);

        vT_agm_tabLayout.setupWithViewPager(viewPager);
        try {
            vT_agm_tabLayout.getTabAt(0).setText(R.string.pending);
            vT_agm_tabLayout.getTabAt(1).setText(R.string.accepted);

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void displayRequestsList() {
        //reference = FirebaseDatabase.getInstance().getReference("requests");

        /*usersadapter = new FirebaseListAdapter<FriendsDetailsbean>(RequestReceivedActivity.this, FriendsDetailsbean.class,
                R.layout.user_layout, reference.orderByChild("requestReceiverEmail").equalTo(email)) {
            @Override
            protected void populateView(View v, FriendsDetailsbean model, final int position) {
                vT_ul_mobile = (TextView) v.findViewById(R.id.vT_ul_mobile);
                vT_ul_name = (TextView) v.findViewById(R.id.vT_ul_name);
                vL_ul_userlayout = (LinearLayout) v.findViewById(R.id.vL_ul_userlayout);
                if (model.getRequestReceiverEmail().equalsIgnoreCase(email)) {
                    vT_ul_name.setText(model.getRequestSendername());
                    vT_ul_mobile.setText(model.getRequestSendermobilenumber());
                    String key = getRef(position).getKey();
                    model.setmREquest_key(key);
                    friendList.add(model);
                }

                vL_ul_userlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent approvel_intent = new Intent(RequestReceivedActivity.this, RequestApprovelActivity.class);
                        approvel_intent.putExtra("Key", friendList.get(position).getmREquest_key());
                        approvel_intent.putExtra("senderName", friendList.get(position).getRequestSendername());
                        approvel_intent.putExtra("senderMobile", friendList.get(position).getRequestSendermobilenumber());
                        approvel_intent.putExtra("senderEmail", friendList.get(position).getRequestSenderEmail());
                        approvel_intent.putExtra("reqStatus", friendList.get(position).getRequestStatus());
                        startActivity(approvel_intent);
                    }
                });

            }
        };*/


      /*  usersadapter = new FirebaseListAdapter<FriendsDetailsbean>(RequestReceivedActivity.this, FriendsDetailsbean.class,
                R.layout.user_layout, reference.orderByChild("requestReceiverEmail").startAt(email).endAt(email).orderByChild("requestStatus")
                                    .startAt("Request sent").endAt("Request sent")) {
            @Override
            protected void populateView(View v, FriendsDetailsbean model, final int position) {
                vT_ul_mobile = (TextView) v.findViewById(R.id.vT_ul_mobile);
                vT_ul_name = (TextView) v.findViewById(R.id.vT_ul_name);
                vL_ul_userlayout = (LinearLayout) v.findViewById(R.id.vL_ul_userlayout);
                if (model.getRequestReceiverEmail().equalsIgnoreCase(email)) {
                    vT_ul_name.setText(model.getRequestSendername());
                    vT_ul_mobile.setText(model.getRequestSendermobilenumber());
                    String key = getRef(position).getKey();
                    model.setmREquest_key(key);
                    friendList.add(model);
                }

                vL_ul_userlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent approvel_intent = new Intent(RequestReceivedActivity.this, RequestApprovelActivity.class);
                        approvel_intent.putExtra("Key", friendList.get(position).getmREquest_key());
                        approvel_intent.putExtra("senderName", friendList.get(position).getRequestSendername());
                        approvel_intent.putExtra("senderMobile", friendList.get(position).getRequestSendermobilenumber());
                        approvel_intent.putExtra("senderEmail", friendList.get(position).getRequestSenderEmail());
                        approvel_intent.putExtra("reqStatus", friendList.get(position).getRequestStatus());
                        startActivity(approvel_intent);
                    }
                });

            }
        };*/

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }



        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }



}

