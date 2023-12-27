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
    // время за которое происходит одно вращение
    private static final int ANIMATION_DURATION = 300;
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
        int imageIndex = RANDOM.nextInt(images.length);
        int numOfRotation = RANDOM.nextInt(4) + 5000 / ANIMATION_DURATION;

        setImage(nextImage, imageIndex);
        rotate(imageIndex, numOfRotation);
    }

    private void rotate(int imageIndex, int numOfRotation) {
        currentImage.animate().translationY(-getHeight()).setDuration(ANIMATION_DURATION).start();
        nextImage.setTranslationY(nextImage.getHeight());
        nextImage.animate().translationY(0).setDuration(ANIMATION_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        setImage(currentImage, imageIndex);
                        currentImage.setTranslationY(0);
                        if (currRotation != numOfRotation) { // запускаем следующее вращение
                            currRotation++;
                            int newInd = RANDOM.nextInt(images.length);
                            setImage(nextImage, newInd);
                            rotate(newInd, numOfRotation);
                        } else { // останавливаем вращение
                            currRotation = 0;
                            setImage(nextImage, imageIndex);
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

    // меняет изображение ImageView
    private void setImage(LinearLayout imageView, int value) {
//        imageView.setImageResource(images[value]);
//        this.setTag(value);
    }

    // устанавливает событие, происходящие по завершению вращения
    public void setRotationEndHandler(RotationEndListener rotationEndListener) {
        this.rotationEndListener = rotationEndListener;
    }

}
