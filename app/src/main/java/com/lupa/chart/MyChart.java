package com.lupa.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class MyChart extends View {

    private float[] datapoints = new float[] {};
    private Paint paint = new Paint();

    int width;
    int height;
    int heightWithPadding;
    int widthWithPadding;

    final int AXIS_WIDTH = 5;
    final int HELP_LINE_WIDTH = 1;
    final int CHART_WIDTH = 3;
    final int HELP_LINES_COUNT = 10;

    public MyChart(Context context) {
        super(context);
    }

    public MyChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setChartData(float[] datapoints) {
        this.datapoints = datapoints.clone();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        heightWithPadding = height - getPaddingTop() - getPaddingBottom();

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawAxis(canvas);
        drawHelpLines(canvas);
        drawLineChart(canvas);
    }

    private void drawHelpLines(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(HELP_LINE_WIDTH);
        paint.setColor(0xFF878787);

        int linesDistance = heightWithPadding / HELP_LINES_COUNT;

        for (int i = 1; i <= HELP_LINES_COUNT; i ++) {
            canvas.drawLine(
                    getPaddingLeft() - (AXIS_WIDTH / 2),
                    (height - getPaddingBottom()) - (linesDistance * i) - (AXIS_WIDTH / 2),
                    width - getPaddingRight(),
                    (height - getPaddingBottom()) - (linesDistance * i) - (AXIS_WIDTH / 2),
                    paint);
        }
    }

    private void drawAxis(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(AXIS_WIDTH);
        paint.setColor(Color.WHITE);

        //osa X
        canvas.drawLine(
                getPaddingLeft() - (AXIS_WIDTH / 2),
                height - getPaddingBottom(),
                width - getPaddingRight(),
                height - getPaddingBottom(),
                paint);

        //osa Y
        canvas.drawLine(
                getPaddingLeft(),
                getPaddingTop() - (AXIS_WIDTH / 2),
                getPaddingLeft(),
                height - getPaddingBottom() + (AXIS_WIDTH / 2),
                paint);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawText("y", getPaddingLeft() + AXIS_WIDTH, getPaddingTop() - paint.ascent(), paint);

        paint.setTextAlign(Paint.Align.RIGHT);

        canvas.drawText("x", width - getPaddingRight(), height - getPaddingBottom() - AXIS_WIDTH, paint);
    }

    private void drawLineChart(Canvas canvas) {
        if (datapoints.length == 0) {
            showNoData(canvas);
            return;
        }

        Path path = new Path();
        path.moveTo(getXPos(0), getYPos(datapoints[0]));

        for (int i = 1; i < datapoints.length; i++) {
            path.lineTo(getXPos(i), getYPos(datapoints[i]));
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(CHART_WIDTH);
        paint.setColor(0xFFFFFB00);
        paint.setAntiAlias(true);
        paint.setShadowLayer(4, 2, 2, 0x80000000);
        canvas.drawPath(path, paint);
        paint.setShadowLayer(0, 0, 0, 0);
    }

    private void showNoData(Canvas canvas) {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);
        paint.setColor(0xFF2E5BB8);
        paint.setTextSize((float) height * 0.2f);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("No data", width / 2, height / 2, paint);
    }

    private float getMax(float[] array) {
        float max = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        return max;
    }

    private float getXPos(float value) {
        widthWithPadding = width - getPaddingLeft() - getPaddingRight();
        float maxValue = datapoints.length - 1;
        float xPos = (value / maxValue) * widthWithPadding;
        xPos += getPaddingLeft() + (AXIS_WIDTH / 2);

        return xPos;
    }

    private float getYPos(float value) {
        heightWithPadding = height - getPaddingTop() - getPaddingBottom();
        float maxValue = getMax(datapoints);
        float yPos = (value / maxValue) * heightWithPadding;
        yPos = heightWithPadding - yPos;
        yPos += getPaddingBottom() - (AXIS_WIDTH / 2) + (CHART_WIDTH / 2);

        return yPos;
    }
}
