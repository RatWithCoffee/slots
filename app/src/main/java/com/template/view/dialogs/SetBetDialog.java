package com.template.view.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.template.R;
import com.template.model.GameState;

public class SetBetDialog {

    public interface SetBetListener {
        void onSetBet(int newBet);
    }

    public static AlertDialog getDialog(Activity activity, SetBetListener setBetListener, GameState state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.set_bet_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();

        Button setBetButton = view.findViewById(R.id.end_button);
        EditText editText = view.findViewById(R.id.editTextNumber);
        editText.setText(String.valueOf(state.getBet()));

        setBetButton.setOnClickListener((v) -> {
            if (editText.getText().length() == 0) {
                editText.setError("Необходимо ввести новую ставку");
                return;
            }
            int bet = Integer.parseInt(editText.getText().toString());
            if (bet > state.getSum()) {
                editText.setError("Ставка не дложна превышать количество ваших денег");
            } else {
                setBetListener.onSetBet(Integer.parseInt(editText.getText().toString()));
                alertDialog.dismiss();
            }

        });


        return alertDialog;
    }
}
