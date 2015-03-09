//automatically 10 seconds recording finished version
//replay finished version
package com.khu.chao.cameravideo12;

import java.util.Timer;
import java.util.TimerTask;
//import android.os.Handler;
//import android.widget.TextView;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
    Button record, play;
    // video file
    File viodFile;
    MediaRecorder mRecorder;
    // show the SurfaceView of the video
    SurfaceView sView;

    boolean isRecording = false;
    Camera camera;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide title bar  Chapter 2.24
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        record = (Button) findViewById(R.id.record);
        play = (Button) findViewById(R.id.play);
        sView = (SurfaceView) findViewById(R.id.dView);
        // set Surface do not need its own buffer zone
        sView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // resolution
        sView.getHolder().setFixedSize(320, 280);
        // keep the screen
        sView.getHolder().setKeepScreenOn(true);
        record.setOnClickListener(this);
        play.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record:
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(this, "No SD card！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    // create MediaPlayer
                    mRecorder = new MediaRecorder();
                    mRecorder.reset();
   /* camera = Camera.open();
    camera.unlock();
    camera.setDisplayOrientation(0);
    mRecorder.setCamera(camera);*/
                    // save the video file
                    viodFile = new File(Environment.getExternalStorageDirectory()
                            .getCanonicalFile() + "/myvideo.mp4");
                    if (!viodFile.exists())
                        viodFile.createNewFile();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
                    //mRecorder.setMaxDuration(10000);
                    mRecorder.setOrientationHint(90);
                    //mRecorder.setVideoSize(320, 280);
                    // mRecorder.setVideoFrameRate(5);
                    mRecorder.setOutputFile(viodFile.getAbsolutePath());
                    mRecorder.setPreviewDisplay(sView.getHolder().getSurface());
                    mRecorder.prepare();
                    // start
                    mRecorder.start();
                    // record button not available
                    record.setEnabled(false);
                    // play button not available
                    play.setEnabled(false);
                    isRecording = true;

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        int i = 10;
                        @Override
                        public void run(){
                           //stop
                            mRecorder.stop();
                           //release resource
                            mRecorder.release();
                            mRecorder = null;
                            isRecording = false;
                          //define a message to send
                           //Message msg = new Message();
                            //msg.what = i--;
                            //handler.sendMessage(msg);
                        }
                    }, 10000);
                   //record button available
                    record.setEnabled(true);
                    //  button available
                    play.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.play:
            // if it is not recording now
            if (!isRecording) {
                try {
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
              break;
            default:
                break;
        }
    }
}
