package com.surveybaba;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.surveybaba.Utilities.Utility;

public class VideoViewActivity extends Activity {

    String path;
    VideoView _videoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_fullscreen);

        path = getIntent().getExtras().getString(Utility.key_intent_videoPath);
        _videoView =(VideoView)findViewById(R.id._videoView);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(_videoView);

        //Setting MediaController and URI, then starting the videoView
        _videoView.setMediaController(mediaController);
        _videoView.setVideoURI(Uri.parse(path));
        _videoView.requestFocus();
        _videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(_videoView!=null)
        {
            _videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(_videoView!=null && !_videoView.isPlaying()) {
            _videoView.resume();
            _videoView.start();
        }
    }
}
