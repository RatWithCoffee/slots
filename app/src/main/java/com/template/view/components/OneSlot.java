package com.template.view.components;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
    private static final int MAX_ROTATION_DIFF_NUMBER = 3;
    private static final int ANIMATION_DURATION = 1000;

    private static final int numOfImages = 4;

    private static final int[] currImgIds = new int[]{R.id.curr_image1, R.id.curr_image2, R.id.curr_image3, R.id.curr_image4};
    private static final int[] nextImgIds = new int[]{R.id.next_image1, R.id.next_image2, R.id.next_image3, R.id.next_image4};
    // количество остановившихся слотов
//    private static int numberOfStoppedSlots;

    private static final int[] images = {R.drawable.ico_1, R.drawable.ico_2, R.drawable.ico_3, R.drawable.ico_4,
            R.drawable.ico_5, R.drawable.ico_6, R.drawable.ico_7, R.drawable.ico_8};

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
        setSlotLineImgs(generateRandomArray(), currImgIds);
        nextImage.setTranslationY(getHeight());
    }

    private int[] generateRandomArray() {
        int[] randValues = new int[numOfImages];
        for (int i = 0; i < randValues.length; i++) {
            randValues[i] = RANDOM.nextInt(images.length);
        }
        return randValues;
    }

    // запускает вращение
    public void startRotation() {
        int numOfRotation = RANDOM.nextInt(MAX_ROTATION_DIFF_NUMBER) + 1;
        findViewById(R.id.next_slot).setVisibility(View.VISIBLE);

        int[] imageIndex = generateRandomArray();
        setSlotLineImgs(imageIndex, nextImgIds);
//        Log.i("current image st", String.valueOf(currentImage.getY()));
//        Log.i("next image st", String.valueOf(nextImage.getY()));

        rotate(imageIndex, numOfRotation);
    }

    private void rotate(int[] imgIndices, int numOfRotation) {
        currentImage.animate().translationY(-getHeight()).setDuration(ANIMATION_DURATION).start();
        nextImage.setTranslationY(nextImage.getHeight());
        nextImage.animate().translationY(0).setDuration(ANIMATION_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        setSlotLineImgs(imgIndices, currImgIds);
                        currentImage.setTranslationY(0);
                        nextImage.setTranslationY(nextImage.getHeight());

                        if (currRotation != numOfRotation) { // запускаем следующее вращение
                            currRotation++;
                            int[] newImgIndices = generateRandomArray();
                            setSlotLineImgs(newImgIndices, nextImgIds);



                            rotate(newImgIndices, numOfRotation);
                        } else { // останавливаем вращение
                            currRotation = 0;
                            setImgsTags(imgIndices);
                            setSlotLineImgs(imgIndices, nextImgIds);
                            rotationEndListener.onRotationEnd();

//                            Log.i("current image end", String.valueOf(currentImage.getY()));
//                            Log.i("next image end", String.valueOf(nextImage.getY()));

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

    //    получение тегов отображающихся изображений на этом слоте
    public int[] getImagesTags() {
        int[] tags = new int[currImgIds.length];
        for (int i = 0; i < tags.length; i++) {
            ImageView imageView = findViewById(currImgIds[i]);
            tags[i] = (Integer) imageView.getTag();
        }
        return tags;
    }

    // меняет изображение для ImageViews с id, переданными в массиве imageIds
    //   newImgIndices - массив новых значений
    private void setSlotLineImgs(int[] newImgIndices, int[] imageIds) {
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = findViewById(imageIds[i]);
            imageView.setImageResource(images[newImgIndices[i]]);
        }

    }

    //    устанавливает теги равные элементам newImgIndices для отображающихся на слоте картинок
    private void setImgsTags(int[] newImgIndices) {
        for (int i = 0; i < currImgIds.length; i++) {
            ImageView imageView = findViewById(currImgIds[i]);
            imageView.setTag(newImgIndices[i]);
        }
    }


    // устанавливает событие, происходящие по завершению вращения
    public void setRotationEndHandler(RotationEndListener rotationEndListener) {
        this.rotationEndListener = rotationEndListener;
    }

}
