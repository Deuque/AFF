package com.dcinspirations.aff.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dcinspirations.aff.LoginClass;
import com.dcinspirations.aff.MainActivity;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.adapters.SliderAdapter;
import com.dcinspirations.aff.models.SliderModel;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    ArrayList<SliderModel> sliderlist;
    private HomeViewModel homeViewModel;
    private int[] imglist = {R.mipmap.car3, R.mipmap.car1, R.mipmap.car2,R.mipmap.car5,R.mipmap.car4};
    private String[] textlist = {"Entertainment at its peak",
            "Vibe to our streams round the clock.",
            "Latest jams from around the globe",
            "Up to date articles on amazing topics",
            "Become a member, enjoy benefits"};
    ImageView od;
    NestedScrollView nestedScrollView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        sliderlist = new ArrayList<>();
        for (int i = 0; i < imglist.length; i++) {
            SliderModel sm = new SliderModel(textlist[i], imglist[i]);
            sliderlist.add(sm);
        }
        final SliderView sliderView = root.findViewById(R.id.imageSlider);

        SliderAdapter adapter = new SliderAdapter(getContext(), sliderlist,0);

        sliderView.setSliderAdapter(adapter);

//        sliderView.setIndicatorAnimation(IndicatorAnimations.FILL); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setScrollTimeInSec(7); //set scroll delay in seconds :

        sliderView.startAutoCycle();
        nestedScrollView = nestedScrollView = root.findViewById(R.id.mscroll);
        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY >= 500){
                    sliderView.stopAutoCycle();
                }else{
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

        return root;
    }

    @Override
    public void onClick(View v) {

    }

  }