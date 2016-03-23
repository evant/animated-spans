package me.tatarka.animatedspans;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;

import java.util.Random;

public class EditTextAnimations {

    public static void dropInOnInsert(final EditText editText) {
        final PropertySpan propertySpan = new PropertySpan(editText);
        propertySpan.setClipOffset(0, editText.getResources().getDimensionPixelOffset(R.dimen.clip_offset_bottom));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final Spannable spannable = (Spannable) s;
                if (count > before) {
                    spannable.setSpan(propertySpan, start + before, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Inserted text, animate in.
                    ObjectAnimator animator = ObjectAnimator.ofFloat(propertySpan, PropertySpan.TRANSLATION_Y, editText.getHeight(), 0);
                    animator.setDuration(300);
                    animator.setInterpolator(new OvershootInterpolator(0.5f));
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            spannable.removeSpan(propertySpan);
                        }
                    });
                    animator.start();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static void acceptAndClear(final EditText editText) {
        Spannable text = editText.getText();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        PropertySpan propertySpan = new PropertySpan(editText);
        propertySpan.setClipOffset(0, editText.getResources().getDimensionPixelOffset(R.dimen.clip_offset_bottom));
        text.setSpan(propertySpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setText(text);

        ObjectAnimator moveUp = ObjectAnimator.ofFloat(propertySpan, PropertySpan.TRANSLATION_Y, 0, -editText.getHeight() / 4);
        moveUp.setDuration(300);
        moveUp.setInterpolator(new AnticipateInterpolator());
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(propertySpan, PropertySpan.ALPHA, 1, 0);
        fadeOut.setDuration(300);
        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                editText.setText(null);
            }
        });
        set.playTogether(moveUp, fadeOut);
        set.start();
    }

    public static void rejectAndClear(final EditText editText) {
        final int startTextColor = editText.getCurrentTextColor();
        final int red = editText.getContext().getResources().getColor(R.color.red);
        int shakeWidth = editText.getContext().getResources().getDimensionPixelOffset(R.dimen.char_shake_width);
        final Spannable text = editText.getText();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        final PropertySpan propertySpan = new PropertySpan(editText);
        propertySpan.setClipOffset(0, editText.getResources().getDimensionPixelOffset(R.dimen.clip_offset_bottom));
        text.setSpan(propertySpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setText(text);

        final ObjectAnimator changeColor = ObjectAnimator.ofArgb(propertySpan, PropertySpan.COLOR, startTextColor, red);
        ObjectAnimator shake = ObjectAnimator.ofFloat(propertySpan, PropertySpan.TRANSLATION_X, 0, -shakeWidth, shakeWidth, 0);
        AnimatorSet shakeSet = new AnimatorSet();
        shakeSet.setDuration(300);
        shakeSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                text.removeSpan(propertySpan);
                
                AnimatorSet set = new AnimatorSet();
                Random random = new Random();
                for (int i = 0; i < text.length(); i++) {
                    PropertySpan charSpan = new PropertySpan(editText);
                    charSpan.setClipOffset(0, editText.getResources().getDimensionPixelOffset(R.dimen.clip_offset_bottom));
                    charSpan.setColor(red);
                    text.setSpan(charSpan, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ObjectAnimator fall = ObjectAnimator.ofFloat(charSpan, PropertySpan.TRANSLATION_Y, 0, editText.getHeight());
                    fall.setInterpolator(new AccelerateInterpolator());
                    fall.setStartDelay(random.nextInt(300));
                    fall.setDuration(300);
                    set.playTogether(fall);
                }
                editText.setText(text);
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        editText.setText(null);
                    }
                });
                set.start();
            }
        });
        shakeSet.playTogether(changeColor, shake);
        shakeSet.start();
    }
}
