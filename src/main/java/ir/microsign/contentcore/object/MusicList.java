package ir.microsign.contentcore.object;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 23/04/2016.
 */
public class MusicList extends BaseObject {
    List<String> mList=null;int current=0;
   public String mTitle=null;
    public Activity mActivity=null;
    public MusicList(Activity activity,String title,String list){
        mTitle=title;
        mList=new ArrayList<>();
        if (Text.isNullOrEmpty(list))return;
        Collections.addAll(mList, list.split(";"));
mActivity=activity;
    }
//    public void  releasePlayer(){
//        if (MusicListView.mMediaPlayer==null)return;
//        MusicListView.mMediaPlayer.release();
//        MusicListView.mMediaPlayer=null;
//    }
    public int getCount(){return mList.size();}
    public int getCurrentIndex(){
        return current;
    }
    public boolean hasPrev(){
        return getCount()>0&&current>0;
    }public boolean hasNext(){
        return getCount()>0&&current<getCount()-1;
    }
    public String get(int i){
        if (mList.size()<1)return null;
        if (i<0) current=getCount()-1;
        else current=i%getCount();
        return mList.get(current);
    }
    public String get(){
        return get(current);
    }
    public String getNext(boolean next){
        return get(current+(next?1:-1));
    }


}
