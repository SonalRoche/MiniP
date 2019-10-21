package com.example.minip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {
   private Button button;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    Button registerbtn;
    EditText name,email,password,flatno;
    Spinner spinner;
    DatabaseReference dbref;
    RegisterDB regdb;  //from class RegisterDB



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        registerbtn=(Button)findViewById(R.id.registerbtn);
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        flatno=(EditText)findViewById(R.id.flatno);
        spinner=(Spinner)findViewById(R.id.spinner);

        Spinner spinner_loc=(Spinner) findViewById(R.id.spinner);

        regdb=new RegisterDB();
        dbref= FirebaseDatabase.getInstance().getReference().child("RegisterDB");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(register.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.location));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_loc.setPrompt("Select your apartment");
        spinner_loc.setAdapter(adapter);

        button=(Button)findViewById(R.id.registerbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail=email.getText().toString().trim();
                String pass=password.getText().toString().trim();
                String uname=name.getText().toString().trim();
                String block=spinner.getSelectedItem().toString();
                String flatnum=flatno.getText().toString().trim();

                if(TextUtils.isEmpty(mail))
                {
                    Toast.makeText(register.this,"Email field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass))
                {
                    Toast.makeText(register.this,"Password field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(uname))
                {
                    Toast.makeText(register.this,"Name field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(flatnum))
                {
                    Toast.makeText(register.this,"Flat No. field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                //Adding to database
                regdb.setName(uname);
                regdb.setEmail(mail);
                regdb.setPassword(pass);
                regdb.setBlock(block);
                regdb.setFlatno(flatnum);
                dbref.push().setValue(regdb);

                Toast.makeText(register.this,"Registered in DB",Toast.LENGTH_SHORT).show();

                //Authentication
                //show  progress bar
                progressDialog.setMessage("Registering User...");
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(mail,pass)
                        .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                   if(task.isSuccessful())
                                   {
                                       Toast.makeText(register.this,"You have successfully registered!",Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent(register.this, login.class);
                                       startActivity(intent);
                                   }
                                   else
                                   {
                                       FirebaseAuthException e=(FirebaseAuthException)task.getException();
                                       Toast.makeText(register.this,"Registration failed"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                       progressDialog.dismiss();

                                   }
                            }
                        });



            }
        });


    }
}
