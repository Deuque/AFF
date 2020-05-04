package com.dcinspirations.aff.ui;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dcinspirations.aff.MainActivity;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.Sp;
import com.dcinspirations.aff.adapters.ExcosAdapter2;
import com.dcinspirations.aff.adapters.SMAdapter;
import com.dcinspirations.aff.models.ExcosModel;
import com.dcinspirations.aff.models.NewsModel;
import com.dcinspirations.aff.models.OthersModel;
import com.dcinspirations.aff.models.OthersModel2;
import com.dcinspirations.aff.models.SM_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }

    ExcosAdapter2 excosAdapter;
    ArrayList<ExcosModel> excoslist;

    SMAdapter smAdapter;
    ArrayList<SM_model> smlist;
    FloatingActionButton fab;
    RelativeLayout mediaupload;
    LinearLayout upload;
    EditText platform, link;
    TextView file, empty;
    Button cancel;
    GifImageView ldgif;
    ImageView cd;
    TextView mv, cp, interest, fm;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        fab = view.findViewById(R.id.fab);
        if (new Sp(view.getContext()).getLoginType() != 3) {
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaupload.setVisibility(View.VISIBLE);
            }
        });
        cd = view.findViewById(R.id.cd);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });
        mediaupload = view.findViewById(R.id.mediaupload);
        upload = view.findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ldgif.getVisibility() != View.VISIBLE) {
                    uploadFile();
                }
            }
        });
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaupload.setVisibility(View.GONE);
            }
        });
        ldgif = view.findViewById(R.id.lgif);

        platform = view.findViewById(R.id.platform);
        link = view.findViewById(R.id.link);

        mv = view.findViewById(R.id.mv);
        cp = view.findViewById(R.id.cp);
        getOtherItems();


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        excoslist = new ArrayList<>();
        smlist = new ArrayList<>();
        RecyclerView rv = view.findViewById(R.id.rv);
        RecyclerView smrv = view.findViewById(R.id.smrv);

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(RecyclerView.HORIZONTAL);
        LinearLayoutManager llm2 = new LinearLayoutManager(view.getContext());
        llm2.setOrientation(RecyclerView.VERTICAL);

        excosAdapter = new ExcosAdapter2(view.getContext(), excoslist);
        rv.setAdapter(excosAdapter);
        rv.setLayoutManager(llm);
        populatePosts();

        smAdapter = new SMAdapter(view.getContext(), smlist);
        smrv.setAdapter(smAdapter);
        smrv.setLayoutManager(llm2);
        populatePosts2();
    }

    private void getOtherItems() {

        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AffOthers");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OthersModel om = dataSnapshot.getValue(OthersModel.class);
                try {
                    mv.setText(om.getMvtext().isEmpty() ? getResources().getString(R.string.msdefinition) : om.getMvtext());
                    cp.setText(om.getCptext().isEmpty() ? getResources().getString(R.string.cpursuit) : om.getCptext());
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populatePosts() {
        excoslist.clear();
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AffExcos");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                excoslist.clear();
                for (final DataSnapshot snaps : dataSnapshot.getChildren()) {
                    ExcosModel em = snaps.getValue(ExcosModel.class);
                    em.setKey(snaps.getKey());
                    excoslist.add(0, em);
                }
                excosAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void populatePosts2() {
        smlist.clear();
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AffSm");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                smlist.clear();
                for (final DataSnapshot snaps : dataSnapshot.getChildren()) {
                    SM_model sm = snaps.getValue(SM_model.class);
                    sm.setKey(snaps.getKey());
                    smlist.add(0, sm);
                }
                smAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void uploadFile() {
        final String ptext = platform.getText().toString().trim();
        final String ltext = link.getText().toString().trim();
        if (ptext.isEmpty()) {
            platform.setError("Enter Platform");
            platform.requestFocus();
            return;
        }
        if (ltext.isEmpty()) {
            link.setError("Enter body");
            link.requestFocus();
            return;
        }
        ldgif.setVisibility(View.VISIBLE);

        final String filename = System.currentTimeMillis() + "";
        SM_model nm = new SM_model(ptext, ltext);
        nm.setKey(filename);
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AffSm");
        dbref.child(filename).setValue(nm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ldgif.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    platform.setText("");
                    link.setText("");
                    mediaupload.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
