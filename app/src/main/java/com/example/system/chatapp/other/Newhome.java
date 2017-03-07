package com.example.system.chatapp.other;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.system.chatapp.R;

/**
 * Created by SYSTEM on 09-12-2016.
 */

public class Newhome extends Activity {

    TextView vT_tb_headtext;
    ImageView vI_tl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newhome_layout);
        vT_tb_headtext= (TextView) findViewById(R.id.vT_tb_headtext);
        vI_tl_back= (ImageView) findViewById(R.id.vI_tl_back);
        vI_tl_back.setImageResource(R.drawable.drawer_icon);
        //vT_tb_headtext.setText("HOME");
    }
}
