package com.example.m03_bounce;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.Random;


/**
 * Created by Russ on 08/04/2014.
 */
public class Ball {

    float radius = 50;      // Ball's radius
    float x;                // Ball's center (x,y)
    float y;
    float speedX;           // Ball's speed
    float speedY;
    private RectF bounds;   // Needed for Canvas.drawOval
    private Paint paint;    // The paint style, color used for drawing

    // Add accelerometer
    // Add ... implements SensorEventListener
    private double ax, ay, az = 0; // acceration from different axis

    private int collisionCount; // To track number of collisions for Logcat output

    public void setAcc(double ax, double ay, double az){
        this.ax = ax;
        this.ay = ay;
        this.az = az;
    }

    Random r = new Random();  // seed random number generator

    // Constructor
    public Ball(int color) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);

        // random position and speed
        x = radius + r.nextInt(800);
        y = radius + r.nextInt(800);
        speedX = r.nextInt(10) - 5;
        speedY = r.nextInt(10) - 5;
    }

    // Constructor
    public Ball(int color, float x, float y, float speedX, float speedY) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);

        // use parameter position and speed
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public void moveWithCollisionDetection(Box box, RectF rectangle) {
        // 1. Get new (x,y) position
        x += speedX;
        y += speedY;

        // 2. Add acceleration to speed
        speedX += ax;
        speedY += ay;

        // 3. Detect collision and react with OUTER BOX
        if (x + radius > box.xMax) {
            speedX = -speedX;
            x = box.xMax - radius;
        } else if (x - radius < box.xMin) {
            speedX = -speedX;
            x = box.xMin + radius;
        }
        if (y + radius > box.yMax) {
            speedY = -speedY;
            y = box.yMax - radius;
        } else if (y - radius < box.yMin) {
            speedY = -speedY;
            y = box.yMin + radius;
        }

        // 4. Detect collision and react with INNER RECTANGLE

        // First, check for a basic overlap (AABB collision check)
        if (x + radius > rectangle.left &&   // Ball's right edge is past rect's left edge
                x - radius < rectangle.right &&  // Ball's left edge is before rect's right edge
                y + radius > rectangle.top &&    // Ball's bottom edge is past rect's top edge
                y - radius < rectangle.bottom) { // Ball's top edge is before rect's bottom edge

            // --- We have a collision! ---
            // <-- 4. INCREMENT AND LOG THE COLLISION
            collisionCount++;
            Log.d("BallCollideRectangle", "Rectangle collision! Total for this ball: " + collisionCount);
            // Now, find out which side we hit by finding the *minimum penetration*.
            // This tells us which side the ball was closest to when it entered.

            float penLeft = (x + radius) - rectangle.left;
            float penRight = rectangle.right - (x - radius);
            float penTop = (y + radius) - rectangle.top;
            float penBottom = rectangle.bottom - (y - radius);

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

            // 5. React to the collision based on the side of impact
            switch (side) {
                case 1: // Hit rect's LEFT side
                    speedX = -Math.abs(speedX); // Force bounce left
                    x = rectangle.left - radius; // Reposition ball to the left
                    break;
                case 2: // Hit rect's RIGHT side
                    speedX = Math.abs(speedX); // Force bounce right
                    x = rectangle.right + radius; // Reposition ball to the right
                    break;
                case 3: // Hit rect's TOP side
                    speedY = -Math.abs(speedY); // Force bounce up
                    y = rectangle.top - radius; // Reposition ball above
                    break;
                case 4: // Hit rect's BOTTOM side
                    speedY = Math.abs(speedY); // Force bounce down
                    y = rectangle.bottom + radius; // Reposition ball below
                    break;
                    }
                }
            }

    public void draw(Canvas canvas) {
        bounds.set(x - radius, y - radius, x + radius, y + radius);
        canvas.drawOval(bounds, paint);
    }

}
