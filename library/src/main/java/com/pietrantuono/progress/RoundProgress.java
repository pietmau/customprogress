package com.pietrantuono.progress;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.pietrantuono.customprogress.R;

public class RoundProgress extends View {
    private float progress;
    private float strokeWidth;
    private float backStrokeWidth;
    private int color = Color.BLACK;
    private int backgroundColor = Color.GRAY;
    private int startAngle = -90;
    private RectF rectF;
    private Paint backPaint;
    private Paint paint;

    private static final String STATE = "state";
    private static final String PROGRESS = "progress";
    private static final String STROKE_WIDTH = "stroke_width";
    private static final String BACK_STROKE_WIDTH = "back_stroke_width";
    private static final String COLOR = "color";
    private static final String BACKGROUND_COLOR = "background_color";

    public RoundProgress(Context context) {
        this(context, null);
    }

    public RoundProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint();
        initRec();
        this.setWillNotDraw(false);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundProgress, 0, 0);
        try {
            progress = typedArray.getFloat(R.styleable.RoundProgress_progress, progress);
            strokeWidth = typedArray.getDimension(R.styleable.RoundProgress_progressbar_width, 7f);
            backStrokeWidth = typedArray.getDimension(R.styleable.RoundProgress_background_progress_width, 3f);
            color = typedArray.getInt(R.styleable.RoundProgress_progressbar_color, color);
            backgroundColor = typedArray.getInt(R.styleable.RoundProgress_background_progress_color, backgroundColor);
        } finally {
            typedArray.recycle();
        }
    }

    protected void initPaint() {
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(backgroundColor);
        backPaint.setStyle(Paint.Style.STROKE);
        backPaint.setStrokeWidth(backStrokeWidth);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
    }

    protected void initRec() {
        rectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(rectF, backPaint);
        float angle = 360 * progress / 100;
        canvas.drawArc(rectF, startAngle, angle, false, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = (strokeWidth > backStrokeWidth) ? strokeWidth : backStrokeWidth;
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
    }

    public float getProgress() {
        return progress;
    }


    public void setProgress(float progress) {
        this.progress = (progress <= 100) ? progress : 100;
        invalidate();
    }


    public float getBarWidth() {
        return strokeWidth;
    }


    public void setBarWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        paint.setStrokeWidth(strokeWidth);
        requestLayout();
        invalidate();
    }


    public float getBackgroundBarWidth() {
        return backStrokeWidth;
    }


    public void setBackgroundBarWidth(float backgroundStrokeWidth) {
        this.backStrokeWidth = backgroundStrokeWidth;
        backPaint.setStrokeWidth(backgroundStrokeWidth);
        requestLayout();
        invalidate();
    }


    public int getColor() {
        return color;
    }


    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
        invalidate();
        requestLayout();
    }


    public int getBackColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backPaint.setColor(backgroundColor);
        invalidate();
        requestLayout();
    }

    public void animateProgress(float progress) {
        animateProgress(progress, 1500);
    }

    public void animateProgress(float progress, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(STATE, super.onSaveInstanceState());
        bundle.putFloat(PROGRESS, progress);
        bundle.putFloat(STROKE_WIDTH, strokeWidth);
        bundle.putFloat(BACK_STROKE_WIDTH, backStrokeWidth);
        bundle.putInt(COLOR, color);
        bundle.putInt(BACKGROUND_COLOR, backgroundColor);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            progress = bundle.getFloat(PROGRESS);
            strokeWidth = bundle.getFloat(STROKE_WIDTH);
            backStrokeWidth = bundle.getFloat(BACK_STROKE_WIDTH);
            color = bundle.getInt(COLOR);
            backgroundColor = bundle.getInt(BACKGROUND_COLOR);
            initPaint();
            initRec();
            super.onRestoreInstanceState(bundle.getParcelable(STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }


}