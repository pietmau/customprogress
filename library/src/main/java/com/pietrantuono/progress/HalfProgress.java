package com.pietrantuono.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.pietrantuono.customprogress.R;

public class HalfProgress extends View {
    private Paint paint;
    protected Paint textPaint;
    private RectF rectF = new RectF();
    private float strokeWidth;
    private float suffixSize;
    private float bottomSize;
    private String bottomText;
    private float textSize;
    private int textColor;
    private int progress = 0;
    private int max;
    private int endStrokeColor;
    private int startStrokeColor;
    private float angle;
    private String suffix = "%";
    private float suffixPadding;
    private float bottomHeight;
    private final int default_end_color = Color.WHITE;
    private final int default_start_color = Color.rgb(255, 0, 0);
    private final int default_color = Color.rgb(255, 0, 0);
    private final float default_stroke_width;
    private final float default_padding_suffix;
    private final float default_suffix_size;
    private final float default_bottom_size;
    private final String default_suffix;
    private final int default_max = 100;
    private final float default_angle = 360 * 0.5f;
    private float default_text_size;
    private final int min_size;
    private static final String STROKE_WIDTH = "stroke_width";
    private static final String SUFFIX_PADDING = "suffix_padding";
    private static final String TEXT = "text";
    private static final String PROGRESS = "progress";
    private static final String SUFFIX_SIZE = "suffix_size";
    private static final String END_STROKE_COLOR = "end_stroke_color";
    private static final String STATE = "saved";
    private static final String TEXT_SIZE = "text_size";
    private static final String TEXT_COLOR = "text_color";
    private static final String BOTTOM_SIZE = "bottom__size";
    private static final String START_STROKE_COLOR = "start_stroke_color";
    private static final String MAX = "max";
    private static final String ANGLE = "angle";
    private static final String SUFFIX = "suffix";
    private float bottomTextSize;

    public HalfProgress(Context context) {
        this(context, null);
    }

    public HalfProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HalfProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        default_text_size = DpPx.sp2px(getResources(), 9);
        min_size = (int) DpPx.dp2px(getResources(), 100);
        default_suffix_size = DpPx.sp2px(getResources(), 15);
        default_padding_suffix = DpPx.dp2px(getResources(), 4);
        default_suffix = "%";
        default_bottom_size = DpPx.sp2px(getResources(), 10);
        default_stroke_width = DpPx.dp2px(getResources(), 4);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HalfProgress, defStyleAttr, 0);
        initAttrs(attributes);
        attributes.recycle();
        initPaint();
    }

    protected void initAttrs(TypedArray attributes) {
        endStrokeColor = attributes.getColor(R.styleable.HalfProgress_half_end_color, default_end_color);
        startStrokeColor = attributes.getColor(R.styleable.HalfProgress_half_start_color, default_start_color);
        textColor = attributes.getColor(R.styleable.HalfProgress_half_text_color, default_color);
        textSize = attributes.getDimension(R.styleable.HalfProgress_half_text_size, default_text_size);
        angle = attributes.getFloat(R.styleable.HalfProgress_half_angle, default_angle);
        setMax(attributes.getInt(R.styleable.HalfProgress_half_max, default_max));
        setProgress(attributes.getInt(R.styleable.HalfProgress_half_progress, 0));
        strokeWidth = attributes.getDimension(R.styleable.HalfProgress_half_stroke_width, default_stroke_width);
        suffixSize = attributes.getDimension(R.styleable.HalfProgress_half_suffix_size, default_suffix_size);
        suffix = TextUtils.isEmpty(attributes.getString(R.styleable.HalfProgress_half_suffix)) ? default_suffix : attributes.getString(R.styleable.HalfProgress_half_suffix);
        suffixPadding = attributes.getDimension(R.styleable.HalfProgress_half_suffix_padding, default_padding_suffix);
        bottomSize = attributes.getDimension(R.styleable.HalfProgress_half_bottom_text_size, default_bottom_size);
        bottomText = attributes.getString(R.styleable.HalfProgress_half_text);
    }

    protected void initPaint() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        paint = new Paint();
        paint.setColor(default_start_color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void invalidate() {
        initPaint();
        super.invalidate();
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.invalidate();
    }

    public float getSuffixSize() {
        return suffixSize;
    }

    public void setSuffixSize(float suffixSize) {
        this.suffixSize = suffixSize;
        this.invalidate();
    }

    public String getText() {
        return bottomText;
    }

    public void setText(String bottomText) {
        this.bottomText = bottomText;
        this.invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        this.invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.invalidate();
    }

    public int getEndStrokeColor() {
        return endStrokeColor;
    }

    public void setEndStrokeColor(int endStrokeColor) {
        this.endStrokeColor = endStrokeColor;
        this.invalidate();
    }

    public int getStartStrokeColor() {
        return startStrokeColor;
    }

    public void setStartStrokeColor(int startStrokeColor) {
        this.startStrokeColor = startStrokeColor;
        this.invalidate();
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        this.invalidate();
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        this.invalidate();
    }

    public float getSuffixPadding() {
        return suffixPadding;
    }

    public void setSuffixPadding(float suffixPadding) {
        this.suffixPadding = suffixPadding;
        this.invalidate();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return min_size;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return min_size;
    }

    public float getBottomTextSize() {
        return bottomTextSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        rectF.set(strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f, MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f);
        float radius = width / 2f;
        float angle = (360 - this.angle) / 2f;
        bottomHeight = radius * (float) (1 - Math.cos(angle / 180 * Math.PI));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = 270 - angle / 2f;
        float finishedSweepAngle = progress / (float) getMax() * angle;
        paint.setColor(startStrokeColor);
        canvas.drawArc(rectF, startAngle, angle, false, paint);
        paint.setColor(endStrokeColor);
        canvas.drawArc(rectF, startAngle, finishedSweepAngle, false, paint);

        String text = String.valueOf(getProgress());
        if (!TextUtils.isEmpty(text)) {
            textPaint.setColor(textColor);
            textPaint.setTextSize(textSize);
            float textHeight = textPaint.descent() + textPaint.ascent();
            float textBaseline = (getHeight() - textHeight) / 2.0f;
            canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, textBaseline, textPaint);
            textPaint.setTextSize(suffixSize);
            float suffixHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(suffix, getWidth() / 2.0f  + textPaint.measureText(text) + suffixPadding, textBaseline + textHeight - suffixHeight, textPaint);
        }

        if (!TextUtils.isEmpty(getText())) {
            textPaint.setTextSize(bottomSize);
            float bottomTextBaseline = getHeight() - bottomHeight - (textPaint.descent() + textPaint.ascent()) / 2;
            canvas.drawText(getText(), (getWidth() - textPaint.measureText(getText())) / 2.0f, bottomTextBaseline, textPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(STATE, super.onSaveInstanceState());
        bundle.putFloat(STROKE_WIDTH, getStrokeWidth());
        bundle.putFloat(SUFFIX_SIZE, getSuffixSize());
        bundle.putFloat(SUFFIX_PADDING, getSuffixPadding());
        bundle.putFloat(BOTTOM_SIZE, getBottomTextSize());
        bundle.putString(TEXT, getText());
        bundle.putFloat(TEXT_SIZE, getTextSize());
        bundle.putInt(TEXT_COLOR, getTextColor());
        bundle.putInt(PROGRESS, getProgress());
        bundle.putInt(MAX, getMax());
        bundle.putInt(END_STROKE_COLOR, getEndStrokeColor());
        bundle.putInt(START_STROKE_COLOR, getStartStrokeColor());
        bundle.putFloat(ANGLE, getAngle());
        bundle.putString(SUFFIX, getSuffix());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            strokeWidth = bundle.getFloat(STROKE_WIDTH);
            suffixSize = bundle.getFloat(SUFFIX_SIZE);
            suffixPadding = bundle.getFloat(SUFFIX_PADDING);
            bottomSize = bundle.getFloat(BOTTOM_SIZE);
            bottomText = bundle.getString(TEXT);
            textSize = bundle.getFloat(TEXT_SIZE);
            textColor = bundle.getInt(TEXT_COLOR);
            setMax(bundle.getInt(MAX));
            setProgress(bundle.getInt(PROGRESS));
            endStrokeColor = bundle.getInt(END_STROKE_COLOR);
            startStrokeColor = bundle.getInt(START_STROKE_COLOR);
            suffix = bundle.getString(SUFFIX);
            initPaint();
            super.onRestoreInstanceState(bundle.getParcelable(STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }


}
