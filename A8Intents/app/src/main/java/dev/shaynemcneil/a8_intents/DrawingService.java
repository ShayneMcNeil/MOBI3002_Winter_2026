package dev.shaynemcneil.a8_intents;

import android.content.Context;
import android.graphics.Bitmap; // <-- Add this import
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable; // <-- Add this import
import android.graphics.drawable.Drawable; // <-- Add this import
import android.net.Uri;
import android.provider.MediaStore; // <-- Add this import
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.IOException; // <-- Add this import
import java.util.ArrayList;

public class DrawingService extends View {

    // ... (Your other member variables are fine) ...
    private Paint drawPaint;
    private Path currentPath;
    private ArrayList<Path> completedPaths;

    // ... (Your constructor and setupDrawing() are fine) ...
    public DrawingService(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        // ... (Your setup logic is fine) ...
        completedPaths = new ArrayList<>();
        currentPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(Color.BLACK);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(10f);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    // ... (Your onDraw() and onTouchEvent() are fine) ...
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Path path : completedPaths) {
            canvas.drawPath(path, drawPaint);
        }
        canvas.drawPath(currentPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // ... (Your touch logic is fine) ...
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                currentPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                currentPath.lineTo(touchX, touchY);
                completedPaths.add(new Path(currentPath));
                currentPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }


    // --- Public Methods to Control Drawing ---

    /**
     * Clears the entire canvas of all drawings AND the background image.
     */
    public void clearCanvas() {
        completedPaths.clear(); // Remove all completed paths
        currentPath.reset();    // Reset the current path

        // --- Add this line to remove the background image ---
        this.setBackground(null);

        invalidate();           // Redraw the view (which will now be empty)
    }

    /**
     * Sets the color of the drawing brush.
     * @param newColor The new color for the brush.
     */
    public void setBrushColor(int newColor) {
        drawPaint.setColor(newColor);
    }

    /**
     * Sets the background of the View from a content URI.
     * @param imageUri The URI of the image to set as the background.
     */
    public void setBackgroundImage(Uri imageUri) {
        // --- This is the new logic you need ---
        try {
            // 1. Get a Bitmap from the URI
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

            // 2. Convert the Bitmap into a Drawable
            Drawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);

            // 3. Set the background of this View
            this.setBackground(drawable);

        } catch (IOException e) {
            // This can happen if the file is missing or corrupt
            e.printStackTrace();
        }
    }
}