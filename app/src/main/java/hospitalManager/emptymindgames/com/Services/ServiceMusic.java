package hospitalManager.emptymindgames.com.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import hospitalManager.emptymindgames.com.PerfectLoopMediaPlayer;
import hospitalManager.emptymindgames.com.R;

public class ServiceMusic extends Service {

    PerfectLoopMediaPlayer mediaPlayer;
    @Override
    public void onCreate() {
        mediaPlayer = PerfectLoopMediaPlayer.create(getApplication(), R.raw.main_music);
        mediaPlayer.setVolume(50, 50);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }



}
