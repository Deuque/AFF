package com.dcinspirations.aff.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dcinspirations.aff.Biodata;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.Sp;
import com.dcinspirations.aff.models.MemberModel;
import com.dcinspirations.aff.models.NewsModel;
import com.dcinspirations.aff.ui.NewsFragment;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by pc on 2/18/2018.
 */

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.viewHolder> {

    private List<MemberModel> objectlist;
    private LayoutInflater inflater;
    private Context context;
    private String layout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;
    private NewsFragment f;

    public MembersAdapter(Context context, List<MemberModel> objectlist) {
        inflater = LayoutInflater.from(context);
        this.objectlist = objectlist;
        this.context = context;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view   = inflater.inflate(R.layout.members_layout, parent, false);

        viewHolder vholder = new viewHolder(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        MemberModel current = objectlist.get(position);
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
        private MemberModel currentObject;
        private TextView aid,email,name,ptype;
        private ImageView delete;

        public void setPosition(int position) {
            this.position = position;
        }

        public viewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, Biodata.class).putExtra("uid",currentObject.getUid()));
                }
            });
            aid = itemView.findViewById(R.id.affid);
            email = itemView.findViewById(R.id.email);
            name = itemView.findViewById(R.id.name);
            ptype = itemView.findViewById(R.id.ptype);
//            delete = itemView.findViewById(R.id.delete);


        }


        public void setData(MemberModel current, int position) {

            if(new Sp(context).getLoginType()!=3){
                this.delete.setVisibility(View.GONE);
//                price.setTextColor(context.getColor(R.color.aux5));
            }else {
//                this.delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        objectlist.remove(currentObject);
//                        FirebaseDatabase.getInstance().getReference().child("AffMembers").child(currentObject.getUid()).removeValue();
//                        refreshEvents();
//                    }
//                });
            }

            this.aid.setText(current.getUid());
            this.email.setText(current.getEmail());
            this.name.setText(current.getName());

            this.ptype.setText(current.getPaymenttype());
            this.ptype.setTextColor(current.getPaymenttype().equalsIgnoreCase("admin")?context.getColor(android.R.color.holo_blue_dark):context.getColor(android.R.color.holo_green_dark));

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
