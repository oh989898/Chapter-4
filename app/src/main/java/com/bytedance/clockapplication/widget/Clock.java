package com.bytedance.clockapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Locale;

public class Clock extends View {

    private final static String TAG = Clock.class.getSimpleName();

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;
    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;

    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    private int mWidth, mCenterX, mCenterY, mRadius;

    /**
     * properties
     */
    private int centerInnerColor;
    private int centerOuterColor;

    private int secondsNeedleColor;
    private int hoursNeedleColor;
    private int minutesNeedleColor;

    private int degreesColor;

    private int hoursValuesColor;

    private int numbersColor;

    private boolean mShowAnalog = true;

    public Clock(Context context) {
        super(context);
        init(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        size = Math.min(widthWithoutPadding, heightWithoutPadding);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    private void init(Context context, AttributeSet attrs) {

        this.centerInnerColor = Color.LTGRAY;
        this.centerOuterColor = DEFAULT_PRIMARY_COLOR;

        this.secondsNeedleColor = DEFAULT_SECONDARY_COLOR;
        this.hoursNeedleColor = DEFAULT_PRIMARY_COLOR;
        this.minutesNeedleColor = DEFAULT_PRIMARY_COLOR;

        this.degreesColor = DEFAULT_PRIMARY_COLOR;

        this.hoursValuesColor = DEFAULT_PRIMARY_COLOR;

        numbersColor = Color.WHITE;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        mWidth = Math.min(getWidth(), getHeight());

        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;

        if (mShowAnalog) {
            drawDegrees(canvas);
            drawHoursValues(canvas);
            drawNeedles(canvas);
            drawCenter(canvas);
        } else {
            drawNumbers(canvas);
        }
            postInvalidateDelayed(1000);
    }

    private void drawDegrees(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);
        paint.setAntiAlias(true);

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.05f);

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0)
                paint.setAlpha(CUSTOM_ALPHA);
            else {
                paint.setAlpha(FULL_ALPHA);
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));

            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
    }

    /**
     * @param canvas
     */
    private void drawNumbers(Canvas canvas) {

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.2f);
        textPaint.setColor(numbersColor);
        textPaint.setColor(numbersColor);
        textPaint.setAntiAlias(true);

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);

        String time = String.format("%s:%s:%s%s",
                String.format(Locale.getDefault(), "%02d", hour),
                String.format(Locale.getDefault(), "%02d", minute),
                String.format(Locale.getDefault(), "%02d", second),
                amPm == AM ? "AM" : "PM");

        SpannableStringBuilder spannableString = new SpannableStringBuilder(time);
        spannableString.setSpan(new RelativeSizeSpan(0.3f), spannableString.toString().length() - 2, spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // se superscript percent

        StaticLayout layout = new StaticLayout(spannableString, textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);
        canvas.translate(mCenterX - layout.getWidth() / 2f, mCenterY - layout.getHeight() / 2f);
        layout.draw(canvas);
    }

    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor
        String rome;
        String[]romepic = {"Ⅻ","Ⅰ","Ⅱ","Ⅲ","Ⅳ","Ⅴ","Ⅵ","Ⅶ","Ⅷ","Ⅸ","Ⅹ","Ⅺ"};
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(hoursValuesColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(mWidth * 0.09f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {
            rome = "Ⅰ";
            int centerX = (int) (mCenterX + 0.75*mRadius * Math.cos(Math.toRadians(-i+90)));
            int centerY = (int) (mCenterY - 0.75*mRadius * Math.sin(Math.toRadians(-i+90)));
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float top = fontMetrics.top;
            float bottom = fontMetrics.bottom;
            int baselineY = (int)(centerY+(bottom-top)/2-bottom);
            if ((i)%30==0){
                rome = romepic[(i/30)];
                canvas.drawText(rome, centerX, baselineY, textPaint);
            }
        }

    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawNeedles(final Canvas canvas) {
        // Default Color:
        // - secondsNeedleColor
        // - hoursNeedleColor
        // - minutesNeedleColor


        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        Paint hourNeedle = new Paint(Paint.ANTI_ALIAS_FLAG);
        hourNeedle.setStyle(Paint.Style.FILL_AND_STROKE);
        hourNeedle.setStrokeCap(Paint.Cap.ROUND);
        hourNeedle.setStrokeWidth(mWidth * 0.02f);
        hourNeedle.setColor(hoursNeedleColor);
        int stopHX = (int) (mCenterX + mRadius*0.4 * Math.cos(Math.toRadians(-(360f/12f*hour+0.5f*minute-90f))));
        int stopHY = (int) (mCenterY - mRadius*0.4 * Math.sin(Math.toRadians(-(360f/12f*hour+0.5f*minute-90f))));
        canvas.drawLine(mCenterX,mCenterY,stopHX,stopHY,hourNeedle);

        Paint minuteNeedle = new Paint(Paint.ANTI_ALIAS_FLAG);
        minuteNeedle.setStyle(Paint.Style.FILL_AND_STROKE);
        minuteNeedle.setStrokeCap(Paint.Cap.ROUND);
        minuteNeedle.setStrokeWidth(mWidth * 0.010f);
        minuteNeedle.setColor(minutesNeedleColor);
        int stopMX = (int) (mCenterX + mRadius*0.6 * Math.cos(Math.toRadians(-(360f/60f*minute+0.1*second-90f))));
        int stopMY = (int) (mCenterY - mRadius*0.6 * Math.sin(Math.toRadians(-(360f/60f*minute+0.1*second-90f))));
        canvas.drawLine(mCenterX,mCenterY,stopMX,stopMY,minuteNeedle);


        Paint secondNeedle = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondNeedle.setStyle(Paint.Style.FILL_AND_STROKE);
        secondNeedle.setStrokeCap(Paint.Cap.ROUND);
        secondNeedle.setStrokeWidth(mWidth * 0.005f);
        secondNeedle.setColor(secondsNeedleColor);
        int stopSX = (int) (mCenterX + mRadius*0.85 * Math.cos(Math.toRadians(-(360f/60f*second-90f))));
        int stopSY = (int) (mCenterY - mRadius*0.85 * Math.sin(Math.toRadians(-(360f/60f*second-90f))));
        canvas.drawLine(mCenterX, mCenterY, stopSX, stopSY, secondNeedle);

    }

    /**
     * Draw Center Dot
     *
     * @param canvas
     */
    private void drawCenter(Canvas canvas) {
        // Default Color:
        // - centerInnerColor
        // - centerOuterColor
            Paint centerOuterpoint = new Paint();
            centerOuterpoint.setColor(centerOuterColor);
            centerOuterpoint.setStrokeCap(Paint.Cap.ROUND);
            centerOuterpoint.setStyle(Paint.Style.STROKE);
            centerOuterpoint.setStrokeWidth(60f);
            canvas.drawPoint(mCenterX,mCenterY,centerOuterpoint);

            Paint centerInpoint = new Paint();
            centerInpoint.setColor(centerInnerColor);
            centerInpoint.setStrokeCap(Paint.Cap.ROUND);
            centerInpoint.setStyle(Paint.Style.FILL);
            centerInpoint.setStrokeWidth(30f);
            canvas.drawPoint(mCenterX,mCenterY,centerInpoint);



    }

    public void setShowAnalog(boolean showAnalog) {
        mShowAnalog = showAnalog;
        invalidate();
    }

    public boolean isShowAnalog() {
        return mShowAnalog;
    }

}