package com.dcinspirations.aff.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dcinspirations.aff.R;
import com.dcinspirations.aff.Sp;
import com.dcinspirations.aff.models.NewsModel;
import com.dcinspirations.aff.models.SM_model;
import com.dcinspirations.aff.ui.AboutFragment;
import com.dcinspirations.aff.ui.NewsFragment;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by pc on 2/18/2018.
 */

public class SMAdapter extends RecyclerView.Adapter<SMAdapter.viewHolder> {

    private List<SM_model> objectlist;
    private LayoutInflater inflater;
    private Context context;
    private String layout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;
    private AboutFragment f;

    public SMAdapter(Context context, List<SM_model> objectlist) {
        inflater = LayoutInflater.from(context);
        this.objectlist = objectlist;
        this.context = context;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view   = inflater.inflate(R.layout.sm_layout, parent, false);

        viewHolder vholder = new viewHolder(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        SM_model current = objectlist.get(position);
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
        private SM_model currentObject;
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
            title = itemView.findViewById(R.id.platform);
            link = itemView.findViewById(R.id.link);
            delete = itemView.findViewById(R.id.delete);


        }


        public void setData(final SM_model current, int position) {

            if(new Sp(context).getLoginType()!=3){
                this.delete.setVisibility(View.GONE);
//                price.setTextColor(context.getColor(R.color.aux5));
            }else {
                this.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectlist.remove(currentObject);
                        FirebaseDatabase.getInstance().getReference().child("AffSm").child(currentObject.getKey()).removeValue();
                        refreshEvents();
                    }
                });
            }

            this.title.setText(current.getPlatform().toUpperCase());
            this.link.setText(current.getLink());
            this.link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", current.getLink());
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(context,"Copied to clipboard",Toast.LENGTH_LONG).show();

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
                if (count % 3==0) {
                    newamount = "," + newamount;

                }

            }

            return newamount;
        }


    }
}
