package com.dcinspirations.aff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dcinspirations.aff.models.AdminModel;
import com.dcinspirations.aff.models.MemberModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.droidsonroids.gif.GifImageView;

public class LoginClass extends AppCompatActivity {

    TextInputLayout elayout,playout;
    EditText email,pass;
    Context ctx;
    GifImageView lgif;
    TextView valaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = getApplicationContext();

        elayout = findViewById(R.id.email_layout);
        playout = findViewById(R.id.pass_layout);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        lgif = findViewById(R.id.lgif);
        valaction = findViewById(R.id.val_action);
    }

    public void goBack(View v){
        finish();
    }

    public void signIn(View v){
        String emailtext = email.getText().toString().trim();
        String passtext = pass.getText().toString().trim();

        if(emailtext.isEmpty()){
            elayout.setError("Email is required");
            email.requestFocus();
            return;
        }
        if(passtext.isEmpty()){
            playout.setError("Set a password");
            pass.requestFocus();
            return;
        }
        valaction.setText("Please wait...");
        lgif.setVisibility(View.VISIBLE);

        AdminLogin(emailtext,passtext);

    }

    private void AdminLogin(final String email, final String pass){
        FirebaseDatabase.getInstance().getReference().child("Admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snaps:dataSnapshot.getChildren()){
                    AdminModel am = snaps.getValue(AdminModel.class);
                    if(email.equalsIgnoreCase(am.getA_email())&&pass.equalsIgnoreCase(am.getA_pass())){
                        new Sp(ctx).setLoginType(3);
                        new Sp(ctx).setUid("admin");
                        finish();
                        return;
                    }
                }
                UserLogin(email,pass);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                valaction.setText("Sign In");
                lgif.setVisibility(View.GONE);
                Toast.makeText(LoginClass.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void UserLogin(final String email, final String pass) {
        FirebaseDatabase.getInstance().getReference().child("AFFMembers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snaps:dataSnapshot.getChildren()){
                    MemberModel mm = snaps.getValue(MemberModel.class);
                    mm.setUid(snaps.getKey());
                    if( (email.equalsIgnoreCase(mm.getEmail())&&pass.equalsIgnoreCase(mm.getPass()) ) ||
                            (email.equalsIgnoreCase(mm.getUid())&&pass.equalsIgnoreCase(mm.getPass()) )){
                        new Sp(ctx).setUid(mm.getUid());
                        if(mm.getCategory().equalsIgnoreCase("member")){

                            new Sp(ctx).setLoginType(2);
                        } else{
                            new Sp(ctx).setLoginType(1);
                        }
                        new Sp(ctx).setUid(mm.getUid());
                        finish();
                        return;
                    }
                }

                valaction.setText("Sign In");
                lgif.setVisibility(View.GONE);
                Toast.makeText(LoginClass.this, "You don't have an account, fill the forms", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                valaction.setText("Sign In");
                lgif.setVisibility(View.GONE);
                Toast.makeText(LoginClass.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
