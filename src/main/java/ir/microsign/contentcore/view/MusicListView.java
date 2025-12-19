package ir.microsign.contentcore.view;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.microsign.R;
import ir.microsign.contentcore.object.MusicList;
import ir.microsign.context.Application;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.dialog.ConnectDialog;
import ir.microsign.net.DownloadFiles;
import ir.microsign.net.Utility;
import ir.microsign.utility.File;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 01/03/2016.
 */
public class MusicListView extends BaseView implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {

    public static MediaPlayer mMediaPlayer;
    public TextView duration;
       double timeElapsed = 0, finalTime = 0;
//    private int forwardTime = 2000;
    private Handler mHandler = new Handler();
    private SeekBar mTimeline,mVolume;
//public String mPath=null;
    View mNext=null,mPrev=null;
    public MusicListView(Context context) {
        super(context);
    }

    public MusicListView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    public MusicListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);


//        setView();

    }

    MusicList getItem() {
        return (MusicList) getDbObject();
    }
ImageView imgPlay=null;
    public static AudioManager am =null;
    public void setView(){

        if (getChildCount()==0) {
            getLayoutInflater().inflate(R.layout.layout_player, this);

        }

        imgPlay= (ImageView) findViewById(R.id.img_play);
        imgPlay.setOnClickListener(this);
        findViewById(R.id.img_stop).setOnClickListener(this);
        duration = (TextView) findViewById(R.id.txt_duration);
        mTimeline = (SeekBar) findViewById(R.id.seekBar);
        mVolume= (SeekBar) findViewById(R.id.seek_volume);
//        ((ImageView)findViewById(R.id.img_image)).setImageBitmap(getItem().getImage());


        mTimeline.setOnSeekBarChangeListener(this);
        mVolume.setOnSeekBarChangeListener(this);


        if (am==null)
            am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVolume.setMax(maxVolume);
        mTimeline.setClickable(false);
        mVolume.setClickable(false);




     mNext =findViewById(R.id.img_next); mPrev=findViewById(R.id.img_back);


        mNext.setOnClickListener(this);
        mPrev.setOnClickListener(this);
//        view.setVisibility(mNext,getItem().hasNext());
//        view.setVisibility(mPrev, getItem().hasPrev());
        mHandler.postDelayed(updateSeekBarTime, 100);

    }

    void prepare(){
        String uri=getItem().get();
        if (mMediaPlayer !=null) {release();}
        preparePath(uri);


    }
    void prepareNextStep(String path0){
//        String uri=getItem().get();
//        if (mMediaPlayer !=null) {release();}
            Uri path= Uri.parse(path0);
//            mPath=uri;
            mMediaPlayer = MediaPlayer.create(getContext(), path);
        if (mMediaPlayer==null) File.Delete(path0);
        finalTime = mMediaPlayer==null?0:mMediaPlayer.getDuration();
        timeElapsed = 0;
        mTimeline.setMax((int) finalTime);


//        view.setVisibility(mNext,getItem().hasNext());
//        view.setVisibility(mPrev, getItem().hasPrev());
        if(mMediaPlayer!=null)play();

    }
    void preparePath(final String url){


       final String localPath= getPathFromUrl(url);//Uri.parse(url)
        if (File.Exist(localPath)){
            prepareNextStep(localPath);
            return;}
        Utility.CheckInternet(getContext(), new ConnectDialog.OnDialogResultListener() {
            @Override
            public void OnDialogResult(boolean ok, boolean isConnect) {
                DownloadFiles downloadFileFromURL=new DownloadFiles();
                List<String> list=new ArrayList<>();
                list.add(url);
        downloadFileFromURL.GetFiles(getItem().mActivity,1024*30,7, getItem().mTitle, localPath.substring(localPath.lastIndexOf('/')+1),localPath, list, new DownloadFiles.DownloadCompletedListener() {
            @Override
            public void OnFileDownloaded(boolean succeed, String path) {
if (!succeed)return;
                prepareNextStep(path);
//                play();
            }

            @Override
            public void OnFinish(boolean allSucceed, String root) {
//                if (succeed)prepareNextStep(path);
            }
        });

    }
});

    }
    static String mFolderName=null;
    static String mRootFolderName=null;
    String getFolderName(){
        if (mFolderName==null){
            mFolderName=getContext().getPackageName();

            mFolderName=mFolderName.substring(mFolderName.lastIndexOf(".")+1);

        }
        return mFolderName;
    }
    String getRootFolderName(){
        if (mRootFolderName==null){
            mRootFolderName= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        }
        return mRootFolderName;
    }
    public  String getPathFromUrl(String url) {
//        if (tag.startsWith("/storage/sdcard"))
//        {
//            return tag;
//        }
//        String path = getSrcContent(tag);
        String path0 = url.trim().toLowerCase();

//        if (path0.startsWith("http://") || path0.startsWith("https://"))
//            path0 = path0.substring(path0.indexOf("//") + 2);
//        String nameSlash=path0.substring(path0.lastIndexOf("/"));
        path0=path0.substring(Text.lastIndexOf(path0,'/',0,path0.length(),2));
        path0=path0.replace("%20"," ");

        return getRootFolderName()+ "/"+getFolderName() + path0;
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void play() {
        if (mMediaPlayer==null/*&&!prepare()*/){prepare();return;}
        if (mMediaPlayer.isPlaying())mMediaPlayer.pause() ;else mMediaPlayer.start();

        mHandler.postDelayed(updateSeekBarTime, 100);
    }
    public void stop() {
        if (mMediaPlayer.isPlaying())mMediaPlayer.pause() ;
        mMediaPlayer.seekTo(0);
        setDetails();
    }

    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            if (mMediaPlayer ==null)return;
           setDetails();
            if (mMediaPlayer.isPlaying())
            mHandler.postDelayed(this, 100);
        }
    };
void setDetails(){
    if(mMediaPlayer ==null)return;
    imgPlay.setImageResource(mMediaPlayer.isPlaying()?R.drawable.ic_action_pause:R.drawable.ic_action_play);
    timeElapsed = mMediaPlayer.getCurrentPosition();

    mTimeline.setProgress((int) timeElapsed);
    mVolume.setProgress(am.getStreamVolume(AudioManager.STREAM_MUSIC));
    String t0= getTime(finalTime);
    String t1=getTime(timeElapsed);
    duration.setText(String.format("%s - %s", t1, t0));
//    Text.setText(findViewById(R.id.txt_title),getItem().code>0 ?getItem().getTitle():null, Font.TextPos.h1);

}

    public static String getTime(double mSec){
        int min= Double.valueOf(mSec/(1000*60)).intValue();
        int sec= Double.valueOf((mSec-min*1000*60)/(1000)).intValue();
        return String.format(Application.getCurrentLocale(),"%d:%d",min, sec);

    }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.img_play)play();
        else  if (v.getId()==R.id.img_stop)stop();
        else  if (v.getId()==R.id.img_next)onNextClicked(true);
        else  if (v.getId()==R.id.img_back)onNextClicked(false);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser||mMediaPlayer==null) return;
         if (seekBar.equals(mTimeline)){

        mMediaPlayer.seekTo(progress);
        }else am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0); //mMediaPlayer.setVolume(progress,progress);
        setDetails();
    }
    public void setVolume(boolean up){
//        int volumeLevel = am.getStreamVolume(AudioManager.STREAM_MUSIC);
//        am.setStreamVolume(AudioManager.STREAM_MUSIC,up?++volumeLevel:--volumeLevel,0);

//        while ( am.getStreamVolume(AudioManager.STREAM_MUSIC) <15){// am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, up?AudioManager.ADJUST_RAISE:AudioManager.ADJUST_LOWER, 0);
//        }

        setDetails();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    public static void release(){
try {
    if (mMediaPlayer!=null){
    mMediaPlayer.release();
    mMediaPlayer = null;}

//    if (mPath != null && mPath.endsWith("temp")) File.Delete(mPath);

}
catch (Exception ex){ex.printStackTrace();
}
    }
    OnNextClickListener mNextListener=null;
    public void setNextListener(OnNextClickListener l){
        mNextListener=l;
    }
    void onNextClicked(boolean next){
        if (getItem().hasNext()||getItem().hasPrev()) {
            getItem().getNext(next);
            prepare();
//        play();
            mNextListener.onClickListener(next);
        }else {
            try {
                int current=mMediaPlayer.getCurrentPosition()+((next?1:-1)*step);
                if (current<0)current=0;
                else if (current>mMediaPlayer.getDuration())current=0;
                mMediaPlayer.seekTo(current);
            }catch (Exception ex){ex.printStackTrace();}

        }
    }
    int step=10000;
    public interface OnNextClickListener{
        void onClickListener(boolean next);
    }
}
