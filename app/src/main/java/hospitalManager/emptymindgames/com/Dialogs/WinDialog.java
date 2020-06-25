package hospitalManager.emptymindgames.com.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import hospitalManager.emptymindgames.com.R;
import hospitalManager.emptymindgames.com.databinding.WinAlertBinding;


public class WinDialog extends DialogFragment {


    public interface DialogListener {
        void onClickPositive(DialogFragment dialog);

    }

    long healthy;
    long dead;

    public WinDialog(long healthy, long dead) {
        this.healthy = healthy;
        this.dead = dead;
    }

    private DialogListener mListener;
    private WinAlertBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.win_alert, container, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        binding = WinAlertBinding.bind(view);
        binding.textViewHealthy.setText(getString(R.string.win_text_healthy, convertNumberToString(healthy)));
        binding.textViewDead.setText(getString(R.string.win_text_dead, convertNumberToString(dead)));
        binding.startAgain.setOnClickListener((v -> {
            mListener.onClickPositive(WinDialog.this);
        }));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
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

}
