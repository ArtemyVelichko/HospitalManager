package hospitalManager.emptymindgames.com;

import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hospitalManager.emptymindgames.com.AndroidAssets.AndroidImageAssets;
import hospitalManager.emptymindgames.com.AndroidAssets.AssetsUpgradeStrings;
import hospitalManager.emptymindgames.com.data.Doctor;
import hospitalManager.emptymindgames.com.databinding.DoctorFragmentBinding;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder> {
    private List<Doctor> mDoctorList;
    private Context context;

    public onImageClickListener mCallBack;
    DoctorFragmentBinding binding;

    private long[] UpgradesPrices = {750, 4500, 22000, 110000, 555000, 2750000, 14000000, 70000000, 350000000, 1750000000,
            15000, 90000, 440000, 2200000, 11100000, 55000000, 280000000, 1400000000, 7000000000L, 35000000000L,
            50000, 270000, 1320000, 6600000, 33300000, 165000000, 840000000, 4200000000L, 21000000000L, 105000000000L,
            150000, 810000, 3960000, 19800000, 99900000, 495000000, 2520000000L, 12600000000L, 63000000000L, 315000000000L,
            750000, 4050000, 19800000, 99000000, 499500000, 2475000000L, 12600000000L, 63000000000L, 315000000000L, 1575000000000L};


    public interface onImageClickListener {
        void clickOnButton(int id, int state, Button button, int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.doctor_fragment, parent, false);

        MyViewHolder vh = new MyViewHolder(binding.constraintLayout, binding.imageDoctor, binding.buttonDoctor, binding.textDoctor, binding.nameHospital, binding.secondTextDoctor, binding.imageViewGreen);
        return vh;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageView imageGreen;
        Button buy;
        TextView information;
        TextView nameHospital;
        TextView influenceInformation;

        public MyViewHolder(@NonNull View itemView, ImageView imageView, Button buy, TextView information, TextView nameHospital, TextView influenceInformation, ImageView imageGreen) {
            super(itemView);
            this.imageView = imageView;
            this.buy = buy;
            this.information = information;
            this.nameHospital = nameHospital;
            this.influenceInformation = influenceInformation;
            this.imageGreen = imageGreen;

        }
    }


    public DoctorAdapter(List<Doctor> mDoctorList, Context context, onImageClickListener listener) {
        this.mDoctorList = mDoctorList;
        this.context = context;
        this.mCallBack = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Doctor currentItem = mDoctorList.get(position);
        holder.imageView.setImageResource(AndroidImageAssets.getPictures().get(currentItem.getId()));
        holder.buy.setText(context.getString(R.string.buy, currentItem.getDoctorPrice()));
        holder.nameHospital.setText(AssetsUpgradeStrings.getHospitalsNames().get(currentItem.getId()));
        holder.information.setTypeface(null, Typeface.BOLD);
        if (currentItem.getState() > 5) {
            holder.information.setText(context.getString(AssetsUpgradeStrings.getUpgradesName().get(currentItem.getId() + 10 * 4), currentItem.getDoctorPrice()));
            holder.influenceInformation.setText(AssetsUpgradeStrings.getInfluence_strings().get(4));
        } else if (currentItem.getState() == 0) {
            holder.information.setText(context.getString(AssetsUpgradeStrings.getUpgradesName().get(currentItem.getId()), currentItem.getDoctorPrice()));
            holder.influenceInformation.setText(AssetsUpgradeStrings.getInfluence_strings().get(0));
        } else {
            holder.information.setText(context.getString(AssetsUpgradeStrings.getUpgradesName().get(currentItem.getId() + 10 * (currentItem.getState() - 1)), currentItem.getDoctorPrice()));
            binding.secondTextDoctor.setText(AssetsUpgradeStrings.getInfluence_strings().get(currentItem.getState() - 1));
        }

        Animation rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.fragment_fade_enter);
        binding.buttonDoctor.setText(context.getString(R.string.buy, currentItem.getDoctorPrice()));
        binding.buttonDoctor.setOnClickListener((View v) -> {

            if (DoctorsActivity.mAllMoney >= currentItem.getDoctorPrice() && currentItem.getState() < 6 && currentItem.getState() > 0) {
                holder.buy.startAnimation(hyperspaceJumpAnimation);
                holder.imageView.startAnimation(rotate);
                MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.buy_upgrade);
                mPlayer.start();
                currentItem.setState(currentItem.getState() + 1);
                mCallBack.clickOnButton(currentItem.getId(), currentItem.getState(), holder.buy, position);

                if (currentItem.getState() != 6) {
                    currentItem.setDoctorPrice(revealPrice(currentItem.getDoctorPrice()));
                    holder.buy.setText(context.getString(R.string.buy, currentItem.getDoctorPrice()));

                    holder.information.setText(context.getString(AssetsUpgradeStrings.getUpgradesName().get(currentItem.getId() + 10 * (currentItem.getState() - 1)), currentItem.getDoctorPrice()));
                    holder.influenceInformation.setText(AssetsUpgradeStrings.getInfluence_strings().get(currentItem.getState() - 1));
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDoctorList.size();
    }

    public static String convertNumberToString(Long number) {
        String s = number.toString();
        String newString = "";
        int counter = 0;
        String space = " ";
        for (int i = s.length() - 1; i >= 0; i--) {

            newString += s.charAt(i);
            if (counter == 2) {
                counter = -1;
                newString += space;
            }
            counter++;

        }

        newString = new StringBuffer(newString).reverse().toString();

        return newString;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public long revealPrice(long price) {
        int index = 0;
        for (int i = 0; i < 50; i++) {
            if (price == UpgradesPrices[i]) {
                index = i;
            }
        }
        if (index > 40) {
            return price;
        }
        return UpgradesPrices[index + 10];
    }

}
