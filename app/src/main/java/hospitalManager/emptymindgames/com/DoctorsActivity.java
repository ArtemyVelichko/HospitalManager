package hospitalManager.emptymindgames.com;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Pair;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hospitalManager.emptymindgames.com.Services.ServiceMusic;
import hospitalManager.emptymindgames.com.data.DataGame;
import hospitalManager.emptymindgames.com.data.Doctor;
import hospitalManager.emptymindgames.com.databinding.ActivityDoctorsBinding;


public class DoctorsActivity extends AppCompatActivity {


    private long[] UpgradesPrices = {750, 4500, 22000, 110000, 555000, 2750000, 14000000, 70000000, 350000000, 1750000000,
            15000, 90000, 440000, 2200000, 11100000, 55000000, 280000000, 1400000000, 7000000000L, 35000000000L,
            50000, 270000, 1320000, 6600000, 33300000, 165000000, 840000000, 4200000000L, 21000000000L, 105000000000L,
            150000, 810000, 3960000, 19800000, 99900000, 495000000, 2520000000L, 12600000000L, 63000000000L, 315000000000L,
            750000, 4050000, 19800000, 99000000, 499500000, 2475000000L, 12600000000L, 63000000000L, 315000000000L, 1575000000000L};


    public static long mAllMoney;
    private int isStartedNewGame;
    public long mAllPeople;
    public long mSickPeople;
    public long mDeadPeople;
    public int mDays;
    public double mGrowthPatiens;
    public double mGrowthDeadPatiens;

    Intent musicIntent;

    public boolean isActivityChanged = false;

    private HospitalViewModel hospitalViewModel;
    List<Doctor> mListDoctors = new ArrayList<>();

    ActivityDoctorsBinding binding;

    RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeScreenComponents();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctors);
        hospitalViewModel = ViewModelProviders.of(this).get(HospitalViewModel.class);

        hospitalViewModel.getDataGame().observe(this, data -> {
            isStartedNewGame = data.get(0).getIsStartedNewGame();
            mAllMoney = data.get(0).getAllMoney();
            mAllPeople = data.get(0).getAllPeople();
            mSickPeople = data.get(0).getSickPeople();
            mDeadPeople = data.get(0).getDeadPeople();
            mDays = data.get(0).getDays();
            mGrowthDeadPatiens = data.get(0).getGrowthDeadPatiens();
            mGrowthPatiens = data.get(0).getGrowthPatiens();
            binding.amount.setText(getString(R.string.amount_of_money, convertNumberToString(mAllMoney)));
            binding.allPeopleSecond.setText(getString(R.string.all_people_second, convertNumberToString(mAllPeople)));
            binding.sickPeopleSecond.setText(getString(R.string.sick_people_second, convertNumberToString(mSickPeople)));
            binding.deadPeopleSecond.setText(getString(R.string.dead_people_second, convertNumberToString(mDeadPeople)));
            binding.days.setText(getString(R.string.days, mDays));
        });


        binding.buttonHospital.setOnClickListener(v -> {
            isActivityChanged = true;
            MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
            mPlayer.setVolume(30, 30);
            mPlayer.start();
            onBackPressed();
        });

        binding.buttonSettings.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                startActivity(new Intent(DoctorsActivity.this, StartActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            } else {
                startActivity(new Intent(DoctorsActivity.this, StartActivity.class));
            }
            finish();

        });
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

    @Override
    public void onBackPressed() {
        isActivityChanged = true;
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
                    Pair.create(binding.sickPeopleSecond, "agreedName2"));
            startActivity(new Intent(DoctorsActivity.this, MainActivity.class), options.toBundle());
        } else {

            startActivity(new Intent(DoctorsActivity.this, MainActivity.class));
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListDoctors = getDisplayedHospitals(hospitalViewModel.getmManagerList());
        RecyclerView recyclerView = binding.recyclerView;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DoctorAdapter(mListDoctors, getApplicationContext(), new DoctorAdapter.onImageClickListener() {
            @Override
            public void clickOnButton(int id, int state, Button button, int position) {

                long price = hospitalViewModel.getmManagerList().get(id).getDoctorPrice();
                switch (state) {
                    case 3:
                        int time = Math.round(hospitalViewModel.getAllMyHospitals().get(id).getTime() / 2);
                        hospitalViewModel.updateTimeById(id, time);
                        break;
                    case 4:
                        hospitalViewModel.updateMultiplierPeople(id, 2);
                        break;
                    case 5:
                        int multiplier = hospitalViewModel.getAllMyHospitals().get(id).getMultiplier() * 2;
                        hospitalViewModel.updateMultiplier(id, multiplier);
                        break;
                    case 6:
                        int myTime = Math.round(hospitalViewModel.getAllMyHospitals().get(id).getTime() / 2);
                        hospitalViewModel.updateTimeById(id, myTime);
                        button.setClickable(false);
                        mListDoctors.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        break;
                }
                DataGame newDataGame = new DataGame(0, isStartedNewGame, mAllMoney - price, mAllPeople, mSickPeople, mDeadPeople, mDays, mGrowthPatiens, mGrowthDeadPatiens);
                hospitalViewModel.updateMainDataGame(newDataGame);
                price = revealPrice(price);
                Doctor doctor = new Doctor(id, price, state);
                hospitalViewModel.updateDoctor(doctor);
                binding.amount.setText(getString(R.string.amount_of_money, convertNumberToString(mAllMoney)));
            }
        });

        recyclerView.setAdapter(mAdapter);

        isActivityChanged = false;
        musicIntent = new Intent(this, ServiceMusic.class);
        startService(musicIntent);
    }


    public List<Doctor> getDisplayedHospitals(List<Doctor> list) {
        List<Doctor> finalList = new ArrayList<>();
        for (Doctor t : list) {
            if (t.getState() > 0 && t.getState() < 6) {
                finalList.add(t);
            }
        }

        return finalList;
    }

    public long revealPrice( long price) {
        int index= 0;
        for (int i = 0; i < 50 ; i++) {
            if(price == UpgradesPrices[i]){
                index = i;
            }
        }
        if(index > 39){
            return price;
        }
        return  UpgradesPrices[index + 10];
    }


    public void initializeScreenComponents() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAllowEnterTransitionOverlap(true);
    }
}

