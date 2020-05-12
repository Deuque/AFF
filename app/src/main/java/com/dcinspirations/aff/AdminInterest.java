package com.dcinspirations.aff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dcinspirations.aff.models.MemberModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class AdminInterest extends AppCompatActivity {

    TextInputLayout slayout, olayout, addresslayout, numlayout, elayout, passlayout, cnumlayout, explayout, cvvlayout;
    EditText sname, oname, address, num, email, email2, pass, cnum, cvv;
    AutoCompleteTextView exp;
    RadioGroup gendergroup, interestgroup;
    TextView nextaction,success;
    RelativeLayout actions;
    RelativeLayout pay;
    GifImageView ngif;
    ImageView cd;
    int checked = 0;
    int checked2 = 0;
    MemberModel memberModel;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_interest);

        cd = findViewById(R.id.cd);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ctx = getApplicationContext();
        slayout = findViewById(R.id.sname_layout);
        olayout = findViewById(R.id.oname_layout);
        addresslayout = findViewById(R.id.address_layout);
        numlayout = findViewById(R.id.num_layout);
        elayout = findViewById(R.id.email_layout);
        passlayout = findViewById(R.id.pass_layout);
        cnumlayout = findViewById(R.id.cnum_layout);
        explayout = findViewById(R.id.exp_layout);
        cvvlayout = findViewById(R.id.cvv_layout);

        sname = findViewById(R.id.sname);
        oname = findViewById(R.id.oname);
        address = findViewById(R.id.address);
        num = findViewById(R.id.num);
        email = findViewById(R.id.email);
        email2 = findViewById(R.id.email2);
        pass = findViewById(R.id.password);

        actions = findViewById(R.id.actions);
        actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
        nextaction = findViewById(R.id.nextaction);
        nextaction.setText("Submit");
        ngif=findViewById(R.id.nextgif);

        gendergroup = findViewById(R.id.gender_group);
        gendergroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checked = checkedId;
            }
        });
        interestgroup = findViewById(R.id.interest_group);
        interestgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checked2 = checkedId;
            }
        });
    }

    private void checkFields() {
        String stext = sname.getText().toString();
        String otext = oname.getText().toString();
        String addrtext = address.getText().toString();
        String ntext = num.getText().toString();
        String etext = email.getText().toString();
        String ptext = pass.getText().toString();
        String gendtext;
        String interesttext;
        EditText[] editTexts = {sname, oname, address, num, email, pass};
        for (EditText e : editTexts) {
            if (e.getText().toString().isEmpty()) {
                TextInputLayout textInputLayout = (TextInputLayout) e.getParent().getParent();
                textInputLayout.setError("Don't skip this part");
                e.requestFocus();
                return;
            }
        }
        if (checked == 0) {
            Toast.makeText(ctx, "Not Filled: Select a gender", Toast.LENGTH_LONG).show();
            return;
        } else {
            RadioButton rb1 = findViewById(gendergroup.getCheckedRadioButtonId());
            gendtext = rb1.getText().toString();
        }
        if (checked2 == 0) {
            Toast.makeText(ctx, "Not Filled: Select an interest", Toast.LENGTH_LONG).show();
            return;
        } else {
            RadioButton rb2 = findViewById(interestgroup.getCheckedRadioButtonId());
            interesttext = rb2.getText().toString();
        }
        if (ptext.length() < 6) {
            Toast.makeText(ctx, "Weak password: You can do better.", Toast.LENGTH_LONG).show();
            return;
        }

        memberModel = new MemberModel(etext, stext + " " + otext, "interest", ptext, addrtext, ntext, gendtext, interesttext);
        nextaction.setText("Creating profile..");
        ngif.setVisibility(View.VISIBLE);
        uploadData();


    }

    private void uploadData() {

        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AFFMembers");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> mlist = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String key = snap.getKey();
                    mlist.add(key);
                }
                String newUid = "";
                if (mlist.size() > 0) {
                    String lkstring = mlist.get(mlist.size() - 1);
                    long lastkey = Long.parseLong(lkstring.substring(lkstring.indexOf("-") + 1, lkstring.length()));
                    newUid = "AFF-" + Long.toString(lastkey + 00001);
                } else {
                    newUid = "AFF-00001";
                }
                memberModel.setUid(newUid);
                memberModel.setPaymenttype("admin");

                dbref.child(newUid).setValue(memberModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ctx, "Congratulations, Interest submitted successfully", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            nextaction.setText("Submit");
                            ngif.setVisibility(View.GONE);
                            Toast.makeText(ctx, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                nextaction.setText("Submit");
                ngif.setVisibility(View.GONE);
                Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
