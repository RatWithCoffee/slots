package com.template.view.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.template.R;
import com.template.model.RotationResult;

public class Slots extends LinearLayout implements OneSlot.RotationEndListener {
    private OneSlot[] slots;

    private int numberOfStoppedSlots;

   private RoundEndListener onRoundEndListener;

    public interface RoundEndListener {
        void onRoundEnd(RotationResult rotationResult, String messageId);
    }


    public Slots(Context context) {
        super(context);
        init(context);
    }

    public Slots(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Slots(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Slots(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.slots, this);
        int[] imageIds = {R.id.image1, R.id.image2, R.id.image3};
        slots = new OneSlot[3];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = findViewById(imageIds[i]);
            slots[i].setRotationEndHandler(this);
        }
    }

    public void startRotation() {
        for (OneSlot scrollView : slots) {
            scrollView.startRotation();
        }
    }

    public void setOnRoundEnd(RoundEndListener roundEndListener) {
        this.onRoundEndListener = roundEndListener;
    }


    // обработка завершения вращения одного слота
    @Override
    public void onRotationEnd() {
        numberOfStoppedSlots++;
        // если все три слота закончили вращение
        if (slots.length == numberOfStoppedSlots) {
            if (slots[0].getTag().equals(slots[1].getTag()) && slots[0].getTag().equals(slots[2].getTag())) {
                onRoundEndListener.onRoundEnd(RotationResult.JACKPOT, getResources().getString(R.string.jackpot));
            } else if (slots[0].getTag().equals(slots[1].getTag())
                    || slots[0].getTag().equals(slots[2].getTag())
                    || slots[1].getTag().equals(slots[2].getTag())
            ) {
                onRoundEndListener.onRoundEnd(RotationResult.SMALL_JACKPOT, getResources().getString(R.string.small_jackpot));
            } else {
                onRoundEndListener.onRoundEnd(RotationResult.LOSS, getResources().getString(R.string.loss));
            }
            numberOfStoppedSlots = 0;
        }
    }


}
