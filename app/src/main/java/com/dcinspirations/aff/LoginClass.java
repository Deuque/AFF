package com.dcinspirations.aff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dcinspirations.aff.models.AdminModel;
import com.dcinspirations.aff.models.MemberModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginClass extends AppCompatActivity {

    TextInputLayout elayout,playout;
    EditText email,pass;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = getApplicationContext();

        elayout = findViewById(R.id.email_layout);
        playout = findViewById(R.id.pass_layout);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
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
//        valaction.setText("Please wait...");
//        lgif.setVisibility(View.VISIBLE);
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

                        finish();
                        return;
                    }
                }
                UserLogin(email,pass);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void UserLogin(final String email, final String pass) {
        FirebaseDatabase.getInstance().getReference().child("Members_profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snaps:dataSnapshot.getChildren()){
                    MemberModel mm = snaps.getValue(MemberModel.class);
                    if( (email.equalsIgnoreCase(mm.getEmail())&&pass.equalsIgnoreCase(mm.getPass()) ) ||
                            (email.equalsIgnoreCase(mm.getUid())&&pass.equalsIgnoreCase(mm.getPass()) )){
                        new Sp(ctx).setUid(mm.getUid());
                        if(mm.getCategory().equalsIgnoreCase("member")){

                            new Sp(ctx).setLoginType(2);
                        } else{
                            new Sp(ctx).setLoginType(1);
                        }
                        finish();
                        return;
                    }
                }

                Toast.makeText(LoginClass.this, "You don't have an account, fill the forms", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
