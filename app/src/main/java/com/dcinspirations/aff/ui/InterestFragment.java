package com.dcinspirations.aff.ui;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dcinspirations.aff.MainActivity;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.Sp;
import com.dcinspirations.aff.adapters.AddressAdapter;
import com.dcinspirations.aff.models.MediaModel;
import com.dcinspirations.aff.models.MemberModel;
import com.dcinspirations.aff.models.OthersModel;
import com.dcinspirations.aff.models.OthersModel2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import pl.droidsonroids.gif.GifImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InterestFragment extends Fragment {


    public InterestFragment() {
        // Required empty public constructor
    }

    TextInputLayout slayout, olayout, addresslayout, numlayout, elayout, passlayout, cnumlayout, explayout, cvvlayout;
    EditText sname, oname, address, num, email, email2, pass, cnum, cvv;
    AutoCompleteTextView exp;
    RadioGroup gendergroup, interestgroup;
    TextView part1, part2, valaction,success;
    RelativeLayout actions,errbutton;
    RelativeLayout pay;
    GifImageView ldgif,egif;
    LinearLayout formlayout, paymentlayout;
    ImageView cd;
    int checked = 0;
    int checked2 = 0;
    int part = 1;
    MemberModel memberModel;
    Context ctx;

    ArrayList<OthersModel2> omlist;
    AddressAdapter addressAdapter;
    int e;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interest, container, false);

        omlist = new ArrayList<>();
        cd = view.findViewById(R.id.cd);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });
        ctx = view.getContext();
        slayout = view.findViewById(R.id.sname_layout);
        olayout = view.findViewById(R.id.oname_layout);
        addresslayout = view.findViewById(R.id.address_layout);
        numlayout = view.findViewById(R.id.num_layout);
        elayout = view.findViewById(R.id.email_layout);
        passlayout = view.findViewById(R.id.pass_layout);
        cnumlayout = view.findViewById(R.id.cnum_layout);
        explayout = view.findViewById(R.id.exp_layout);
        cvvlayout = view.findViewById(R.id.cvv_layout);
        formlayout = view.findViewById(R.id.form_layout);
        paymentlayout = view.findViewById(R.id.payment_layout);

        sname = view.findViewById(R.id.sname);
        oname = view.findViewById(R.id.oname);
        address = view.findViewById(R.id.address);
        num = view.findViewById(R.id.num);
        email = view.findViewById(R.id.email);
        email2 = view.findViewById(R.id.email2);
        pass = view.findViewById(R.id.password);
        exp = view.findViewById(R.id.exp);
        exp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                e = count;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>e) {
                    if (exp.getText().toString().length() == 2) {
                        exp.setText(exp.getText().toString() + "/");
                        exp.setSelection(exp.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cvv = view.findViewById(R.id.cvv);
        cnum = view.findViewById(R.id.cnum);


        gendergroup = view.findViewById(R.id.gender_group);
        gendergroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checked = checkedId;
            }
        });
        interestgroup = view.findViewById(R.id.interest_group);
        interestgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checked2 = checkedId;
            }
        });

        part1 = view.findViewById(R.id.part1);
        part1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ldgif.getVisibility()!=View.VISIBLE) {
                    part1.setTextColor(getResources().getColor(R.color.colorAccent));
                    part2.setTextColor(getResources().getColor(R.color.aux5));
                    formlayout.setVisibility(View.VISIBLE);
                    paymentlayout.setVisibility(View.GONE);
                }
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
                if (ldgif.getVisibility() != View.VISIBLE&&!valaction.getText().toString().equalsIgnoreCase("payment successful")) {
                    checkCardDetails();
                }
            }
        });

        success = view.findViewById(R.id.success);
        errbutton = view.findViewById(R.id.error_button);
        errbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(egif.getVisibility()!=View.VISIBLE) {
                    egif.setVisibility(View.VISIBLE);
                    uploadData();
                }
            }
        });
        egif = view.findViewById(R.id.egif);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addressAdapter = new AddressAdapter(ctx,omlist);
        RecyclerView lrv = view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(ctx);
        llm.setOrientation(RecyclerView.VERTICAL);
        lrv.setLayoutManager(llm);
        lrv.setAdapter(addressAdapter);
        getOtherItems();
    }

    private void getOtherItems(){

        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AffOthers").child("addresslocations");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                omlist.clear();

                        for(DataSnapshot snaps:dataSnapshot.getChildren()){
                            OthersModel2 om = snaps.getValue(OthersModel2.class);
                            omlist.add(om);
                        }
                addressAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            Toast.makeText(getContext(), "Not Filled: Select a gender", Toast.LENGTH_LONG).show();
            return;
        } else {
            RadioButton rb1 = getView().findViewById(gendergroup.getCheckedRadioButtonId());
            gendtext = rb1.getText().toString();
        }
        if (checked2 == 0) {
            Toast.makeText(getContext(), "Not Filled: Select an interest", Toast.LENGTH_LONG).show();
            return;
        } else {
            RadioButton rb2 = getView().findViewById(interestgroup.getCheckedRadioButtonId());
            interesttext = rb2.getText().toString();
        }
        if (ptext.length() < 6) {
            Toast.makeText(getContext(), "Weak password: You can do better.", Toast.LENGTH_LONG).show();
            return;
        }

        memberModel = new MemberModel(etext, stext + " " + otext, "interest", ptext, addrtext, ntext, gendtext, interesttext);
        part1.setTextColor(getResources().getColor(R.color.aux5));
        part2.setTextColor(getResources().getColor(R.color.colorAccent));
        formlayout.setVisibility(View.GONE);
        paymentlayout.setVisibility(View.VISIBLE);
        part = 2;


    }

    private void checkCardDetails() {
        String etext2 = email2.getText().toString().trim();
        String cntext = cnum.getText().toString().trim();
        String exptext = exp.getText().toString().trim();
        String cvvrtext = cvv.getText().toString().trim();
        EditText[] editTexts = {email2, cnum, exp, cvv};
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
        try {
            Card card = new Card(cntext, Integer.parseInt(exptext.substring(0, exptext.indexOf("/"))), Integer.parseInt("20"+exptext.substring(exptext.indexOf("/")+1)), cvvrtext);
//        Card card = new Card(cntext,Integer.parseInt(exptext.substring(0,exptext.indexOf("/")-1)) , Integer.parseInt(exptext.substring(exptext.indexOf("/")-1,exptext.length()-1)), cvvrtext);

            if (card.isValid()) {
                performCharge(card, etext2);
            } else {
                Toast.makeText(getContext(), "Invalid Card, Check details again", Toast.LENGTH_LONG).show();
                ldgif.setVisibility(View.GONE);
                valaction.setText("Pay NGN 1,200.00");
            }
        }catch (Exception e){
            ldgif.setVisibility(View.GONE);
            valaction.setText("Pay NGN 1,200.00");
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void performCharge(Card card, String email) {
        //create a Charge object
        Charge charge = new Charge();

        //set the card to charge
        charge.setCard(card);

        //call this method if you set a plan
        //charge.setPlan("PLN_yourplan");

        charge.setEmail(email); //dummy email address

        charge.setAmount(155000); //test amount

        PaystackSdk.chargeCard(getActivity(), charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
                valaction.setText("Uploading Data...");
                email2.setText("");
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
                valaction.setText("Pay NGN 1,200.00");

            }
        });
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
                memberModel.setPaymenttype("self");

                dbref.child(newUid).setValue(memberModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            new Sp(ctx).setLoginType(1);
                            new Sp(ctx).setUid(memberModel.getUid());
                            Toast.makeText(getContext(), "Congratulations, Interest submitted successfully", Toast.LENGTH_LONG).show();
                            ((MainActivity) getActivity()).onBackPressed();
                            ((MainActivity) getActivity()).updateData(memberModel.getUid());
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errbutton.setVisibility(View.VISIBLE);
                valaction.setText("Payment successful");
                ldgif.setVisibility(View.GONE);
                egif.setVisibility(View.GONE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
