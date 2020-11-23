package com.example.habit.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.habit.R;

public class ProgressTable extends View {

    private int mPointsNum;
    private int mActivePointsNum;
    private int mWidth;
    private int mHeight;
    private int mRowCount;
    private int mColumnCount;

    private final Paint mAccentPaint;
    private final Paint mBackgroundPaint;


    public ProgressTable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAccentPaint = new Paint();
        mBackgroundPaint = new Paint();

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressTable);
        mPointsNum = typedArray.getInt(R.styleable.ProgressTable_numberOfPoints,0);
        mActivePointsNum = typedArray.getInt(R.styleable.ProgressTable_numberOfActivePoints,0);
        mAccentPaint.setColor(typedArray.getColor(R.styleable.ProgressTable_circleColor,
                getResources().getColor(R.color.colorAccent)));
        mBackgroundPaint.setColor(typedArray.getColor(R.styleable.ProgressTable_backgroundColor,
                getResources().getColor(R.color.design_default_color_background)));
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specHeight = MeasureSpec.getSize(heightMeasureSpec);

        if(modeWidth != MeasureSpec.UNSPECIFIED){
            mWidth = specWidth;
        }
        else {
            //TODO доделать чтоб по красоте
            throw new NullPointerException("Эта штука еще не работает");
        }
        if(modeHeight != MeasureSpec.UNSPECIFIED){
            mHeight = specHeight;
        }
        else {
            //TODO доделать чтоб по красоте
            throw new NullPointerException("Эта штука еще не работает");
        }
        setMeasuredDimension(mWidth, mHeight);
        calcRowColumnCount();
    }

    private void calcRowColumnCount(){
        double rowColumnRelationship = (double) mWidth / mHeight;

        mRowCount = ((int) Math.round(Math.sqrt(mPointsNum / rowColumnRelationship)));
        mColumnCount = (int) Math.ceil((double)mPointsNum / mRowCount);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        double cageHeight = (double) mHeight / mRowCount;
        double cageWidth = (double) mWidth / mColumnCount;

        //the radius of the circle will be equal to the smaller side of the cell divided by 4
        double circleRadius = (Math.min(cageHeight, cageWidth)) / 4;
        double internalCircleRadius = (Math.min(cageHeight, cageWidth)) / 8;

        mAccentPaint.setStrokeWidth((float)circleRadius / 10);
        mBackgroundPaint.setStrokeWidth((float)circleRadius / 10);

        int iCountActive = mActivePointsNum;
        int iCountPoints = mPointsNum;

        for (double vertical = 0; vertical < mHeight; vertical +=  cageHeight){
            for (double horizontal = 0; horizontal < mWidth; horizontal += cageWidth){
                if(iCountPoints <= 0) continue;
                double centerCircleY = vertical + cageHeight / 2;
                double centerCircleX = horizontal + cageWidth / 2;

                canvas.drawCircle((float) centerCircleX,
                        (float)centerCircleY,
                        (float)circleRadius,
                        mAccentPaint);

                if(iCountActive > 0){
                    canvas.drawCircle((float) centerCircleX,
                            (float)centerCircleY,
                            (float)internalCircleRadius,
                            mBackgroundPaint);
                }
                iCountActive--;
                iCountPoints--;

            }
        }
    }

    public int getPointsNum() {
        return mPointsNum;
    }

    public int getActivePointsNum() {
        return mActivePointsNum;
    }

    //sets new value and update view
    public void setPointsNum(int pointsNum) {
        mPointsNum = pointsNum;
        invalidate();
    }

    //sets new value and update view
    public void setActivePointsNum(int activePointsNum) {
        mActivePointsNum = activePointsNum;
        invalidate();
    }

    // increments ActivePointsNum and update view
    public void completeOne(){
        mActivePointsNum++;
        invalidate();
    }


}
