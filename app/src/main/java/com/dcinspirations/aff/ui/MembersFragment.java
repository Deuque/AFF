package com.dcinspirations.aff.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcinspirations.aff.R;
import com.google.android.material.tabs.TabLayout;

public class MembersFragment extends Fragment {
    public MembersFragment() {
        // Required empty public constructor
    }

    TabLayout tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_members, container, false);
        setupTablayout(view);
        return view;
    }

    public void setupTablayout(View v) {
        tabs = v.findViewById(R.id.tabs);
        TabLayout.Tab tab1 = tabs.newTab();
        tab1.setText("Interested");
        TabLayout.Tab tab2 = tabs.newTab();
        tab2.setText("Full Time");
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabs.getTabAt(0)) {
                } else {
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabs.addTab(tab1, 0, true);
        tabs.addTab(tab2, 1);


    }
}

