package hospitalManager.emptymindgames.com.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DataGameDao {

    @Insert
    void insert(DataGame dataGame);

    @Update
    void update(DataGame... dataGame);

    @Delete
    void delete(DataGame dataGame);


    @Query("SELECT * FROM `datagame`")
    LiveData<List<DataGame>> getAll();

    @Query("SELECT allMoney FROM 'datagame'")
    LiveData<Long> getMoney();

    @Query("SELECT allMoney FROM datagame")
    int getMoneyFromDataBase();


    @Query("SELECT mAllPeople FROM datagame")
    long getAllPeople();

    @Query("SELECT sickPeople FROM datagame")
    long getSickPeople();

    @Query("SELECT deadPeople FROM datagame")
    long getDeadPeople();

    @Query("SELECT days FROM datagame")
    int getDays();

    @Query("SELECT growthPatiens FROM datagame")
    double getGrowthPatiens();

    @Query("SELECT growthDeadPatiens FROM datagame")
    double getGrowthDeadPatiens();

    @Query("SELECT isStartedNewGame FROM datagame")
    int getIsStartedNewGame();








}