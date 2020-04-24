package com.dcinspirations.aff.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.dcinspirations.aff.adapters.ExcosAdapter;
import com.dcinspirations.aff.adapters.MediaAdapter;
import com.dcinspirations.aff.models.ExcosModel;
import com.dcinspirations.aff.models.MediaModel;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.EmptyStackException;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 */
public class ExcosFragment extends Fragment {
    public ExcosFragment() {
        // Required empty public constructor
    }

    TabLayout tabs;
    private static final int req1 = 9;
    private static final int req2 = 56;
    Context ctx;
    Uri fileuri;
    String details;
    String type;
    FloatingActionButton fab;
    ImageView cd;
    RelativeLayout mediaupload;
    LinearLayout upload;
    EditText ename, eposition;
    PorterShapeImageView file;
    Button cancel;
    GifImageView ldgif;
    LinearLayout load;
    ExcosAdapter excosAdapter;
    ArrayList<ExcosModel> excoslist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_excos, container, false);

        fab = view.findViewById(R.id.fab);
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
        ename = view.findViewById(R.id.ename);
        eposition = view.findViewById(R.id.eposition);
        file = view.findViewById(R.id.file);
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaupload.setVisibility(View.GONE);
            }
        });
        ldgif = view.findViewById(R.id.lgif);

        load = view.findViewById(R.id.load);
        excoslist = new ArrayList<>();
        ctx = view.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rv);
        GridLayoutManager glm = new GridLayoutManager(getContext(), 3);
        excosAdapter = new ExcosAdapter(getContext(), excoslist, ExcosFragment.this);
        rv.setAdapter(excosAdapter);
        rv.setLayoutManager(glm);
        populatePosts();
    }

    private void populatePosts() {
        excoslist.clear();
        load.setVisibility(View.VISIBLE);
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
                load.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                load.setVisibility(View.GONE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

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
                    .into(this.file);
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
            details[0] = name.substring(0, name.indexOf("."));
        }


        String ext = name.substring(name.indexOf(".") + 1, name.length());
        details[1] = ext;
        return details[0] + "." + details[1];
    }

    private void uploadFile() {
        final String entext = ename.getText().toString().trim();
        final String eptext = eposition.getText().toString().trim();
        if (entext.isEmpty()) {
            ename.setError("Enter name");
            ename.requestFocus();
            return;
        }
        if (eptext.isEmpty()) {
            eposition.setError("Enter position");
            eposition.requestFocus();
            return;
        }
        ldgif.setVisibility(View.VISIBLE);

        final String filename = System.currentTimeMillis() + "";
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("AffExcos")
                .child(filename);
        storageReference.putFile(fileuri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {

                                    final ExcosModel excosModel = new ExcosModel(uri.toString(), entext, eptext);
                                    DatabaseReference reference = FirebaseDatabase.getInstance()
                                            .getReference().child("AffExcos");
                                    reference.child(filename).setValue(excosModel)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    ldgif.setVisibility(View.GONE);
                                                    if (!task.isSuccessful()) {
                                                        Toast.makeText(ctx, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    } else {
                                                        mediaupload.setVisibility(View.GONE);
                                                        Toast.makeText(ctx, "Exco added successfully", Toast.LENGTH_LONG).show();
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
