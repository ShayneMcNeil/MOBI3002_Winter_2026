package dev.shaynemcneil.a7sensors;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Square {

    private int side;
    private float xPosit;
    private float yPosit;
    private Paint paint;
    private RectF bounds;

    public Square(int side, float xPosit, float yPosit, int color) {
        this.side = side;
        this.xPosit = xPosit;
        this.yPosit = yPosit;
        this.paint = new Paint();
        this.paint.setColor(color);
        this.bounds = new RectF();
        updateBounds();
    }

    private void updateBounds() {
        bounds.set(xPosit - side / 2, yPosit - side / 2, xPosit + side / 2, yPosit + side / 2);
    }

    public void draw(Canvas canvas) {
        // Draw the square at the specified position
        canvas.drawRect(bounds, paint);
    }

    public void updatePosition(float dx, float dy, int viewWidth, int viewHeight) {
        // Adjust position based on accelerometer data
        // The dx and dy from the accelerometer are swapped for intuitive movement
        // A scaling factor is added to control the speed
        float scalingFactor = 2.0f;
        xPosit -= dx * scalingFactor;
        yPosit += dy * scalingFactor;

        // Ensure the square stays within the view bounds
        if (xPosit - side / 2 < 0) {
            xPosit = side / 2;
        }
        if (xPosit + side / 2 > viewWidth) {
            xPosit = viewWidth - side / 2;
        }
        if (yPosit - side / 2 < 0) {
            yPosit = side / 2;
        }
        if (yPosit + side / 2 > viewHeight) {
            yPosit = viewHeight - side / 2;
        }

        updateBounds();
    }
}