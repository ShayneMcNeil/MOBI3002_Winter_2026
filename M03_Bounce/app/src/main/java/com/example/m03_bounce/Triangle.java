package com.example.m03_bounce;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Triangle {
    private Paint paint;
    private Path path;
    float x, y; // Center of the triangle
    float sideLength;

    public Triangle(float x, float y, float sideLength, int color) {
        this.x = x;
        this.y = y;
        this.sideLength = sideLength;

        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);

        path = new Path();
        setupPath();
    }

    private void setupPath() {
        // Calculate the height of the equilateral triangle
        float height = (float) (Math.sqrt(3) / 2 * sideLength);

        // Define the three vertices based on the center (x, y)
        // Point 1 (top vertex)
        float p1x = x;
        float p1y = y - height / 2;

        // Point 2 (bottom-left vertex)
        float p2x = x - sideLength / 2;
        float p2y = y + height / 2;

        // Point 3 (bottom-right vertex)
        float p3x = x + sideLength / 2;
        float p3y = y + height / 2;

        path.moveTo(p1x, p1y);
        path.lineTo(p2x, p2y);
        path.lineTo(p3x, p3y);
        path.close();
    }

    public void draw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }
}
