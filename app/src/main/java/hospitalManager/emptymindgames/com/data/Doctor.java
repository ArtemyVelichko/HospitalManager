package hospitalManager.emptymindgames.com.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "id", unique = true)})
public class Doctor {
    @PrimaryKey
    int id;
    long mDoctorPrice;
    int state;


    public void setId(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public long getDoctorPrice() {
        return mDoctorPrice;
    }

    public int getState() {
        return state;
    }




    public Doctor(int id, long doctorPrice, int state) {
        this.id = id;
        this.mDoctorPrice = doctorPrice;
        this.state = state;

    }


    public static Doctor[] populate() {

        Doctor first = new Doctor(0, 750, 1);
        Doctor second = new Doctor(1, 4500, 0);
        Doctor third = new Doctor(2, 22000, 0);
        Doctor fourth = new Doctor(3, 110000, 0);
        Doctor fifth = new Doctor(4, 55000, 0);
        Doctor sixth = new Doctor(5, 2750, 0);
        Doctor seventh = new Doctor(6, 14000000,0);
        Doctor eight = new Doctor(7, 70000000, 0);
        Doctor ninth = new Doctor(8, 350000000, 0);
        Doctor tenth = new Doctor(9, 1750000000, 0);

        return new Doctor[]{

                first,
                second,
                third,
                fourth,
                fifth,
                sixth,
                seventh,
                eight,
                ninth,
                tenth
        };
    }

    public void setDoctorPrice(long doctorPrice) {
        this.mDoctorPrice = doctorPrice;
    }

    public void setState(int state) {
        this.state = state;
    }

}
