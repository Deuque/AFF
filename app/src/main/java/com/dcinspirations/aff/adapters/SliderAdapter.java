package com.dcinspirations.aff.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.models.MediaModel;
import com.dcinspirations.aff.models.SliderModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

/**
 * Created by pc on 2/18/2018.
 */

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.viewHolder> {

    private List<SliderModel> objectlist;
    private List<MediaModel> objectlist2;
    private LayoutInflater inflater;
    private Context context;
    private String layout;
    private int type;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;

    public SliderAdapter(Context context, List objectlist, int type) {
        inflater = LayoutInflater.from(context);
//        try {
//            this.objectlist = objectlist;
//        }catch (Exception e){
//            this.objectlist2 = objectlist;
//        }
        this.context = context;
        this.type = type;
        if(type==0){
            this.objectlist = objectlist;
        }else{
            this.objectlist2 = objectlist;

        }
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(type==0?R.layout.card_slider:R.layout.slide_item, parent, false);

        viewHolder vholder = new viewHolder(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        if (type == 0) {
            SliderModel current = objectlist.get(position);
            holder.setData(current, position);

        }else{

            MediaModel current = objectlist2.get(position);
            holder.setData2(current, position);
        }

    }

    public void refreshEvents() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return type==0?objectlist.size():objectlist2.size();
    }


    public class viewHolder extends SliderViewAdapter.ViewHolder {
        private LinearLayout otherimgs;
        private int position;
        private SliderModel currentObject;
        private MediaModel currentObject2;
        private TextView title;
        private ImageView img;

        public void setPosition(int position) {
            this.position = position;
        }

        public viewHolder(final View itemView) {
            super(itemView);


            if(type==0) {
                title = itemView.findViewById(R.id.cartext);
                img = itemView.findViewById(R.id.carimg);
            }else{
                img = itemView.findViewById(R.id.img);
            }



        }


        public void setData(SliderModel current, int position) {


                this.title.setText(current.getTitle());
                this.img.setImageResource(current.getImgid());


            this.position = position;
            this.currentObject = current;
        }
        public void setData2(MediaModel current, int position) {

            Glide.with(context)
                    .load(current.getImgUrl())
                    .centerCrop()
                    .into(this.img);




            this.position = position;
            this.currentObject2 = current;
        }


    }
}
