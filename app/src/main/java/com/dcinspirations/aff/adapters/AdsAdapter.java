package com.dcinspirations.aff.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dcinspirations.aff.ui.AdsFragment;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.models.SliderModel;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

/**
 * Created by pc on 2/18/2018.
 */

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.viewHolder> {

    private List<SliderModel> objectlist;
    private LayoutInflater inflater;
    private Context context;
    private String layout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;
    private AdsFragment f;


    public AdsAdapter(Context context, List<SliderModel> objectlist, AdsFragment fragment) {
        inflater = LayoutInflater.from(context);
        this.objectlist = objectlist;
        this.context = context;
        f = fragment;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_slider2, parent, false);

        viewHolder vholder = new viewHolder(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        SliderModel current = objectlist.get(position);
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
        private RelativeLayout container;
        private int position;
        private SliderModel currentObject;
        private TextView caption;
        private ImageView delete, label, type;
        PorterShapeImageView image;

        public void setPosition(int position) {
            this.position = position;
        }

        public viewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                   if(currentObject.getCategory().equalsIgnoreCase("video")){
//                       String objectAsString = new Gson().toJson(currentObject);
//                       context.startActivity(new Intent(context, PlayMusicActivity.class).putExtra("music_info",objectAsString));
//                   }else{
//                       f.loadImageSlide(objectlist.indexOf(currentObject));
//                   }
                }
            });
            caption = itemView.findViewById(R.id.cartext);
            container = itemView.findViewById(R.id.container);
            image = itemView.findViewById(R.id.carimg);
            delete = itemView.findViewById(R.id.delete);
            type = itemView.findViewById(R.id.type);

        }


        public void setData(final SliderModel current, int position) {
//            DisplayMetrics displayMetrics = new DisplayMetrics();
//            f.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//            int height = displayMetrics.heightPixels;
//            int width = displayMetrics.widthPixels;
//
//            RelativeLayout.LayoutParams clayoutparams = (RelativeLayout.LayoutParams) this.container.getLayoutParams();
////            clayoutparams.width = (width / 2) - 20;
//            clayoutparams.height = 400;
//            this.container.setLayoutParams(clayoutparams);

            if (current.getCategory().equalsIgnoreCase("image")) {
//                Picasso.get()
//                        .load(current.getImgUrl())
//                        .resize((width/3)-20,110)
//                        .centerCrop()
//                        .into(image);
                Glide.with(context)
                        .load(current.getImgUrl())
                        .placeholder(R.drawable.image)
                        .into(this.image);
            } else {
                long interval = 1000;
                RequestOptions options = new RequestOptions().frame(interval);
                Glide.with(context).asBitmap().load(current.getImgUrl()).apply(options).into(this.image);
            }
            this.caption.setText(current.getTitle());

            this.type.setImageResource(current.getCategory().equalsIgnoreCase("image")?R.drawable.image:R.drawable.video);
            this.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objectlist.remove(current);
                    FirebaseDatabase.getInstance().getReference().child("AffAds").child(current.getImgid()).removeValue();
                    FirebaseStorage.getInstance().getReference().child("AffAds").child(current.getImgid()).delete();
                    refreshEvents();
                }
            });

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
                if (count % 3 == 0) {
                    newamount = "," + newamount;

                }

            }

            return newamount;
        }


    }
}
