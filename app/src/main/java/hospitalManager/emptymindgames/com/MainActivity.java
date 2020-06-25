package hospitalManager.emptymindgames.com;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.transition.Explode;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hospitalManager.emptymindgames.com.Dialogs.DefeatDialog;
import hospitalManager.emptymindgames.com.Dialogs.WinDialog;
import hospitalManager.emptymindgames.com.Services.ServiceMusic;
import hospitalManager.emptymindgames.com.data.DataGame;
import hospitalManager.emptymindgames.com.data.Doctor;
import hospitalManager.emptymindgames.com.data.Hospital;
import hospitalManager.emptymindgames.com.databinding.ActivityMainBinding;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements DefeatDialog.DialogListener, WinDialog.DialogListener {

    public static long mAllMoney;
    public long mAllPeople;
    public long mSickPeople;
    public long mDeadPeople;
    public int mDays;
    public double mGrowthPatiens;
    public double mGrowthDeadPatiens;
    private int isStartedNewGame;

    private boolean[] isHospitalRunning = new boolean[10];
    public final static String TAG = "ArtemyDebugger";
    private HospitalViewModel hospitalViewModel;
    List<Hospital> mListHospitals = new ArrayList<>(10);
    ProgressBar[] ProgressBarArray = new ProgressBar[10];
    List<HospitalTimer> TimerHospitalsList = new ArrayList<>(10);
    List<EndLessTimer> EndLessTimerHospitalsList = new ArrayList<>(10);
    private boolean isActivityChanged = false;
    static private EndLessEpidemic endLessEpidemic;
    private boolean isEpidemicStarted = false;

    PerfectLoopMediaPlayer mediaPlayer;
    ActivityMainBinding binding;
    Intent musicIntent;
    RecyclerView.Adapter mAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeScreenComponents();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        hospitalViewModel = ViewModelProviders.of(this).get(HospitalViewModel.class);
        initializeComponents();

        hospitalViewModel.getDataGame().observe(this, data -> {
            isStartedNewGame = data.get(0).getIsStartedNewGame();
            mAllMoney = data.get(0).getAllMoney();
            mAllPeople = data.get(0).getAllPeople();
            mSickPeople = data.get(0).getSickPeople();
            mDeadPeople = data.get(0).getDeadPeople();
            mDays = data.get(0).getDays();
            mGrowthDeadPatiens = data.get(0).getGrowthDeadPatiens();
            mGrowthPatiens = data.get(0).getGrowthPatiens();

            if (mAllPeople > 0 && mSickPeople <= 0) {
                finishGame(mAllPeople, mDeadPeople, true);
            } else if (mAllPeople == 0 && mSickPeople <= 0) {
                finishGame(0, 0, false);
            }
            binding.amount.setText(getString(R.string.amount_of_money, convertNumberToString(mAllMoney)));
            binding.allPeopleSecond.setText(getString(R.string.all_people_second, convertNumberToString(mAllPeople)));
            binding.sickPeopleSecond.setText(getString(R.string.sick_people_second, convertNumberToString(mSickPeople)));
            binding.deadPeopleSecond.setText(getString(R.string.dead_people_second, convertNumberToString(mDeadPeople)));
            binding.days.setText(getString(R.string.days, mDays));
        });
        binding.buttonDoctor.setOnClickListener((v) -> {
            buttonDoctorPressed();
        });

        binding.buttonSettings.setOnClickListener((v) -> {
            buttonSettingPressed();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityChanged = false;
        musicIntent = new Intent(this, ServiceMusic.class);
        startService(musicIntent);
        recyclerView = binding.recyclerView;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyHospitalAdapter(mListHospitals, getApplicationContext(), new MyHospitalAdapter.onImageClickListener() {

            @Override
            public void setProgressBars(int id, ProgressBar progressBar) {
                ProgressBarArray[id] = progressBar;
            }

            @Override
            public void clickOnImage(int id, ProgressBar progressBar) {

                if (hospitalViewModel.getAllMyHospitals().get(id).getAmount() == 0)
                    return;

                if (hospitalViewModel.getmManagerList().get(id).getState() > 1) {
                    return;
                }

                startTimer(id);
            }

            @Override
            public void onTextViewOfPriceSelected(Hospital hospital) {
                DataGame dataGame = new DataGame(0, isStartedNewGame, mAllMoney, mAllPeople, mSickPeople, mDeadPeople, mDays, mGrowthPatiens, mGrowthDeadPatiens);
                hospitalViewModel.updateMainDataGame(dataGame);
                hospitalViewModel.updateHospital(hospital);
                if (hospital.getAmount() == 1) {
                    hospitalViewModel.updateStateById(hospital.getId(), 1);
                    if (hospital.getId() != 9) {
                        mListHospitals.add(hospitalViewModel.getAllMyHospitals().get(hospital.getId() + 1));
                        mAdapter.notifyItemInserted(hospital.getId() + 1);
                    }
                }

            }

            @Override
            public void startHospital(int id, GifImageView gifImageView) {

                if (hospitalViewModel.getmManagerList().get(id).getState() < 2 && hospitalViewModel.getAllMyHospitals().get(id).getTime() < 1000 || hospitalViewModel.getAllMyHospitals().get(id).getTime() >= 1000) {
                    gifImageView.setVisibility(View.INVISIBLE);

                } else {
                    ProgressBarArray[id].setProgressTintList(ColorStateList.valueOf(getApplicationContext().getColor(R.color.green)));
                    ProgressBarArray[id].setProgress(20);
                }

                startEndLessTimer(id, gifImageView);
            }

            @Override
            public void finishTimer(int id, int value) {
                if (value != 100) {

                    isHospitalRunning[id] = true;
                    int time = hospitalViewModel.getAllMyHospitals().get(id).getTime();
                    TimerHospitalsList.set(id, null);
                    TimerHospitalsList.set(id, new HospitalTimer(time * 10, time / (100 - value)));
                    TimerHospitalsList.get(id).setId(id);
                    TimerHospitalsList.get(id).setProgressBarValue(value);
                    TimerHospitalsList.get(id).setIncrement(1);
                    TimerHospitalsList.get(id).start();
                }


            }

            @Override
            public void motivatorArchieved(int id) {
                int time = hospitalViewModel.getAllMyHospitals().get(id).getTime();
                time = (int) (time - time * 0.05);
                hospitalViewModel.updateTimeById(id, time);
            }
        });

        recyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
//
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isEpidemicStarted)
            startEpidemic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataGame dataGame = new DataGame(0, isStartedNewGame, mAllMoney, mAllPeople, mSickPeople, mDeadPeople, mDays, mGrowthPatiens, mGrowthDeadPatiens);
        hospitalViewModel.updateMainDataGame(dataGame);

        pauseGame();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTimers();
    }

    @Override
    protected void onDestroy() {
        stopTimers();
        super.onDestroy();
    }

    public class HospitalTimer extends CountDownTimer {

        private int id;
        private int increment;
        boolean isEndLess = false;
        private int progressBarValue;

        public void setId(int id) {
            this.id = id;
        }

        public void setIncrement(int increment) {
            this.increment = increment;
        }


        public void setProgressBarValue(int progressBarValue) {
            this.progressBarValue = progressBarValue;
            if (progressBarValue > 0 && hospitalViewModel.getmManagerList().get(id).getState() > 1) {
                isEndLess = true;
            }

        }

        public HospitalTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        @Override
        public void onTick(long millisUntilFinished) {


            if (progressBarValue != 100)
                progressBarValue += increment;
            ProgressBarArray[id].setProgress(progressBarValue);
            hospitalViewModel.updateValueProgressBarById(id, progressBarValue);
            if (progressBarValue == 100) {
                isHospitalRunning[id] = false;
                long mHealthyPeople = hospitalViewModel.getAllMyHospitals().get(id).getMultiplierPeople() * hospitalViewModel.getAllMyHospitals().get(id).getAmount();
                mAllMoney += hospitalViewModel.getAllMyHospitals().get(id).getMultiplier() * hospitalViewModel.getAllMyHospitals().get(id).getAmount() * hospitalViewModel.getAllMyHospitals().get(id).getMultiplierPeople();
                mSickPeople -= mHealthyPeople;
                mAllPeople += mHealthyPeople;
                DataGame dataGame = new DataGame(0, isStartedNewGame, mAllMoney, mAllPeople, mSickPeople, mDeadPeople, mDays, mGrowthPatiens, mGrowthDeadPatiens);
                hospitalViewModel.updateMainDataGame(dataGame);
                ProgressBarArray[id].setProgress(100);
                ProgressBarArray[id].setProgress(0);
                hospitalViewModel.updateValueProgressBarById(id, 0);


                new Handler().post(() -> {
                    this.cancel();
                });


            }
        }

        @Override
        public void onFinish() {

        }
    }

    public void startTimer(int id) {
        if (!isHospitalRunning[id]) {
            isHospitalRunning[id] = true;
            int time = hospitalViewModel.getAllMyHospitals().get(id).getTime();

            TimerHospitalsList.set(id, null);


            if (time <= 600) {
                TimerHospitalsList.set(id, new HospitalTimer(time, time));
                TimerHospitalsList.get(id).setIncrement(100);
            }

            if (time < 1000) {
                TimerHospitalsList.set(id, new HospitalTimer(time * 10, time / 10));
                TimerHospitalsList.get(id).setIncrement(10);
            } else if (time < 2000) {
                TimerHospitalsList.set(id, new HospitalTimer(time * 10, time / 20));
                TimerHospitalsList.get(id).setIncrement(5);
            } else {
                TimerHospitalsList.set(id, new HospitalTimer(time * 10, time / 100));
                TimerHospitalsList.get(id).setIncrement(1);

            }

            TimerHospitalsList.get(id).setProgressBarValue(0);
            TimerHospitalsList.get(id).setId(id);

            TimerHospitalsList.get(id).start();
        }
    }


    public void startEpidemic() {
        endLessEpidemic = new EndLessEpidemic(100000000, 10000);
        endLessEpidemic.start();
    }


    public class EndLessEpidemic extends CountDownTimer {
        public EndLessEpidemic(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        int i = 0;

        @Override
        public void onTick(long millisUntilFinished) {

            if (i == 0) {
                i++;
                return;
            }

            long newDeadPeople = (long) Math.ceil(mSickPeople * mGrowthDeadPatiens / 100);
            mDays++;
            mDeadPeople += newDeadPeople;
            long newSickPeople = (long) Math.ceil(Math.pow(mGrowthPatiens, mDays) * mSickPeople);
            if (mAllPeople == 0) {
                mSickPeople -= newDeadPeople;
            } else if (newSickPeople - mSickPeople > mAllPeople) {
                mSickPeople += mAllPeople;
                mSickPeople -= newDeadPeople;
                mAllPeople = 0;
            } else {
                mAllPeople -= (newSickPeople - mSickPeople);
                mSickPeople = newSickPeople;
                mSickPeople -= newDeadPeople;
            }
            DataGame dataGame = new DataGame(0, isStartedNewGame, mAllMoney, mAllPeople, mSickPeople, mDeadPeople, mDays, mGrowthPatiens, mGrowthDeadPatiens);
            hospitalViewModel.updateMainDataGame(dataGame);
        }

        @Override
        public void onFinish() {
        }


    }

    public void finishGame(long healthPeople, long deadPeople,
                           boolean isConquering) {

        isEpidemicStarted = false;
        endLessEpidemic.cancel();
        stopService(musicIntent);
        pauseGame();
        setDataToDefault();
        if (isConquering) {
            mediaPlayer = PerfectLoopMediaPlayer.create(getApplicationContext(), R.raw.win_music);
            mediaPlayer.setVolume(50, 50);
            mediaPlayer.start();
            WinDialog winDialog = new WinDialog(healthPeople, deadPeople);
            winDialog.show(getSupportFragmentManager(), "lala");
        } else {
            mediaPlayer = PerfectLoopMediaPlayer.create(getApplicationContext(), R.raw.defeat_music);
            mediaPlayer.setVolume(30, 30);
            mediaPlayer.start();
            DefeatDialog defeatDialog = new DefeatDialog();
            defeatDialog.show(getSupportFragmentManager(), "lala");
        }
    }

    public void setDataToDefault() {
        hospitalViewModel.updateMainDataGame(DataGame.populate());
        hospitalViewModel.updateDoctor(Doctor.populate());
        hospitalViewModel.updateHospital(Hospital.populate());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, StartActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }


    public static String convertNumberToString(Long number) {
        String s = number.toString();
        String newString = "";
        int counter = 0;
        String space = " ";
        for (int i = s.length() - 1; i >= 0; i--) {

            newString += s.charAt(i);
            if (counter == 2) {
                counter = -1;
                newString += space;
            }
            counter++;

        }

        newString = new StringBuffer(newString).reverse().toString();

        return newString;
    }


    public class EndLessTimer extends CountDownTimer {
        private int id;
        private GifImageView gifImageView;


        public void setGifImageView(GifImageView gifImageView) {
            this.gifImageView = gifImageView;
        }

        public void setId(int id) {
            this.id = id;
        }

        public EndLessTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            if (hospitalViewModel.getAllMyHospitals().get(id).getAmount() > 0 && hospitalViewModel.getAllMyHospitals().get(id).getTime() < 1000) {
                long mHealthyPeople = hospitalViewModel.getAllMyHospitals().get(id).getMultiplierPeople() * hospitalViewModel.getAllMyHospitals().get(id).getAmount();
                mAllMoney += hospitalViewModel.getAllMyHospitals().get(id).getMultiplier() * hospitalViewModel.getAllMyHospitals().get(id).getAmount() * hospitalViewModel.getAllMyHospitals().get(id).getMultiplierPeople();
                mSickPeople -= mHealthyPeople;
                mAllPeople += mHealthyPeople;
                DataGame dataGame = new DataGame(0, isStartedNewGame, mAllMoney, mAllPeople, mSickPeople, mDeadPeople, mDays, mGrowthPatiens, mGrowthDeadPatiens);
                hospitalViewModel.updateMainDataGame(dataGame);
            } else {
                startTimer(id);
            }

        }

        @Override
        public void onFinish() {
        }
    }


    public void stopTimers() {
        for (int i = 0; i < 10; i++) {
            if (EndLessTimerHospitalsList.get(i) != null) {

                EndLessTimerHospitalsList.get(i).cancel();
                EndLessTimerHospitalsList.set(i, null);
            }
            if (TimerHospitalsList.get(i) != null) {

                TimerHospitalsList.get(i).cancel();
                TimerHospitalsList.set(i, null);
            }

            isHospitalRunning[i] = false;
        }
        endLessEpidemic.cancel();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        pauseGame();
        mediaPlayer.pause();
        setDataToDefault();
        startActivity(new Intent(MainActivity.this, StartActivity.class));
    }

    @Override
    public void onClickPositive(DialogFragment dialog) {
        pauseGame();
        setDataToDefault();
        mediaPlayer.pause();
        startActivity(new Intent(MainActivity.this, StartActivity.class));
    }

    public void pauseGame() {
        hospitalViewModel.onCleared();
        isEpidemicStarted = false;
        stopTimers();

        for (int i = 0; i < 10; i++) {
            isHospitalRunning[i] = false;
        }


        if (!isActivityChanged)
            stopService(musicIntent);
    }

    public List<Hospital> getDisplayedHospitals(List<Hospital> list) {
        int amountDisplayedList = 0;
        List<Hospital> finalList = new ArrayList<>();
        for (Hospital t : list) {
            if (t.getAmount() > 0) {
                finalList.add(t);
                amountDisplayedList++;
            }
        }

        if (amountDisplayedList != 10) {
            finalList.add(list.get(amountDisplayedList));
        }
        return finalList;
    }

    public void startEndLessTimer(int id, GifImageView gifImageView) {


        int time = hospitalViewModel.getAllMyHospitals().get(id).getTime();
        if (hospitalViewModel.getmManagerList().get(id).getState() > 1 && !isHospitalRunning[id]) {
            if (time < 1000) {
                isHospitalRunning[id] = true;
                EndLessTimerHospitalsList.set(id, null);
                EndLessTimerHospitalsList.set(id, new EndLessTimer(1000000000000L, time));
                EndLessTimerHospitalsList.get(id).setId(id);
                EndLessTimerHospitalsList.get(id).setGifImageView(gifImageView);
                EndLessTimerHospitalsList.get(id).start();
            } else {
                EndLessTimerHospitalsList.set(id, null);
                EndLessTimerHospitalsList.set(id, new EndLessTimer(10000000000000L, time / 100));
                EndLessTimerHospitalsList.get(id).setId(id);
                EndLessTimerHospitalsList.get(id).setGifImageView(gifImageView);
                EndLessTimerHospitalsList.get(id).start();
            }

        }
    }

    private void initializeComponents() {
        for (int i = 0; i < 10; i++) {
            EndLessTimerHospitalsList.add(null);
            TimerHospitalsList.add(null);
            mListHospitals.add(null);
        }

        mAllPeople = hospitalViewModel.getAllPeople();
        mDeadPeople = hospitalViewModel.getDeadPeople();
        mSickPeople = hospitalViewModel.getSickPeople();
        mDays = hospitalViewModel.getDays();
        mGrowthPatiens = hospitalViewModel.getGrowthPatiens();
        mAllMoney = hospitalViewModel.getMoney();
        mGrowthDeadPatiens = hospitalViewModel.getGrowthDeadPatiens();
        isStartedNewGame = hospitalViewModel.getIsStartedNewGame();

        if (isStartedNewGame == 0) {
            isStartedNewGame++;
            DataGame dataGame = new DataGame(0, isStartedNewGame, mAllMoney, mAllPeople, mSickPeople, mDeadPeople, mDays, mGrowthPatiens, mGrowthDeadPatiens);
            hospitalViewModel.updateMainDataGame(dataGame);
        }
        mListHospitals = getDisplayedHospitals(hospitalViewModel.getAllMyHospitals());
        startEpidemic();
    }

    public void initializeScreenComponents(){
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAllowEnterTransitionOverlap(true);
    }

    public void buttonDoctorPressed(){
        MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
        mPlayer.setVolume(30, 30);
        mPlayer.start();
        isActivityChanged = true;

        Intent intent = new Intent(MainActivity.this, DoctorsActivity.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            //startActivity(new Intent(DoctorsActivity.this, MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create(binding.allPeopleFirst, "allpeoplefirst"),
                    Pair.create(binding.allPeopleSecond, "agreedName2"),
                    Pair.create(binding.amount, "agreedName2"),
                    Pair.create(binding.deadPeopleFirst, "agreedName2"),
                    Pair.create(binding.deadPeopleSecond, "agreedName2"),
                    Pair.create(binding.days, "agreedName2"),
                    Pair.create(binding.imageSettings, "agreedName2"),
                    Pair.create(binding.imageHospital, "agreedName2"),
                    Pair.create(binding.imageDoctor, "agreedName2"),
                    Pair.create(binding.sickPeopleFirst, "agreedName2"),
                    Pair.create(binding.sickPeopleSecond, "agreedName2"),
                    Pair.create(binding.backgroundButtons, "alla"),
                    Pair.create(binding.recyclerView, "slksd"));
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    public void buttonSettingPressed(){
        MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
        mPlayer.setVolume(30, 30);
        mPlayer.start();
        pauseGame();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            startActivity(new Intent(MainActivity.this, StartActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(new Intent(MainActivity.this, StartActivity.class));
        }
        finish();
    }


}


