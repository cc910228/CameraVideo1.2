package com.khu.chao.cameravideo12;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.SeekBar;

import java.io.File;

/**
 * Created by CHAO on 2015/3/5.
 */
public class SecondActivity extends Activity implements OnClickListener {

    private VideoView videoView;
    private Button play_from_beginning;
    private Button start_from;
    private EditText editText;
    private SeekBar seekBar;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //make this second activity respond for button play of main activity
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.play);

        //setup the the media play
        play_from_beginning = (Button) findViewById(R.id.play_from_beginning);
        start_from = (Button) findViewById(R.id.start_from);
        videoView = (VideoView) findViewById(R.id.video_view);
        //editText.setInputType(InputType.TYPE_CLASS_NUMBER);//输入类型为数字

        seekBar=(SeekBar)findViewById(R.id.seekBar);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                seekBar.setMax(videoView.getDuration());
                seekBar.postDelayed(onEverySecond, 1000);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                if(fromUser) {
                    // this is when actually seekbar has been seeked to a new position
                    videoView.seekTo(progress);
                }
            }
        });


        play_from_beginning.setOnClickListener(this);
        start_from.setOnClickListener(this);
        initVideoPath();
    }

    private void initVideoPath(){
        File file = new File(Environment.getExternalStorageDirectory(),"myvideo.mp4");
        videoView.setVideoPath(file.getPath());
    }

    private Runnable onEverySecond=new Runnable() {

        @Override
        public void run() {

            if(seekBar != null) {
                seekBar.setProgress(videoView.getCurrentPosition());
            }

            if(videoView.isPlaying()) {
                seekBar.postDelayed(onEverySecond, 1000);
            }

        }
    };


    @Override
    public void onClick(View v){
    switch (v.getId()){
        case R.id.play_from_beginning:
            if (!videoView.isPlaying()){
                videoView.resume();
                videoView.start();
            }
            break;
        /*case R.id.start_from:
            if (!videoView.isPlaying()){
                //String inputText = editText.getText().toString();
                videoView.resume();
            }
            break;*/
        default:
            break;
    }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (videoView != null){
            videoView.suspend();
        }
    }
}