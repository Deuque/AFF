package com.dcinspirations.aff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dcinspirations.aff.models.MemberModel;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import co.paystack.android.PaystackSdk;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public DrawerLayout drawer;
    NavigationView navigationView;
    ImageView cd;
    Button gotologin;
    public NestedScrollView nestedScrollView;
    Context ctx;
    MemberModel mainmember = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();
        PaystackSdk.initialize(getApplicationContext());
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_video, R.id.nav_article, R.id.nav_about, R.id.nav_members,
                R.id.nav_news,R.id.nav_media)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        cd = navigationView.getHeaderView(0).findViewById(R.id.cd);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.END);
            }
        });

//        gotologin = navigationView.getHeaderView(0)findViewById(R.id.gotologin);
//        gotologin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), LoginClass.class));
//            }
//        });

    }

    public void gotoLogin(View v){
        startActivity(new Intent(getApplicationContext(), LoginClass.class));
    }

    public void logout(View v){
        new Sp(getApplicationContext()).setLoginType(0);
       changeUser();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void transformNav(String email, String title, int menu){
        navigationView.getHeaderView(0).findViewById(R.id.non_reg).setVisibility(View.GONE);
        navigationView.getHeaderView(0).findViewById(R.id.reg).setVisibility(View.VISIBLE);
        TextView t1 = navigationView.getHeaderView(0).findViewById(R.id.email);
        t1.setText(email);
        TextView t2 = navigationView.getHeaderView(0).findViewById(R.id.title);
        t2.setText(title);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(menu);
        navigationView.setSelected(true);
    }
    private void changeUser(){
        int lt = new Sp(getApplicationContext()).getLoginType();
        if(lt==0){
            navigationView.getHeaderView(0).findViewById(R.id.non_reg).setVisibility(View.VISIBLE);
            navigationView.getHeaderView(0).findViewById(R.id.reg).setVisibility(View.GONE);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu1);
            drawer.closeDrawer(GravityCompat.END);
        }
        if(lt==1){
            transformNav(mainmember.getEmail(),mainmember.getName(), R.menu.menu2);

        }
        if(lt==2){
            transformNav(mainmember.getEmail(),mainmember.getName(), R.menu.menu3);

        }
        if(lt==3){
            transformNav("Admin","Admin", R.menu.menu4);

        }

    }

    private void updateData(){
        FirebaseDatabase.getInstance().getReference().child("Members_profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snaps:dataSnapshot.getChildren()){
                    MemberModel mm = snaps.getValue(MemberModel.class);
                    int t = new Sp(ctx).getLoginType();
                    if(t!=3&&t!=0){
                        String uid = new Sp(ctx).getUid();
                        if(uid.equalsIgnoreCase(mm.getUid())){
                            mainmember = mm;
                            int newT=0;
                            if(mm.getCategory().equalsIgnoreCase("member")){
                                newT = 2;
                            }else{
                                newT = 1;

                            }
                            new Sp(ctx).setLoginType(newT);
                            changeUser();

                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateData();
        changeUser();
    }
}
