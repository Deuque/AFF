package com.dcinspirations.aff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dcinspirations.aff.models.MediaModel;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import pl.droidsonroids.gif.GifImageView;

public class PlayMusicActivity extends AppCompatActivity {

    SeekBar mprog;
    TextView mtimer, sname, aname;
    boolean isPlaying = false, prepared = false, iscompleted = false, askingPerm = false;
    int duration = 0, currentTime = 0;
    String durString;
    MediaPlayer mediaPlayer;
    GifImageView buffergif;
    ImageView mcontrol;
    MusicTask musicTask;
    MediaModel mediasource;
    SimpleExoPlayer simpleExoPlayer;
    final Handler h = new Handler();
    final Runnable r = new Runnable() {
        @Override
        public void run() {
            if (isPlaying) {
                try {
                    processProgress();
                } catch (Exception e) {

                }

            }
            h.postDelayed(r, 1000);
        }
    };
    private static final int req3 = 27;
    private static final int req4 = 64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        String objectAsString = getIntent().getExtras().getString("music_info");
        Type gsontype = new TypeToken<MediaModel>() {
        }.getType();
        mediasource = new Gson().fromJson(objectAsString, gsontype);
        buffergif = findViewById(R.id.bgif);
        mcontrol = findViewById(R.id.mcontrol);
        mtimer = findViewById(R.id.mtimer);
        mprog = findViewById(R.id.mprog);
        mprog.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int position = seekBar.getProgress() * duration / 100;
                mediaPlayer.seekTo(position * 1000);
            }
        });
        sname = findViewById(R.id.sname);
        sname.setText(mediasource.getSong());
        aname = findViewById(R.id.aname);
        aname.setText(mediasource.getArtist());

        buffergif.setVisibility(View.VISIBLE);
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == mp.MEDIA_INFO_BUFFERING_START) {
                    buffergif.setVisibility(View.VISIBLE);
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
                duration = (int) (mp.getDuration() / 1000);
                durString = String.format("%02d:%02d", duration / 60, duration % 60);
                mtimer.setText("0 / " + durString);
                prepared = true;
                h.post(r);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                iscompleted = true;
                currentTime = 0;
                mcontrol.setImageResource(R.drawable.play);
            }
        });


    }

    public void goBack(View v) {
        finish();
    }

    public void initDownload(View view) {
        if (!prepared) {
            Toast.makeText(this, "Loading File, Please wait..", Toast.LENGTH_SHORT).show();
            return;
        }

        checkPerm();

    }

    public void downloadMedia() {
        String title = "" + mediasource.getSong() + "-" + mediasource.getArtist();
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "AFF Media/" + title + ".mp3");
        if (mediaStorageDir.exists()) {
            Toast.makeText(this, "You already downloaded this song", Toast.LENGTH_LONG).show();
            return;
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mediasource.getImgUrl()));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE |
                DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(mediasource.getSong() + " download from AFF");
        request.setDescription("Downloading " + mediasource.getSong());
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir("AFF Media", "" + mediasource.getSong() + "-" + mediasource.getArtist() + ".mp3");

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }

    public void checkPerm() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            downloadMedia();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, req3);
            askingPerm = true;
//            checkPerm();
        }
    }

    public void checkPerm2() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, req4);
            checkPerm2();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        askingPerm = false;
        if (requestCode == req3 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadMedia();
        } else if (requestCode == req4 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            Toast.makeText(this, "You need to give permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (prepared) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onBackPressed();
    }

    public void InitVideoPlayback(View view) {
        if (prepared) {
            isPlaying = !isPlaying;
            if (isPlaying) {
                mcontrol.setImageResource(R.drawable.pause);
                try {
                    mediaPlayer.start();
                } catch (Exception e) {
                }

            } else {
                mcontrol.setImageResource(R.drawable.play);
                mediaPlayer.pause();
            }
        }


    }

    class MusicTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (!prepared) {
                try {
                    prepared = false;
                    buffergif.setVisibility(View.VISIBLE);
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(mediasource.getImgUrl());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(PlayMusicActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

//                setUpPlayer();
//                simpleExoPlayer.addListener(new Player.EventListener() {
//                    @Override
//                    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
//
//                    }
//
//                    @Override
//                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//
//                    }
//
//                    @Override
//                    public void onLoadingChanged(boolean isLoading) {
//                        if(!isLoading){
//                            prepared=true;
//                            setDuration();
//                            buffergif.setVisibility(View.INVISIBLE);
//                        }
//                    }
//
//                    @Override
//                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//
//                    }
//
//                    @Override
//                    public void onRepeatModeChanged(int repeatMode) {
//
//                    }
//
//                    @Override
//                    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
//
//                    }
//
//                    @Override
//                    public void onPlayerError(ExoPlaybackException error) {
//
//                    }
//
//                    @Override
//                    public void onPositionDiscontinuity(int reason) {
//
//                    }
//
//                    @Override
//                    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//
//                    }
//
//                    @Override
//                    public void onSeekProcessed() {
//
//                    }
//                });
            } else {

            }
            return null;
        }
    }


    private void processProgress() {

        if (isPlaying) {
            currentTime = (int) (mediaPlayer.getCurrentPosition() / 1000);
            try {
                int cpercent = currentTime * 100 / duration;
                mprog.setProgress(cpercent);
                String currentString = String.format("%02d:%02d", currentTime / 60, currentTime % 60);
                mtimer.setText(currentString + " / " + durString);


            } catch (Exception e) {

            }
        } else {
            if (iscompleted) {
                mtimer.setText(durString + " / " + durString);
            }
        }

    }

    @Override
    public void onPause() {


        if (!askingPerm) {
            isPlaying = false;
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                mediaPlayer.release();
            }
            mcontrol.setImageResource(R.drawable.play);
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicTask = new MusicTask();
        musicTask.execute();
    }


    @Override
    public void onStop() {
        h.removeCallbacks(r);
        isPlaying = false;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        musicTask.cancel(true);
        mcontrol.setImageResource(R.drawable.play);
        super.onStop();
    }
}
