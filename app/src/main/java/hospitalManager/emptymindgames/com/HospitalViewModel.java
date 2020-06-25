package hospitalManager.emptymindgames.com;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hospitalManager.emptymindgames.com.data.AppDatabase;
import hospitalManager.emptymindgames.com.data.DataGame;
import hospitalManager.emptymindgames.com.data.DataGameDao;
import hospitalManager.emptymindgames.com.data.Doctor;
import hospitalManager.emptymindgames.com.data.DoctorDao;
import hospitalManager.emptymindgames.com.data.Hospital;
import hospitalManager.emptymindgames.com.data.HospitalDao;

public class HospitalViewModel extends AndroidViewModel {

    private AppDatabase mAppDatabase;
    private HospitalDao hospitalDao;
    private DataGameDao dataGameDao;
    private DoctorDao doctorDao;


    @Nullable
    private LiveData<List<Hospital>> mAllHospitals;

    private LiveData<List<DataGame>> mMainDataGameInformation;


    @Nullable
    private LiveData<List<Doctor>> mDoctors;

    private List<Integer> mDoctorStatesList;


    private long deadPeople;
    private int isStartedNewGame;
    private long sickPeople;
    private long AllPeople;
    private int days;
    private double growthPatiens;
    private double growthDeadPatiens;

    private int mMoney;


    public int getMoney() {
        return mMoney;
    }


    public HospitalViewModel(@NonNull Application application) {
        super(application);
        mAppDatabase = AppDatabase.getDataBase(application);
        hospitalDao = mAppDatabase.businessDao();
        dataGameDao = mAppDatabase.dataGame();
        doctorDao = mAppDatabase.managerDao();
        mAllHospitals = hospitalDao.getAllLiveDataBusiness();
        mMainDataGameInformation = dataGameDao.getAll();
        mDoctorStatesList = doctorDao.getDoctorsStates();
        mDoctors = doctorDao.getAllDoctors();
        mMoney = dataGameDao.getMoneyFromDataBase();

        deadPeople = dataGameDao.getDeadPeople();
        sickPeople = dataGameDao.getSickPeople();
        AllPeople = dataGameDao.getAllPeople();
        days = dataGameDao.getDays();
        growthPatiens = dataGameDao.getGrowthPatiens();
        growthDeadPatiens = dataGameDao.getGrowthDeadPatiens();
        isStartedNewGame = dataGameDao.getIsStartedNewGame();

    }


    LiveData<List<Hospital>> getAllHospitals() {
        return mAllHospitals;
    }

    LiveData<List<DataGame>> getDataGame() {
        return mMainDataGameInformation;
    }

    public LiveData<List<Doctor>> getDoctors() {
        return mDoctors;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void updateHospital(Hospital... hospital) {
        new Thread(() -> hospitalDao.update(hospital)).start();
    }

    public void updateMainDataGame(DataGame... dataGame) {
        new Thread(() -> dataGameDao.update(dataGame)).start();
    }

    public void updateDoctor(Doctor... doctor) {
        new Thread(() -> doctorDao.update(doctor)).start();
    }

    public List<Hospital> getAllMyHospitals() {
        return hospitalDao.getAllBusiness();
    }

    public List<Doctor> getmManagerList() {
        return doctorDao.getDoctors();
    }

    public List<Integer> getDoctorsStates() {
        return mDoctorStatesList;
    }

    public void updateMultiplier(int id, int multiplier) {
        new Thread(() -> hospitalDao.updateMultiplierById(id, multiplier)).start();
    }




    public long getDeadPeople() {
        return deadPeople;
    }

    public long getSickPeople() {
        return sickPeople;
    }

    public long getAllPeople() {
        return AllPeople;
    }

    public int getDays() {
        return days;
    }

    public double getGrowthPatiens() {
        return growthPatiens;
    }

    public double getGrowthDeadPatiens() {
        return growthDeadPatiens;
    }

    public int getIsStartedNewGame() {
        return isStartedNewGame;
    }


    public void updateMultiplierPeople(int id, int multiplier) {
        new Thread(() -> hospitalDao.updateMultiplierPeopleById(id, multiplier)).start();
    }

    public void updateTimeById(int id, int time) {
        new Thread(() -> hospitalDao.updateTimeById(id, time)).start();
    }


    public void updateValueProgressBarById(int id, int value) {
        new Thread(() -> hospitalDao.updateValueProgressBarById(id, value)).start();
    }

    public void updateStateById(int id, int state) {
        new Thread(() -> doctorDao.updateStateById(id, state)).start();
    }



}

