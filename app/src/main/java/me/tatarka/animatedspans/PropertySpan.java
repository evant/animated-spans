package me.tatarka.animatedspans;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.text.style.ReplacementSpan;
import android.util.Property;
import android.view.View;

public class PropertySpan extends ReplacementSpan {

    public static final Property<PropertySpan, Float> TRANSLATION_X = new Property<PropertySpan, Float>(Float.class, "translationX") {
        @Override
        public Float get(PropertySpan object) {
            return object.getTranslationX();
        }

        @Override
        public void set(PropertySpan object, Float value) {
            object.setTranslationX(value);
        }
    };

    public static final Property<PropertySpan, Float> TRANSLATION_Y = new Property<PropertySpan, Float>(Float.class, "translationY") {
        @Override
        public Float get(PropertySpan object) {
            return object.getTranslationY();
        }

        @Override
        public void set(PropertySpan object, Float value) {
            object.setTranslationY(value);
        }
    };

    public static final Property<PropertySpan, Float> ALPHA = new Property<PropertySpan, Float>(Float.class, "alpha") {
        @Override
        public Float get(PropertySpan object) {
            return object.getAlpha();
        }

        @Override
        public void set(PropertySpan object, Float value) {
            object.setAlpha(value);
        }
    };

    public static final Property<PropertySpan, Integer> COLOR = new Property<PropertySpan, Integer>(Integer.class, "color") {
        @Override
        public Integer get(PropertySpan object) {
            return object.getColor();
        }

        @Override
        public void set(PropertySpan object, Integer value) {
            object.setColor(value);
        }
    };

    private final View view;
    private float translationX = 0;
    private float translationY = 0;
    private float alpha = -1;
    private int color = Color.TRANSPARENT;
    private boolean clipToBounds = true;
    private int clipOffsetTop;
    private int clipOffsetBottom;

    public PropertySpan(View view) {
        this.view = view;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return (int) Math.ceil(paint.measureText(text, start, end));
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        if (clipToBounds) {
            canvas.save();
            canvas.clipRect(x, top - clipOffsetTop, x + paint.measureText(text, start, end), bottom + clipOffsetBottom);
        }
        if (alpha >= 0) {
            paint.setAlpha((int) (255 * alpha));
        }
        if (color != Color.TRANSPARENT) {
            paint.setColor(color);
        }
        canvas.drawText(text, start, end, x + translationX, y + translationY, paint);
        if (clipToBounds) {
            canvas.restore();
        }
    }

    public void setClipOffset(int top, int bottom) {
        clipOffsetTop = top;
        clipOffsetBottom = bottom;
    }
    
    public void setClipToBounds(boolean value) {
        clipToBounds = value;
    }

    public boolean isClipToBounds() {
        return clipToBounds;
    }

    public void setTranslationX(float translationX) {
        this.translationX = translationX;
        view.invalidate();
    }

    public float getTranslationX() {
        return translationX;
    }

    public void setTranslationY(float translationY) {
        this.translationY = translationY;
        view.invalidate();
    }

    public float getTranslationY() {
        return translationY;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        view.invalidate();
    }

    public float getAlpha() {
        return alpha;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    @ColorInt
    public int getColor() {
        return color;
    }
}
