package com.template.view.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.template.R;
import com.template.model.RotationResult;

import java.util.Arrays;

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
//        handleResultForOneLine(grid[0]);
        // если все слоты закончили вращение
        if (slots.length == numberOfStoppedSlots) {
            for (int i = 0; i < numOfImagesInSlot; i++) {
                grid[i] = slots[i].getImagesTags();

            }
            grid = transpose(grid);
            for (int[] line: grid) {
//                Log.i("line", Arrays.toString(line));
                handleResultForOneLine(line);
            }


//            onRoundEndListener.onRoundEnd(RotationResult.LOSS, getResources().getString(R.string.loss));
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
    private void handleResultForOneLine(int[] tags) {
        int num = getNumOfDistinctStrings(tags);

        if (num == 1) {
            onRoundEndListener.onRoundEnd(RotationResult.JACKPOT, getResources().getString(R.string.jackpot));
//            Log.i("result", "JACKPOT");
        } else if (num == 2) {
            onRoundEndListener.onRoundEnd(RotationResult.SMALL_JACKPOT, getResources().getString(R.string.small_jackpot));
//            Log.i("result", "SMALL_JACKPOT");
        } else {
            onRoundEndListener.onRoundEnd(RotationResult.LOSS, getResources().getString(R.string.loss));
//            Log.i("result", "LOSS");
        }

    }

    private int getNumOfDistinctStrings(int[] tags) {
        return (int) java.util.Arrays.stream(tags).distinct().count();
    }


}
