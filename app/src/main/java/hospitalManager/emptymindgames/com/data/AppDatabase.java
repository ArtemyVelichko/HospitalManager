package hospitalManager.emptymindgames.com.data;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Hospital.class, DataGame.class, Doctor.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {


    public abstract HospitalDao businessDao();

    public abstract DataGameDao dataGame();

    public abstract DoctorDao managerDao();


    private static volatile AppDatabase businessRoomInstance;


    public static AppDatabase getDataBase(final Context context) {
        if (businessRoomInstance == null) {
            synchronized (AppDatabase.class) {
                if (businessRoomInstance == null) {
                    businessRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database").
                            allowMainThreadQueries().
                            addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    new Thread(() -> {
                                        businessRoomInstance.businessDao().insertAll(Hospital.populate());
                                        businessRoomInstance.managerDao().insert(Doctor.populate());
                                        businessRoomInstance.dataGame().insert(DataGame.populate());
                                    }).start();

                                }

                            })
                            .build();
                }
            }
        }
        return businessRoomInstance;
    }


}
