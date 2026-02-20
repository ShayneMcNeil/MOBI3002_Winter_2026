package dev.shaynemcneil.a6dbballgui;

/**
 * Represents a single Ball object.
 * This is a simple Plain Old Java Object (POJO) that holds the state of a ball,
 * including its position, speed, and appearance.
 */
public class Ball {
    // The unique name of the ball.
    public String ballName;
    // The horizontal coordinate of the ball's center.
    public float x;
    // The vertical coordinate of the ball's center.
    public float y;
    // The horizontal speed and direction of the ball.
    public float dx;
    // The vertical speed and direction of the ball.
    public float dy;
    // The radius of the ball.
    public float radius;
    // The color of the ball, stored as an integer.
    public int color;

    /**
     * Constructor to create a new Ball object.
     *
     * @param ballName The unique name for the ball.
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     * @param dx The initial horizontal speed.
     * @param dy The initial vertical speed.
     * @param radius The radius of the ball.
     * @param color The color of the ball.
     */
    public Ball(String ballName, float x, float y, float dx, float dy, float radius, int color) {
        this.ballName = ballName;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
        this.color = color;
    }

    /**
     * Updates the ball's position based on its speed and handles collisions with the view boundaries.
     * This method is called on every frame of the animation.
     *
     * @param viewWidth The width of the containing view.
     * @param viewHeight The height of the containing view.
     */
    public void move(int viewWidth, int viewHeight) {
        // Update the ball's position by adding its speed components.
        x += dx;
        y += dy;

        // Check for and handle collision with the left or right walls.
        if (x - radius < 0 || x + radius > viewWidth) {
            dx = -dx; // Reverse the horizontal direction.
            // Reposition the ball to be just within the boundary to prevent it from getting stuck.
            if (x - radius < 0) {
                x = radius;
            } else if (x + radius > viewWidth) {
                x = viewWidth - radius;
            }
        }

        // Check for and handle collision with the top or bottom walls.
        if (y - radius < 0 || y + radius > viewHeight) {
            dy = -dy; // Reverse the vertical direction.
            // Reposition the ball to be just within the boundary.
            if (y - radius < 0) {
                y = radius;
            } else if (y + radius > viewHeight) {
                y = viewHeight - radius;
            }
        }
    }
}
