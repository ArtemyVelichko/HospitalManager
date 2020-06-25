package hospitalManager.emptymindgames.com.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "id", unique = true)})
public class DataGame {

    @PrimaryKey
    private int id;
    private int isStartedNewGame;
    private long allMoney;
    private long mAllPeople;
    private long sickPeople;
    private long deadPeople;
    private int days;
    private double growthPatiens;
    private double growthDeadPatiens;



    public DataGame(int id, int isStartedNewGame,long allMoney, long mAllPeople, long sickPeople, long deadPeople, int days,double growthPatiens, double growthDeadPatiens) {
        this.id = id;
        this.isStartedNewGame = isStartedNewGame;
        this.allMoney = allMoney;
        this.mAllPeople = mAllPeople;
        this.sickPeople = sickPeople;
        this.deadPeople = deadPeople;
        this.days = days;
        this.growthPatiens = growthPatiens;
        this.growthDeadPatiens = growthDeadPatiens;
    }

    public int getId() {
        return id;
    }

    public long getAllMoney() {
        return allMoney;
    }


    public long getSickPeople() {
        return sickPeople;
    }

    public long getDeadPeople() {
        return deadPeople;
    }


    public static DataGame populate() {
        return new  DataGame(0, 0,0, 7700000000L,100000, 0,1,1.01,1.5);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAllMoney(long allMoney) {
        this.allMoney = allMoney;
    }


    public void setSickPeople(long sickPeople) {
        this.sickPeople = sickPeople;
    }

    public void setDeadPeople(long deadPeople) {
        this.deadPeople = deadPeople;
    }

    public long getAllPeople() {
        return mAllPeople;
    }

    public void setAllPeople(long mAllPeople) {
        this.mAllPeople = mAllPeople;
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
}