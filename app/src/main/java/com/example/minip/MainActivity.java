package com.example.minip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
   private Button button;
   private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to check if user has already logged in, if yes, open tabbed activity
        mAuth=FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null)
        {
            //profile activity
            finish();
            startActivity(new Intent(getApplicationContext(),Tabbed_MainActivity.class));
        }

        //on clicking register
        button=(Button)findViewById(R.id.btn_reg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this , register.class);
                startActivity(intent);
            }
        });

        //on clicking login
        button=(Button)findViewById(R.id.btn_lgn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this , login.class);
                startActivity(intent);
            }
        });
    }
}
