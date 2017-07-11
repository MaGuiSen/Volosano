package com.volosano.manager;

import android.content.Context;
import android.media.MediaPlayer;

import com.volosano.R;

import java.io.IOException;

/**
 * Created by mags on 2017/7/11.
 */
public class MediaManager {
    private static MediaManager ourInstance = null;
    MediaPlayer player;
    Context context;
    public static synchronized MediaManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new MediaManager(context);
        }
        return ourInstance;
    }

    private MediaManager(Context context) {
        this.context = context;
    }

    public void play(){
        try {
            if (player == null) {
                player = MediaPlayer.create(context, R.raw.bg);
            }
            player.setLooping(true);
            player.reset();
            player.prepare();
            player.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            if(player.isPlaying()) {
//                player.stop();
                player.release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void pause(){
        try {
            if(player.isPlaying()) {
                player.pause();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
