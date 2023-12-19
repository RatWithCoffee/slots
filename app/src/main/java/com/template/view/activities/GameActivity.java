package com.template.view.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.template.EventEnd;
import com.template.R;
import com.template.model.GameState;
import com.template.view.components.OneSlot;
import com.template.view.components.Slots;

import java.text.DecimalFormat;
import java.util.Objects;

public class GameActivity extends AppCompatActivity implements EventEnd, View.OnClickListener {

    private final GameState state = new GameState();
    private final static DecimalFormat REAL_FORMATTER = new DecimalFormat("0.###");

    boolean isRotating = false;

    private static final int TIME_BETWEEN_ROTATIONS = 1000;


    private OneSlot[] imageScrollView;
    private Slots slots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        createToolBar();

        int[] imageIds = {R.id.image1, R.id.image2, R.id.image3};
        imageScrollView = new OneSlot[3];
        for (int i = 0; i < imageScrollView.length; i++) {
            imageScrollView[i] = findViewById(imageIds[i]);
            imageScrollView[i].setRotationEndHandler(GameActivity.this);
        }
        slots = new Slots();


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


    @Override
    public void eventEnd(int numberOfRollingSlots) {
        if (numberOfRollingSlots == imageScrollView.length) {
            TextView moneyTextView = findViewById(R.id.textView_money);
            TextView resultTextView = findViewById(R.id.result_textview);
            resultTextView.setVisibility(View.VISIBLE);
            if (imageScrollView[0].getTag().equals(imageScrollView[1].getTag()) && imageScrollView[0].getTag().equals(imageScrollView[2].getTag())) {
                state.increaseSum();
                state.increaseSum();
                resultTextView.setText(R.string.jackpot);
            } else if (imageScrollView[0].getTag().equals(imageScrollView[1].getTag())
                    || imageScrollView[0].getTag().equals(imageScrollView[2].getTag())
                    || imageScrollView[1].getTag().equals(imageScrollView[2].getTag())
            ) {
                state.increaseSum();
                resultTextView.setText(R.string.small_jackpot);
            } else {
                state.decreaseSum();
                resultTextView.setText(R.string.loss);
            }
            moneyTextView.setText(REAL_FORMATTER.format(state.getSum()));


            if (state.isGameOver()) {
//               показываем модальное окно с выбором выйти из игры / закинуть деньги
//                resultTextView.setText("Вы проиграли");

            }


            new Handler(Looper.getMainLooper()).postDelayed(this::startRotation, TIME_BETWEEN_ROTATIONS);


        }


    }


    @Override
    public void onClick(View view) {
        isRotating = !isRotating;
        startRotation();

    }

    private void startRotation() {
        if (isRotating) {
            for (OneSlot scrollView : imageScrollView) {
                scrollView.startRotation();
            }
            TextView resultTextView = findViewById(R.id.result_textview);
            resultTextView.setVisibility(View.INVISIBLE);


        }
    }
}