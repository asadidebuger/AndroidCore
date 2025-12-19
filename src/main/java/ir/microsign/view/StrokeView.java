package ir.microsign.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Mohammad on 25/11/2014.
 */
public class StrokeView extends TextView {
    int mCounter = 5;

    public StrokeView(Context context) {
        super(context);
    }

    public StrokeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StrokeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDrawCounter(int count) {
        mCounter = count;
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < mCounter; i++) {
            super.draw(canvas);
        }
    }
}
