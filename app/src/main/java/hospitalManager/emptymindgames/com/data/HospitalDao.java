package hospitalManager.emptymindgames.com.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HospitalDao {

    @Insert
    void insertAll(Hospital... hospitals);

    @Update
    void update(Hospital... hospital);

    @Delete
    void delete(Hospital hospital);

    @Query("SELECT * FROM Hospital")
    LiveData<List<Hospital>> getAllLiveDataBusiness();

    @Query("SELECT * FROM Hospital")
    List<Hospital> getAllBusiness();

    @Query("UPDATE Hospital SET multiplier = :multiplier WHERE id =  :id")
    int updateMultiplierById(int id, int multiplier);

    @Query("UPDATE Hospital SET multiplierPeople = :multiplier WHERE id =  :id")
    int updateMultiplierPeopleById(int id, int multiplier);

    @Query("UPDATE Hospital SET time= :time WHERE id =  :id")
    void  updateTimeById(int id, int time);

    @Query("UPDATE Hospital SET valueProgressBar= :value WHERE id =  :id")
    void updateValueProgressBarById(int id, int value);


}
