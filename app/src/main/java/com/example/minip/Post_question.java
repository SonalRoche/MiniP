package com.example.minip;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Post_question extends AppCompatActivity {

    Button postbtn;
    EditText question,topic;
    private FirebaseAuth mAuth;
    DatabaseReference dbref;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    String name,flatno,blk,tpc,ques,email;
    String currentemail;

    PostQuestionDB postQuestionDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);



        question=(EditText)findViewById(R.id.question);
        topic=(EditText)findViewById(R.id.topic);
        postbtn=(Button)findViewById(R.id.buttonpost) ;





        //get currently loggedin user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            currentemail=user.getEmail();  //current users mail


            FirebaseDatabase.getInstance().getReference().child("RegisterDB").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        RegisterDB registerDB=snapshot.getValue(RegisterDB.class);
                        if(currentemail.equals(registerDB.getEmail())) {
                            //temp2.setText(registerDB.getBlock());
                            email=currentemail;
                            name=registerDB.getName();
                            flatno=registerDB.getFlatno();
                            blk=registerDB.getBlock();
                            ques=question.getText().toString();
                            tpc=topic.getText().toString();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }



        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(TextUtils.isEmpty(question.getText().toString()))
                {
                    Toast.makeText(Post_question.this,"Question field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(topic.getText().toString()))
                {
                    Toast.makeText(Post_question.this,"Topic field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                postQuestionDB=new PostQuestionDB();
                dbref= FirebaseDatabase.getInstance().getReference().child("PostQuestionDB");

                postQuestionDB.setEmail(email);
                postQuestionDB.setFlatno(flatno);
                postQuestionDB.setBlock(blk);
                postQuestionDB.setName(name);
                postQuestionDB.setQuestion(question.getText().toString());
                postQuestionDB.setTopic(topic.getText().toString());
                dbref.push().setValue(postQuestionDB);
                Toast.makeText(Post_question.this,"Posted Successfully",Toast.LENGTH_SHORT).show();

            }
        });



    }
}
