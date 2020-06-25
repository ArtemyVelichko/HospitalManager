package hospitalManager.emptymindgames.com;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import hospitalManager.emptymindgames.com.Dialogs.NewGameDialog;
import hospitalManager.emptymindgames.com.data.DataGame;
import hospitalManager.emptymindgames.com.data.Doctor;
import hospitalManager.emptymindgames.com.data.Hospital;
import hospitalManager.emptymindgames.com.databinding.StartActivityBinding;


public class StartActivity extends AppCompatActivity implements NewGameDialog.clickListener {

    StartActivityBinding binding;
    HospitalViewModel hospitalViewModel;
    PerfectLoopMediaPlayer mediaPlayer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.start_activity);

        hospitalViewModel = new ViewModelProvider(this).get(HospitalViewModel.class);
        int stateGame = hospitalViewModel.getIsStartedNewGame();
        if (stateGame == 0) {
            binding.continueButton.setVisibility(View.INVISIBLE);
        }

        binding.continueButton.setOnClickListener((v) -> {
            if (stateGame == 1) {
                MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
                mPlayer.start();
                mediaPlayer.stop();
                
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        });

        binding.newGameButton.setOnClickListener((v) -> {
            if (stateGame == 0) {
                mediaPlayer.stop();
                MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
                mPlayer.start();
                updateDataBase();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    startActivity(new Intent(StartActivity.this, MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                }

                finish();
            } else {
                NewGameDialog newGameDialog = new NewGameDialog();
                newGameDialog.show(getSupportFragmentManager(), "");
            }
        });


        binding.buttonInfo.setOnClickListener((v) -> {
            startActivity(new Intent(StartActivity.this, Info_Activity.class));
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        updateDataBase();

        mediaPlayer.stop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            startActivity(new Intent(StartActivity.this, MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = PerfectLoopMediaPlayer.create(getApplicationContext(), R.raw.start_activity_music);
        mediaPlayer.setVolume(30, 30);
        mediaPlayer.start();

    }

    protected void updateDataBase() {
        hospitalViewModel.updateMainDataGame(DataGame.populate());
        hospitalViewModel.updateDoctor(Doctor.populate());
        hospitalViewModel.updateHospital(Hospital.populate());
    }
}
