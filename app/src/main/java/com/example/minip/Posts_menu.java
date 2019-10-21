package com.example.minip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Posts_menu extends AppCompatActivity {

     Button btnques,btnevent,btnsale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_menu);

        btnques=(Button)findViewById(R.id.questionbtn);
        btnevent=(Button)findViewById(R.id.eventbtn);
        btnsale=(Button)findViewById(R.id.salebtn);

      btnques.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(Posts_menu.this,Post_question.class);
              startActivity(intent);
          }
      });


        btnevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Posts_menu.this,Post_event.class);
                startActivity(intent);
            }
        });

        btnsale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Posts_menu.this,Post_sale.class);
                startActivity(intent);
            }
        });
    }
}
