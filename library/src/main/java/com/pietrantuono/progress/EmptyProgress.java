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


public class EmptyProgress extends View {
    private Paint endPaint;
    private Paint startPaint;
    private Paint inPaint;
    protected Paint textPaint;
    protected Paint inTextPaint;

    private RectF endRect = new RectF();
    private RectF startRect = new RectF();

    private float textSize;
    private int textColor;
    private int innerTextColor;
    private int progress = 0;
    private int max;
    private int endStrokeColor;
    private int startStrokeColor;
    private int startingDegree;
    private float endStrokeWidth;
    private float startStrokeWidth;
    private int innerColor;
    private String prefix = "";
    private String suffix = "%";
    private String text = null;
    private float innerTextSize;
    private String innerText;
    private float innerTextHeight;

    private final float stroke_width;
    private final int default_end_color = Color.rgb(66, 145, 241);
    private final int default_start_color = Color.rgb(204, 204, 204);
    private final int default_text_color = Color.rgb(66, 145, 241);
    private final int default_inner_text_color = Color.rgb(66, 145, 241);
    private final int default_background_color = Color.TRANSPARENT;
    private final int default_max = 100;
    private final int default_startingDegree = 0;
    private final float text_size;
    private final float default_text_size;
    private final int min_size;

    private static final String END_STROKE_COLOR = "end_stroke_color";
    private static final String SAVED_INSTANCE = "instance";
    private static final String TEXT_SIZE = "text_size";
    private static final String START_STROKE_COLOR = "start_color";
    private static final String STARTING_DEGREE = "start_degree";
    private static final String INSTANCE_TEXT = "text";
    private static final String INNER_TEXT_SIZE = "inner_text_size";
    private static final String PROGRESS = "progress";
    private static final String BACKGROUND_COLOR = "inner_color";
    private static final String INNER_TEXT_COLOR = "inner_text_color";
    private static final String PREFIX = "prefix";
    private static final String END_STROKE_WIDTH = "end_width";
    private static final String INNER_TEXT = "inner_text";
    private static final String START_STROKE_WIDTH = "start_stroke_width";
    private static final String MAX = "max";
    private static final String SUFFIX = "suffix";
    private static final String TEXT_COLOR = "text_color";

    public EmptyProgress(Context context) {
        this(context, null);
    }

    public EmptyProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        text_size = DpPx.sp2px(getResources(), 18);
        min_size = (int) DpPx.dp2px(getResources(), 100);
        stroke_width = DpPx.dp2px(getResources(), 10);
        default_text_size = DpPx.sp2px(getResources(), 18);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EmptyProgress, defStyleAttr, 0);
        initAttrs(attributes);
        attributes.recycle();

        initPaint();
    }

    protected void initPaint() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        inTextPaint = new TextPaint();
        inTextPaint.setColor(innerTextColor);
        inTextPaint.setTextSize(innerTextSize);
        inTextPaint.setAntiAlias(true);

        endPaint = new Paint();
        endPaint.setColor(endStrokeColor);
        endPaint.setStyle(Paint.Style.STROKE);
        endPaint.setAntiAlias(true);
        endPaint.setStrokeWidth(endStrokeWidth);

        startPaint = new Paint();
        startPaint.setColor(startStrokeColor);
        startPaint.setStyle(Paint.Style.STROKE);
        startPaint.setAntiAlias(true);
        startPaint.setStrokeWidth(startStrokeWidth);

        inPaint = new Paint();
        inPaint.setColor(innerColor);
        inPaint.setAntiAlias(true);
    }

    protected void initAttrs(TypedArray attributes) {
        endStrokeColor = attributes.getColor(R.styleable.EmptyProgress_empty_end_color, default_end_color);
        startStrokeColor = attributes.getColor(R.styleable.EmptyProgress_empty_start_color, default_start_color);
        textColor = attributes.getColor(R.styleable.EmptyProgress_empty_text_color, default_text_color);
        textSize = attributes.getDimension(R.styleable.EmptyProgress_empty_text_size, text_size);

        setMax(attributes.getInt(R.styleable.EmptyProgress_empty_max, default_max));
        setProgress(attributes.getInt(R.styleable.EmptyProgress_empty_progress, 0));
        endStrokeWidth = attributes.getDimension(R.styleable.EmptyProgress_empty_end_stroke_width, stroke_width);
        startStrokeWidth = attributes.getDimension(R.styleable.EmptyProgress_empty_start_stroke_width, stroke_width);
        if (attributes.getString(R.styleable.EmptyProgress_empty_prefix) != null) {
            prefix = attributes.getString(R.styleable.EmptyProgress_empty_prefix);
        }
        if (attributes.getString(R.styleable.EmptyProgress_empty_suffix) != null) {
            suffix = attributes.getString(R.styleable.EmptyProgress_empty_suffix);
        }
        if (attributes.getString(R.styleable.EmptyProgress_empty_text) != null) {
            text = attributes.getString(R.styleable.EmptyProgress_empty_text);
        }
        innerColor = attributes.getColor(R.styleable.EmptyProgress_empty_background_color, default_background_color);

        innerTextSize = attributes.getDimension(R.styleable.EmptyProgress_empty_inner_text_size, default_text_size);
        innerTextColor = attributes.getColor(R.styleable.EmptyProgress_empty_inner_text_color, default_inner_text_color);
        innerText = attributes.getString(R.styleable.EmptyProgress_empty_inner_text);

        startingDegree = attributes.getInt(R.styleable.EmptyProgress_empty_starting_degree, default_startingDegree);
    }

    @Override
    public void invalidate() {
        initPaint();
        super.invalidate();
    }

    public float getEndStrokeWidth() {
        return endStrokeWidth;
    }

    public void setEndStrokeWidth(float endStrokeWidth) {
        this.endStrokeWidth = endStrokeWidth;
        this.invalidate();
    }

    public float getStartStrokeWidth() {
        return startStrokeWidth;
    }

    public void setStartStrokeWidth(float startStrokeWidth) {
        this.startStrokeWidth = startStrokeWidth;
        this.invalidate();
    }

    private float getProgressAngle() {
        return getProgress() / (float) max * 360f;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.invalidate();
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        this.invalidate();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.invalidate();
    }

    public int getInnerColor() {
        return innerColor;
    }

    public void setInnerColor(int innerColor) {
        this.innerColor = innerColor;
        this.invalidate();
    }


    public String getInnerText() {
        return innerText;
    }

    public void setInnerText(String innerText) {
        this.innerText = innerText;
        this.invalidate();
    }


    public float getInnerTextSize() {
        return innerTextSize;
    }

    public void setInnerTextSize(float innerTextSize) {
        this.innerTextSize = innerTextSize;
        this.invalidate();
    }

    public int getInnerTextColor() {
        return innerTextColor;
    }

    public void setInnerTextColor(int innerTextColor) {
        this.innerTextColor = innerTextColor;
        this.invalidate();
    }

    public int getStartingDegree() {
        return startingDegree;
    }

    public void setStartingDegree(int startingDegree) {
        this.startingDegree = startingDegree;
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
        innerTextHeight = getHeight() - (getHeight()*3) /4 ;
    }

    private int measure(int measureSpec){
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if(mode == MeasureSpec.EXACTLY){
            result = size;
        }else{
            result = min_size;
            if(mode == MeasureSpec.AT_MOST){
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float delta = Math.max(endStrokeWidth, startStrokeWidth);
        endRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta);
        startRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta);

        float innerCircleRadius = (getWidth() - Math.min(endStrokeWidth, startStrokeWidth) + Math.abs(endStrokeWidth - startStrokeWidth)) / 2f;
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, innerCircleRadius, inPaint);
        canvas.drawArc(endRect, getStartingDegree(), getProgressAngle(), false, endPaint);
        canvas.drawArc(startRect, getStartingDegree() + getProgressAngle(), 360 - getProgressAngle(), false, startPaint);

        String text = this.text != null ? this.text : prefix + progress + suffix;
        if (!TextUtils.isEmpty(text)) {
            float textHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, (getWidth() - textHeight) / 2.0f, textPaint);
        }

        if (!TextUtils.isEmpty(getInnerText())) {
            inTextPaint.setTextSize(innerTextSize);
            float bottomTextBaseline = getHeight() - innerTextHeight - (textPaint.descent() + textPaint.ascent()) / 2;
            canvas.drawText(getInnerText(), (getWidth() - inTextPaint.measureText(getInnerText())) / 2.0f, bottomTextBaseline, inTextPaint);
        }

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(SAVED_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(TEXT_COLOR, getTextColor());
        bundle.putFloat(TEXT_SIZE, getTextSize());
        bundle.putFloat(INNER_TEXT_SIZE, getInnerTextSize());
        bundle.putFloat(INNER_TEXT_COLOR, getInnerTextColor());
        bundle.putString(INNER_TEXT, getInnerText());
        bundle.putInt(INNER_TEXT_COLOR, getInnerTextColor());
        bundle.putInt(END_STROKE_COLOR, getEndStrokeColor());
        bundle.putInt(START_STROKE_COLOR, getStartStrokeColor());
        bundle.putInt(MAX, getMax());
        bundle.putInt(STARTING_DEGREE, getStartingDegree());
        bundle.putInt(PROGRESS, getProgress());
        bundle.putString(SUFFIX, getSuffix());
        bundle.putString(PREFIX, getPrefix());
        bundle.putString(INSTANCE_TEXT, getText());
        bundle.putFloat(END_STROKE_WIDTH, getEndStrokeWidth());
        bundle.putFloat(START_STROKE_WIDTH, getStartStrokeWidth());
        bundle.putInt(BACKGROUND_COLOR, getInnerColor());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            textColor = bundle.getInt(TEXT_COLOR);
            textSize = bundle.getFloat(TEXT_SIZE);
            innerTextSize = bundle.getFloat(INNER_TEXT_SIZE);
            innerText = bundle.getString(INNER_TEXT);
            innerTextColor = bundle.getInt(INNER_TEXT_COLOR);
            endStrokeColor = bundle.getInt(END_STROKE_COLOR);
            startStrokeColor = bundle.getInt(START_STROKE_COLOR);
            endStrokeWidth = bundle.getFloat(END_STROKE_WIDTH);
            startStrokeWidth = bundle.getFloat(START_STROKE_WIDTH);
            innerColor = bundle.getInt(BACKGROUND_COLOR);
            initPaint();
            setMax(bundle.getInt(MAX));
            setStartingDegree(bundle.getInt(STARTING_DEGREE));
            setProgress(bundle.getInt(PROGRESS));
            prefix = bundle.getString(PREFIX);
            suffix = bundle.getString(SUFFIX);
            text = bundle.getString(INSTANCE_TEXT);
            super.onRestoreInstanceState(bundle.getParcelable(SAVED_INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

}
