    package com.example.minip;

    import android.content.Context;
    import android.icu.text.SimpleDateFormat;
    import android.os.Build;
    import android.os.Bundle;
    import android.os.Handler;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.firebase.ui.database.FirebaseRecyclerAdapter;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.google.firebase.storage.FirebaseStorage;
    import com.google.firebase.storage.StorageReference;
    import com.squareup.picasso.Picasso;

    import java.util.Date;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.annotation.RequiresApi;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;


    public class Tab3 extends Fragment {


        public static final String TAG = "Tab 3 fragment";
        private RecyclerView recyclerView;
        private DatabaseReference dbref;
        private StorageReference storageReference;
        private FirebaseAuth mAuth;
        private static Context context = null;  //STEP 1
        static String buyer;



        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.tab3,container,false);

           storageReference =  FirebaseStorage.getInstance().getReference("PostSaleDB");
           // storageReference =  FirebaseStorage.getInstance().getReference();
            dbref= FirebaseDatabase.getInstance().getReference().child("PostSaleDB");
            dbref.keepSynced(true);
            context=getActivity(); //STEP 2

            recyclerView=(RecyclerView)view.findViewById(R.id.myrecycleview3);
            recyclerView.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getActivity());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);



            //currently loggedin user
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {

                String curremail = user.getEmail();  //current users mail


                FirebaseDatabase.getInstance().getReference().child("RegisterDB").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            RegisterDB registerDB=snapshot.getValue(RegisterDB.class);

                            if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(registerDB.getEmail())) {



                                buyer=registerDB.getName();


                                //Toast.makeText(context.getApplicationContext(), finalname,Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } // end of finding user



            
            return view;

        }

        @Override
        public void onStart() {
            super.onStart();



            FirebaseRecyclerAdapter<PostSaleDB, PostSaleViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<PostSaleDB, PostSaleViewHolder>(PostSaleDB.class,R.layout.tab3cardview, PostSaleViewHolder.class,dbref) {


                @Override
                protected void populateViewHolder(PostSaleViewHolder postSaleViewHolder, PostSaleDB postSaleDB, int i) {


                            postSaleViewHolder.setProd_Name(postSaleDB.getProd_name());
                            postSaleViewHolder.setProd_desc(postSaleDB.getProd_desc());
                            postSaleViewHolder.setProd_cost(postSaleDB.getProd_cost());
                            postSaleViewHolder.setUsername(postSaleDB.getUsername());
                            postSaleViewHolder.setUserblock(postSaleDB.getUserblock());
                            postSaleViewHolder.setUserflat(postSaleDB.getUserflat());
                            postSaleViewHolder.setImageId(getActivity().getApplicationContext(),postSaleDB.getImageId());

                }
            };

            recyclerView.setAdapter(firebaseRecyclerAdapter);
        }




        public static class PostSaleViewHolder extends RecyclerView.ViewHolder
        {

            Context context1;
            TextView cost,productname,seller;
            Button buy,sold;
            View mView;
            SoldItemsDB soldItemsDB;
            DatabaseReference dbreff;
            String k;
            int count=0;
            Handler setDelay;  //for delay
            Runnable startDelay; //for delay
            public  PostSaleViewHolder(final View itemView)
            {
                super(itemView);
                mView=itemView;

                setDelay=new Handler();  //for delay

                productname=(TextView)itemView.findViewById(R.id.prodname);
                seller=(TextView)itemView.findViewById(R.id.prod_username);
                cost=(TextView)itemView.findViewById(R.id.prodcost);
                buy=(Button)itemView.findViewById(R.id.buttonbuy);
                sold=(Button)itemView.findViewById(R.id.buttonsold);

                buy.setOnClickListener(new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {



                        final int x=getAdapterPosition();
                       // Toast.makeText(context.getApplicationContext(), "position= "+x, Toast.LENGTH_SHORT).show();

                        String cst=cost.getText().toString().trim();
                        String sller=seller.getText().toString();
                        String prodnme=productname.getText().toString();

                        String finalbuyer=buyer;
                        String finalcost=cst.substring(cst.lastIndexOf(":")+1).trim();
                        String finalseller=sller.substring(sller.lastIndexOf(":")+1).trim();
                        String finalprodname=prodnme.substring(prodnme.lastIndexOf(":")+1);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String date = sdf.format(new Date());


                        if(finalbuyer.equals(finalseller))
                        {
                            Toast.makeText(context.getApplicationContext(),"You cant purchase your own item",Toast.LENGTH_SHORT).show();
                            return;
                        }


                       // buy.setVisibility(View.INVISIBLE);
                        //sold.setBackgroundResource(R.drawable.btn_sold);
                        //sold.setVisibility(View.VISIBLE);



                        //adding to database
                        soldItemsDB=new SoldItemsDB();
                        dbreff= FirebaseDatabase.getInstance().getReference().child("SoldItemsDB");

                        soldItemsDB.setBuyer(finalbuyer);
                        soldItemsDB.setCost(finalcost);
                        soldItemsDB.setDate(date);
                        soldItemsDB.setProduct(finalprodname);
                        soldItemsDB.setSeller(finalseller);
                        dbreff.push().setValue(soldItemsDB);
                        Toast.makeText(context.getApplicationContext(),"CONGRATULATIONS ON YOUR PURCHASE!!", Toast.LENGTH_LONG).show();



                        // delay before deleting item
                        startDelay=new Runnable() {
                            @Override
                            public void run() {

                                //deletion
                                FirebaseDatabase.getInstance().getReference().child("PostSaleDB").limitToFirst(x+1).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                        {
                                            k=snapshot.getKey();  //key of item to be deleted

                                        }


                                        //Toast.makeText(context.getApplicationContext(),"Key ="+k, Toast.LENGTH_LONG).show();

                                        DatabaseReference keyval=FirebaseDatabase.getInstance().getReference("PostSaleDB").child(k);
                                        keyval.removeValue();


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                        };
                        setDelay.postDelayed(startDelay,5000);  //setting delay of 5 sec



                    }
                });



            }



            public void setProd_Name(String prodname)
            {
                TextView product_name=(TextView)mView.findViewById(R.id.prodname);
                product_name.setText("Product name : "+prodname);
            }


            public void setProd_desc(String prodesc)
            {
                TextView product_des=(TextView)mView.findViewById(R.id.proddesc);
                product_des.setText("Product description : "+prodesc);
            }

            public void setProd_cost(String cost)
            {
                TextView product_cost=(TextView)mView.findViewById(R.id.prodcost);
                product_cost.setText("Product cost : Rs. "+cost);
            }

            public void setUsername(String name)
            {
                TextView uname=(TextView)mView.findViewById(R.id.prod_username);
                uname.setText("Posted by : "+name);
            }

            public void setUserblock(String b)
            {
                TextView blk=(TextView)mView.findViewById(R.id.prod_userblock);
                blk.setText("Block : "+b);
            }

            public void setUserflat(String f)
            {
                TextView flat=(TextView)mView.findViewById(R.id.prod_userflat);
                flat.setText("Flat No. : "+f);
            }

            public void setImageId(Context ctx, String image)
            {
                ImageView prodimg=(ImageView)mView.findViewById(R.id.prodimage);
                Picasso.with(ctx).load(image).into(prodimg);
            }


        }


    }
