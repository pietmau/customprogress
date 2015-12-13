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

public class CustomProgress extends View {
    private Paint textPaint;
    private RectF rectF = new RectF();

    private float textSize;
    private int textColor;
    private int progress = 0;
    private int max;
    private int endColor;
    private int startColor;
    private String prefix = "";
    private String suffix = "%";

    private final int default_max = 100;
    private final float text_size;
    private final int default_start_color = Color.rgb(204, 204, 204);
    private final int default_end_color = Color.rgb(66, 145, 241);
    private final int default_text_color = Color.WHITE;

    private final int min_size;

    private static final String PREFIX = "prefix";
    private static final String START_STROKE_COLOR = "start_color";
    private static final String STATE = "instance";
    private static final String PROGRESS = "progress";
    private static final String TEXT_SIZE = "text_size";
    private static final String STROKE_COLOR = "end_color";
    private static final String TEXT_COLOR = "text_color";
    private static final String SUFFIX = "suffix";
    private static final String MAX = "max";

    private Paint paint = new Paint();

    public CustomProgress(Context context) {
        this(context, null);
    }

    public CustomProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        text_size = DpPx.sp2px(getResources(), 18);
        min_size = (int) DpPx.dp2px(getResources(), 100);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgress, defStyleAttr, 0);
        initAttrs(attributes);
        attributes.recycle();

        initPaint();
    }

    protected void initAttrs(TypedArray attributes) {
        endColor = attributes.getColor(R.styleable.CustomProgress_custom_end_color, default_end_color);
        startColor = attributes.getColor(R.styleable.CustomProgress_custom_start_color, default_start_color);
        textColor = attributes.getColor(R.styleable.CustomProgress_custom_text_color, default_text_color);
        textSize = attributes.getDimension(R.styleable.CustomProgress_custom_text_size, text_size);

        setMax(attributes.getInt(R.styleable.CustomProgress_custom_max, default_max));
        setProgress(attributes.getInt(R.styleable.CustomProgress_custom_progress, 0));

        if (attributes.getString(R.styleable.CustomProgress_custom_prefix) != null) {
            setPrefix(attributes.getString(R.styleable.CustomProgress_custom_prefix));
        }
        if (attributes.getString(R.styleable.CustomProgress_custom_suffix) != null) {
            setSuffix(attributes.getString(R.styleable.CustomProgress_custom_suffix));
        }
    }

    protected void initPaint() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        paint.setAntiAlias(true);
    }

    @Override
    public void invalidate() {
        initPaint();
        super.invalidate();
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

    public int getEndColor() {
        return endColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
        this.invalidate();
    }

    public int getStartColor() {
        return startColor;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
        this.invalidate();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.invalidate();
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        this.invalidate();
    }

    public String getText() {
        return getPrefix() + getProgress() + getSuffix();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return min_size;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return min_size;
    }

    public float getProgressPercentage() {
        return getProgress() / (float) getMax();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        rectF.set(0, 0, MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override protected void onDraw(Canvas canvas) {
        float yHeight = getProgress() / (float) getMax() * getHeight();
        float radius = getWidth() / 2f;
        float angle = (float) (Math.acos((radius - yHeight) / radius) * 180 / Math.PI);
        float startAngle = 90 + angle;
        float sweepAngle = 360 - angle * 2;
        paint.setColor(getStartColor());
        canvas.drawArc(rectF, startAngle, sweepAngle, false, paint);

        canvas.save();
        canvas.rotate(180, getWidth() / 2, getHeight() / 2);
        paint.setColor(getEndColor());
        canvas.drawArc(rectF, 270 - angle, angle * 2, false, paint);
        canvas.restore();

        String text = getText();
        if (!TextUtils.isEmpty(text)) {
            float textHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, (getWidth() - textHeight) / 2.0f, textPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(STATE, super.onSaveInstanceState());
        bundle.putInt(TEXT_COLOR, getTextColor());
        bundle.putFloat(TEXT_SIZE, getTextSize());
        bundle.putInt(STROKE_COLOR, getEndColor());
        bundle.putInt(START_STROKE_COLOR, getStartColor());
        bundle.putInt(MAX, getMax());
        bundle.putInt(PROGRESS, getProgress());
        bundle.putString(SUFFIX, getSuffix());
        bundle.putString(PREFIX, getPrefix());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            textColor = bundle.getInt(TEXT_COLOR);
            textSize = bundle.getFloat(TEXT_SIZE);
            endColor = bundle.getInt(STROKE_COLOR);
            startColor = bundle.getInt(START_STROKE_COLOR);
            initPaint();
            setMax(bundle.getInt(MAX));
            setProgress(bundle.getInt(PROGRESS));
            prefix = bundle.getString(PREFIX);
            suffix = bundle.getString(SUFFIX);
            super.onRestoreInstanceState(bundle.getParcelable(STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }


}
