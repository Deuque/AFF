package com.dcinspirations.aff.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dcinspirations.aff.MainActivity;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.Sp;
import com.dcinspirations.aff.adapters.ExcosAdapter2;
import com.dcinspirations.aff.adapters.MediaAdapter2;
import com.dcinspirations.aff.adapters.SliderAdapter;
import com.dcinspirations.aff.models.ExcosModel;
import com.dcinspirations.aff.models.MediaModel;
import com.dcinspirations.aff.models.OthersModel;
import com.dcinspirations.aff.models.OthersModel2;
import com.dcinspirations.aff.models.SliderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {

    ArrayList<SliderModel> sliderlist;
    private int[] imglist = {R.mipmap.car3, R.mipmap.car1, R.mipmap.car2, R.mipmap.car5, R.mipmap.car4};
    private String[] textlist = {"Entertainment at its peak",
            "Vibe to our streams round the clock.",
            "Latest jams from around the globe",
            "Up to date articles on amazing topics",
            "Become a member, enjoy benefits"};
    ImageView od;
    NestedScrollView nestedScrollView;
    ExcosAdapter2 excosAdapter;
    ArrayList<ExcosModel> excoslist;
    RelativeLayout lc, ilayout, mlayout;

    MediaAdapter2 mediaAdapter;
    ArrayList<MediaModel> medialist;

    public SliderView sliderView;
    SliderAdapter adapter;

    Context ctx;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> filearray;
    AutoCompleteTextView search;

    TextView mv, cp, interest, fm;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        ctx = root.getContext();
        excoslist = new ArrayList<>();
        sliderlist = new ArrayList<>();
        medialist = new ArrayList<>();
        filearray = new ArrayList<>();

        root.findViewById(R.id.mainLayout).requestFocus();

        sliderView = root.findViewById(R.id.imageSlider);
        adapter = new SliderAdapter(getContext(), sliderlist, 0, HomeFragment.this);
        sliderView.setSliderAdapter(adapter);
        sliderView.startAutoCycle();
        getAds();

//        sliderView.setIndicatorAnimation(IndicatorAnimations.FILL); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setScrollTimeInSec(7); //set scroll delay in seconds :
        sliderView.setCurrentPageListener(new SliderView.OnSliderPageListener() {
            @Override
            public void onSliderPageChanged(int position) {
//                if (sliderlist.get(position).getCategory().equalsIgnoreCase("video")) {
//
//                } else {
//
//                }
            }
        });


        nestedScrollView = nestedScrollView = root.findViewById(R.id.mscroll);
        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= 500) {
                    sliderView.stopAutoCycle();
                } else {
                    sliderView.startAutoCycle();

                }
            }
        });


        od = root.findViewById(R.id.od);
        od.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).drawer.openDrawer(GravityCompat.END);
            }
        });

        lc = root.findViewById(R.id.later_contents);
        ilayout = root.findViewById(R.id.ilayout);
        mlayout = root.findViewById(R.id.mlayout);

        search = root.findViewById(R.id.search);
        getSearchItems();


        mv = root.findViewById(R.id.mv);
        cp = root.findViewById(R.id.cp);
        interest = root.findViewById(R.id.interest);
        fm = root.findViewById(R.id.fm);
        getOtherItems();
        return root;
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
                    interest.setText(om.getInteresttext().isEmpty() ? getResources().getString(R.string.interest_form) : om.getInteresttext());
                    fm.setText(om.getMembertext().isEmpty() ? getResources().getString(R.string.member_form) : om.getMembertext());

                } catch (Exception e) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setAutoComplete() {
        Collections.sort(filearray);
        arrayAdapter = new ArrayAdapter<String>(ctx, R.layout.autocomplete, filearray.toArray(new String[filearray.size()]));
        search.setAdapter(arrayAdapter);
        search.setDropDownBackgroundResource(R.color.mytransparent);
        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = search.getText().toString();
                Navigation.findNavController(((MainActivity) getActivity()).findViewById(R.id.nav_host_fragment)).navigate(R.id.nav_media);
            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    Navigation.findNavController(((MainActivity) getActivity()).findViewById(R.id.nav_host_fragment)).navigate(R.id.nav_media);
                }
                return false;
            }
        });
    }

    private void getSearchItems() {
        FirebaseDatabase.getInstance().getReference().child("AffMedia").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                filearray.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MediaModel mm = snapshot.getValue(MediaModel.class);
                    if (mm.getType().equalsIgnoreCase("audio")) {

                        filearray.add(mm.getSong() + "-" + mm.getArtist());
                        filearray.add(mm.getArtist() + "-" + mm.getSong());

                    }
                    setAutoComplete();
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAds() {
        sliderlist.clear();
        FirebaseDatabase.getInstance().getReference().child("AffAds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sliderlist.clear();
                for (DataSnapshot snaps : dataSnapshot.getChildren()) {
                    SliderModel sm = snaps.getValue(SliderModel.class);
                    sliderlist.add(sm);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rv);
        RecyclerView trv = view.findViewById(R.id.trv);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(RecyclerView.HORIZONTAL);
        LinearLayoutManager llm2 = new LinearLayoutManager(view.getContext());
        llm2.setOrientation(RecyclerView.HORIZONTAL);
        excosAdapter = new ExcosAdapter2(view.getContext(), excoslist);
        rv.setAdapter(excosAdapter);
        rv.setLayoutManager(llm);

        mediaAdapter = new MediaAdapter2(view.getContext(), medialist);
        trv.setAdapter(mediaAdapter);
        trv.setLayoutManager(llm2);
        populatePosts();
        populatePosts2();
    }

    private void populatePosts2() {
        medialist.clear();
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AffMedia");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medialist.clear();
                for (final DataSnapshot snaps : dataSnapshot.getChildren()) {
                    MediaModel mm = snaps.getValue(MediaModel.class);
                    mm.setKey(snaps.getKey());
                    if (mm.getCategory().equalsIgnoreCase("trending")) {
                        medialist.add(0, mm);
                    }
                }
                mediaAdapter.notifyDataSetChanged();

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

    @Override
    public void onResume() {
        super.onResume();
        int i = new Sp(getContext()).getLoginType();

        if (i == 1) {
            lc.setVisibility(View.GONE);
            mlayout.setVisibility(View.GONE);
            ilayout.setVisibility(View.VISIBLE);
        } else if (i == 2) {
            lc.setVisibility(View.GONE);
            mlayout.setVisibility(View.VISIBLE);
            ilayout.setVisibility(View.GONE);
        } else {
            lc.setVisibility(View.VISIBLE);
            mlayout.setVisibility(View.GONE);
            ilayout.setVisibility(View.GONE);
        }
    }
}