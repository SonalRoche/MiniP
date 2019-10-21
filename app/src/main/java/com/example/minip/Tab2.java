package com.example.minip;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Tab2 extends Fragment {

    public static final String TAG = "Tab 2 fragment";
    private RecyclerView recyclerView;
    private DatabaseReference dbref;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.tab2,container,false);

        dbref= FirebaseDatabase.getInstance().getReference().child("PostEventDB");
        dbref.keepSynced(true);

        recyclerView=(RecyclerView)view.findViewById(R.id.myrecycleview2);
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

        FirebaseRecyclerAdapter<PostEventDB,PostEventViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<PostEventDB, PostEventViewHolder>(PostEventDB.class,R.layout.tab2cardview, PostEventViewHolder.class,dbref) {
            @Override
            protected void populateViewHolder(PostEventViewHolder postEventViewHolder, PostEventDB postEventDB, int i) {

                postEventViewHolder.setBlock(postEventDB.getBlock());
                postEventViewHolder.setDate(postEventDB.getDate());
                postEventViewHolder.setDescription(postEventDB.getDescription());
                postEventViewHolder.setFlat(postEventDB.getFlat());
                postEventViewHolder.setName(postEventDB.getName());
                postEventViewHolder.setTime(postEventDB.getTime());
                postEventViewHolder.setType(postEventDB.getType());
                postEventViewHolder.setVenue(postEventDB.getVenue());

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostEventViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public  PostEventViewHolder(View itemView)
        {
            super(itemView);
            mView=itemView;
        }

        public void setType(String type)
        {
            TextView post_type=(TextView)mView.findViewById(R.id.post_eventype);
                    post_type.setText("Event Type : "+type);
        }

        public void setFlat(String flat)
        {
            TextView post_flat=(TextView)mView.findViewById(R.id.post_flat);
            post_flat.setText("Flat No. : "+flat);
        }

        public void setBlock(String block)
        {
            TextView post_blk=(TextView)mView.findViewById(R.id.post_blk);
            post_blk.setText("Block : "+block);
        }

        public void setDescription(String des)
        {
            TextView post_des=(TextView)mView.findViewById(R.id.post_desc);
            post_des.setText("Description : "+des);
        }

        public void setVenue(String ven)
        {
            TextView post_ven=(TextView)mView.findViewById(R.id.post_venue);
            post_ven.setText("Venue : "+ven);
        }

        public void setTime(String t)
        {
            TextView post_time=(TextView)mView.findViewById(R.id.post_time);
            post_time.setText("Time : "+t);
        }

        public void setDate(String d)
        {
            TextView post_date=(TextView)mView.findViewById(R.id.post_date);
            post_date.setText("Date : "+d);
        }

        public void setName(String name)
        {
            TextView post_name=(TextView)mView.findViewById(R.id.post_user);
            post_name.setText("Posted by : "+name);
        }


    }
}
