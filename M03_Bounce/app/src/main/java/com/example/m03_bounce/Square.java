package com.example.m03_bounce;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.Random;

public class Square {
    // Fields

    private float x;
    private float y;
    private float sideLength;

    private float speedX;
    private float speedY;

    private RectF bounds;
    private Paint paint;

    private int collisionCount; // To track number of collision for Logcat output

    public Square(int color, float x, float y, float sideLength, float speedX, float speedY) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);

        this.x = x;
        this.y = y;
        this.sideLength = sideLength;
        this.speedX = speedX;
        this.speedY = speedY;

        bounds.set(x, y, x + sideLength, y + sideLength);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(bounds, paint);
    }

    public void moveWithCollisionDetection(Box box, RectF rectangle) {
        // 1. Get new (x,y) position based on constant speed
        x += speedX;
        y += speedY;

        // 2. Update bounds to this new position *temporarily* for checking
        bounds.set(x, y, x + sideLength, y + sideLength);

        // 3. Detect collision and react with OUTER BOX
        if (bounds.right > box.xMax) {
            speedX = -speedX;
            x = box.xMax - sideLength; // Reposition
        } else if (bounds.left < box.xMin) {
            speedX = -speedX;
            x = box.xMin; // Reposition
        }
        if (bounds.bottom > box.yMax) {
            speedY = -speedY;
            y = box.yMax - sideLength; // Reposition
        } else if (bounds.top < box.yMin) {
            speedY = -speedY;
            y = box.yMin; // Reposition
        }

        // 4. Update bounds again *after* box collision correction
        //    This is needed so the inner rect check is correct
        bounds.set(x, y, x + sideLength, y + sideLength);

        // 5. Detect collision and react with INNER RECTANGLE

        // Check for overlap (AABB collision)
        if (bounds.right > rectangle.left &&
                bounds.left < rectangle.right &&
                bounds.bottom > rectangle.top &&
                bounds.top < rectangle.bottom) {

            // --- We have a collision! ---
            // <-- 4. INCREMENT AND LOG THE COLLISION
            collisionCount++;
            Log.d("SquareCollisionRectangle", "Rectangle collision! Total for this square: " + collisionCount);

            // Find out which side we hit by finding the minimum penetration.

            float penLeft = bounds.right - rectangle.left;
            float penRight = rectangle.right - bounds.left;
            float penTop = bounds.bottom - rectangle.top;
            float penBottom = rectangle.bottom - bounds.top;

            // Find the smallest penetration (the side of impact)
            float minPen = penLeft;
            int side = 1; // 1=Left

            if (penRight < minPen) {
                minPen = penRight;
                side = 2; // 2=Right
            }
            if (penTop < minPen) {
                minPen = penTop;
                side = 3; // 3=Top
            }
            if (penBottom < minPen) {
                minPen = penBottom;
                side = 4; // 4=Bottom
            }

            // 6. React to the collision based on the side of impact
            switch (side) {
                case 1: // Hit rect's LEFT side
                    speedX = -Math.abs(speedX); // Force bounce left
                    x = rectangle.left - sideLength; // Reposition
                    break;
                case 2: // Hit rect's RIGHT side
                    speedX = Math.abs(speedX); // Force bounce right
                    x = rectangle.right; // Reposition
                    break;
                case 3: // Hit rect's TOP side
                    speedY = -Math.abs(speedY); // Force bounce up
                    y = rectangle.top - sideLength; // Reposition
                    break;
                case 4: // Hit rect's BOTTOM side
                    speedY = Math.abs(speedY); // Force bounce down
                    y = rectangle.bottom; // Reposition
                    break;
            }
        }

        // 7. Final update to bounds
        // This ensures the .draw() method uses the fully corrected position
        bounds.set(x, y, x + sideLength, y + sideLength);
    }
}
