package com.example.minip;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Replies extends AppCompatActivity {


   private  DatabaseReference dbref,dbreff;
   private ListView comment_list;

   ArrayList<String> myArrayList = new ArrayList<>();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies);

        Intent intent=getIntent();
        String que=intent.getStringExtra("Question");
        final String qs=que.substring(11);  //to remove "Question : "
        int adap=intent.getIntExtra("Adapter",0);



        comment_list=(ListView)findViewById(R.id.comment_list);
        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myArrayList)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.parseColor("#1D18F3"));

                // Generate ListView Item using TextView
                return view;
            }
        };



        comment_list.setAdapter(myArrayAdapter);






        dbref= FirebaseDatabase.getInstance().getReference().child("PostQuestionDB");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                     PostQuestionDB postQuestionDB=snapshot.getValue(PostQuestionDB.class);
                    if(qs.equals(postQuestionDB.getQuestion()))
                    {
                        DataSnapshot snap=snapshot.child("Comments");
                        long c =snap.getChildrenCount();

                        if(c!=0)
                        {


                            for(DataSnapshot s : snap.getChildren())
                            {
                                PostQuestionDB post=s.getValue(PostQuestionDB.class);
                                String comment=post.getComment();

                                myArrayList.add(comment);
                                myArrayAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            myArrayList.add("No replies yet");
                            myArrayAdapter.notifyDataSetChanged();

                        }



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
