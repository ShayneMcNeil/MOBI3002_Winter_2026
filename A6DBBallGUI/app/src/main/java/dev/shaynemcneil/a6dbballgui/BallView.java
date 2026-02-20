package dev.shaynemcneil.a6dbballgui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom View that displays and animates balls.
 */
public class BallView extends View {

    // List to hold all the Ball objects.
    private List<Ball> balls = new ArrayList<>();
    // Paint object for drawing the balls.
    private Paint paint = new Paint();
    // Handler for scheduling the animation loop.
    private Handler handler = new Handler();
    // The frame rate for the animation, in frames per second.
    private final int FRAME_RATE = 30;

    /**
     * Constructor for creating a BallView.
     * @param context The context of the application.
     * @param attrs The attributes from the XML layout.
     */
    public BallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Start the animation loop when the view is created.
        handler.postDelayed(runnable, 1000 / FRAME_RATE);
    }

    /**
     * Adds a new ball to the view.
     * @param ball The Ball object to add.
     */
    public void addBall(Ball ball) {
        balls.add(ball);
        // Redraw the view to show the new ball.
        invalidate();
        Log.d("BallView", "Added new Ball");
    }

    /**
     * Removes all balls from the view.
     */
    public void clearBalls() {
        balls.clear();
        // Redraw the view to remove the balls.
        invalidate();
    }

    // The animation loop, implemented as a Runnable.
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Move each ball in the list.
            for (Ball ball : balls) {
                ball.move(getWidth(), getHeight());
            }
            // Redraw the view to show the updated positions.
            invalidate();
            // Schedule the next frame of the animation.
            handler.postDelayed(this, 1000 / FRAME_RATE);
        }
    };

    /**
     * Called to draw the view. This is where the balls are drawn on the canvas.
     * @param canvas The canvas to draw on.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw each ball in the list.
        for (Ball ball : balls) {
            paint.setColor(ball.color);
            canvas.drawCircle(ball.x, ball.y, ball.radius, paint);
        }
    }
}
