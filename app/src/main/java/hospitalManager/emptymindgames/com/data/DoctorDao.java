package hospitalManager.emptymindgames.com.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DoctorDao {

    @Insert
    void insert(Doctor... doctor);

    @Update
    void update(Doctor... doctor);

    @Delete
    void delete(Doctor doctor);


    @Query("SELECT * FROM Doctor")
    LiveData<List<Doctor>> getAllDoctors();

    @Query("SELECT * FROM Doctor")
    List<Doctor> getDoctors();

    @Query("SELECT `state` FROM Doctor ")
    List<Integer> getDoctorsStates();


    @Query("UPDATE doctor SET state = :state WHERE id =  :id")
    void updateStateById(int id, int state);


}
