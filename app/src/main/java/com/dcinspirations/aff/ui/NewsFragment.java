package com.dcinspirations.aff.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dcinspirations.aff.MainActivity;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.adapters.MediaAdapter;
import com.dcinspirations.aff.adapters.NewsAdapter;
import com.dcinspirations.aff.models.MediaModel;
import com.dcinspirations.aff.models.NewsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import pl.droidsonroids.gif.GifImageView;


public class NewsFragment extends Fragment {


    public NewsFragment() {
        // Required empty public constructor
    }

    TabLayout tabs;
    FloatingActionButton fab;
    ImageView cd;
    RelativeLayout mediaupload;
    LinearLayout upload,load;
    EditText ntitle, nbody, nlink;
    TextView file;
    Button cancel;
    GifImageView ldgif;
    Context ctx;
    ArrayList<NewsModel> newslist;
    NewsAdapter newsAdapter;
    String selecttab = "general";
    String[] Datalist = {"General","Interest","Membership"};
    Spinner cat;
    ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        fab = view.findViewById(R.id.fab);
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
        ntitle = view.findViewById(R.id.ntitle);
        nbody = view.findViewById(R.id.nbody);
        nlink = view.findViewById(R.id.nlink);
        file = view.findViewById(R.id.file);
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaupload.setVisibility(View.GONE);
            }
        });
        ldgif = view.findViewById(R.id.lgif);
        cat = view.findViewById(R.id.cat);
        adapter = new ArrayAdapter<>(view.getContext(),R.layout.spinner_layout,Datalist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat.setAdapter(adapter);

        load = view.findViewById(R.id.load);
        newslist= new ArrayList<>();
        ctx = view.getContext();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        newsAdapter = new NewsAdapter(getContext(), newslist, NewsFragment.this);
        rv.setAdapter(newsAdapter);
        rv.setLayoutManager(llm);
        setupTablayout(view);
        populatePosts();

    }

    private void populatePosts() {
        newslist.clear();
        load.setVisibility(View.VISIBLE);
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AffNews");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newslist.clear();
                for (final DataSnapshot snaps : dataSnapshot.getChildren()) {
                    NewsModel mm = snaps.getValue(NewsModel.class);
                    mm.setNkey(snaps.getKey());
                    if(mm.getCategory().equalsIgnoreCase(selecttab)) {
                        newslist.add(0, mm);
                    }
                }
                newsAdapter.notifyDataSetChanged();
                load.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                load.setVisibility(View.GONE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setupTablayout(View v) {
        tabs = v.findViewById(R.id.tabs);
        TabLayout.Tab tab1 = tabs.newTab();
        tab1.setText("General");
        TabLayout.Tab tab2 = tabs.newTab();
        tab2.setText("Interest");
        TabLayout.Tab tab3 = tabs.newTab();
        tab3.setText("Membership");
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabs.getTabAt(0)) {
                    selecttab= "general";
                } else if(tab == tabs.getTabAt(1)){
                    selecttab= "interest";
                }else{
                    selecttab = "membership";
                }
                populatePosts();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab == tabs.getTabAt(0)) {
                    selecttab= "general";
                } else if(tab == tabs.getTabAt(1)){
                    selecttab= "interest";
                }else{
                    selecttab = "membership";
                }
                populatePosts();
            }
        });
        tabs.addTab(tab1, 0, true);
        tabs.addTab(tab2, 1);
        tabs.addTab(tab3, 2);
    }

    private void uploadFile() {
        final String nttext = ntitle.getText().toString().trim();
        final String nbtext = nbody.getText().toString().trim();
        final String nltext = nlink.getText().toString().trim();
        if (nttext.isEmpty()) {
            ntitle.setError("Enter title");
            ntitle.requestFocus();
            return;
        }
        if (nbtext.isEmpty()) {
            nbody.setError("Enter body");
            nbody.requestFocus();
            return;
        }
        ldgif.setVisibility(View.VISIBLE);
        NewsModel nm = new NewsModel(nttext, nbtext, nltext, getTime(),cat.getSelectedItem().toString());
        final String filename = System.currentTimeMillis() + "";
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AffNews");
        dbref.child(filename).setValue(nm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ldgif.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    mediaupload.setVisibility(View.GONE);
                    Toast.makeText(ctx, "Successful", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ctx, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public static String getTime() {
        SimpleDateFormat tf = new SimpleDateFormat("MM/yy");
        String date = tf.format(Calendar.getInstance().getTime());
        return String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + "/" + date;
    }

}
