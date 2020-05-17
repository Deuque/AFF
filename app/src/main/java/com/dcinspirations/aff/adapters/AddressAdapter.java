package com.dcinspirations.aff.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.Sp;
import com.dcinspirations.aff.models.ExcosModel;
import com.dcinspirations.aff.models.OthersModel2;
import com.dcinspirations.aff.ui.ExcosFragment;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

/**
 * Created by pc on 2/18/2018.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.viewHolder> {

    private List<OthersModel2> objectlist;
    private LayoutInflater inflater;
    private Context context;
    private String layout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;

    public AddressAdapter(Context context, List<OthersModel2> objectlist) {
        inflater = LayoutInflater.from(context);
        this.objectlist = objectlist;
        this.context = context;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view   = inflater.inflate(R.layout.address_layout, parent, false);

        viewHolder vholder = new viewHolder(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        OthersModel2 current = objectlist.get(position);
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
        private int position;
        private OthersModel2 currentObject;
        private TextView address;
        private ImageView delete;
        PorterShapeImageView image;

        public void setPosition(int position) {
            this.position = position;
        }

        public viewHolder(final View itemView) {
            super(itemView);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    hf.controlBS(currentObject,"details");
//                }
//            });
            address = itemView.findViewById(R.id.address);
            image = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.delete);



        }


        public void setData(OthersModel2 current, int position) {

            if(new Sp(context).getLoginType()!=3){
                this.delete.setVisibility(View.GONE);
//                price.setTextColor(context.getColor(R.color.aux5));
            }else {
                this.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectlist.remove(currentObject);
                        FirebaseDatabase.getInstance().getReference().child("AffOthers").child("addresslocations").child(currentObject.getId()).removeValue();
                        FirebaseStorage.getInstance().getReference().child("AffOthers").child(currentObject.getId()).delete();
                        refreshEvents();
                    }
                });
            }
//                Picasso.get()
//                        .load(current.getImgUrl())
//                        .resize(80,70)
//                        .centerCrop()
//                        .noFade()
//                        .into(image);
            Glide.with(context)
                    .load(current.getImgUrl())

//                        .thumbnail(.05f)
                    .placeholder(R.drawable.image)
                    .into(this.image);
                this.address.setText(current.getAddress().toUpperCase());


            this.position = position;
            this.currentObject = current;
        }


    }
}
