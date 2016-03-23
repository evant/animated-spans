package me.tatarka.animatedspans;

import android.support.annotation.NonNull;
import android.text.GetChars;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class SpannablePasswordTransformationMethod extends PasswordTransformationMethod {

    private static SpannablePasswordTransformationMethod sInstance;

    public static SpannablePasswordTransformationMethod getInstance() {
        if (sInstance != null)
            return sInstance;

        sInstance = new SpannablePasswordTransformationMethod();
        return sInstance;
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        CharSequence s = super.getTransformation(source, view);
        if (source instanceof Spannable) {
            return new SpannableCharSequence(s, (Spannable) source);
        } else {
            return source;
        }
    }

    private static class SpannableCharSequence implements CharSequence, GetChars, Spannable {

        private final CharSequence wrappedSource;
        private final Spannable spannable;

        public SpannableCharSequence(CharSequence wrappedSource, Spannable spannable) {
            this.wrappedSource = wrappedSource;
            this.spannable = spannable;
        }

        @Override
        public int length() {
            return wrappedSource.length();
        }

        @Override
        public char charAt(int i) {
            return wrappedSource.charAt(i);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return wrappedSource.subSequence(start, end);
        }

        @NonNull
        @Override
        public String toString() {
            return wrappedSource.toString();
        }

        @Override
        public void getChars(int start, int end, char[] dest, int off) {
            TextUtils.getChars(wrappedSource, start, end, dest, off);
        }

        @Override
        public <T> T[] getSpans(int start, int end, Class<T> type) {
            return spannable.getSpans(start, end, type);
        }

        @Override
        public int getSpanStart(Object tag) {
            return spannable.getSpanStart(tag);
        }

        @Override
        public int getSpanEnd(Object tag) {
            return spannable.getSpanEnd(tag);
        }

        @Override
        public int getSpanFlags(Object tag) {
            return spannable.getSpanFlags(tag);
        }

        @Override
        public int nextSpanTransition(int start, int limit, Class type) {
            return spannable.nextSpanTransition(start, limit, type);
        }

        @Override
        public void setSpan(Object what, int start, int end, int flags) {
            spannable.setSpan(what, start, end, flags);
        }

        @Override
        public void removeSpan(Object what) {
            spannable.removeSpan(what);
        }
    }
}
