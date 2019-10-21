package com.example.minip;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Post_event extends AppCompatActivity {

    private EditText date,time,venue,desc;
   private Spinner eventype;
   private Button eventpost;
    private DatabaseReference dbref;
   private String name,curremail,flat,block,email;

    PostEventDB postEventDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        venue = (EditText) findViewById(R.id.venue);
        desc = (EditText) findViewById(R.id.desc);
        eventype = (Spinner) findViewById(R.id.spinner2);
        eventpost = (Button) findViewById(R.id.eventbtn);


        //get currently loggedin user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            curremail = user.getEmail();  //current users mail


            FirebaseDatabase.getInstance().getReference().child("RegisterDB").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        RegisterDB registerDB=snapshot.getValue(RegisterDB.class);
                        if(curremail.equals(registerDB.getEmail())) {
                            email=curremail;
                            name=registerDB.getName();
                            flat=registerDB.getFlatno();
                            block=registerDB.getBlock();

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        eventpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(TextUtils.isEmpty(date.getText().toString()))
                {
                    Toast.makeText(Post_event.this,"Date field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(time.getText().toString()))
                {
                    Toast.makeText(Post_event.this,"Time field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(venue.getText().toString()))
                {
                    Toast.makeText(Post_event.this,"Venue field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(desc.getText().toString()))
                {
                    Toast.makeText(Post_event.this,"Description field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }


                postEventDB=new PostEventDB();
                dbref= FirebaseDatabase.getInstance().getReference().child("PostEventDB");

                postEventDB.setBlock(block);
                postEventDB.setDate(date.getText().toString());
                postEventDB.setDescription(desc.getText().toString());
                postEventDB.setFlat(flat);
                postEventDB.setName(name);
                postEventDB.setTime(time.getText().toString());
                postEventDB.setEmail(email);
                postEventDB.setType(eventype.getSelectedItem().toString());
                postEventDB.setVenue(venue.getText().toString());
                dbref.push().setValue(postEventDB);
                Toast.makeText(Post_event.this,"Posted Successfully",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
