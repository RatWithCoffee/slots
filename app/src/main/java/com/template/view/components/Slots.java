package com.template.view.components;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.template.EventEnd;
import com.template.R;
import com.template.view.activities.GameActivity;

public class Slots extends LinearLayout {
    private  OneSlot[] slots;

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
        }
    }

    public void setRotationEndHandler(EventEnd eventEnd) {
        for (OneSlot slot : slots) {
            slot.setRotationEndHandler(eventEnd);
        }
    }


}
