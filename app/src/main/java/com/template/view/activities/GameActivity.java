package com.template.view.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.template.R;
import com.template.model.GameState;
import com.template.model.RotationResult;
import com.template.view.components.Slots;
import com.template.view.dialogs.EndGameDialog;
import com.template.view.dialogs.SetBetDialog;

import java.text.DecimalFormat;
import java.util.Objects;

public class GameActivity extends AppCompatActivity implements Slots.RoundEndListener, EndGameDialog.GameRestartListener, SetBetDialog.SetBetListener {

    private final GameState state = new GameState();
    private final static DecimalFormat REAL_FORMATTER = new DecimalFormat("0.###");

    private static final int TIME_BETWEEN_ROTATIONS = 1500;

    private Slots slots;

    private boolean stopRotation = false;
    private boolean isRotating = false;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        createToolBar();


        slots = findViewById(R.id.slots);
        slots.setOnRoundEnd(this);

        Button setBetButton = findViewById(R.id.button_set_bet);
        setBetButton.setOnClickListener(v -> {
            AlertDialog dialog = SetBetDialog.getDialog(this, this, state);
            dialog.show();
        });

        Button startEndButton = findViewById(R.id.button_st_end);
        startEndButton.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();


//            stopRotation = false -> пользователь захотел остановить игру
//            stopRotation = true -> пользователь захотел начать игру

//            если во время вращения пользователь остановил игру,
//            то приложение не дает ему во время того же вращения игру снова начать
            if (isRotating && !stopRotation) {
                return;
            }

            stopRotation = !stopRotation;
            if (stopRotation) {
                startEndButton.setText(R.string.end_rotation_button);
                startRotation();
            } else {
                startEndButton.setText(R.string.start_rotation_button);

            }


        });
    }


    private void createToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView moneyTextView = findViewById(R.id.textView_money);
        moneyTextView.setText(REAL_FORMATTER.format(state.getSum()));
    }

    public void onRoundEnd(RotationResult rotationResult, String message) {
        TextView moneyTextView = findViewById(R.id.textView_money);
        TextView resultTextView = findViewById(R.id.result_textview);
        state.changeSum(rotationResult);
        moneyTextView.setText(REAL_FORMATTER.format(state.getSum()));
        resultTextView.setText(message);
        resultTextView.setVisibility(View.VISIBLE);

        moneyTextView.setText(REAL_FORMATTER.format(state.getSum()));


        if (state.isGameOver()) {
            AlertDialog dialog = EndGameDialog.getDialog(this, this);
            dialog.show();
            isRotating = false;
            return;
        }

//        пользователь нажал на кнопку остановки
        if (!stopRotation) {
            isRotating = false;
            Button setBetButton = findViewById(R.id.button_set_bet);
            setBetButton.setVisibility(View.VISIBLE);
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(this::startRotation, TIME_BETWEEN_ROTATIONS);
    }


    public void startRotation() {
        isRotating = true;
        slots.startRotation();
        TextView resultTextView = findViewById(R.id.result_textview);
        resultTextView.setVisibility(View.INVISIBLE);
        Button setBetButton = findViewById(R.id.button_set_bet);
        setBetButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onGameRestart() {
        Button startEndButton = findViewById(R.id.button_st_end);
        startEndButton.setText(R.string.start_rotation_button);
        state.restartGame();
        TextView moneyTextView = findViewById(R.id.textView_money);
        moneyTextView.setText(REAL_FORMATTER.format(state.getSum()));
        TextView resultTextView = findViewById(R.id.result_textview);
        resultTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSetBet(int newBet) {
        state.setBet(newBet);
    }
}