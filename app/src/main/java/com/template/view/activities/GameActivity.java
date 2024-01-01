package com.template.view.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.template.R;
import com.template.model.GameState;
import com.template.model.RotationResult;
import com.template.view.components.Slots;
import com.template.view.dialogs.EndGameDialog;
import com.template.view.dialogs.SetBetDialog;

import java.text.DecimalFormat;

public class GameActivity extends AppCompatActivity implements Slots.RoundEndListener, EndGameDialog.GameRestartListener, SetBetDialog.SetBetListener {

    private final GameState state = new GameState();
    private final static DecimalFormat REAL_FORMATTER = new DecimalFormat("0.###");

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
//
//        Button setBetButton = findViewById(R.id.button_set_bet);
//        setBetButton.setOnClickListener(v -> {
//            AlertDialog dialog = SetBetDialog.getDialog(this, this, state);
//            dialog.show();
//        });

        ImageButton autoSpinButton = findViewById(R.id.auto_spin_button);
        autoSpinButton.setOnClickListener(v -> {
            onAutoSpinClick(autoSpinButton);
        });

        ImageButton spinButton = findViewById(R.id.spin_button);
        spinButton.setOnClickListener(v -> {
            onSpinClick();
        });
    }

    private void onSpinClick() {
        if (isRotating && !stopRotation) {
            return;
        }

        stopRotation = !stopRotation;
        if (stopRotation) {
            stopRotation = false;
            startRotation();
        }
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





    public void onRoundEnd(RotationResult rotationResult, String message) {
        if (state.isGameOver()) {
            AlertDialog dialog = EndGameDialog.getDialog(this, this);
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
        slots.startRotation();
    }


    @Override
    public void onSetBet(int newBet) {
        state.setBet(newBet);
    }

    @Override
    public void onGameRestart() {

    }
}