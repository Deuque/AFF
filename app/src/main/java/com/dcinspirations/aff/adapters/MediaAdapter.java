package com.dcinspirations.aff.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dcinspirations.aff.PlayMusicActivity;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.Sp;
import com.dcinspirations.aff.models.MediaModel;
import com.dcinspirations.aff.ui.MediaFragment;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pc on 2/18/2018.
 */

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.viewHolder> {

    private List<MediaModel> objectlist;
    private LayoutInflater inflater;
    private Context context;
    private String layout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;
    private MediaFragment f;


    public MediaAdapter(Context context, List<MediaModel> objectlist,MediaFragment fragment) {
        inflater = LayoutInflater.from(context);
        this.objectlist = objectlist;
        this.context = context;
        f = fragment;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view   = inflater.inflate(R.layout.media_layout, parent, false);

        viewHolder vholder = new viewHolder(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        MediaModel current = objectlist.get(position);
        holder.setData(current, position);

    }

    @Override
    public int getItemCount() {
        return objectlist.size();
    }

    public void refreshEvents() {
        notifyDataSetChanged();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        private LinearLayout otherimgs;
        private int position;
        private MediaModel currentObject;
        private TextView aname,sname,downloads;
        private LinearLayout mlayout;
        private ImageView delete,label,trend;
        PorterShapeImageView image;

        public void setPosition(int position) {
            this.position = position;
        }

        public viewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(currentObject.getType().equalsIgnoreCase("audio")){
                       String objectAsString = new Gson().toJson(currentObject);
                       context.startActivity(new Intent(context, PlayMusicActivity.class).putExtra("music_info",objectAsString));
                   }else{
                       f.loadImageSlide(objectlist.indexOf(currentObject));
                   }
                }
            });
            aname = itemView.findViewById(R.id.aname);
            sname = itemView.findViewById(R.id.sname);
            downloads = itemView.findViewById(R.id.downloads);
            mlayout = itemView.findViewById(R.id.medialayout);
            image = itemView.findViewById(R.id.image);
            label = itemView.findViewById(R.id.label);
            delete = itemView.findViewById(R.id.delete);
            trend = itemView.findViewById(R.id.trend);

        }


        public void setData(final MediaModel current, int position) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            f.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            LinearLayout.LayoutParams clayoutparams = (LinearLayout.LayoutParams) this.mlayout.getLayoutParams();
            clayoutparams.width = (width/3)-40;
            this.mlayout.setLayoutParams(clayoutparams);

            if(current.getType().equalsIgnoreCase("image")){
//                Picasso.get()
//                        .load(current.getImgUrl())
//                        .resize((width/3)-20,110)
//                        .centerCrop()
//                        .into(image);
                Glide.with(context)
                        .load(current.getImgUrl())
                        .into(this.image);
                this.aname.setVisibility(View.GONE);
                this.sname.setVisibility(View.GONE);
                this.downloads.setVisibility(View.GONE);
                this.label.setVisibility(View.GONE);
            }else {
                this.image.setImageResource(R.mipmap.mbg);
                this.aname.setVisibility(View.VISIBLE);
                this.sname.setVisibility(View.VISIBLE);
                this.aname.setText(current.getArtist());
                this.sname.setText(current.getSong());
                this.downloads.setVisibility(View.VISIBLE);
                this.label.setVisibility(View.VISIBLE);
                this.downloads.setText(current.getDownload()==null?"0":current.getDownload());
            }

            if(new Sp(context).getLoginType()!=3){
                this.delete.setVisibility(View.GONE);
                this.downloads.setVisibility(View.GONE);
                this.trend.setVisibility(View.GONE);
//                price.setTextColor(context.getColor(R.color.aux5));
            }else{
                this.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectlist.remove(current);
                        FirebaseDatabase.getInstance().getReference().child("AffMedia").child(current.getKey()).removeValue();
                        FirebaseStorage.getInstance().getReference().child("AffMedia").child(current.getKey()).delete();
                        refreshEvents();
                    }
                });
                if(current.getCategory().equalsIgnoreCase("normal")){
                    this.trend.setVisibility(View.GONE);
                }
            }
            this.position = position;
            this.currentObject = current;
        }

        private String resolveMoney(String amount) {
            String newamount = "";
            String bram[] = amount.split("");
            int count = 0;
            for (int i = bram.length - 1; i >= 1; i--) {
                newamount = bram[i] + newamount;
                count++;
                if (count % 3==0) {
                    newamount = "," + newamount;

                }

            }

            return newamount;
        }


    }
}
