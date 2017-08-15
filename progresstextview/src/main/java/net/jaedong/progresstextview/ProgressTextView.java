package net.jaedong.progresstextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

/**
 * Created by jd on 2017. 8. 15..
 */

public class ProgressTextView extends AppCompatTextView {

    private SpannableTask spannableTask;
    private ProgressListener progressListener;

    public ProgressTextView(Context context) {
        super(context);
        init(null);
    }

    public ProgressTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(getContext().obtainStyledAttributes(attrs, R.styleable.ProgressTextView));
    }

    public ProgressTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(getContext().obtainStyledAttributes(attrs, R.styleable.ProgressTextView, defStyleAttr, 0));
    }

    private void init(@Nullable TypedArray typedArray) {
        this.spannableTask = new SpannableTask(this, getText().toString());
        this.spannableTask.setDefaultTextColor(this.getTextColors().getDefaultColor());

        int textColor = typedArray.getColor(R.styleable.ProgressTextView_highlightTextColor, 0);
        if (textColor != 0) {
            this.spannableTask.setHighlightTextColor(textColor);
        }
    }

    public void addProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public SpannableTask getSpannableTask() {
        return this.spannableTask;
    }

    public class SpannableTask implements Runnable {
        private Handler handler = new Handler();
        private SpannableString spannableString;
        private String text;
        private ProgressTextView spannableTextView;
        private int second;
        private int defaultTextColor = Color.GRAY;
        private int highlightTextColor = Color.RED;

        public SpannableTask(ProgressTextView spannableTextView, String text) {
            this.text = text;
            this.spannableString = new SpannableString(text);
            this.spannableTextView = spannableTextView;
            initTextView(defaultTextColor, text.length());
        }

        public void runTask(float second) {
            this.second = ((int) (second * 1000) / text.length());
            new Thread(this).run();
        }

        public void runTask(int second) {
            this.second = second / text.length();
            new Thread(this).run();
        }

        @Override
        public void run() {
            for (int i = 0; i <= text.length(); i++) {
                final int value = i;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initTextView(highlightTextColor, value);
                        if (value == text.length() && progressListener != null) {
                            cancel();
                            progressListener.complete();
                        }
                    }
                }, second * i);
            }
        }

        private void initTextView(int color, int length) {
            this.spannableString.setSpan(new ForegroundColorSpan(color), 0, length, 0);
            this.spannableTextView.setText(spannableString);
        }

        public void cancel() {
            initTextView(defaultTextColor, text.length());
            handler.removeCallbacksAndMessages(null);
        }

        public void setDefaultTextColor(int defaultTextColor) {
            this.defaultTextColor = defaultTextColor;
            initTextView(defaultTextColor, text.length());
        }

        public void setHighlightTextColor(int highlightTextColor) {
            this.highlightTextColor = highlightTextColor;
        }
    }
}
