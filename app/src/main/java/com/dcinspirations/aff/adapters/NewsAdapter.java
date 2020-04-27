package com.dcinspirations.aff.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dcinspirations.aff.R;
import com.dcinspirations.aff.Sp;
import com.dcinspirations.aff.models.MediaModel;
import com.dcinspirations.aff.models.NewsModel;
import com.dcinspirations.aff.ui.MediaFragment;
import com.dcinspirations.aff.ui.NewsFragment;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pc on 2/18/2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.viewHolder> {

    private List<NewsModel> objectlist;
    private LayoutInflater inflater;
    private Context context;
    private String layout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;
    private NewsFragment f;

    public NewsAdapter(Context context, List<NewsModel> objectlist) {
        inflater = LayoutInflater.from(context);
        this.objectlist = objectlist;
        this.context = context;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view   = inflater.inflate(R.layout.news_layout, parent, false);

        viewHolder vholder = new viewHolder(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        NewsModel current = objectlist.get(position);
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
        private NewsModel currentObject;
        private TextView title,body,date,link;
        private ImageView delete;

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
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            body = itemView.findViewById(R.id.body);
            link = itemView.findViewById(R.id.link);
            delete = itemView.findViewById(R.id.delete);


        }


        public void setData(NewsModel current, int position) {

            if(new Sp(context).getLoginType()!=3){
                this.delete.setVisibility(View.GONE);
//                price.setTextColor(context.getColor(R.color.aux5));
            }else {
                this.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectlist.remove(currentObject);
                        FirebaseDatabase.getInstance().getReference().child("AffNews").child(currentObject.getNkey()).removeValue();
                        refreshEvents();
                    }
                });
            }

            this.title.setText(current.getNtitle());
            this.date.setText(current.getDate());
            this.body.setText(current.getNbody());
            this.link.setText(current.getNlink());

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
