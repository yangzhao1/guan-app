package com.submeter.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

public class CountDownTextView extends TextView {

    private CountDownTimer countDownTimer;

    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void startCountDown(long time, int interval, final CountDownListener listener) {
        countDownTimer = new CountDownTimer(time, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(listener != null) {
                    listener.onTick(CountDownTextView.this, millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                if(listener != null) {
                    listener.onFinish(CountDownTextView.this);
                }
            }
        };
        countDownTimer.start();
    }

    public void cancelTimer() {
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public interface CountDownListener {
        public void onTick(CountDownTextView textView, long millisUntilFinished);

        public void onFinish(CountDownTextView textView);
    }
}