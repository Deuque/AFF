package com.dcinspirations.aff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dcinspirations.aff.models.MemberModel2;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.List;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import pl.droidsonroids.gif.GifImageView;

public class AdminFulltime extends AppCompatActivity {

    EditText sname, pob, lgo, so, nat, lor, loe, ms, ref, cvv, cnum, email;
    TextView nextaction;
    RelativeLayout actions, img;
    GifImageView ngif;
    MemberModel2 memberModel;

    Context ctx;
    private static final int req1 = 9;
    private static final int req2 = 56;
    Uri fileuri;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_fulltime);
        ctx = getApplicationContext();

        path = getIntent().getStringExtra("uid");

        img = findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPerm();
            }
        });
        sname = findViewById(R.id.sname2);
        pob = findViewById(R.id.pob);
        lgo = findViewById(R.id.lgo);
        so = findViewById(R.id.so);
        nat = findViewById(R.id.nat);
        lor = findViewById(R.id.sor);
        loe = findViewById(R.id.loe);
        ms = findViewById(R.id.ms);
        ref = findViewById(R.id.ref);

        actions = findViewById(R.id.actions);
        actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
        nextaction = findViewById(R.id.nextaction);
        nextaction.setText("Submit");
        ngif = findViewById(R.id.nextgif);
    }

    public void close(View view) {
        finish();
    }

    private void checkFields() {

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

        if (fileuri == null) {
            Toast.makeText(ctx, "Select a picture", Toast.LENGTH_LONG).show();
            return;
        }

        memberModel = new MemberModel2(adt[0], adt[1], adt[2], adt[3], adt[4], adt[5], adt[6], adt[7], adt[8]);
        nextaction.setText("Processing..");
        ngif.setVisibility(View.VISIBLE);
        uploadData();

    }


    private void uploadData() {
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
                                    memberModel.setImgUrl(uri.toString());

                                    final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AFFMembers").child(path);

                                    dbref.child("category").setValue("member");
                                    dbref.child("AdditionalInfo").setValue(memberModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ctx, "Successful", Toast.LENGTH_LONG).show();
                                                finish();
                                            } else {
                                                nextaction.setText("Submit");
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
            ActivityCompat.requestPermissions(AdminFulltime.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, req1);
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
        if (requestCode == req2 && resultCode == AdminFulltime.this.RESULT_OK && data != null) {
            fileuri = data.getData();
            PorterShapeImageView pimg = (PorterShapeImageView) img.getChildAt(0);
            Glide.with(ctx)
                    .load(fileuri)
                    .into(pimg);
        } else {
            Toast.makeText(ctx, "Please Select a file", Toast.LENGTH_SHORT).show();
        }
    }
}
