package com.example.m03_bounce;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Russ on 08/04/2014.
 */
public class BouncingBallView extends View {

    private ArrayList<Ball> balls = new ArrayList<Ball>(); // list of Balls
    private Ball ball_1;  // use this to reference first ball in arraylist
    private Box box;
    private ArrayList<Square> squares = new ArrayList<Square>(); // To store all of our squares
    private RectF rectangle = new RectF(100, 500, 900, 600);
    private Paint paintForRect = new Paint();
    private Triangle triangle;

    // For touch inputs - previous touch (x, y)
    private float previousX;
    private float previousY;

    public BouncingBallView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Log.v("BouncingBallView", "Constructor BouncingBallView");

        // create the box
        box = new Box(Color.LTGRAY);  // ARGB
        Log.d("BoxColourChange", "Box background colour changed to light grey");

        //ball_1 = new Ball(Color.GREEN);
        balls.add(new Ball(Color.GREEN));
        ball_1 = balls.get(0);  // points ball_1 to the first; (zero-ith) element of list
        Log.w("BouncingBallLog", "Just added a bouncing ball");

        //ball_2 = new Ball(Color.CYAN);
        balls.add(new Ball(Color.CYAN));
        Log.w("BouncingBallLog", "Just added another bouncing ball");

        Random r = new Random();
        triangle = new Triangle(r.nextInt(800), r.nextInt(800), 100, Color.GREEN);

        // To enable keypad
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);
    }

    // Called back to draw the view. Also called after invalidate().
    @Override
    protected void onDraw(Canvas canvas) {

        Log.v("BouncingBallView", "onDraw");

        // Draw the components
        box.draw(canvas);
        //canvas.drawARGB(0,25,25,25);
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);


            for (Ball b : balls) {
                b.draw(canvas);  //draw each ball in the list
                b.moveWithCollisionDetection(box, rectangle);  // Update the position of the ball

//                /// check if ball is in this square
//                float rect_x1 = 100;
//                float rect_x2 = 200;
//                float rect_y1 = 250;
//                float rect_y2 = 350;
//
//                //check if x1-x2 is small AND y1-y2 is small
//                if ((b.x>rect_x1) &&
//                        (b.x<rect_x2) &&
//                        (b.y>rect_y1) &&
//                        (b.y<rect_y2)) {
//                    // Hit .. double speed
//                    b.speedX = 2* b.speedX;
//                    b.speedY = 2* b.speedY;
//                    Log.w("Bounce", "HIT!");
//                }

            }

            if(!squares.isEmpty()) {
                for(Square s : squares) {
                    s.draw(canvas);
                    s.moveWithCollisionDetection(box, rectangle);
                }
            }

        // Draw our rectangle in the view

        paintForRect.setColor(Color.MAGENTA);
        canvas.drawRect(rectangle, paintForRect);
        triangle.draw(canvas);


        // Delay on UI thread causes big problems!
        // Simulates doing busy work or waits on UI (DB connections, Network I/O, ....)
        //  I/Choreographer? Skipped 64 frames!  The application may be doing too much work on its main thread.
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//        }

        // Check what happens if you draw the box last
        //box.draw(canvas);

        // Check what happens if you don't call invalidate (redraw only when stopped-started)
        // Force a re-draw
        this.invalidate();
    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        box.set(0, 0, w, h);
        Log.w("BouncingBallLog", "onSizeChanged w=" + w + " h=" + h);
    }

    // Touch-input handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        float deltaX, deltaY;
        float scalingFactor = 5.0f / ((box.xMax > box.yMax) ? box.yMax : box.xMax);
        switch(event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // Modify rotational angles according to movement
                deltaX = currentX - previousX;
                deltaY = currentY - previousY;
                float speedThreshold = 200.0f;
                float speedMagnitude = (deltaX * deltaX) + (deltaY * deltaY);

                ball_1.speedX += deltaX * scalingFactor;
                ball_1.speedY += deltaY * scalingFactor;

                if(speedMagnitude > speedThreshold) {
                    squares.add(new Square(Color.RED, previousX, previousY, 100, deltaX, deltaY));
                } else {
                    //Log.w("BouncingBallLog", " Xspeed=" + ball_1.speedX + " Yspeed=" + ball_1.speedY);
                    Log.w("BouncingBallLog", "x,y= " + previousX + " ," + previousY + "  Xdiff=" + deltaX + " Ydiff=" + deltaY);
                    balls.add(new Ball(Color.BLUE, previousX, previousY, deltaX, deltaY));  // add ball at every touch event
                }


                // A way to clear list when too many balls
                if ((balls.size() + squares.size()) > 20) {
                    // leave first ball, remove the rest
                    Log.v("BouncingBallLog", "too many balls, clear back to 1");
                    balls.clear();
                    squares.clear();
                    balls.add(new Ball(Color.RED));
                    ball_1 = balls.get(0);  // points ball_1 to the first (zero-ith) element of list
                }

        }
        // Save current x, y
        previousX = currentX;
        previousY = currentY;

        // Try this (remove other invalidate(); )
        //this.invalidate();

        return true;  // Event handled
    }

    Random rand = new Random();
    // called when button is pressed
    public void RussButtonPressed() {
        Log.d("BouncingBallView  BUTTON", "User tapped the  button ... VIEW");

        // Choose whether to create slow or fast ball
        boolean isFastBall = rand.nextBoolean();
        // Choose random colour for the balls
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.BLACK);
        colors.add(Color.WHITE);

        int randomColor = colors.get(rand.nextInt(colors.size()));
        Log.d("RandomColourChosen", "Random colour has been chosen for ball.");

        if(isFastBall) {
            Ball ball = makeFastBall(randomColor);
            balls.add(ball);
            Log.d("FastBallCreated", "Fast ball has been created.");
        } else {
            Ball ball = makeSlowBall(randomColor);
            balls.add(ball);
            Log.d("SlowBallCreated", "Slow ball has been created.");
        }
    }

    public Ball makeSlowBall(int color) {
        int viewWidth = this.getMeasuredWidth();
        int viewHeight = this.getMeasuredHeight();

        int x = rand.nextInt(viewWidth);
        int y = rand.nextInt(viewHeight);
        int dx = 20;
        int dy = 15;

        return new Ball(color, x, y, dx, dy);
    }

    public Ball makeFastBall(int color) {
        int viewWidth = this.getMeasuredWidth();
        int viewHeight = this.getMeasuredHeight();

        int x = rand.nextInt(viewWidth);
        int y = rand.nextInt(viewHeight);
        int dx = 80;
        int dy = 40;

        return new Ball(color, x, y, dx, dy);
    }
}
