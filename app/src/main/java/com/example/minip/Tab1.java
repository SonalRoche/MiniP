package com.example.minip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Tab1 extends Fragment {

    public static final String TAG = "Tab 1 fragment";
    private RecyclerView recyclerView;
    private DatabaseReference dbref;
    private static Context context = null;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {


        View view=inflater.inflate(R.layout.tab1,container,false);

        context=getActivity();


        dbref= FirebaseDatabase.getInstance().getReference().child("PostQuestionDB");
        dbref.keepSynced(true);

        recyclerView=(RecyclerView)view.findViewById(R.id.myrecycleview);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<PostQuestionDB,PostQuestionViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<PostQuestionDB, PostQuestionViewHolder>(PostQuestionDB.class,R.layout.tab1cardview,PostQuestionViewHolder.class,dbref) {
            @Override
            protected void populateViewHolder(PostQuestionViewHolder postQuestionViewHolder, PostQuestionDB postQuestionDB, int i) {

                postQuestionViewHolder.setTopic(postQuestionDB.getTopic());
                postQuestionViewHolder.setBlock(postQuestionDB.getBlock());
                postQuestionViewHolder.setFlatno(postQuestionDB.getFlatno());
                postQuestionViewHolder.setName(postQuestionDB.getName());
                postQuestionViewHolder.setQuestion(postQuestionDB.getQuestion());


            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }




    public static class PostQuestionViewHolder extends RecyclerView.ViewHolder
    {
         View mView;
         Button post,replies;
         TextView comment,question;
         String k,key;
         DatabaseReference dbreff,dbrefff;
         PostQuestionDB postQuestionDB;



         public  PostQuestionViewHolder(View itemView)
         {
             super(itemView);
             mView=itemView;


             int adapterpos=getAdapterPosition();

             comment=(TextView)mView.findViewById(R.id.comment);
             question=(TextView)mView.findViewById(R.id.post_question);
             post=(Button)mView.findViewById(R.id.postbtn);
             replies=(Button)mView.findViewById(R.id.replies);


             replies.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {


                     int adap=getAdapterPosition();
                     String q=question.getText().toString();
                     Intent intent=new Intent(context,Replies.class);
                     intent.putExtra("Adapter",adap);
                     intent.putExtra("Question",q);
                     context.startActivity(intent);
                 }
             });



             post.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     int x=getAdapterPosition();
                     final String comm=comment.getText().toString();
                     String ques=question.getText().toString();
                     //Toast.makeText(context.getApplicationContext(),"pos "+x+"\n comment "+comm,Toast.LENGTH_SHORT).show();


                     //getting key of current cardview
                     FirebaseDatabase.getInstance().getReference().child("PostQuestionDB").limitToFirst(x+1).addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                             for(DataSnapshot snapshot : dataSnapshot.getChildren())
                             {
                                 k=snapshot.getKey();

                             }
                             //Toast.makeText(context.getApplicationContext(),"key = "+k,Toast.LENGTH_SHORT).show();

                             //adding comments to database
                             postQuestionDB=new PostQuestionDB();
                             dbreff=FirebaseDatabase.getInstance().getReference().child("PostQuestionDB").child(k).child("Comments");
                             dbreff.keepSynced(true);
                             postQuestionDB.setComment(comm);
                             dbreff.push().setValue(postQuestionDB);
                             Toast.makeText(context,"Posted comment Successfully",Toast.LENGTH_SHORT).show();

                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                     });

                 }
             }); // button click end

         }







         public void setTopic(String topic)
         {
             TextView post_topic=(TextView)mView.findViewById(R.id.post_topic);
             post_topic.setText("Topic : "+topic);
         }


        public void setQuestion(String question)
        {
            TextView post_question=(TextView)mView.findViewById(R.id.post_question);
            post_question.setText("Question : "+question);
        }

        public void setName(String name)
        {
            TextView post_name=(TextView)mView.findViewById(R.id.post_username);
            post_name.setText("Posted by : "+name);
        }

        public void setBlock(String blk)
        {
            TextView post_blk=(TextView)mView.findViewById(R.id.post_block);
            post_blk.setText("Block : "+blk);
        }

        public void setFlatno(String flatno)
        {
            TextView post_flat=(TextView)mView.findViewById(R.id.post_flatnum);
            post_flat.setText("Flat No. : "+flatno);
        }
    }
}
