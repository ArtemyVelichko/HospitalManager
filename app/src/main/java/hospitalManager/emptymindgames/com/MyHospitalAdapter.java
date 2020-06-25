package hospitalManager.emptymindgames.com;


import android.content.Context;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;
import hospitalManager.emptymindgames.com.AndroidAssets.AndroidImageAssets;
import hospitalManager.emptymindgames.com.AndroidAssets.AssetsUpgradeStrings;
import hospitalManager.emptymindgames.com.data.Hospital;
import hospitalManager.emptymindgames.com.databinding.OneBusinesBinding;


public class MyHospitalAdapter extends RecyclerView.Adapter<MyHospitalAdapter.MyViewHolder> {
    private List<Hospital> mHospitalList;
    private Context context;


    public onImageClickListener mCallBack;

    int[] mMotivatorsArray = {5, 10, 20, 50, 60, 80, 100};


    public interface onImageClickListener {
        void setProgressBars(int id, ProgressBar progressBar);

        void clickOnImage(int id, ProgressBar progressBar);

        void onTextViewOfPriceSelected(Hospital hospital);

        void startHospital(int id , GifImageView gifImageView);

        void finishTimer(int id, int value);

        void motivatorArchieved(int id);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        TextView textViewOnProgressBar;
        Button price;
        TextView productionAmount;
        TextView nameHospital;
        TextView textViewMotivator;
        ImageView background;
        GifImageView gifImageView;

        public MyViewHolder(@NonNull View itemView, ImageView imageView, ProgressBar progressBar, TextView textViewOnProgressBar, Button price,
                            TextView productionAmount, TextView nameHospital, TextView textViewMotivator, ImageView background, GifImageView gifImageView) {
            super(itemView);
            this.imageView = imageView;
            this.progressBar = progressBar;
            this.textViewOnProgressBar = textViewOnProgressBar;
            this.price = price;
            this.productionAmount = productionAmount;
            this.nameHospital = nameHospital;
            this.textViewMotivator = textViewMotivator;
            this.background = background;
            this.gifImageView = gifImageView;

        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyHospitalAdapter(List<Hospital> mHospitalsList, Context context, onImageClickListener listener) {
        this.mHospitalList = mHospitalsList;
        this.context = context;
        this.mCallBack = listener;

    }

    @Override
    public MyHospitalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {

        OneBusinesBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.one_busines, parent, false);

        MyViewHolder vh = new MyViewHolder(binding.constraintLayout, binding.imageBusiness, binding.progressBar,
                binding.textViewOnProgressbar, binding.price, binding.amountofProduction, binding.nameHospital, binding.textViewamount, binding.background,binding.animatedImageOnProgressbar);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        Hospital currentItem = mHospitalList.get(position);
        mCallBack.setProgressBars(currentItem.getId(), holder.progressBar);
        holder.imageView.setImageResource(AndroidImageAssets.getPictures().get(position));
        holder.productionAmount.setText(context.getString(R.string.amount_of_production, currentItem.getAmount()));


        holder.progressBar.setProgress(0);

        holder.price.setText(context.getString(AssetsUpgradeStrings.getStrings_On_Button_Buy().get(position), convertNumberToString(currentItem.getPrice())));
        holder.progressBar.setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.blue)));
        holder.nameHospital.setText(AssetsUpgradeStrings.getHospitalsNames().get(position));
        long number = currentItem.getAmount() * currentItem.getMultiplier();
        holder.textViewOnProgressBar.setText(context.getString(R.string.string_on_progressbar, number));

        int purposeMotivator = getValueMotivator(currentItem.getAmount());
        holder.textViewMotivator.setText(context.getString(R.string.textViewAmount, currentItem.getAmount(), purposeMotivator));

        Animation zoom = AnimationUtils.loadAnimation(context, R.anim.zoomin);

        if (currentItem.getAmount() != 0) {
            holder.imageView.setAnimation(zoom);
        }

        holder.imageView.setOnClickListener((v) -> {

            if (currentItem.getAmount() > 0) {

                mCallBack.clickOnImage(position, holder.progressBar);

                Handler mHandler = new Handler();
                holder.imageView.clearAnimation();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.imageView.clearAnimation();
                                holder.imageView.setAnimation(zoom);
                            }
                        });

                    }
                }, currentItem.getTime());


            }
        });
        holder.price.setOnClickListener((v) -> {
            if (MainActivity.mAllMoney >= currentItem.getPrice()) {
                currentItem.setAmount(currentItem.getAmount() + 1);
                MainActivity.mAllMoney -= currentItem.getPrice();
                currentItem.setPrice((int) Math.ceil(MyConstants.getPrices().get(position) * Math.pow(1.12, currentItem.getAmount())));
                mCallBack.onTextViewOfPriceSelected(currentItem);
                holder.price.setText(context.getString(AssetsUpgradeStrings.getStrings_On_Button_Buy().get(position), convertNumberToString(currentItem.getPrice())));
                holder.productionAmount.setText(context.getString(R.string.amount_of_production, currentItem.getAmount()));
                holder.textViewOnProgressBar.setText(context.getString(R.string.string_on_progressbar, currentItem.getAmount() * currentItem.getMultiplier()));
                MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.button_click);
                mPlayer.setVolume(30, 30);
                mPlayer.start();

                if (isArchieved(currentItem.getAmount())) {
                    mCallBack.motivatorArchieved(currentItem.getId());
                }

                holder.textViewMotivator.setText(context.getString(R.string.textViewAmount, currentItem.getAmount(), 5));

                int purpose = getValueMotivator(currentItem.getAmount());
                holder.textViewMotivator.setText(context.getString(R.string.textViewAmount, currentItem.getAmount(), purpose));

                if (currentItem.getAmount() == 1) {
                    holder.imageView.setAnimation(zoom);
                }
            }
        });
        mCallBack.startHospital(currentItem.getId(),holder.gifImageView);

        if (currentItem.getValueProgressBar() != 0) {
            mCallBack.finishTimer(position, currentItem.getValueProgressBar());
            currentItem.setValueProgressBar(0);
        }

    }

    @Override
    public int getItemCount() {
        return mHospitalList.size();
    }

    public String convertNumberToString(int number) {

        String numberStr = Long.toString(number);

        char prefix = ' ';

        int bit = Integer.toString(number).length() - 1;

        int raznost = 1;
        if (bit < 3) {
            return Integer.toString(number);
        } else if (bit < 6) {
            prefix = 'k';
            raznost = 3;
        } else if (bit < 9) {
            prefix = 'M';
            raznost = 6;
        } else if (bit < 12) {
            prefix = 'G';
            raznost = 9;
        } else if (bit < 15) {
            prefix = 'T';
            raznost = 12;
        } else if (bit < 18) {
            prefix = 'P';
            raznost = 15;
        }

        StringBuffer firstAmountOfBit = new StringBuffer();
        StringBuffer secondAmountOfBit = new StringBuffer();

        int amountOfChars = numberStr.length() - raznost;
        for (int i = 0; i < amountOfChars; i++) {
            firstAmountOfBit.append(numberStr.charAt(i));
        }
        secondAmountOfBit.append(numberStr.charAt(amountOfChars));

        return firstAmountOfBit + "," + secondAmountOfBit.toString() + prefix;



    }

    public int getValueMotivator(int x) {
        for (int i = 0; i < mMotivatorsArray.length; i++) {
            if (mMotivatorsArray[i] > x)
                return mMotivatorsArray[i];

        }

        return 10000;
    }

    public boolean isArchieved(int number) {
        for (int i = 0; i < mMotivatorsArray.length; i++) {
            if (mMotivatorsArray[i] == number) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void insert(int position, Hospital hospital) {
        mHospitalList.add(position, hospital);
        notifyItemInserted(position);
    }
}
