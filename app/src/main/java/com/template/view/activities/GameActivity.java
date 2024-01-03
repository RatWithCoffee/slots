package com.template.view.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.template.R;
import com.template.model.GameState;
import com.template.model.RotationResult;
import com.template.view.components.Slots;
import com.template.view.dialogs.EndGameDialog;

import java.util.Locale;

public class GameActivity extends AppCompatActivity implements Slots.RoundEndListener, EndGameDialog.GameRestartListener {

    private final GameState state = new GameState();

    private static final int TIME_BETWEEN_ROTATIONS = 1500;

    private Slots slots;

    private boolean stopRotation = false;
    private boolean isRotating = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        slots = findViewById(R.id.slots);
        slots.setOnRoundEnd(this);

        TextView moneyTextView = findViewById(R.id.money_textview);
        moneyTextView.setText(String.format(Locale.ENGLISH, "%08d", state.getSum()));


        ImageButton autoSpinButton = findViewById(R.id.auto_spin_button);
        autoSpinButton.setOnClickListener(v -> {
            onAutoSpinClick(autoSpinButton);
        });

        ImageButton spinButton = findViewById(R.id.spin_button);
        spinButton.setOnClickListener(v -> {
            onSpinClick();
        });


        TextView betTextView = findViewById(R.id.bet_textview);
        betTextView.setText(String.valueOf(state.getBet()));

        betTextView.setText(String.valueOf(state.getBet()));

        ImageButton setBetButton = findViewById(R.id.set_bet_button);
        setBetButton.setOnClickListener(v -> {
            if (!isRotating) {
                state.updateBet();
            }
        });

        ImageButton increaseBetButton = findViewById(R.id.increase_bet_button);
        increaseBetButton.setOnClickListener(v -> {
            if (!isRotating) {
                state.increaseNewBet();
                betTextView.setText(String.valueOf(state.getNewBet()));
            }
        });

        ImageButton decreaseBetButton = findViewById(R.id.decrease_bet_button);
        decreaseBetButton.setOnClickListener(v -> {
            if (!isRotating) {
                state.decreaseNewBet();
                betTextView.setText(String.valueOf(state.getNewBet()));
            }
        });
    }

    private void onSpinClick() {
        if (isRotating) {
            return;
        }

        stopRotation = false;
        startRotation();

    }

    private void onAutoSpinClick(ImageButton autoSpinButton) {
//            stopRotation = false -> пользователь захотел остановить игру
//            stopRotation = true -> пользователь захотел начать игру

//            если во время вращения пользователь остановил игру,
//            то приложение не дает ему во время того же вращения игру снова начать
        if (isRotating && !stopRotation) {
            return;
        }

        stopRotation = !stopRotation;
        if (stopRotation) {
            autoSpinButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bott_stopauto));
            startRotation();
        } else {
            autoSpinButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bott_auto));
        }
    }


    public void onRoundEnd(RotationResult rotationResult) {
        state.changeSum(rotationResult);
        TextView moneyTextView = findViewById(R.id.money_textview);
        moneyTextView.setText(String.format(Locale.ENGLISH, "%08d", state.getSum()));


        if (state.isGameOver()) {
            AlertDialog dialog = EndGameDialog.getDialog(this, this);
            Log.i("show dialog", "show");
            dialog.show();
            isRotating = false;
            return;
        }

//        пользователь нажал на кнопку остановки
        if (!stopRotation) {
            isRotating = false;
            return;
        }


        new Handler(Looper.getMainLooper()).postDelayed(this::startRotation, TIME_BETWEEN_ROTATIONS);
    }


    public void startRotation() {
        isRotating = true;
        state.dropNewBet();
        TextView betTextView = findViewById(R.id.bet_textview);
        betTextView.setText(String.valueOf(state.getBet()));
        slots.startRotation();
    }



    @Override
    public void onGameRestart() {
        state.restartGame();
        TextView betTextView = findViewById(R.id.bet_textview);
        betTextView.setText(String.valueOf(state.getBet()));
        TextView moneyTextView = findViewById(R.id.money_textview);
        moneyTextView.setText(String.format(Locale.ENGLISH, "%08d", state.getSum()));
    }
}