package com.template.view.components;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.template.EventEnd;
import com.template.R;

import java.util.Random;

// класс-отображение для одного слота
public class OneSlot extends FrameLayout {
    // время за которое происходит одно вращение
    private static final int ANIMATION_DURATION = 300;
    // количество остановившихся слотов
    private static int numberOfStoppedSlots;

    private static final int[] images = {R.drawable.slot1, R.drawable.slot2, R.drawable.slot3, R.drawable.slot4,
            R.drawable.slot5, R.drawable.slot6};

    public ImageView currentImage, nextImage;

    public final Random RANDOM = new Random();

    // событие, происходящие по завершению вращения
    private EventEnd rotationEndHandler;
    private int currRotation = 0;

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
        LayoutInflater.from(context).inflate(R.layout.image_view_scrolling, this);
        currentImage = getRootView().findViewById(R.id.current_image);
        nextImage = getRootView().findViewById(R.id.next_image);
        nextImage.setTranslationY(getHeight());
    }

    // запускает вращение
    public void startRotation() {
        int imageIndex = RANDOM.nextInt(images.length);
        int numOfRotation = RANDOM.nextInt(4) + 5000 / ANIMATION_DURATION;

        // сбрасываем счетчик слотов, закончивших вращение
        numberOfStoppedSlots = 0;

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
                            numberOfStoppedSlots++;
                            rotationEndHandler.eventEnd(numberOfStoppedSlots);
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
    private void setImage(ImageView imageView, int value) {
        imageView.setImageResource(images[value]);
        this.setTag(value);
    }

    // устанавливает событие, происходящие по завершению вращения
    public void setRotationEndHandler(EventEnd eventEnd) {
        this.rotationEndHandler = eventEnd;
    }

}
