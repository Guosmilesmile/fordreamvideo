 package cn.edu.xmu.ForDream.bean;

import java.io.File;

import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.util.FinalName;
import cn.sharesdk.framework.h;



import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.widget.ImageView;
import android.widget.Toast;
public class Mymedia {

    private String path;
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private ImageView imageView;
    private boolean pause;
    private int position;
    private Context context;
    private SurfaceCallback surfaceCallback;
    private Handler handler;
    public Mymedia(Context context,Handler handler) {
		super();
		this.context=context;
		mediaPlayer=null;
		this.handler=handler;
		position=-1;
		pause=false;
	        //surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);

	}
    
    public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void init()
    {
    	surfaceCallback=new SurfaceCallback();
        //把输送给surfaceView的视频画面，直接显示到屏幕上,不要维持它自身的缓冲区
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(176, 144);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(surfaceCallback);
       
      //  surfaceView.setBackgroundColor(Color.TRANSPARENT);
       // surfaceView.setZOrderOnTop(true);
       // surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
		
    }
	public void play() {

     if(surfaceView!=null)
     {
    	
 		surfaceView.setVisibility(View.INVISIBLE);
        surfaceView.setVisibility(View.VISIBLE);

     }

        
        
 	}
 	
	public void  stop() {
		
		Log.i("Media Stop","Media Stop");
		if(mediaPlayer.isPlaying()||pause)
		{
			//mediaPlayer.release();
			//mediaPlayer=null;	
			mediaPlayer.pause();
			pause=false;
			//mediaPlayer.seekTo(0);
			//surfaceView.removeCallbacks(null);
			surfaceView.getHolder().removeCallback(surfaceCallback);
			surfaceCallback=null;
			surfaceView=null;
			
			if(imageView!=null)
			{
				imageView.setVisibility(View.VISIBLE);
				imageView=null;
			}
		}
		
	}
    
 	public void mediaplay(View v){
 			
 			
 			if((v.getId()==R.id.talkgroup_item_videoimageView||v.getId()==R.id.square_item_videoimageView)&&(mediaPlayer==null||!mediaPlayer.isPlaying()))
 			{
 				 imageView=(ImageView)v;
 				if(imageView.getVisibility()==View.VISIBLE)
 				{
 	 				imageView.setVisibility(View.INVISIBLE);                                             
 	 				

 	 				if(path!=null)
 	 					{
 	 						//通知主线程停止正在播放的视频
	 						handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_STOPPLAYER,position));
 	 						play();
 	 						//通知主线程该视频播放次数加1
 	 						handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_PLAYNUM_ADD,position));
 	 					}
 				}

 			}
 			else if((v.getId()==R.id.talkgroup_item_sv_danmaku||v.getId()==R.id.sv_danmaku)&&mediaPlayer!=null) {
 				Log.i("mediaPlayer.isPlaying()","mediaPlayer.isPlaying()"+mediaPlayer.isPlaying());
 				if(mediaPlayer.isPlaying()){
 	 				mediaPlayer.pause();
 	 				pause = true;
 	 			}else{
 	 				if(pause){
 	 					mediaPlayer.start();
 	 					pause = false;
 	 				}
 	 			}
 				Log.i("pause","pause"+pause);
			}
 			
 		
     }
    
    public SurfaceView getSurfaceView() {
		return surfaceView;
	}
	public void setSurfaceView(SurfaceView surfaceView) {
		
		if(this.surfaceView==null||!this.surfaceView.equals(surfaceView))
		{
			//Log.i("setSurfaceView",surfaceView.toString());
			this.surfaceView = surfaceView;
			init();
		}
		
		
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}
	
 	private final class PrepareListener implements OnPreparedListener{
 		private int position;
 		
 		public PrepareListener(int position) {
 		     this.position = position;
 		}

 		public void onPrepared(MediaPlayer mp) {
 			mediaPlayer.start();//播放视频
 			
 			//if(position>0) mediaPlayer.seekTo(position);
 		}
 	}
 	private final class CompleteListener implements OnCompletionListener{

		public void onCompletion(MediaPlayer mp) {
			
			mp.seekTo(0);
			mp.start();
			
		}
 	
 	}
	private final class SurfaceCallback implements Callback{

		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
 		}
 		public void surfaceCreated(SurfaceHolder holder) {
 			
 	 		try {
 	 			//Log.i("holder",holder.toString());
 	 			//Log.i("surfaceView", surfaceView.toString());
 	 			//Log.i("surfaceView.getHolder()", surfaceView.getHolder().toString());
 	 			
 	 			if(mediaPlayer==null)
 	 			{	
 	 				
 	 				mediaPlayer = new MediaPlayer();
	 				mediaPlayer.reset();
	 				mediaPlayer.setDataSource(path);
	 				//mediaPlayer.setDisplay(surfaceView.getHolder());
	 				mediaPlayer.setDisplay(holder);
	 				mediaPlayer.prepare();//缓冲
	 				mediaPlayer.setOnPreparedListener(new PrepareListener(0));
	 				mediaPlayer.setOnCompletionListener(new CompleteListener());
	 				//Log.i("First Who Play?  ",mediaPlayer.toString());
 	 			}
 	 			else
 	 			{
 	 				
 	 				//mediaPlayer.setDisplay(surfaceView.getHolder());
 	 				mediaPlayer.setDisplay(holder);
 	 				if(!mediaPlayer.isPlaying())
 	 					{
 	 						mediaPlayer.start();
 	 						
 	 						//Log.i("Else Who Play?  ",mediaPlayer.toString());
 	 					}
 	 				
 	 				
 	 			}	

 	 			 

 	 		} catch (Exception e) {
 	 			e.printStackTrace();
 	 		}
 			
 			/*if(position>0 && path!=null){
 				play(position);
 				position = 0;
 			}*/
 		}
 		public void surfaceDestroyed(SurfaceHolder holder) {
 			if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
 				//position = mediaPlayer.getCurrentPosition();
 				mediaPlayer.pause();
 				pause = true;
 			}
 		}
     }
	
	public boolean isPause() {
		return pause;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
}
