package com.submeter.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.submeter.android.R;
import com.submeter.android.tools.Utils;

public class CircleProgressBar extends View {

    private int mMaxProgress = 100;

    private int mProgress = 100;

    private long duration;

    private int mCircleLineStrokeWidth = 8;

    private int mTxtStrokeWidth = 2;

    private int mTextHeight = 24;

    // 画圆所在的距形区域
    private final RectF mRectF;

    private final Paint mPaint;

    private String text = "";

    private CountDownTimer timer;

    private CountDownListener countDownListener;

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRectF = new RectF();
        mPaint = new Paint();

        mCircleLineStrokeWidth = Utils.dip2px(context, 2);
        mTxtStrokeWidth = Utils.dip2px(context, 2);
        mTextHeight = Utils.dip2px(context, 8);

        text = context.getString(R.string.skip);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mRectF.left + width / 2, mRectF.top + height / 2, (width) / 2  - mCircleLineStrokeWidth, mPaint);

        mPaint.setColor(Color.rgb(0xE2, 0xE2, 0xE2));
        mPaint.setStrokeWidth(mCircleLineStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        // 位置
        mRectF.left = mCircleLineStrokeWidth / 2; // 左上角
        mRectF.top = mCircleLineStrokeWidth / 2; // 左上角
        mRectF.right = width - mCircleLineStrokeWidth / 2; // 左下角
        mRectF.bottom = height - mCircleLineStrokeWidth / 2; // 右下角

        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF, -90, 360, false, mPaint);
        mPaint.setColor(Color.rgb(0xD5, 0x20, 0x47));
        float passAngle = ((float)mMaxProgress - mProgress) / mMaxProgress * 360;
        canvas.drawArc(mRectF, -90 + passAngle, 360 - passAngle, false, mPaint);

        // 绘制进度文案显示
        mPaint.setStrokeWidth(mTxtStrokeWidth);
        if(!TextUtils.isEmpty(text)) {
            mPaint.setTextSize(mTextHeight);
            mPaint.setStyle(Paint.Style.FILL);
            Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
            int baseline = (int)(mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2;
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text, width / 2, baseline, mPaint);
        }
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setCountDownListener(CountDownListener listener) {
        this.countDownListener = listener;
    }

    public void start() {
        //text = String.valueOf((duration + 999) * mProgress / 100000 );

        invalidate();
        if(null != timer) {
            timer.cancel();
            timer = null;
        }

        timer = new CountDownTimer(duration, duration / 100) {
            @Override
            public void onFinish() {
                mProgress = 0;
                invalidate();

                if(null != countDownListener) {
                    countDownListener.onFinish();
                }
            }

            @Override
            public void onTick(long l) {
                //text = String.valueOf((l + 999) / 1000);

                mProgress = (int)(l / (duration / 100));
                invalidate();
            }
        }.start();
    }

    public void cancel() {
        if(null != timer) {
            timer.cancel();
        }
        timer = null;
    }

    public interface CountDownListener {
        public void onFinish();
    }
}