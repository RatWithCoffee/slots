package com.template.view.components;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.template.R;

import java.util.Random;

// класс-отображение для одного слота
public class OneSlot extends FrameLayout {
    // максимальное разница между количеством вращений у разных линий
    private static final int MAX_ROTATION_NUMBER = 5;
    private static final int ANIMATION_DURATION = 1000;
    // количество остановившихся слотов
//    private static int numberOfStoppedSlots;

    private static final int[] images = {R.drawable.ico_1, R.drawable.ico_2, R.drawable.ico_3, R.drawable.ico_4,
            R.drawable.ico_5, R.drawable.ico_6,  R.drawable.ico_7,  R.drawable.ico_8};

    public LinearLayout currentImage, nextImage;

    public final Random RANDOM = new Random();

    // событие, происходящие по завершению вращения
    private RotationEndListener rotationEndListener;
    private int currRotation = 0;

    interface RotationEndListener {
        void onRotationEnd();
    }

    public OneSlot(@NonNull Context context) {
        super(context);
        init(context);
    }

    public OneSlot(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OneSlot(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OneSlot(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // инициализация обьекта класса
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.one_slot, this);
        currentImage = getRootView().findViewById(R.id.current_slot);
        nextImage = getRootView().findViewById(R.id.next_slot);
        nextImage.setTranslationY(getHeight());
    }

    // запускает вращение
    public void startRotation() {
        int[] imageIndex = new int[4];
        for (int i = 0; i < imageIndex.length; i++) {
            imageIndex[i] = RANDOM.nextInt(images.length);
        }
        int numOfRotation = RANDOM.nextInt(MAX_ROTATION_NUMBER) + 10;

        setSlotLineImages(imageIndex, false);
        rotate(imageIndex, numOfRotation);
    }

    private void rotate(int[] imageIndex, int numOfRotation) {
        currentImage.animate().translationY(-getHeight()).setDuration(ANIMATION_DURATION).start();
        nextImage.setTranslationY(nextImage.getHeight());
        nextImage.animate().translationY(0).setDuration(ANIMATION_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        setSlotLineImages(imageIndex ,true);
                        currentImage.setTranslationY(0);
                        if (currRotation != numOfRotation) { // запускаем следующее вращение
                            currRotation++;
                            int[] newInd = new int[4];
                            for (int i = 0; i < imageIndex.length; i++) {
                                newInd[i] = RANDOM.nextInt(images.length);
                            }
                            setSlotLineImages(newInd,false);
                            rotate(newInd, numOfRotation);
                        } else { // останавливаем вращение
                            currRotation = 0;
                            setSlotLineImages(imageIndex,false);
                            rotationEndListener.onRotationEnd();
                        }

                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                });


    }

    public int[] getImagesTags() {
        int[] imageIds = new int[]{R.id.curr_image1, R.id.curr_image2, R.id.curr_image3, R.id.curr_image4};
        int[] retTags = new int[imageIds.length];
        for (int i = 0; i < retTags.length; i++) {
            ImageView imageView = findViewById(imageIds[i]);
            retTags[i] = (Integer) imageView.getTag();
        }
        return retTags;
    }

    // меняет изображение ImageView
    private void setSlotLineImages(int[] newInd, boolean currSlotLine) {
        int[] imageIds;
        if (currSlotLine) {
            imageIds = new int[]{R.id.curr_image1, R.id.curr_image2, R.id.curr_image3, R.id.curr_image4};
        } else {
            imageIds = new int[]{R.id.next_image1, R.id.next_image2, R.id.next_image3, R.id.next_image4};
        }

        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = findViewById(imageIds[i]);
            imageView.setImageResource(images[newInd[i]]);
            imageView.setTag(newInd[i]);
        }

    }

    private void setRandomImage(int id) {
        int valueRand = RANDOM.nextInt(images.length);
        ImageView imageView = findViewById(id);
        imageView.setImageResource(images[valueRand]);
        this.setTag(valueRand);
    }

    // устанавливает событие, происходящие по завершению вращения
    public void setRotationEndHandler(RotationEndListener rotationEndListener) {
        this.rotationEndListener = rotationEndListener;
    }

}
