package com.dcinspirations.aff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dcinspirations.aff.models.MemberModel;
import com.dcinspirations.aff.models.MemberModel2;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class Biodata extends AppCompatActivity {

    EditText sname, oname, address, num, email, gend, interest;
    EditText sname2, pob, lgo, so, nat, lor, loe, ms, ref;
    String id;
    TextView uid, valaction;
    Context ctx;
    MemberModel oldmember, memberModel;
    MemberModel2 oldmember2, memberModel2;
    GifImageView lgif;
    TabLayout tabs;
    RelativeLayout al, pl;
    PorterShapeImageView img;

    TextView nextaction,addimage;
    RelativeLayout update,actions;
    GifImageView ngif;

    private static final int req1 = 9;
    private static final int req2 = 56;
    Uri fileuri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata);

        ctx = getApplicationContext();
        tabs = findViewById(R.id.tabs);

        id = getIntent().getStringExtra("uid");
        sname = findViewById(R.id.sname);
        oname = findViewById(R.id.oname);
        address = findViewById(R.id.address);
        num = findViewById(R.id.num);
        email = findViewById(R.id.email);
        gend = findViewById(R.id.gend);
        interest = findViewById(R.id.interest);
        uid = findViewById(R.id.uid);

        img = findViewById(R.id.pimg);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new Sp(ctx).getLoginType()==3) {
                    checkPerm();
                }
            }
        });
        sname2 = findViewById(R.id.sname2);
        pob = findViewById(R.id.pob);
        lgo = findViewById(R.id.lgo);
        so = findViewById(R.id.so);
        nat = findViewById(R.id.nat);
        lor = findViewById(R.id.sor);
        loe = findViewById(R.id.loe);
        ms = findViewById(R.id.ms);
        ref = findViewById(R.id.ref);

        al = findViewById(R.id.al);
        pl = findViewById(R.id.pl);

        valaction = findViewById(R.id.val_action);
        lgif = findViewById(R.id.lgif);
        update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });

        addimage = findViewById(R.id.add_image);
        actions = findViewById(R.id.actions);
        nextaction = findViewById(R.id.nextaction);
        nextaction.setText("Update");
        actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields2();
            }
        });
        ngif = findViewById(R.id.nextgif);


        getData();
    }

    private void getData() {
        EditText[] editTexts = {sname, oname, address, num, email, gend, interest,
                sname2, pob, lgo, so, nat, lor, loe, ms, ref};
        int i = new Sp(this).getLoginType();
        if (i != 3) {
            for (EditText e : editTexts) {
                e.setEnabled(false);
                e.setFocusable(false);
            }

            findViewById(R.id.add).setVisibility(View.GONE);
            update.setVisibility(View.GONE);
            findViewById(R.id.add_image).setVisibility(View.GONE);
            actions.setVisibility(View.GONE);
        } else {
            email.setEnabled(false);
            email.setFocusable(false);
        }
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AFFMembers").child(id);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MemberModel mm = dataSnapshot.getValue(MemberModel.class);
                mm.setUid(id);
                oldmember = mm;
                uid.setText("userID: " + mm.getUid());
                sname.setText(mm.getName().split(" ")[0]);
                oname.setText(mm.getName().substring(mm.getName().indexOf(" "), mm.getName().length()));
                address.setText(mm.getAddress());
                num.setText(mm.getNum());
                email.setText(mm.getEmail());
                gend.setText(mm.getGender());
                interest.setText(mm.getInterest());
                if(mm.getCategory().equalsIgnoreCase("member")) {
                    dbref.child("AdditionalInfo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            oldmember2 = dataSnapshot.getValue(MemberModel2.class);
                            Glide.with(ctx)
                                    .load(oldmember2.getImgUrl())
                                    .into(img);
                            sname2.setText(oldmember2.getStage_name());
                            pob.setText(oldmember2.getPlace_birth());
                            lgo.setText(oldmember2.getLga_origin());
                            so.setText(oldmember2.getS_origin());
                            nat.setText(oldmember2.getNationality());
                            lor.setText(oldmember2.getLga_reside());
                            loe.setText(oldmember2.getEdu_level());
                            ms.setText(oldmember2.getMar_status());
                            ref.setText(oldmember2.getRef());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    tabs.setVisibility(View.VISIBLE);
                    setupTablayout();
                    findViewById(R.id.add).setVisibility(View.GONE);

                } else {
                    tabs.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void close(View view) {
        finish();
    }


    public void setupTablayout() {
        tabs = findViewById(R.id.tabs);
        tabs.removeAllTabs();
        TabLayout.Tab tab1 = tabs.newTab();
        tab1.setText("Personal Info");
        TabLayout.Tab tab2 = tabs.newTab();
        tab2.setText("Additional Info");
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabs.getTabAt(0)) {
                    al.setVisibility(View.GONE);
                    pl.setVisibility(View.VISIBLE);
                } else {
                    al.setVisibility(View.VISIBLE);
                    pl.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabs.addTab(tab2);
        tabs.addTab(tab1, 0, true);

    }

    public void launchAdd(View view) {
        startActivity(new Intent(this, AdminFulltime.class).putExtra("uid", oldmember.getUid()));
    }

    public void update(View view) {
        checkFields();
    }

    private void checkFields() {
        String stext = sname.getText().toString();
        String otext = oname.getText().toString();
        String addrtext = address.getText().toString();
        String ntext = num.getText().toString();
        String etext = email.getText().toString();
        String gendtext = gend.getText().toString();
        String interesttext = interest.getText().toString();
        EditText[] editTexts = {sname, oname, address, num, gend, interest,};
        for (EditText e : editTexts) {
            if (e.getText().toString().isEmpty()) {
                TextInputLayout textInputLayout = (TextInputLayout) e.getParent().getParent();
                textInputLayout.setError("Don't skip this part");
                e.requestFocus();
                return;
            }
        }

        memberModel = new MemberModel(etext, stext + " " + otext, oldmember.getCategory(), oldmember.getPass(), addrtext, ntext, gendtext, interesttext);
        memberModel.setUid(oldmember.getUid());
        memberModel.setPaymenttype(oldmember.getPaymenttype());
        valaction.setText("Updating");
        lgif.setVisibility(View.VISIBLE);
        uploadData();


    }

    private void uploadData() {

        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AFFMembers");

        dbref.child(oldmember.getUid()).updateChildren(parameters(memberModel)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ctx, "Updated successfully", Toast.LENGTH_LONG).show();
                    valaction.setText("Update");
                    lgif.setVisibility(View.GONE);
                } else {
                    valaction.setText("Update");
                    lgif.setVisibility(View.GONE);
                    Toast.makeText(ctx, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void checkFields2() {
        EditText[] editTexts = {sname, pob, lgo, so, nat, lor, loe, ms, ref};
        List<EditText> editTexts2 = Arrays.asList(editTexts);
        String[] adt = new String[editTexts.length];
        for (EditText e : editTexts2) {
            if (e.getText().toString().isEmpty()) {
                TextInputLayout textInputLayout = (TextInputLayout) e.getParent().getParent();
                textInputLayout.setError("Don't skip this part");
                e.requestFocus();
                return;
            } else {

                adt[editTexts2.indexOf(e)] = e.getText().toString().trim();
            }
        }

        memberModel2 = new MemberModel2(adt[0], adt[1], adt[2], adt[3], adt[4], adt[5], adt[6], adt[7], adt[8]);
        nextaction.setText("Processing..");
        ngif.setVisibility(View.VISIBLE);
        if (fileuri == null) {
            memberModel2.setImgUrl(oldmember2.getImgUrl());
            final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AFFMembers").child(id + "/AdditionalInfo");
            dbref.updateChildren(parameters(memberModel2)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ctx, "Successful", Toast.LENGTH_LONG).show();
                        nextaction.setText("Update");
                        ngif.setVisibility(View.GONE);
                    } else {
                        nextaction.setText("Update");
                        ngif.setVisibility(View.GONE);
                        Toast.makeText(ctx, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            return;
        }
        uploadData2();

    }

    private void uploadData2() {
        final String filename = System.currentTimeMillis() + "";
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("AffMembers")
                .child(filename);
        storageReference.putFile(fileuri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    memberModel2.setImgUrl(uri.toString());

                                    final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AFFMembers").child(id + "/AdditionalInfo");


                                    dbref.setValue(memberModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ctx, "Successful", Toast.LENGTH_LONG).show();
                                                nextaction.setText("Update");
                                                ngif.setVisibility(View.GONE);
                                            } else {
                                                nextaction.setText("Update");
                                                ngif.setVisibility(View.GONE);
                                                Toast.makeText(ctx, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    nextaction.setText("Submit");
                                    ngif.setVisibility(View.GONE);
                                    Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
//
//

                        } else {
                            nextaction.setText("Submit");
                            ngif.setVisibility(View.GONE);
                            Toast.makeText(ctx, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void checkPerm() {
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectFile();
        } else {
            ActivityCompat.requestPermissions(Biodata.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, req1);
            checkPerm();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == req1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectFile();
        } else {
            Toast.makeText(ctx, "You need to give permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectFile() {
        String[] mimeTypes = {"image/jpeg", "image/png"};
        final Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                .setType("image/*")
                .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, req2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == req2 && resultCode == Biodata.this.RESULT_OK && data != null) {
            fileuri = data.getData();
            Glide.with(ctx)
                    .load(fileuri)
                    .into(img);
        } else {
            Toast.makeText(ctx, "Please Select a file", Toast.LENGTH_SHORT).show();
        }
    }

    public static Map<String, Object> parameters(Object obj) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try { map.put(field.getName(), field.get(obj)); } catch (Exception e) { }
        }
        return map;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
