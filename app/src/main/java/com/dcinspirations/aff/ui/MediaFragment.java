package com.dcinspirations.aff.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dcinspirations.aff.MainActivity;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.Sp;
import com.dcinspirations.aff.adapters.MediaAdapter;
import com.dcinspirations.aff.adapters.SliderAdapter;
import com.dcinspirations.aff.models.MediaModel;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 */
public class MediaFragment extends Fragment {
    public MediaFragment() {
        // Required empty public constructor
    }

    TabLayout tabs;
    private static final int req1 = 9;
    private static final int req2 = 56;
    Context ctx;
    Uri fileuri;
    String details;
    String type;
    FloatingActionButton fab,fab2;
    ImageView cd,cd2;
    RelativeLayout mediaupload,mm,slider;
    LinearLayout upload;
    EditText aname, sname;
    TextView file,empty;
    PorterShapeImageView file2;
    Button cancel;
    GifImageView ldgif,ldgif2;
    MediaAdapter mediaAdapter;
    ArrayList<MediaModel> medialist;
    LinearLayout load;
    String selecttab="audio";
    SliderView sliderView;
    SliderAdapter adapter;
    String[] Datalist = {"Normal","Trending"};
    Spinner cat;
    ArrayAdapter arradapter;
    View modalview;


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media, container, false);

        modalview = view.findViewById(R.id.view);
        modalview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaupload.setVisibility(View.GONE);
            }
        });
        mm = view.findViewById(R.id.mediamain);
        slider = view.findViewById(R.id.slider);
        sliderView = view.findViewById(R.id.imageSlider);
        fab = view.findViewById(R.id.fab);
        if(new Sp(view.getContext()).getLoginType()!=3){
            fab.setVisibility(View.GONE);
        }
//        fab2 = view.findViewById(R.id.fab2);
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadImageSlide(0);
//            }
//        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPerm();
            }
        });
        cd = view.findViewById(R.id.cd);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });
        cd2 = view.findViewById(R.id.cd2);
        cd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImageSlide();
            }
        });

        mediaupload = view.findViewById(R.id.mediaupload);
        upload = view.findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ldgif.getVisibility()!=View.VISIBLE) {
                    uploadFile();
                }
            }
        });
        aname = view.findViewById(R.id.aname);
        sname = view.findViewById(R.id.sname);
        file = view.findViewById(R.id.file);
        file2 = view.findViewById(R.id.file2);
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaupload.setVisibility(View.GONE);
            }
        });
        ldgif = view.findViewById(R.id.lgif);

        medialist = new ArrayList<>();
        load = view.findViewById(R.id.load);
        ldgif2 = view.findViewById(R.id.lgif2);
        empty = view.findViewById(R.id.empty);

        cat = view.findViewById(R.id.cat);
        arradapter = new ArrayAdapter<>(view.getContext(),R.layout.spinner_layout,Datalist);
        arradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat.setAdapter(arradapter);
        ctx = view.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rv);
        GridLayoutManager glm = new GridLayoutManager(getContext(), 3);
        mediaAdapter = new MediaAdapter(getContext(), medialist,MediaFragment.this);
        rv.setAdapter(mediaAdapter);
        rv.setLayoutManager(glm);
        setupTablayout(view);
        populatePosts();

    }

    public void loadImageSlide(int index){
        mm.setVisibility(View.GONE);
        slider.setVisibility(View.VISIBLE);

        adapter = new SliderAdapter(getContext(), medialist,1,null);
        sliderView.setSliderAdapter(adapter);
        adapter.notifyDataSetChanged();
//        sliderView.setIndicatorAnimation(IndicatorAnimations.FILL); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setCurrentPagePosition(index);

    }
    public void removeImageSlide(){
        mm.setVisibility(View.VISIBLE);
        slider.setVisibility(View.GONE);
    }

    private void populatePosts() {
        medialist.clear();
        load.setVisibility(View.VISIBLE);
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AffMedia");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medialist.clear();
                for (final DataSnapshot snaps : dataSnapshot.getChildren()) {
                    MediaModel mm = snaps.getValue(MediaModel.class);
                    mm.setKey(snaps.getKey());
                    if(mm.getType().equalsIgnoreCase(selecttab)) {
                        medialist.add(0, mm);
                    }
                }
                mediaAdapter.notifyDataSetChanged();
                if(medialist.isEmpty()){
                    ldgif2.setVisibility(View.GONE);
                    empty.setText("No news here");
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



    public void setupTablayout(View v) {
        tabs = v.findViewById(R.id.tabs);
        TabLayout.Tab tab1 = tabs.newTab();
        tab1.setText("Music");
        TabLayout.Tab tab2 = tabs.newTab();
        tab2.setText("Gallery");
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabs.getTabAt(0)) {
                    selecttab= "audio";
//                    fab2.setVisibility(View.GONE);
                } else {
                    selecttab= "image";
//                    fab2.setVisibility(View.VISIBLE);
                }
                populatePosts();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab == tabs.getTabAt(0)) {
                    selecttab= "audio";
//                    fab2.setVisibility(View.GONE);
                } else {
                    selecttab= "image";
//                    fab2.setVisibility(View.VISIBLE);
                }
                populatePosts();
            }
        });
        tabs.addTab(tab2);
        tabs.addTab(tab1, 0, true);

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
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"image/*", "audio/*"};
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, req2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == req2 && resultCode == getActivity().RESULT_OK && data != null) {
            fileuri = data.getData();

            Toast.makeText(ctx, fileuri.getPath(), Toast.LENGTH_LONG).show();
            if (fileuri.toString().contains("image")||fileuri.getPath().contains("jpg")||fileuri.getPath().contains("png")) {
                type = "image";
                file2.setVisibility(View.VISIBLE);
                file.setVisibility(View.GONE);
                aname.setVisibility(View.GONE);
                sname.setVisibility(View.GONE);
//                Picasso.get()
//                        .load(fileuri)
//                        .resize(80,70)
//                        .centerCrop()
//                        .into(file2);
                Glide.with(ctx)
                        .load(fileuri)
                        .override(100,100)
//                        .thumbnail(.05f)
                        .placeholder(R.drawable.image)
                        .into(this.file2);
            } else if (fileuri.toString().contains("audio")||fileuri.getPath().contains("mp3")) {
                details = getFileDetails(fileuri.getPath());
                type = "audio";
                file2.setVisibility(View.GONE);
                file.setVisibility(View.VISIBLE);
                aname.setVisibility(View.VISIBLE);
                sname.setVisibility(View.VISIBLE);
                file.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.music2, 0, 0, 0);
            }
            file.setText(details);
            mediaupload.setVisibility(View.VISIBLE);

//            saveFile(fileuri);
        } else {
            Toast.makeText(ctx, "Please Select a file", Toast.LENGTH_SHORT).show();
        }
    }


    public static String getFileDetails(String string) {

        String details[] = new String[2];
        String array[] = string.split("/");
        String name = array[array.length - 1].trim();
        if (name.contains(":")) {
            details[0] = name.substring(name.indexOf(":") + 1, name.indexOf("."));
        } else {
            details[0] = name.substring(0, name.lastIndexOf("."));
        }


        String ext = name.substring(name.indexOf(".") + 1, name.length());
        details[1] = ext;
        return details[0] + "." + details[1];
    }

    private void uploadFile() {
        final String atext;
        final String stext;
        final String scat;
        if(type.equalsIgnoreCase("audio")) {
            atext= aname.getText().toString().trim();
             stext= sname.getText().toString().trim();
            if (atext.isEmpty()) {
                aname.setError("Enter Artiste");
                aname.requestFocus();
                return;
            }
            if (stext.isEmpty()) {
                sname.setError("Enter Song");
                sname.requestFocus();
                return;
            }
        }else{
            atext = "";
            stext = "";
        }
        scat = cat.getSelectedItem().toString();
        ldgif.setVisibility(View.VISIBLE);

        final String filename = System.currentTimeMillis() + "";
        final MediaModel mediaModel = new MediaModel("", atext, stext, type,scat);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("AffMedia")
                .child(filename);
        storageReference.putFile(fileuri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {

                                    mediaModel.setImgUrl(uri.toString());
                                    DatabaseReference reference = FirebaseDatabase.getInstance()
                                            .getReference().child("AffMedia");
                                    reference.child(filename).setValue(mediaModel)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    ldgif.setVisibility(View.GONE);
                                                    if (!task.isSuccessful()) {
                                                        Toast.makeText(ctx, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    } else {
                                                        mediaupload.setVisibility(View.GONE);
                                                        Toast.makeText(ctx, "Media added successfully", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    ldgif.setVisibility(View.GONE);
                                    Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
//
//

                        } else {
                            ldgif.setVisibility(View.GONE);
                            Toast.makeText(ctx, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

}
