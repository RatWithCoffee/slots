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
        void onRoundEnd(RotationResult[] rotationResult);
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
        int[] slotIds = {R.id.slot1, R.id.slot2, R.id.slot3, R.id.slot4};
        slots = new OneSlot[slotIds.length];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = findViewById(slotIds[i]);
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
        int numOfImagesInSlot = 4;
        int[][] grid = new int[slots.length][numOfImagesInSlot];
        // если все слоты закончили вращение
        if (slots.length == numberOfStoppedSlots) {
            for (int i = 0; i < numOfImagesInSlot; i++) {
                grid[i] = slots[i].getImagesTags();

            }
            grid = transpose(grid);
            RotationResult[] results = new RotationResult[4];
            for (int i = 0; i < results.length; i++) {
                results[i] = handleResultForOneLine(grid[i]);
            }

            onRoundEndListener.onRoundEnd(results);

            numberOfStoppedSlots = 0;
        }
    }

    public static int[][] transpose(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int[][] transposed = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }

        return transposed;
    }

    private RotationResult handleResultForOneLine(int[] tags) {
        int num = getNumOfDistinctStrings(tags);

        if (num == 1) {
            return RotationResult.JACKPOT;
        } else if (num == 2) {
            return RotationResult.SMALL_JACKPOT;
        } else {
            return RotationResult.LOSS;
        }

    }

    private int getNumOfDistinctStrings(int[] tags) {
        return (int) java.util.Arrays.stream(tags).distinct().count();
    }


}
