package com.dcinspirations.aff.ui;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.dcinspirations.aff.MainActivity;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.Sp;
import com.dcinspirations.aff.models.MemberModel;
import com.dcinspirations.aff.models.MemberModel2;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.remita.paymentsdk.core.RemitaInlinePaymentSDK;
import com.remita.paymentsdk.util.RIPGateway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import pl.droidsonroids.gif.GifImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FullTimeFragment extends Fragment {


    public FullTimeFragment() {
        // Required empty public constructor
    }


    EditText sname, pob, lgo, so, nat, lor, loe, ms, ref, cvv, cnum, email;
    AutoCompleteTextView exp;
    RadioGroup gendergroup, interestgroup;
    TextView part1, part2, valaction, success;
    RelativeLayout actions, errbutton, img;
    RelativeLayout pay;
    GifImageView ldgif, egif;
    LinearLayout formlayout, paymentlayout;
    ImageView cd;
    int checked = 0;
    int checked2 = 0;
    int part = 1;
    MemberModel2 memberModel;
    Context ctx;
    private static final int req1 = 9;
    private static final int req2 = 56;
    Uri fileuri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fulltime, container, false);

        cd = view.findViewById(R.id.cd);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });
        ctx = view.getContext();
        img = view.findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPerm();
            }
        });
        formlayout = view.findViewById(R.id.form_layout);
        paymentlayout = view.findViewById(R.id.payment_layout);

        sname = view.findViewById(R.id.sname2);
        pob = view.findViewById(R.id.pob);
        lgo = view.findViewById(R.id.lgo);
        so = view.findViewById(R.id.so);
        nat = view.findViewById(R.id.nat);
        lor = view.findViewById(R.id.sor);
        loe = view.findViewById(R.id.loe);
        ms = view.findViewById(R.id.ms);
        ref = view.findViewById(R.id.ref);
        email = view.findViewById(R.id.email2);
        exp = view.findViewById(R.id.exp);
        exp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (exp.getText().toString().length() == 2) {
                    exp.setText(exp.getText().toString() + "/");
                    exp.setSelection(exp.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cvv = view.findViewById(R.id.cvv);
        cnum = view.findViewById(R.id.cnum);


        part1 = view.findViewById(R.id.part1);
        part1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part1.setTextColor(getResources().getColor(R.color.colorAccent));
                part2.setTextColor(getResources().getColor(R.color.aux5));
                formlayout.setVisibility(View.VISIBLE);
                paymentlayout.setVisibility(View.GONE);
            }
        });
        part2 = view.findViewById(R.id.part2);
        actions = view.findViewById(R.id.actions);
        actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });

        valaction = view.findViewById(R.id.val_action);
        ldgif = view.findViewById(R.id.lgif);
        pay = view.findViewById(R.id.pay_button);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ldgif.getVisibility() != View.VISIBLE && !valaction.getText().toString().equalsIgnoreCase("payment successful")) {
                    checkCardDetails();
                }
            }
        });

        success = view.findViewById(R.id.success);
        errbutton = view.findViewById(R.id.error_button);
        errbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (egif.getVisibility() != View.VISIBLE) {
                    egif.setVisibility(View.VISIBLE);
                    uploadData();
                }
            }
        });
        egif = view.findViewById(R.id.egif);
        return view;
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
        part1.setTextColor(getResources().getColor(R.color.aux5));
        part2.setTextColor(getResources().getColor(R.color.colorAccent));
        formlayout.setVisibility(View.GONE);
        paymentlayout.setVisibility(View.VISIBLE);
        part = 2;

    }

    private void checkCardDetails() {
        String etext2 = email.getText().toString();
        String cntext = cnum.getText().toString();
        String exptext = exp.getText().toString();
        String cvvrtext = cvv.getText().toString();
        EditText[] editTexts = {email, cnum, exp, cvv};
        for (EditText e : editTexts) {
            if (e.getText().toString().isEmpty()) {
                TextInputLayout textInputLayout = (TextInputLayout) e.getParent().getParent();
                textInputLayout.setError("required");
                return;
            }
        }

        ldgif.setVisibility(View.VISIBLE);
        valaction.setText("Processing Payment..");
        String cardNumber = "4084084084084081";

        int expiryMonth = 12; //any month in the future

        int expiryYear = 2020; // any year in the future

        String cvv = "408";
        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);
//        Card card = new Card(cntext,Integer.parseInt(exptext.substring(0,exptext.indexOf("/")-1)) , Integer.parseInt(exptext.substring(exptext.indexOf("/")-1,exptext.length()-1)), cvvrtext);
        Toast.makeText(getContext(), exptext.substring(0, exptext.indexOf("/")) + " " + exptext.substring(exptext.indexOf("/"), exptext.length()), Toast.LENGTH_LONG).show();

        if (card.isValid()) {
            performCharge(card, etext2);
        } else {
            Toast.makeText(getContext(), "Invalid Card", Toast.LENGTH_LONG).show();
            ldgif.setVisibility(View.GONE);
            valaction.setText("Pay NGN 10,000.00");
        }
    }

    private void performCharge(Card card, final String emailtext) {
        //create a Charge object
        Charge charge = new Charge();

        //set the card to charge
        charge.setCard(card);

        //call this method if you set a plan
        //charge.setPlan("PLN_yourplan");

        charge.setEmail("mytestemail@test.com"); //dummy email address

        charge.setAmount(100); //test amount

        PaystackSdk.chargeCard(getActivity(), charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
                valaction.setText("Uploading Data...");
                email.setText("");
                cnum.setText("");
                exp.setText("");
                cvv.setText("");
                success.setVisibility(View.VISIBLE);
                String paymentReference = transaction.getReference();
                Toast.makeText(getActivity(), "Transaction Successful! payment reference: "
                        + paymentReference, Toast.LENGTH_LONG).show();
                uploadData();
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP.
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server.
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                ldgif.setVisibility(View.GONE);
                valaction.setText("Pay NGN 10,000.00");

            }
        });
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
                                    final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AFFMembers").child(new Sp(ctx).getUid());

                                    dbref.child("category").setValue("member");
                                    dbref.child("AdditionalInfo").setValue(memberModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                new Sp(ctx).setLoginType(2);
                                                Toast.makeText(getContext(), "Congratulations, You're now a full member.", Toast.LENGTH_LONG).show();
                                                ((MainActivity) getActivity()).onBackPressed();
                                            } else {
                                                errbutton.setVisibility(View.VISIBLE);
                                                valaction.setText("Payment successful");
                                                ldgif.setVisibility(View.GONE);
                                                egif.setVisibility(View.GONE);
                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    errbutton.setVisibility(View.VISIBLE);
                                    valaction.setText("Payment successful");
                                    ldgif.setVisibility(View.GONE);
                                    egif.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
//
//

                        } else {
                            errbutton.setVisibility(View.VISIBLE);
                            valaction.setText("Payment successful");
                            ldgif.setVisibility(View.GONE);
                            egif.setVisibility(View.GONE);
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void checkPerm() {
        if (ContextCompat.checkSelfPermission(getView().getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectFile();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, req1);
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
        startActivityForResult(intent,req2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == req2 && resultCode == getActivity().RESULT_OK && data != null) {
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
