package com.template.view.components;

import android.view.View;
import android.widget.ImageView;

import com.template.EventEnd;
import com.template.R;
import com.template.view.activities.GameActivity;

public class Slots {
    OneSlot[] slots = new OneSlot[3];

    Slots(OneSlot[] imageViews, EventEnd eventEnd) {
        slots = new OneSlot[3];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = imageViews[i];
            slots[i].setRotationEndHandler(eventEnd);
        }
    }
}
