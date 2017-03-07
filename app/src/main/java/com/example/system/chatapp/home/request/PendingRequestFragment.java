package com.example.system.chatapp.home.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.system.chatapp.R;
import com.example.system.chatapp.home.FriendslistActivity;
import com.example.system.chatapp.home.HomeActivity;
import com.example.system.chatapp.utils.CircularImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PendingRequestFragment extends Fragment {


    ListView vL_fpr_requestlist;
    ArrayList<FriendsDetailsbean> friendList;
    DatabaseReference reference;
    SharedPreferences login_pref;
    String email,isCallRequired;
    TextView vT_tb_headtext, vT_ul_mobile, vT_ul_name;
    LinearLayout vL_ul_userlayout;
    requestAdapter adapter;
    CircularImageView vI_ul_userprofile;
    SharedPreferences call_list,call_list2;
    ProgressDialog dialog;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("bhaskar","onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("bhaskar","onCreate");
        call_list=getActivity().getSharedPreferences("Callstatus",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=call_list.edit();
        editor.putString("callnow","yes");
        editor.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_pending_request, container, false);
        initializeViews(view);
        Log.d("bhaskar","onCreateView");
        return view;
    }

    private void initializeViews(View view) {
        login_pref = getActivity().getSharedPreferences("LoginDetails", 0);
        email = login_pref.getString("email", null);
        vL_fpr_requestlist = (ListView) view.findViewById(R.id.vL_fpr_requestlist);
        setValues();

    }

    private void setValues() {
        friendList=new ArrayList<>();
        adapter=new requestAdapter(getActivity());
        dialog=new ProgressDialog(getActivity());
        vL_fpr_requestlist.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("bhaskar","onResume");

        call_list2=getActivity().getSharedPreferences("Callstatus",0);
        isCallRequired=call_list2.getString("callnow","");
        if(isCallRequired.equalsIgnoreCase("yes")) {
            displayPendingRequests();
        }
    }

    public void displayPendingRequests() {

        dialog.setMessage("Proccessing...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if (friendList != null) {
            if (friendList.size() != 0) {
                friendList.clear();
            }
        }

        reference = FirebaseDatabase.getInstance().getReference("requests");
        Query quesry_ref = reference.orderByChild("requestReceiverEmail").equalTo(email);
        quesry_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String key = dsp.getKey();
                    FriendsDetailsbean user_profile = dsp.getValue(FriendsDetailsbean.class);
                    if (!email.equalsIgnoreCase(user_profile.getRequestSenderEmail())&&user_profile.getRequestStatus().equalsIgnoreCase("Request Sent")) {
                        user_profile.setmREquest_key(key);
                        user_profile.setRequestStatus("Pending");
                        friendList.add(user_profile);
                        Log.d("bhaskar", user_profile.getRequestStatus() + " " + user_profile.getRequestSenderEmail());
                    }
                }
                adapter.notifyDataSetChanged();
                if(dialog.isShowing())
                    dialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }

    public  class requestAdapter extends BaseAdapter {

        LayoutInflater inflater;
        public requestAdapter(Context context){
            inflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return friendList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {

            if(view==null){
                view=inflater.inflate(R.layout.user_layout,viewGroup,false);
            }
            vT_ul_mobile = (TextView) view.findViewById(R.id.vT_ul_mobile);
            vT_ul_name = (TextView) view.findViewById(R.id.vT_ul_name);
            vL_ul_userlayout = (LinearLayout) view.findViewById(R.id.vL_ul_userlayout);
            vI_ul_userprofile= (CircularImageView) view.findViewById(R.id.vI_ul_userprofile);

          /*  byte[] imageAsBytes = Base64.decode(friendList.get(position).getRequestSenderProfilepic().getBytes(), Base64.DEFAULT);
            vI_ul_userprofile.setImageBitmap(
                    BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
            );*/

            Picasso.with(getActivity())
                    .load(friendList.get(position).getRequestSenderProfilepic())
                    .placeholder(R.drawable.profiles)        // optional
                    .into(vI_ul_userprofile);

            vT_ul_mobile.setText(friendList.get(position).getRequestSendermobilenumber());
            vT_ul_name.setText(friendList.get(position).getRequestSendername());

            vL_ul_userlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent approvel_intent = new Intent(getActivity(), RequestApprovelActivity.class);
                    approvel_intent.putExtra("Key", friendList.get(position).getmREquest_key());
                    approvel_intent.putExtra("senderName", friendList.get(position).getRequestSendername());
                    approvel_intent.putExtra("senderMobile", friendList.get(position).getRequestSendermobilenumber());
                    approvel_intent.putExtra("senderEmail", friendList.get(position).getRequestSenderEmail());
                    approvel_intent.putExtra("reqStatus", friendList.get(position).getRequestStatus());
                    approvel_intent.putExtra("profilePic",friendList.get(position).getRequestSenderProfilepic());
                    startActivity(approvel_intent);
                }
            });
            return view;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("bhaskar","onPause");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("bhaskar","onDestroyView");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("bhaskar","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("bhaskar","onDetach");
    }
}

