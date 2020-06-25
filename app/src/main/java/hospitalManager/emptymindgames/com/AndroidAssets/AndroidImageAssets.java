package hospitalManager.emptymindgames.com.AndroidAssets;




import java.util.ArrayList;
import java.util.List;

import hospitalManager.emptymindgames.com.R;


public class AndroidImageAssets {


    private static final List<Integer> pictures = new ArrayList<Integer>(){{
        add(R.drawable.first_hospital);
        add(R.drawable.second_hospital);
        add(R.drawable.third_hospital);
        add(R.drawable.fourth_hospital);
        add(R.drawable.fifth_hospital);
        add(R.drawable.sixth_hospital);
        add(R.drawable.seventh_hospital);
        add(R.drawable.eight_hospital);
        add(R.drawable.ninth_hospital);
        add(R.drawable.tenth_hospital);
    }};

    public static List<Integer > getPictures() {
        return pictures;
    }
}