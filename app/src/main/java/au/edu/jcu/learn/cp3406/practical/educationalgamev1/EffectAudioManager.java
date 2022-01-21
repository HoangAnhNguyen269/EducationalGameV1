package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class EffectAudioManager implements SoundPool.OnLoadCompleteListener{
    private final Map<EffectSound, Integer> soundIds;
    private final SoundPool pool;
    private boolean ready;

    EffectAudioManager(Context context){
        soundIds = new HashMap<>();
        pool = new SoundPool(10,
                AudioManager.STREAM_MUSIC,
                0);
        pool.setOnLoadCompleteListener(this);

        // load order matches Sound's declaration order
        pool.load(context, R.raw.correct_ans, 0);

        pool.load(context, R.raw.incorrect_ans, 0);
        pool.load(context, R.raw.congratulation, 0);
        pool.load(context, R.raw.twitter, 0);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        this.ready = status == 0;

        // Each time a load finishes, the next loadId
        // is used to determine which enum value to use
        EffectSound sound;
//        sound = EffectSound.values()[loadId++];
        if(sampleId==1){
            sound = EffectSound.values()[0];
        } else if(sampleId==2){
            sound = EffectSound.values()[1];
        }else if(sampleId==3){
            sound = EffectSound.values()[2];
        }else{
            sound = EffectSound.values()[3];
        }
        Log.i("EffectAudioManager", "loaded sound: " + sound+ sampleId);
        soundIds.put(sound, sampleId);
    }

    boolean isReady() {
        return ready;
    }

    void play(EffectSound sound) {
        Integer id = soundIds.get(sound);
        Log.i("EffectAudioManager", "Playsound: " + sound);
        assert id != null;
        Log.i("EffectAudioManager", "id: " + id);
        pool.play(id, 1, 1,
                1, 0, 1);
    }
}
