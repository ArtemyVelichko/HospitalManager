package hospitalManager.emptymindgames.com.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "id", unique = true)})
public class Hospital {

    @PrimaryKey
    private int id;

    private int amount;

    private int price;

    private int time;

    private int multiplier;

    private int multiplierPeople;

    private int valueProgressBar;


    public Hospital(int id, int amount, int price, int time, int multiplier, int multiplierPeople, int valueProgressBar) {
        this.id = id;
        this.amount = amount;
        this.price = price;
        this.time = time;
        this.multiplier = multiplier;
        this.multiplierPeople = multiplierPeople;
        this.valueProgressBar = valueProgressBar;

    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public int getTime() {
        return time;
    }


    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public int getMultiplierPeople() {
        return multiplierPeople;
    }

    public void setMultiplierPeople(int multiplierPeople) {
        this.multiplierPeople = multiplierPeople;
    }

    public int getValueProgressBar() {
        return valueProgressBar;
    }

    public void setValueProgressBar(int valueProgressBar) {
        this.valueProgressBar = valueProgressBar;
    }

    public static Hospital[] populate() {

        Hospital firstHospital = new Hospital(0, 1, 1, 1000, 1, 1, 0);
        Hospital secondHospital = new Hospital(1, 0, 5, 3000, 5, 1, 0);
        Hospital thirdHospital = new Hospital(2, 0, 25, 6000, 20, 1, 0);
        Hospital fourthHospital = new Hospital(3, 0, 125, 12000, 80, 1, 0);
        Hospital fifthHospital = new Hospital(4, 0, 625, 24000, 320, 1, 0);
        Hospital sixthHospital = new Hospital(5, 0, 3125, 48000, 1280, 1, 0);
        Hospital seventhHospital = new Hospital(6, 0, 15625, 96000, 5120, 1, 0);
        Hospital eighthHospital = new Hospital(7, 0, 78125, 192000, 20480, 1, 0);
        Hospital ninthHospital = new Hospital(8, 0, 390625, 384000, 81920, 1, 0);
        Hospital tenthHospital = new Hospital(9, 0, 1953125, 768000, 327680, 1, 0);
        return new Hospital[]{

                firstHospital,
                secondHospital,
                thirdHospital,
                fourthHospital,
                fifthHospital,
                sixthHospital,
                seventhHospital,
                eighthHospital,
                ninthHospital,
                tenthHospital
        };
    }
}