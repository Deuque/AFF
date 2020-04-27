package com.dcinspirations.aff.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.google.android.material.textfield.TextInputLayout;

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

    TextInputLayout slayout, olayout, addresslayout, numlayout, elayout, passlayout, cnumlayout, explayout,cvvlayout;
    EditText sname, oname, address, num, email, email2,pass, cnum,cvv;
    AutoCompleteTextView exp;
    RadioGroup gendergroup, interestgroup;
    TextView part1, part2, valaction;
    RelativeLayout actions;
    RelativeLayout pay;
    GifImageView ldgif;
    LinearLayout formlayout,paymentlayout;
    ImageView cd;
    int checked =0;int checked2 = 0;
    int part = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interest, container, false);

        cd = view.findViewById(R.id.cd);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
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
        email2= view.findViewById(R.id.email2);
        pass = view.findViewById(R.id.password);
        exp = view.findViewById(R.id.exp);
        exp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(exp.getText().toString().length()==2){
                    exp.setText(exp.getText().toString()+"/");
                    exp.setSelection(exp.getText().length());
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
                if(ldgif.getVisibility()!=View.VISIBLE) {
                    checkCardDetails();
                }
            }
        });
        return view;
    }

    private void checkFields(){
        String stext = sname.getText().toString();
        String otext = oname.getText().toString();
        String addrtext = address.getText().toString();
        String ntext = num.getText().toString();
        String etext = email.getText().toString();
        String ptext = pass.getText().toString();
        String gendtext;
        String interesttext;
        EditText[] editTexts = {sname,oname,address,num,email,pass};
        for(EditText e:editTexts){
            if(e.getText().toString().isEmpty()){
                TextInputLayout textInputLayout = (TextInputLayout) e.getParent().getParent();
                textInputLayout.setError("Don't skip this part");
                e.requestFocus();
                return;
            }
        }
        if(checked==0){
            Toast.makeText(getContext(), "Not Filled: Select a gender", Toast.LENGTH_LONG).show();
            return;
        }else{
            RadioButton rb1 = getView().findViewById(gendergroup.getCheckedRadioButtonId());
            gendtext = rb1.getText().toString();
        }
        if(checked2==0){
            Toast.makeText(getContext(), "Not Filled: Select an interest", Toast.LENGTH_LONG).show();
            return;
        }else{
            RadioButton rb2 = getView().findViewById(interestgroup.getCheckedRadioButtonId());
            interesttext = rb2.getText().toString();
        }
        if(ptext.length()<6){
            Toast.makeText(getContext(), "Weak password: You can do better.", Toast.LENGTH_LONG).show();
            return;
        }

        part1.setTextColor(getResources().getColor(R.color.aux5));
        part2.setTextColor(getResources().getColor(R.color.colorAccent));
        formlayout.setVisibility(View.GONE);
        paymentlayout.setVisibility(View.VISIBLE);
        part = 2;


    }

    private void checkCardDetails(){
        String etext2 = email2.getText().toString();
        String cntext = cnum.getText().toString();
        String exptext = exp.getText().toString();
        String cvvrtext = cvv.getText().toString();
        EditText[] editTexts = {email2,cnum,exp,cvv};
        for(EditText e:editTexts){
            if(e.getText().toString().isEmpty()){
                TextInputLayout textInputLayout = (TextInputLayout) e.getParent().getParent();
                textInputLayout.setError("required");
                return;
            }
        }

        ldgif.setVisibility(View.VISIBLE);
        valaction.setText("Processing..");
        String cardNumber = "4084084084084081";

        int expiryMonth = 12; //any month in the future

        int expiryYear = 2020; // any year in the future

        String cvv = "408";
        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);
//        Card card = new Card(cntext,Integer.parseInt(exptext.substring(0,exptext.indexOf("/")-1)) , Integer.parseInt(exptext.substring(exptext.indexOf("/")-1,exptext.length()-1)), cvvrtext);
        Toast.makeText(getContext(), exptext.substring(0,exptext.indexOf("/")) + " " + exptext.substring(exptext.indexOf("/"),exptext.length()), Toast.LENGTH_LONG).show();

        if(card.isValid()){
            performCharge(card,etext2);
        }else{
            Toast.makeText(getContext(), "Invalid Card", Toast.LENGTH_LONG).show();
            ldgif.setVisibility(View.GONE);
            valaction.setText("Pay NGN 1,200.00");
        }
    }

    private void performCharge(Card card, String email) {
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
                ldgif.setVisibility(View.GONE);
                valaction.setText("Pay NGN 1,200.00");
                email2.setText("");
                cnum.setText("");
                exp.setText("");
                cvv.setText("");
                String paymentReference = transaction.getReference();
                Toast.makeText(getActivity(), "Transaction Successful! payment reference: "
                        + paymentReference, Toast.LENGTH_LONG).show();
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

}
