package com.template.view.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.template.R;
import com.template.model.GameState;
import com.template.model.RotationResult;
import com.template.view.components.Slots;

import java.text.DecimalFormat;
import java.util.Objects;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, Slots.RoundEndListener {

    private final GameState state = new GameState();
    private final static DecimalFormat REAL_FORMATTER = new DecimalFormat("0.###");

    private static final int TIME_BETWEEN_ROTATIONS = 1500;

    private Slots slots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        createToolBar();


        slots = findViewById(R.id.slots);
        slots.setOnRoundEnd(this);

        Button startEndButton = findViewById(R.id.button_st_end);
        startEndButton.setOnClickListener(this);
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
//               показываем модальное окно с выбором выйти из игры / закинуть деньги
//                resultTextView.setText("Вы проиграли");

        }



        new Handler(Looper.getMainLooper()).postDelayed(this::startRotation, TIME_BETWEEN_ROTATIONS);
    }




    @Override
    public void onClick(View view) {
        startRotation();
    }
    public void startRotation() {
        slots.startRotation();
        TextView resultTextView = findViewById(R.id.result_textview);
        resultTextView.setVisibility(View.INVISIBLE);
    }

}