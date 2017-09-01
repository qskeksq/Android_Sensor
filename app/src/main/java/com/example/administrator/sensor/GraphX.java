package com.example.administrator.sensor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-09-01.
 */

public class GraphX extends View {

    private int realWidth, realHeight;
    private final int xDensity = 300, yDensity = 300;
    private int cellHeight, cellWidth;
    private Paint graphPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int lineWidth = 10;
    private List<Integer> points = new ArrayList<>();
    private float[] lines = new float[300];
    private Point zeroByZero = new Point();

    public GraphX(Context context) {
        this(context, null);
    }

    public GraphX(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GraphX);
        // 빈번하게 쓰고 지우고 쓰고 지우고 하는 것들은 차라리 만들어 놓고 계속 재활용한다. 왜냐하면 gc 사용은 오버헤드이기 때문이다
        // 더이상 쓰지 않겠다는 말(close 같은 느낌)
        int graphColor = ta.getInt(R.styleable.GraphX_graphColor, 0);
        graphPaint.setColor((graphColor != 0) ? graphColor : Color.CYAN);
        graphPaint.setStrokeWidth(lineWidth);
        ta.recycle();
    }

    // onMeasure 에서 크기가 정해진다
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        realWidth = getMeasuredWidth() - getPaddingRight() - getPaddingLeft();
        realHeight = getMeasuredHeight() /*- getPaddingTop() - getPaddingBottom()*/;
        cellWidth = realWidth/xDensity;
        cellHeight = realHeight/yDensity;
        Log.e("width, height", getWidth()+":"+getHeight());
        zeroByZero.set(getPaddingLeft(), getPaddingTop()+realHeight/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLines(lines, graphPaint);
    }

    public void setPoints(List<Integer> points) {
        if(points.size() < 2){
            throw new IllegalArgumentException("Require at least two points");
        } else if(points.size() > yDensity) {
            int deletable = points.size() - yDensity;
            for (int d = 0; d < deletable; d++) {
                points.remove(d);
            }
        }
        this.points = points;
        populateLinePoints();
        invalidate();
    }

    public void setPoint(Integer point){
        if(points.size() == yDensity){
            points.remove(0);
        } else if(points.size() == 0){
            points.add(0);
        }
        points.add(point);
        populateLinePoints();
        invalidate();
    }

    private void populateLinePoints(){
        for (int i = 0; i < points.size(); i++) {
            if(i % 2 == 1){
                float y = zeroByZero.y - (points.get(i/2)*cellHeight);
                lines[i] = y; // y 값
            } else {
                float x = zeroByZero.x + (i)*cellWidth;
                lines[i] = x; // x 값
            }
        }
    }

}
