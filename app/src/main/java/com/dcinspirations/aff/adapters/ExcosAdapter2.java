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
import com.dcinspirations.aff.ui.ExcosFragment;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

/**
 * Created by pc on 2/18/2018.
 */

public class ExcosAdapter2 extends RecyclerView.Adapter<ExcosAdapter2.viewHolder> {

    private List<ExcosModel> objectlist;
    private LayoutInflater inflater;
    private Context context;
    private String layout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;

    public ExcosAdapter2(Context context, List<ExcosModel> objectlist) {
        inflater = LayoutInflater.from(context);
        this.objectlist = objectlist;
        this.context = context;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view   = inflater.inflate(R.layout.excos_layout2, parent, false);

        viewHolder vholder = new viewHolder(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        ExcosModel current = objectlist.get(position);
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
        private ExcosModel currentObject;
        private TextView name,title;
        private RelativeLayout nlayout;
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
            name = itemView.findViewById(R.id.name);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            nlayout = itemView.findViewById(R.id.newslayout);
            delete = itemView.findViewById(R.id.delete);



        }


        public void setData(ExcosModel current, int position) {
//            DisplayMetrics displayMetrics = new DisplayMetrics();
//            f.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//            int height = displayMetrics.heightPixels;
//            int width = displayMetrics.widthPixels;
//
//            LinearLayout.LayoutParams clayoutparams = (LinearLayout.LayoutParams) this.nlayout.getLayoutParams();
//            clayoutparams.width = (width/3)-20;
//            this.nlayout.setLayoutParams(clayoutparams);

                this.delete.setVisibility(View.GONE);
//                price.setTextColor(context.getColor(R.color.aux5));

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
                this.name.setText(current.getName());
                this.title.setText(current.getPosition());


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
