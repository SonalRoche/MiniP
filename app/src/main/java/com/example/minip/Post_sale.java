package com.example.minip;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Post_sale extends AppCompatActivity {


    private static  final  int PICK_IMAGE_REQUEST=1;
    private Button chooseimage,post;
    private EditText prod_name,prod_desc,cost;
    private ImageView uploadimg;

    private Uri imageuri;

    private StorageReference mstorageReference,filepath;
    private DatabaseReference dbref;
    private StorageTask uplaodTask;

    private String currentmail;
    String finalurl;
    private String usermail,userblock,userflat,username;

    PostSaleDB postSaleDB;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sale);


        mstorageReference = FirebaseStorage.getInstance().getReference();  //just getReference() cuz we're not creating a subfolder in storage

        chooseimage = (Button) findViewById(R.id.buttonchoose);
        post = (Button) findViewById(R.id.postbutton);
        prod_name = (EditText) findViewById(R.id.prod_name);
        prod_desc = (EditText) findViewById(R.id.prod_desc);
        cost = (EditText) findViewById(R.id.cost);
        uploadimg = (ImageView) findViewById(R.id.uploadimage);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            currentmail = user.getEmail();  //current users mail

            FirebaseDatabase.getInstance().getReference().child("RegisterDB").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        RegisterDB registerDB = snapshot.getValue(RegisterDB.class);
                        if (currentmail.equals(registerDB.getEmail())) {
                            usermail = currentmail;
                            username = registerDB.getName();
                            userblock = registerDB.getBlock();
                            userflat = registerDB.getFlatno();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        chooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openfilechooser();

            }
        });


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postSaleDB=new PostSaleDB();
                dbref=FirebaseDatabase.getInstance().getReference().child("PostSaleDB");

                final String imageid;
                imageid=(System.currentTimeMillis()+"."+getFileExtension(imageuri)); // ex.548726337.jpeg
                final StorageReference Ref=mstorageReference.child(imageid);
                uplaodTask=Ref.putFile(imageuri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(Post_sale.this,"Posted image Successfully",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Post_sale.this,"Image Unsuccessful",Toast.LENGTH_SHORT).show();

                            }
                        })
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                        // on completing image upload in storage  get image url and then store info in database
                        filepath=mstorageReference.child(imageid);
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                finalurl=uri.toString();  //gets the download uri

                                Toast.makeText(Post_sale.this,uri.toString(),Toast.LENGTH_LONG).show();

                                //store in database (will be inside OnComplete method and not outside)
                                postSaleDB.setProd_cost(cost.getText().toString());
                                postSaleDB.setProd_desc(prod_desc.getText().toString());
                                postSaleDB.setProd_name(prod_name.getText().toString());
                                postSaleDB.setUserblock(userblock);
                                postSaleDB.setUseremail(usermail);
                                postSaleDB.setUserflat(userflat);
                                postSaleDB.setUsername(username);
                                postSaleDB.setImageId(finalurl);  //image url
                                dbref.push().setValue(postSaleDB);
                                Toast.makeText(Post_sale.this,"Posted Successfully",Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });





            }
        });


    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr= getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void openfilechooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            imageuri=data.getData();
            Picasso.with(this).load(imageuri).into(uploadimg);

        }

    }




}
