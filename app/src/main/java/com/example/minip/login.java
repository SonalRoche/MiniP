package com.example.minip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {
   private Button loginbutton;
   private FirebaseAuth mAuth;
   private EditText emailid,passwd;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        loginbutton=(Button)findViewById(R.id.loginbtn);
        emailid=(EditText)findViewById(R.id.emailid);
        passwd=(EditText)findViewById(R.id.passwd);




        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail=emailid.getText().toString().trim();
                String pass=passwd.getText().toString().trim();
                if(TextUtils.isEmpty(mail))
                {
                    Toast.makeText(login.this,"Email field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(pass))
                {
                    Toast.makeText(login.this,"Password field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Logging in...");
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(mail,pass)
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful())
                                {
                                    finish();
                                    startActivity(new Intent(getApplicationContext(),Tabbed_MainActivity.class));
                                }
                            }
                        });

            }
        });

    }
}
