package dev.shaynemcneil.a7sensors;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

    private Square square;
    private boolean gravity;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gravity = false;
        Log.d("DrawingView", "Drawing view created");
    }

    public void setGravity(boolean gravity) {
        this.gravity = gravity;
    }

    public void updateSquarePosition(float x, float y) {
        if (square != null && gravity) {
            square.updatePosition(x, y, getWidth(), getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(square != null) {
            square.draw(canvas);
        }
        Log.d("DrawingView", "Square created and drawn.");
        invalidate();
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("DrawingView", "Touch down");
                // Get x and y coordinates of touch
                float x = event.getX();
                float y = event.getY();

                // Create square at those coordinates
                square = new Square(100, x, y, Color.RED);
                return true;
        }

        // Return event
        return false;
    }
}