package com.example.system.chatapp.other;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.system.chatapp.R;
import com.example.system.chatapp.beans.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NextActivity extends AppCompatActivity {

    Button signout,send_text;
    EditText send_data;
    String text_data;
    TextView data_fromserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        send_data= (EditText) findViewById(R.id.send_data);
        signout= (Button) findViewById(R.id.signout);
        send_text= (Button) findViewById(R.id.send_text);
        data_fromserver= (TextView) findViewById(R.id.data_fromserver);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref2 = db.getReference("message");

        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve latest value
                String message = dataSnapshot.getValue(String.class);
                data_fromserver.setText(message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error handling
            }
        });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        send_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_data=send_data.getText().toString().trim();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference().push();
                // Key

                ref.setValue(new ChatMessage("hii","bhaskar"));
            }
        });



    }
}
