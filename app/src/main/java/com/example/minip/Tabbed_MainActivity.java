package com.example.minip;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class Tabbed_MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FirebaseAuth mAuth;
    int backButtonPressed=0;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbedmain);

        Toolbar toolbar =findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);  // remove title in toolbar
        getSupportActionBar().setIcon(R.drawable.ic_account_circle); //add icon to toolbar

        mAuth=FirebaseAuth.getInstance();
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);

        Log.d(TAG,"onCreate: Starting.");
        mSectionsPagerAdapter =new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager=(ViewPager)findViewById(R.id.view_pager);
        setupViewPager(mViewPager);
        //display tabs
        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Tabbed_MainActivity.this , Posts_menu.class);
                startActivity(intent);
            }
        });



    }

    @Override  //TO EXIT APP WHEN USER IS LOGGED IN AND PRESSES BACK BUTTON AND NOT RETURN TO HOME SCREEN
    public void onBackPressed() {
        if(backButtonPressed>=1)
        {
            Intent intent=new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else
        {
            Toast.makeText(this,"Press BACK again to exit application",Toast.LENGTH_SHORT).show();
            backButtonPressed++;
        }
    }

    @Override  //To add 3 dots menu in toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    //give functionality to menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.item1:
                mAuth.signOut();
                finish();
                startActivity(new Intent(this,MainActivity.class));

            default:
                return super.onOptionsItemSelected(item);


        }


    }

    private void setupViewPager(ViewPager viewPager)
    {   //add tabs in tabbed view
        SectionsPagerAdapter adapter=new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addfragment(new Tab1(),"POSTS");
        adapter.addfragment(new Tab2(),"EVENTS");
        adapter.addfragment(new Tab3(),"ON SALE");
        adapter.addfragment(new Tab4(),"EXPENSES");
        viewPager.setAdapter(adapter);
    }
}