package com.zk.zk_online.Utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

/**
 *
 * 声音播放类
 * Created by ZYB on 2017/7/21 0021.
 */

public class SoundPoolUtil {
    private SoundPool soundPool;
    int soundid= 0;

    public  SoundPoolUtil()
    {
        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,100);
    }

    /**
     * 单例模式
     * @return
     */
  /*  public static SoundPoolUtil newInstance()
    {
        if (soundPool==null)
            soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,100);
        return new SoundPoolUtil();
    }
*/
    /**
     * 播放声音
     * @param resid 资源id
     * @param context   上下文对象
     */
    public  void playSoundPool(int resid, Context context, final boolean isloop)
    {

        try {
            soundid = soundPool.load(context,resid,1);
            final int finalSoundid = soundid;
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    if (isloop)
                        soundPool.play(finalSoundid,1,1,0,-1,1f);
                    else  soundPool.play(finalSoundid,1,1,0,0,1f);
                    Log.i("sound_play", finalSoundid +"");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //释放播放资源
    public void Soundrelease()
    {
        soundPool.stop(soundid);
    }

}
