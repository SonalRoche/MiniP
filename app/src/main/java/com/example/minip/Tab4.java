package com.example.minip;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class Tab4 extends Fragment {

    public static final String TAG = "Tab 4 fragment";
    private RecyclerView recyclerView;
    private DatabaseReference dbref;
    static String curruser;
    private static Context context = null;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.tab4,container,false);

        context=getActivity();

        dbref= FirebaseDatabase.getInstance().getReference().child("SoldItemsDB");
        dbref.keepSynced(true);

        recyclerView=(RecyclerView)view.findViewById(R.id.myrecyclerview4);
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

        FirebaseRecyclerAdapter<SoldItemsDB,SoldItemsViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<SoldItemsDB, SoldItemsViewHolder>(SoldItemsDB.class,R.layout.tab4cardview,SoldItemsViewHolder.class,dbref) {
            @Override
            protected void populateViewHolder(SoldItemsViewHolder soldItemsViewHolder, SoldItemsDB soldItemsDB, int i) {


                soldItemsViewHolder.setPurchase(soldItemsDB.getBuyer(), soldItemsDB.getProduct(), soldItemsDB.getSeller(), soldItemsDB.getDate(), soldItemsDB.getCost());



            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class SoldItemsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public SoldItemsViewHolder(View itemView)
        {
            super(itemView);
            mView=itemView;


        }

        public void setPurchase(final String buyer, final String item, final String seller, final String date, final String amount)
        {
            final TextView purchase=(TextView)mView.findViewById(R.id.purchase);
            final TextView dat=(TextView)mView.findViewById(R.id.purchase_date);
            final TextView cost=(TextView)mView.findViewById(R.id.amount);
            final TextView itemname=(TextView)mView.findViewById(R.id.item_name);


            //currently loggedin user
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {

                FirebaseDatabase.getInstance().getReference().child("RegisterDB").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            RegisterDB registerDB=snapshot.getValue(RegisterDB.class);

                            if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(registerDB.getEmail())) {



                                curruser=registerDB.getName();

                                if(curruser.equals(buyer))
                                {
                                    purchase.setText("You purchased from "+seller);
                                    dat.setText(date);
                                    cost.setText(amount);
                                    itemname.setText(item);

                                }
                                else if(curruser.equals(seller))
                                {
                                    purchase.setText( buyer+" purchased from you");
                                    dat.setText(date);
                                    cost.setText(amount);
                                    itemname.setText(item);

                                }
                                else
                                {
                                    purchase.setText( buyer+" purchased from "+seller);
                                    dat.setText(date);
                                    cost.setText(amount);
                                    itemname.setText(item);

                                }



                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } // end of finding user



        }


    }
}
