package com.dcinspirations.aff;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dcinspirations.aff.adapters.ExcosAdapter;
import com.dcinspirations.aff.adapters.ExcosAdapter2;
import com.dcinspirations.aff.models.ExcosModel;
import com.dcinspirations.aff.models.MemberModel;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dcinspirations.aff.models.NewsModel;
import com.dcinspirations.aff.ui.ExcosFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.paystack.android.PaystackSdk;

import static com.dcinspirations.aff.BaseApp.CHANNEL_1_ID;

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

        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }catch (Exception e)
        {}
//
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_about, R.id.nav_members,
                R.id.nav_news, R.id.nav_media)
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


        checkForMessages();

    }



    public void gotoLogin(View v) {
        startActivity(new Intent(ctx, LoginClass.class));
    }

    public void logout(View v) {
        new Sp(ctx).setLoginType(0);
        new Sp(ctx).setUid("");
        changeUser();
        onResume();
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

    private void transformNav(String email, String title, int menu) {
        navigationView.getHeaderView(0).findViewById(R.id.non_reg).setVisibility(View.GONE);
        navigationView.getHeaderView(0).findViewById(R.id.reg).setVisibility(View.VISIBLE);
        TextView t1 = navigationView.getHeaderView(0).findViewById(R.id.email);
        t1.setText(email);
        TextView t2 = navigationView.getHeaderView(0).findViewById(R.id.title);
        t2.setText(title);
        navigationView.getHeaderView(0).findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx,Biodata.class).putExtra("uid",mainmember.getUid()));
            }
        });
        navigationView.getMenu().clear();
        navigationView.inflateMenu(menu);
        navigationView.setSelected(true);
    }

    private void changeUser() {
        int lt = new Sp(getApplicationContext()).getLoginType();
        navigationView.getHeaderView(0).findViewById(R.id.profile).setVisibility(View.VISIBLE);
        if (lt == 0) {
            navigationView.getHeaderView(0).findViewById(R.id.non_reg).setVisibility(View.VISIBLE);
            navigationView.getHeaderView(0).findViewById(R.id.reg).setVisibility(View.GONE);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu1);
            drawer.closeDrawer(GravityCompat.END);
        }
        if (lt == 1 && mainmember != null) {

            transformNav(mainmember.getEmail(), mainmember.getName(), R.menu.menu2);

        }
        if (lt == 2 && mainmember != null) {
            transformNav(mainmember.getEmail(), mainmember.getName(), R.menu.menu3);

        }
        if (lt == 3) {
            transformNav("Admin", "Admin", R.menu.menu4);
            navigationView.getHeaderView(0).findViewById(R.id.profile).setVisibility(View.GONE);
        }

    }

    public void updateData(final String uid) {
        if(uid.isEmpty()){
            return;
        }
        FirebaseDatabase.getInstance().getReference().child("AFFMembers").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MemberModel mm = dataSnapshot.getValue(MemberModel.class);
                mainmember = mm;
                int newT = 0;
                if (mainmember != null) {
                    if (mainmember.getCategory().equalsIgnoreCase("member")) {
                        newT = 2;
                    } else {
                        newT = 1;

                    }
                    new Sp(ctx).setLoginType(newT);
                }

                changeUser();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkForMessages(){
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AffNews");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final ArrayList<NewsModel> nlist = new ArrayList<>();
                for (final DataSnapshot snaps : dataSnapshot.getChildren()) {

                    final NewsModel mm = snaps.getValue(NewsModel.class);
                    mm.setNkey(snaps.getKey());
                    nlist.add(mm);

                    if(new Sp(ctx).getUid().equalsIgnoreCase("")){
                        if(nlist.size()<=3){
                            AddNotification(ctx,mm.getNtitle(),mm.getNbody(),nlist.indexOf(mm));
                        }
                    }else {

                        dbref.child(mm.getNkey()).child("Viewers").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snaps : dataSnapshot.getChildren()) {
                                    if (snaps.getValue().toString().equalsIgnoreCase(new Sp(ctx).getUid())) {
                                        return;
                                    }
                                }

                                AddNotification(ctx, mm.getNtitle(), mm.getNbody(), nlist.indexOf(mm));

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void AddNotification(Context ctx, String Title, String message,int id){
        NotificationManagerCompat notificationManagerCompat;
        notificationManagerCompat = NotificationManagerCompat.from(ctx);
        Intent notifyIntent = new Intent(ctx, MainActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                ctx, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        Notification notification = new NotificationCompat.Builder(ctx,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo1)
                .setContentTitle(Title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(notifyPendingIntent)
                .setAutoCancel(true)
                .build();


        notificationManagerCompat.notify(id,notification);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String uid = new Sp(ctx).getUid();
        updateData(uid);
        changeUser();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.onResume();
    }
}
