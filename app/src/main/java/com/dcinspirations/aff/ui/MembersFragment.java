package com.dcinspirations.aff.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dcinspirations.aff.AdminInterest;
import com.dcinspirations.aff.MainActivity;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.adapters.MembersAdapter;
import com.dcinspirations.aff.adapters.NewsAdapter;
import com.dcinspirations.aff.models.MemberModel;
import com.dcinspirations.aff.models.NewsModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class MembersFragment extends Fragment {
    public MembersFragment() {
        // Required empty public constructor
    }

    TabLayout tabs;
    GifImageView ldgif;
    Context ctx;
    ArrayList<MemberModel> memberslist;
    MembersAdapter membersAdapter;
    TextView empty,admin,self;
    LinearLayout load;
    String selecttab;
    ImageView cd,add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_members, container, false);
        ctx = view.getContext();
        cd = view.findViewById(R.id.cd);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });
        add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, AdminInterest.class));
            }
        });


        tabs = view.findViewById(R.id.tabs);
        ldgif = view.findViewById(R.id.ldgif2);
        empty = view.findViewById(R.id.empty);
        load = view.findViewById(R.id.load);
        memberslist= new ArrayList<>();

        admin = view.findViewById(R.id.admin);
        self = view.findViewById(R.id.self);

        return view;
    }

    private void getCount(){

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        membersAdapter = new MembersAdapter(getContext(), memberslist);
        rv.setAdapter(membersAdapter);
        rv.setLayoutManager(llm);
        setupTablayout();
        populatePosts();

    }

    private void populatePosts() {

        memberslist.clear();
        load.setVisibility(View.VISIBLE);
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AFFMembers");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                memberslist.clear();
                int a = 0;
                int s = 0;
                for (final DataSnapshot snaps : dataSnapshot.getChildren()) {
                    MemberModel mm = snaps.getValue(MemberModel.class);
                    mm.setUid(snaps.getKey());
                    if(mm.getCategory().equalsIgnoreCase(selecttab)) {
                        memberslist.add(0, mm);
                    }
                    if(mm.getPaymenttype().equalsIgnoreCase("self")){
                        s = s+1;
                    }else{
                        a = a+1;
                    }
                }
                self.setText("self: "+Integer.toString(s));
                admin.setText("admin: "+Integer.toString(a));


                membersAdapter.notifyDataSetChanged();
                if(memberslist.isEmpty()){
                    ldgif.setVisibility(View.GONE);
                    empty.setText("No members yet");
                }else {
                    load.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                load.setVisibility(View.GONE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setupTablayout() {
        TabLayout.Tab tab1 = tabs.newTab();
        tab1.setText("Interested");
        TabLayout.Tab tab2 = tabs.newTab();
        tab2.setText("Full Time");
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab == tabs.getTabAt(0)) {
                    selecttab="interest";
                } else {
                    selecttab="member";
                }
                populatePosts();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab == tabs.getTabAt(0)) {
                    selecttab="interest";
                } else {
                    selecttab="member";
                }
                populatePosts();
            }
        });
        tabs.addTab(tab2);
        tabs.addTab(tab1, 0,true);


    }
}

