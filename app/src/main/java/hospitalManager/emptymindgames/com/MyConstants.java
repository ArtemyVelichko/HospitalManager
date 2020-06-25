package hospitalManager.emptymindgames.com;

import java.util.ArrayList;
import java.util.List;

public class MyConstants {
    public static final int PriceOfFirstBusiness = 1;
    public static final int PriceOfSecondBusiness = 5;
    public static final int PriceOfThirdBusiness = 20;
    public static final int PriceOfFourthBusiness = 500;
    public static final int PriceOfFifthBusiness = 1200;
    public static final int PriceOfSixthBusiness = 2500;
    public static final int PriceOfSeventhBusiness = 6000;
    public static final int PriceOfEightBusiness = 15000;
    public static final int PriceOfNinthBusiness = 50000;
    public static final int PriceOfTenthBusiness = 1000000;


    private static final List<Integer> Prices = new ArrayList<Integer>() {{
        add(PriceOfFirstBusiness);
        add(PriceOfSecondBusiness);
        add(PriceOfThirdBusiness);
        add(PriceOfFourthBusiness);
        add(PriceOfFifthBusiness);
        add(PriceOfSixthBusiness);
        add(PriceOfSeventhBusiness);
        add(PriceOfEightBusiness);
        add(PriceOfNinthBusiness);
        add(PriceOfTenthBusiness);
    }};

    public static List<Integer> getPrices() {
        return Prices;
    }
}
