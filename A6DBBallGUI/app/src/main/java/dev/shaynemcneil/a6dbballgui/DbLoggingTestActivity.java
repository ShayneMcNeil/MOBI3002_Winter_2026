package dev.shaynemcneil.a6dbballgui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

// In DbLoggingTestActivity.java
public class DbLoggingTestActivity extends AppCompatActivity {
    private BallDataSource dataSource;
    private static final String TAG = "DB_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSource = new BallDataSource(this);
        dataSource.open();

        Log.d(TAG, "--- STARTING DATABASE TEST ---");

        // 1. Clear the database
        Log.d(TAG, "Clearing all existing balls...");
        dataSource.deleteAllBalls();
        Log.d(TAG, "Database cleared.");

        // 2. Add a new ball
        Log.d(TAG, "Adding ball 'Alpha'...");
        dataSource.createBall("Alpha", 10, 20, 5, 5, 50, "#FF0000");
        Log.d(TAG, "Ball 'Alpha' added.");

        // 3. Add another new ball
        Log.d(TAG, "Adding ball 'Beta'...");
        dataSource.createBall("Beta", 100, 120, -3, -3, 30, "#0000FF");
        Log.d(TAG, "Ball 'Beta' added.");

        // 4. Retrieve and log all balls
        Log.d(TAG, "Retrieving all balls from DB...");
        List<Ball> balls = dataSource.getAllBalls();
        Log.d(TAG, "Found " + balls.size() + " balls.");
        for (Ball ball : balls) {
            Log.i(TAG, "Retrieved Ball: name=" + ball.ballName + ", x=" + ball.x + ", color=" + String.format("#%06X", (0xFFFFFF & ball.color)));
        }

        Log.d(TAG, "--- DATABASE TEST COMPLETE ---");

        dataSource.close();
    }
}
