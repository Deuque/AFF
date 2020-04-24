package com.dcinspirations.aff.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dcinspirations.aff.R;
import com.dcinspirations.aff.models.SliderModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

/**
 * Created by pc on 2/18/2018.
 */

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.viewHolder> {

    private List<SliderModel> objectlist;
    private LayoutInflater inflater;
    private Context context;
    private String layout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;

    public SliderAdapter(Context context, List<SliderModel> objectlist) {
        inflater = LayoutInflater.from(context);
        this.objectlist = objectlist;
        this.context = context;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.card_slider, parent, false);

        viewHolder vholder = new viewHolder(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        SliderModel current = objectlist.get(position);
        holder.setData(current, position);

    }

    public void refreshEvents() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return objectlist.size();
    }


    public class viewHolder extends SliderViewAdapter.ViewHolder {
        private LinearLayout otherimgs;
        private int position;
        private SliderModel currentObject;
        private TextView title;
        private ImageView img;

        public void setPosition(int position) {
            this.position = position;
        }

        public viewHolder(final View itemView) {
            super(itemView);


            title = itemView.findViewById(R.id.cartext);
            img = itemView.findViewById(R.id.carimg);



        }


        public void setData(SliderModel current, int position) {

            this.title.setText(current.getTitle());
            this.img.setImageResource(current.getImgid());

            this.position = position;
            this.currentObject = current;
        }


    }
}
