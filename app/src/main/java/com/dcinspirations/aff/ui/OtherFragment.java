package com.dcinspirations.aff.ui;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dcinspirations.aff.MainActivity;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.adapters.AddressAdapter;
import com.dcinspirations.aff.models.ExcosModel;
import com.dcinspirations.aff.models.OthersModel;
import com.dcinspirations.aff.models.OthersModel2;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherFragment extends Fragment {

    Button  editform,adloc;
    ImageView cd;

    RelativeLayout fupload,aupload;
    Spinner cat;
    EditText formtext,address;
    LinearLayout upload,upload2;
    GifImageView lgif1,lgif2;
    Button cancel1,cancel2;
    PorterShapeImageView file;
    OthersModel othersModel;
    ArrayList<OthersModel2> alocationlist;
    String catitems[] = {"Mission and vision","Career Pursuit","Interest Form","Membership Form"};
    String formitems[]= new String[4];
    ArrayAdapter adapter;
    AddressAdapter addressAdapter;
    int selected = 0;
    Uri fileuri;
    Context ctx;
    View modalview;

    private static final int req1 = 9;
    private static final int req2 = 56;

    public OtherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        ctx = view.getContext();
        modalview = view.findViewById(R.id.view);
        modalview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aupload.setVisibility(View.GONE);
            }
        });

        cd = view.findViewById(R.id.cd);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aupload.getVisibility()!=View.VISIBLE&&fupload.getVisibility()!=View.VISIBLE) {
                    ((MainActivity) getActivity()).onBackPressed();
                }else{
                    fupload.setVisibility(View.GONE);
                    aupload.setVisibility(View.GONE);
                }
            }
        });

        editform = view.findViewById(R.id.editform);
        editform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fupload.setVisibility(View.VISIBLE);
            }
        });
        adloc = view.findViewById(R.id.addloc);
        adloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPerm();
            }
        });
        fupload = view.findViewById(R.id.formupload);
        cat = view.findViewById(R.id.cat);
        adapter = new ArrayAdapter<>(view.getContext(),R.layout.spinner_layout,catitems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat.setAdapter(adapter);
        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formtext.setText(formitems[position]);
                selected=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        formtext = view.findViewById(R.id.formtext);
        upload2 = view.findViewById(R.id.upload2);
        upload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lgif2.getVisibility()!=View.VISIBLE){
                    uploadForm2();
                }
            }
        });
        lgif2 = view.findViewById(R.id.lgif2);
        cancel2 = view.findViewById(R.id.cancel2);
        cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fupload.setVisibility(View.GONE);
            }
        });

        aupload = view.findViewById(R.id.addrupload);
        address = view.findViewById(R.id.aname);
        file = view.findViewById(R.id.file);
        upload = view.findViewById(R.id.upload1);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
        lgif1 = view.findViewById(R.id.lgif1);
        cancel1 = view.findViewById(R.id.cancel1);
        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aupload.setVisibility(View.GONE);
            }
        });
        alocationlist = new ArrayList<>();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addressAdapter = new AddressAdapter(ctx,alocationlist);
        RecyclerView lrv = view.findViewById(R.id.locrv);
        LinearLayoutManager llm = new LinearLayoutManager(ctx);
        llm.setOrientation(RecyclerView.VERTICAL);
        lrv.setLayoutManager(llm);
        lrv.setAdapter(addressAdapter);

        getOtherItems();
    }

    private void getOtherItems(){

        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("AffOthers");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                alocationlist.clear();
                othersModel = dataSnapshot.getValue(OthersModel.class);
                try{
                    formitems[0] = othersModel.getMvtext();
                    formitems[1] = othersModel.getCptext();
                    formitems[2] = othersModel.getInteresttext();
                    formitems[3] = othersModel.getMembertext();
                }catch (Exception e){

                }

                dbref.child("addresslocations").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snaps:dataSnapshot.getChildren()){
                            OthersModel2 om = snaps.getValue(OthersModel2.class);
                            alocationlist.add(om);
                        }
                        addressAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadForm2(){
        formitems[selected] = formtext.getText().toString();
        OthersModel om = new OthersModel(formitems[0],formitems[1],formitems[2],formitems[3]);
        lgif2.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("AffOthers").updateChildren(parameters(om)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    lgif2.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Successful", Toast.LENGTH_LONG).show();
                }     else{
                    lgif2.setVisibility(View.GONE);
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static Map<String, Object> parameters(Object obj) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try { map.put(field.getName(), field.get(obj)); } catch (Exception e) { }
        }
        return map;
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
        String[] mimeTypes = {"image/*"};
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, req2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == req2 && resultCode == getActivity().RESULT_OK && data != null) {
            fileuri = data.getData();
//            Picasso.get()
//                    .load(fileuri)
//                    .resize(80,70)
//                    .centerCrop()
//                    .into(file);
            Glide.with(ctx)
                    .load(fileuri)
                    .into(file);
            aupload.setVisibility(View.VISIBLE);

//            saveFile(fileuri);
        } else {
            Toast.makeText(ctx, "Please Select a file", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile() {
        final String atext = address.getText().toString().trim();
        if (atext.isEmpty()) {
            address.setError("Enter Address");
            address.requestFocus();
            return;
        }
        lgif1.setVisibility(View.VISIBLE);

        final String filename = System.currentTimeMillis() + "";
        final OthersModel2 om2 = new OthersModel2(atext,filename);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("AffOthers")
                .child(filename);
        storageReference.putFile(fileuri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    om2.setImgUrl(uri.toString());
                                    DatabaseReference reference = FirebaseDatabase.getInstance()
                                            .getReference().child("AffOthers").child("addresslocations");
                                    reference.child(filename).setValue(om2)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    lgif1.setVisibility(View.GONE);
                                                    if (!task.isSuccessful()) {
                                                        Toast.makeText(ctx, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    } else {
                                                        aupload.setVisibility(View.GONE);
                                                        Toast.makeText(ctx, "Address added successfully", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    lgif1.setVisibility(View.GONE);
                                    Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
//
//

                        } else {
                            lgif1.setVisibility(View.GONE);
                            Toast.makeText(ctx, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

}
