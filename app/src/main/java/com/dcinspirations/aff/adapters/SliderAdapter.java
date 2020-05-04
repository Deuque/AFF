package com.dcinspirations.aff.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.dcinspirations.aff.R;
import com.dcinspirations.aff.models.MediaModel;
import com.dcinspirations.aff.models.SliderModel;
import com.dcinspirations.aff.ui.HomeFragment;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

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
    HomeFragment f;

    public SliderAdapter(Context context, List objectlist, int type, @NonNull HomeFragment fragment) {
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
        this.f = fragment;
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
        private ImageView img,play;
        public VideoView carvid=null;
        public GifImageView buffergif=null;



        public void setPosition(int position) {
            this.position = position;
        }

        public viewHolder(final View itemView) {
            super(itemView);


            if(type==0) {
                title = itemView.findViewById(R.id.cartext);
                img = itemView.findViewById(R.id.carimg);
                carvid=itemView.findViewById(R.id.carvid);
                buffergif=itemView.findViewById(R.id.bgif);
                play = itemView.findViewById(R.id.play);
                itemView.findViewById(R.id.delete).setVisibility(View.GONE);
                itemView.findViewById(R.id.type).setVisibility(View.GONE);
            }else{
                img = itemView.findViewById(R.id.img);
            }



        }


        public void setData(SliderModel current, int position) {
            this.position = position;
            this.currentObject = current;
                if(current.getCategory().equalsIgnoreCase("video")){
                    this.img.setVisibility(View.GONE);
                    this.carvid.setVisibility(View.VISIBLE);
                    this.play.setVisibility(View.VISIBLE);
                    this.play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            play.setVisibility(View.GONE);
                            f.sliderView.stopAutoCycle();
                            new VidTask().execute(carvid);
                        }
                    });

                }else{
                    this.img.setVisibility(View.VISIBLE);
                    this.carvid.setVisibility(View.GONE);
                    this.play.setVisibility(View.GONE);
                    this.buffergif.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(current.getImgUrl())
                            .into(this.img);
                }
                this.title.setText(current.getTitle());



        }
        public void setData2(MediaModel current, int position) {

            Glide.with(context)
                    .load(current.getImgUrl())
                    .centerCrop()
                    .into(this.img);




            this.position = position;
            this.currentObject2 = current;
        }

        public void setUpVideo(VideoView mediaPlayer, final GifImageView buffergif){

        }
        private boolean isViewOnScreen(View view) {
            Rect r = new Rect();
            return view.getGlobalVisibleRect(r);
        }

        class VidTask extends AsyncTask<VideoView, Integer, Void> {
            boolean isPlaying = false, prepared = false, iscompleted = false, askingPerm = false;
            VideoView mediaPlayer;
            @Override
            protected Void doInBackground(VideoView... videoViews) {
                mediaPlayer = videoViews[0];
                mediaPlayer.setSoundEffectsEnabled(false);
                mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == mp.MEDIA_INFO_BUFFERING_START) {
                            buffergif.setVisibility(View.GONE);
                        } else if (what == mp.MEDIA_INFO_BUFFERING_END) {
                            buffergif.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        buffergif.setVisibility(View.GONE);
                        prepared = true;
                        mediaPlayer.start();

                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        isPlaying = false;
                        iscompleted = true;
                        f.sliderView.startAutoCycle();
                    }
                });
                mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        f.sliderView.startAutoCycle();
                        return false;
                    }
                });
                if (!prepared) {
                    try {
                        prepared = false;
                        buffergif.setVisibility(View.VISIBLE);
                        mediaPlayer.setVideoURI(Uri.parse(currentObject.getImgUrl()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                }

                return null;
            }



        }


    }
}
